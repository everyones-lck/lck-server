package com.lckback.lckforall.team.model;

import java.util.ArrayList;
import java.util.List;

import com.lckback.lckforall.base.model.BaseEntity;
import com.lckback.lckforall.player.model.SeasonTeamPlayer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeasonTeam extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Integer winningPoint;

	@Column(nullable = false)
	private Integer rating;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEAM_ID", nullable = false)
	private Team team;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEASON_ID", nullable = false)
	private Season season;

	@OneToMany(mappedBy = "seasonTeam")
	private List<SeasonTeamPlayer> seasonTeamPlayers = new ArrayList<>();
}
