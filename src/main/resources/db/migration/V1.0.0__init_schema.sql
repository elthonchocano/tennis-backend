-- 1. Table: players (Prepared for Google OAuth2 integration)
CREATE TABLE players (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE,
    phone_number VARCHAR(30) NOT NULL UNIQUE,
    hand VARCHAR(1),
    
    -- Authentication Infrastructure
    oauth2_provider VARCHAR(20) DEFAULT 'LOCAL',
    oauth2_id VARCHAR(100) UNIQUE,
    avatar_url VARCHAR(255),
    invitation_code VARCHAR(100) UNIQUE,
    -- Audit fields
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_players_email ON players(email);

-- 2. Table: teams
CREATE TABLE teams (
    id BIGSERIAL PRIMARY KEY,
    player1_id BIGINT NOT NULL,
    player2_id BIGINT,
    team_name VARCHAR(150),
    CONSTRAINT fk_teams_player1 FOREIGN KEY (player1_id) REFERENCES players(id) ON DELETE RESTRICT,
    CONSTRAINT fk_teams_player2 FOREIGN KEY (player2_id) REFERENCES players(id) ON DELETE RESTRICT
);

-- 3. Table: leagues
CREATE TABLE leagues (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    season VARCHAR(50),
    points_per_win INTEGER NOT NULL DEFAULT 3,
    points_per_loss INTEGER NOT NULL DEFAULT 1,
    points_per_walkover INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT unique_league_name_season UNIQUE (name, season)
);

-- 4. Table: league_participants
CREATE TABLE league_participants (
    id BIGSERIAL PRIMARY KEY,
    league_id BIGINT NOT NULL,
    team_id BIGINT NOT NULL,
    matches_played INTEGER DEFAULT 0,
    matches_won INTEGER DEFAULT 0,
    matches_lost INTEGER NOT NULL DEFAULT 0,
    sets_won INTEGER DEFAULT 0,
    sets_lost INTEGER DEFAULT 0,
    losses_by_walkover INTEGER NOT NULL DEFAULT 0,
    points INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT unique_league_team UNIQUE (league_id, team_id),
    CONSTRAINT fk_participants_league FOREIGN KEY (league_id) REFERENCES leagues(id) ON DELETE CASCADE,
    CONSTRAINT fk_participants_team FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE RESTRICT
);

-- 5. Table: matches
CREATE TABLE matches (
    id BIGSERIAL PRIMARY KEY,
    league_id BIGINT NOT NULL,
    team1_id BIGINT NOT NULL,
    team2_id BIGINT NOT NULL,
    winner_id BIGINT,
    match_date DATE,
    match_type VARCHAR(30) NOT NULL,
    walkover BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_matches_league FOREIGN KEY (league_id) REFERENCES leagues(id) ON DELETE CASCADE,
    CONSTRAINT fk_matches_team1 FOREIGN KEY (team1_id) REFERENCES teams(id) ON DELETE RESTRICT,
    CONSTRAINT fk_matches_team2 FOREIGN KEY (team2_id) REFERENCES teams(id) ON DELETE RESTRICT,
    CONSTRAINT fk_matches_winner FOREIGN KEY (winner_id) REFERENCES teams(id) ON DELETE RESTRICT
);

-- 6. Table: set_results
CREATE TABLE set_results (
    id BIGSERIAL PRIMARY KEY,
    match_id BIGINT NOT NULL,
    set_number INTEGER NOT NULL,
    set_index INTEGER, -- Maps to Hibernate's @OrderColumn
    team1_games INTEGER NOT NULL,
    team2_games INTEGER NOT NULL,
    team1_tiebreak_points INTEGER,
    team2_tiebreak_points INTEGER,
    CONSTRAINT fk_set_results_match FOREIGN KEY (match_id) REFERENCES matches(id) ON DELETE CASCADE
);

CREATE INDEX idx_set_results_match_index ON set_results(match_id, set_index);