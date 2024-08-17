package com.lckback.lckforall.player.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.lckback.lckforall.base.model.BaseEntity;
import com.lckback.lckforall.base.type.PlayerPosition;
import com.lckback.lckforall.base.type.PlayerRole;
import com.lckback.lckforall.match.model.Match;
import com.lckback.lckforall.match.model.Set;
import com.lckback.lckforall.vote.model.MatchPogVote;
import com.lckback.lckforall.vote.model.SetPogVote;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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

	@Column(nullable = false, length = 20)
	private String realName;

	@Column(nullable = false)
	private LocalDate birth;

	@Column(nullable = false)
	private String ProfileImageUrl;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PlayerRole role;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PlayerPosition position;

	@OneToMany(mappedBy = "player")
	private List<SeasonTeamPlayer> seasonTeamPlayers = new ArrayList<>();

	@OneToMany(mappedBy = "player")
	private List<SetPogVote> setPogVotes = new ArrayList<>();

	@OneToMany(mappedBy = "player")
	private List<MatchPogVote> matchPogVotes = new ArrayList<>();

	@OneToMany(mappedBy = "pogPlayer")
	private List<Match> pogMatches = new ArrayList<>();

	@OneToMany(mappedBy = "pogPlayer")
	private List<Set> pogSets = new ArrayList<>();
}
