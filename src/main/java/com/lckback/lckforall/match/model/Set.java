package com.lckback.lckforall.match.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.lckback.lckforall.base.model.BaseEntity;
import com.lckback.lckforall.player.model.Player;
import com.lckback.lckforall.team.model.Team;
import com.lckback.lckforall.vote.model.SetPogVote;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@Table(name = "MATCH_SETS")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Set extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Integer matchIndex;

	@Column(nullable = false)
	private LocalDateTime startDate;

	@Column(nullable = false)
	private LocalDateTime endDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MATCH_ID", nullable = false)
	private Match match;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "POG_PLAYER_ID", nullable = false)
	private Player pogPlayer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WINNER_TEAM_ID", nullable = false)
	private Team winnerTeam;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOSE_TEAM_ID", nullable = false)
	private Team loseTeam;

	@OneToMany(mappedBy = "set")
	private List<SetPogVote> setPogVotes = new ArrayList<>();
}
