package com.echocano.tennis.league.application.service;

import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;

import com.echocano.tennis.league.application.port.in.RegisterMatchUseCase;
import com.echocano.tennis.league.application.port.in.UpdateMatchResultUseCase;
import com.echocano.tennis.league.application.port.in.ViewMatchUseCase;
import com.echocano.tennis.league.application.port.out.LeagueParticipantRepositoryPort;
import com.echocano.tennis.league.application.port.out.MatchRepositoryPort;
import com.echocano.tennis.league.domain.model.League;
import com.echocano.tennis.league.domain.model.LeagueParticipant;
import com.echocano.tennis.league.domain.model.Match;
import com.echocano.tennis.league.domain.model.MatchResultsSummary;
import com.echocano.tennis.league.domain.model.SetResult;
import com.echocano.tennis.league.domain.model.Team;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

public class MatchApplicationService implements RegisterMatchUseCase, UpdateMatchResultUseCase, ViewMatchUseCase {

	private final MatchRepositoryPort matchRepository;
	private final LeagueParticipantRepositoryPort participantRepository;

	private static final Logger LOG = Logger.getLogger(LeagueApplicationService.class);

	public MatchApplicationService(MatchRepositoryPort matchRepository,
			LeagueParticipantRepositoryPort participantRepository) {
		this.matchRepository = matchRepository;
		this.participantRepository = participantRepository;
	}

	@Override
	public Uni<Match> execute(Match match) {
		try {
			match.validate();
		} catch (IllegalArgumentException e) {
			return Uni.createFrom().failure(e);
		}
		return matchRepository.save(match);
	}

	@Override
	public Uni<Match> execute(Long matchId, List<SetResult> sets, boolean isWalkover, Long walkoverWinnerId) {
		LOG.infof("[DEBUG] 1. Starting use case for Match ID: ", matchId);
		return matchRepository.findById(matchId)
				.invoke(match -> LOG.infof("[DEBUG] 2. Match search results: %s",
						match != null ? "FIND (ID: " + match.getId() + ")" : "NULL"))
				.onItem().ifNull().failWith(() -> new NotFoundException("Match was not found."))
				.flatMap((Match match) -> {
					final boolean hadPreviousResult = match.getWinner() != null;
					final List<SetResult> oldSets = match.getSets() != null
							? new ArrayList<>(match.getSets())
							: List.of();
					final boolean oldWalkover = match.isWalkover();
					final Team oldWinner = match.getWinner();
					match.setSets(sets);
					match.setWalkover(isWalkover);
					if (isWalkover) {
						if (walkoverWinnerId == null) {
							return Uni.createFrom().failure(
									new BadRequestException(
											"A winner must be specified for a walkover."));
						}
						match.setWinner(walkoverWinnerId.equals(match.getTeam1().getId())
								? match.getTeam1()
								: match.getTeam2());
					} else {
						match.setWinner(null);
					}
					if (!isWalkover) {
						try {
							match.validateSetsAmount();
						} catch (IllegalArgumentException e) {
							return Uni.createFrom().failure(
									new BadRequestException(e.getMessage()));
						}
					}
					MatchResultsSummary newSummary = match.calculateResults();
					League league = match.getLeague();
					int pointsForWinner = league.getPointsPerWin();
					int pointsForLoser = newSummary.isWalkover() ? league.getPointsPerWalkover()
							: league.getPointsPerLoss();
					LOG.infof(
							"[DEBUG] 3. Match data ready. Searching for winner in standings table. League ID: %d Winner Team ID: %d",
							league.getId(), newSummary.winner().getId());
					return participantRepository
							.findByLeagueAndTeam(league.getId(),
									newSummary.winner().getId())
							.flatMap(pWinner -> {
								return participantRepository.findByLeagueAndTeam(
										league.getId(),
										newSummary.loser().getId())
										.flatMap(pLoser -> {
											Team managedWinner = match
													.getTeam1()
													.getId()
													.equals(newSummary
															.winner()
															.getId()) ? match
																	.getTeam1()
																	: match.getTeam2();
											Team managedLoser = match
													.getTeam1()
													.getId()
													.equals(newSummary
															.loser()
															.getId()) ? match
																	.getTeam1()
																	: match.getTeam2();

											final LeagueParticipant finalWinner = (pWinner != null)
													? pWinner
													: createNewParticipant(
															match.getLeague(),
															managedWinner);
											final LeagueParticipant finalLoser = (pLoser != null)
													? pLoser
													: createNewParticipant(
															match.getLeague(),
															managedLoser);
											if (hadPreviousResult) {
												LOG.info("[DEBUG] Score edit detected. Reverting previous result...");
												int oldT1Sets = 0;
												int oldT2Sets = 0;
												if (!oldWalkover) {
													for (SetResult s : oldSets) {
														if (s.getTeam1Games() > s
																.getTeam2Games())
															oldT1Sets++;
														else if (s.getTeam2Games() > s
																.getTeam1Games())
															oldT2Sets++;
														else if (s.getTeam1Games() == 6
																&& s.getTeam2Games() == 6) {
															int p1 = s.getTeam1TieBreakPoints() != null
																	? s.getTeam1TieBreakPoints()
																	: 0;
															int p2 = s.getTeam2TieBreakPoints() != null
																	? s.getTeam2TieBreakPoints()
																	: 0;
															if (p1 > p2)
																oldT1Sets++;
															else if (p2 > p1)
																oldT2Sets++;
														}
													}
												} else {
													oldT1Sets = oldWinner
															.equals(match.getTeam1())
																	? match.getMatchType().getMinSets()
																	: 0;
													oldT2Sets = oldWinner
															.equals(match.getTeam2())
																	? match.getMatchType().getMinSets()
																	: 0;
												}
												Team oldLoser = oldWinner
														.equals(match.getTeam1())
																? match.getTeam2()
																: match.getTeam1();
												int oldWinnerSets = oldWinner
														.equals(match.getTeam1())
																? oldT1Sets
																: oldT2Sets;
												int oldLoserSets = oldWinner
														.equals(match.getTeam1())
																? oldT2Sets
																: oldT1Sets;
												int oldPointsWinner = league
														.getPointsPerWin();
												int oldPointsLoser = oldWalkover
														? league.getPointsPerWalkover()
														: league.getPointsPerLoss();
												if (oldWinner.getId()
														.equals(finalWinner
																.getTeam()
																.getId())) {
													finalWinner.revertStatsAsWinner(
															oldWinnerSets,
															oldLoserSets,
															oldPointsWinner);
													finalLoser.revertStatsAsLoser(
															oldLoserSets,
															oldWinnerSets,
															oldWalkover,
															oldPointsLoser);
												} else {
													finalWinner.revertStatsAsLoser(
															oldWinnerSets,
															oldLoserSets,
															oldWalkover,
															oldPointsLoser);
													finalLoser.revertStatsAsWinner(
															oldLoserSets,
															oldWinnerSets,
															oldPointsWinner);
												}
											}
											int winnerSets = newSummary
													.winner()
													.equals(match.getTeam1())
															? newSummary.team1SetsWon()
															: newSummary.team2SetsWon();
											int loserSets = newSummary
													.winner()
													.equals(match.getTeam1())
															? newSummary.team2SetsWon()
															: newSummary.team1SetsWon();
											finalWinner.updateStatsAsWinner(
													winnerSets,
													loserSets,
													pointsForWinner);
											finalLoser.updateStatsAsLoser(
													loserSets,
													winnerSets,
													newSummary.isWalkover(),
													pointsForLoser);
											LOG.info("[DEBUG] 7. Attempting to persist updated changes in cascade...");
											return participantRepository
													.save(finalWinner)
													.flatMap(savedWinner -> participantRepository
															.save(finalLoser))
													.flatMap(savedLoser -> matchRepository
															.save(match))
													.invoke(savedMatch -> LOG.info(
															"[DEBUG] 8. Modification successfully saved in Postgres."))
													.replaceWith(match);
										});
							});
				});
	}

	private LeagueParticipant createNewParticipant(League league, Team team) {
		LeagueParticipant participant = new LeagueParticipant();
		participant.setLeague(league);
		participant.setTeam(team);
		return participant;
	}

	@Override
	public Uni<List<Match>> getAllMatch(Long leagueId, Long teamId) {
		return matchRepository.findAllMatches(leagueId, teamId);
	}
}