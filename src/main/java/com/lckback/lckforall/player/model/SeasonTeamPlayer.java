package com.lckback.lckforall.player.model;

import com.lckback.lckforall.base.model.BaseEntity;
import com.lckback.lckforall.team.model.SeasonTeam;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@Table(name = "SEASON_TEAM_PLAYER")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeasonTeamPlayer extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "SEASON_TEAM_ID", nullable = false)
	private SeasonTeam seasonTeam;

	@ManyToOne
	@JoinColumn(name = "PLAYER_ID", nullable = false)
	private Player player;
}
