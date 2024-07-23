package com.lckback.lckforall.player.model;

import java.util.ArrayList;
import java.util.List;

import com.lckback.lckforall.base.model.BaseEntity;
import com.lckback.lckforall.base.type.PlayerRole;
import com.lckback.lckforall.match.model.Match;
import com.lckback.lckforall.match.model.Set;
import com.lckback.lckforall.team.model.Team;
import com.lckback.lckforall.vote.model.MatchPogVote;
import com.lckback.lckforall.vote.model.SetPogVote;

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
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Player extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 20)
	private String name;

	@Column(nullable = false, length = 100)
	private String ProfileImageUrl;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PlayerRole role;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEAM_ID", nullable = false)
	private Team team;

	@OneToMany(mappedBy = "player")
	private List<SetPogVote> setPogVotes = new ArrayList<>();

	@OneToMany(mappedBy = "player")
	private List<MatchPogVote> matchPogVotes = new ArrayList<>();

	@OneToMany(mappedBy = "pogPlayer")
	private List<Match> pogMatches = new ArrayList<>();

	@OneToMany(mappedBy = "pogPlayer")
	private List<Set> pogSets = new ArrayList<>();
}
