package com.lckback.lckforall.team.model;

import java.util.ArrayList;
import java.util.List;

import com.lckback.lckforall.base.model.BaseEntity;
import com.lckback.lckforall.match.model.Match;
import com.lckback.lckforall.match.model.Set;
import com.lckback.lckforall.player.model.Player;
import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.vote.model.MatchVote;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 20)
	private String teamName;

	@Column(nullable = false, length = 100)
	private String teamLogoUrl;

	@Column(nullable = false, length = 100)
	private String season;

	@Column(nullable = false)
	private Integer winningPoint;

	@OneToMany(mappedBy = "team")
	private List<User> users = new ArrayList<>();

	@OneToMany(mappedBy = "team")
	private List<Player> players = new ArrayList<>();

	@OneToMany(mappedBy = "winnerTeam")
	private List<Set> winSets = new ArrayList<>();

	@OneToMany(mappedBy = "loseTeam")
	private List<Set> loseSets = new ArrayList<>();

	@OneToMany(mappedBy = "team")
	private List<MatchVote> matchVotes = new ArrayList<>();

	@OneToMany(mappedBy = "team1")
	private List<Match> matches1 = new ArrayList<>();

	@OneToMany(mappedBy = "team2")
	private List<Match> matches2 = new ArrayList<>();

}
