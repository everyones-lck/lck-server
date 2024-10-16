package com.lckback.lckforall.match.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.lckback.lckforall.base.model.BaseEntity;
import com.lckback.lckforall.base.type.MatchResult;
import com.lckback.lckforall.player.model.Player;
import com.lckback.lckforall.team.model.Season;
import com.lckback.lckforall.team.model.SeasonTeam;
import com.lckback.lckforall.team.model.Team;
import com.lckback.lckforall.vote.model.MatchPogVote;
import com.lckback.lckforall.vote.model.MatchVote;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@Table(name = "MATCHES")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Match extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private LocalDateTime matchDate;

	@Column(nullable = false)
	private Integer matchNumber;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private MatchResult matchResult;

	@Column(nullable = false)
	private Boolean votable;

	@Column(nullable = false)
	private Boolean matchPogVotable;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "POG_PLAYER_ID", nullable = false)
	private Player pogPlayer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEAM_ID1", nullable = false)
	private Team team1;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEAM_ID2", nullable = false)
	private Team team2;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEASON_ID", nullable = false)
	private Season season;

	@OneToMany(mappedBy = "match")
	private List<Set> sets = new ArrayList<>();

	@OneToMany(mappedBy = "match")
	private List<MatchPogVote> matchPogVotes = new ArrayList<>();

	@OneToMany(mappedBy = "match")
	private List<MatchVote> matchVotes = new ArrayList<>();

	public void savePogPlayer(Player player) {
		pogPlayer = player;
	}

	public void testSetMatchVotes(List<MatchVote> votes) {//test를 위한 함수(삭제 에정)
		matchVotes = votes;
	}

	public void testSetPogVote(List<MatchPogVote> votes) {//test를 위한 함수(삭제 에정)
		matchPogVotes = votes;
	}

	public void setSets(List<Set> sets) {//test를 위한 함수(삭제 에정)
		this.sets = sets;
	}

	public void setSeason(Season season) {//test를 위한 함수(삭제 에정)
		this.season = season;
	}
}
