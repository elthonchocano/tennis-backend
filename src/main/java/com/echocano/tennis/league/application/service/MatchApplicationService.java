package com.echocano.tennis.league.application.service;

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
		return matchRepository.findById(matchId)
				.onItem().ifNull().failWith(() -> new NotFoundException("Match not found."))
				.flatMap(match -> {
					match.setSets(sets);
					match.setWalkover(isWalkover);
					if (isWalkover) {
						match.setWinner(walkoverWinnerId.equals(match.getTeam1().getId()) ? match.getTeam1()
								: match.getTeam2());
					} else {
						match.validateSetsAmount();
						MatchResultsSummary summary = match.calculateResults();
						match.setWinner(summary.winner());
					}
					return matchRepository.save(match);
				})
				.flatMap(updatedMatch -> {
					League league = updatedMatch.getLeague();
					Team team1 = updatedMatch.getTeam1();
					Team team2 = updatedMatch.getTeam2();
					return matchRepository.findAllMatches(league.getId(), team1.getId())
							.flatMap(matchesT1 -> matchRepository.findAllMatches(league.getId(), team2.getId())
									.flatMap(matchesT2 -> updateParticipantStats(league, team1, matchesT1)
											.flatMap(p1 -> updateParticipantStats(league, team2, matchesT2)
													.map(p2 -> updatedMatch))));
				});
	}

	private Uni<LeagueParticipant> updateParticipantStats(League league, Team team, List<Match> allMatches) {
		return participantRepository.findByLeagueAndTeam(league.getId(), team.getId())
				.onItem().ifNull().continueWith(() -> createNewParticipant(league, team))
				.flatMap(participant -> {
					participant.recalculateStats(allMatches, league);
					return participantRepository.save(participant);
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