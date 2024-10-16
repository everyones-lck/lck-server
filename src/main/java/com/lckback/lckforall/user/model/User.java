package com.lckback.lckforall.user.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import com.lckback.lckforall.base.model.BaseEntity;
import com.lckback.lckforall.base.type.UserRole;
import com.lckback.lckforall.base.type.UserStatus;
import com.lckback.lckforall.community.model.Comment;
import com.lckback.lckforall.community.model.Post;
import com.lckback.lckforall.report.model.CommentReport;
import com.lckback.lckforall.report.model.PostReport;
import com.lckback.lckforall.team.model.Team;
import com.lckback.lckforall.viewing.model.ChatMessage;
import com.lckback.lckforall.viewing.model.Participate;
import com.lckback.lckforall.viewing.model.ViewingParty;
import com.lckback.lckforall.vote.model.MatchPogVote;
import com.lckback.lckforall.vote.model.MatchVote;
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
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@Table(name = "USERS")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String kakaoUserId;

	@Column(nullable = false, length = 20)
	private String nickname;

	@Column(nullable = false)
	private String profileImageUrl;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserRole role;

	@Column(nullable = false)
	private String tier;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserStatus status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEAM_ID", nullable = false)
	private Team team;

	@Column(nullable = false)
	private LocalDateTime lastUpdatedMyTeam;

	@OneToMany(mappedBy = "user", orphanRemoval = true)
	private List<Post> posts = new ArrayList<>();

	@OneToMany(mappedBy = "user", orphanRemoval = true)
	private List<PostReport> postReports = new ArrayList<>();

	@OneToMany(mappedBy = "user", orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();

	@OneToMany(mappedBy = "user", orphanRemoval = true)
	private List<CommentReport> commentReports = new ArrayList<>();

	@OneToMany(mappedBy = "user", orphanRemoval = true)
	private List<Participate> participatingViewingParties = new ArrayList<>();

	@OneToMany(mappedBy = "user", orphanRemoval = true)
	private List<ViewingParty> hostingViewingParties = new ArrayList<>();

	@OneToMany(mappedBy = "user", orphanRemoval = true)
	private List<MatchVote> matchVotes = new ArrayList<>();

	@OneToMany(mappedBy = "user", orphanRemoval = true)
	private List<MatchPogVote> matchPogVotes = new ArrayList<>();

	@OneToMany(mappedBy = "user", orphanRemoval = true)
	private List<SetPogVote> setPogVotes = new ArrayList<>();

	public void updateNickname(String nickname) {
		this.nickname = nickname;
	}

	public void updateProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public void withdrawFromAccount() {
		this.status = UserStatus.INACTIVE;
	}

	public void updateMyTeam(Team team) {
		this.team = team;
		this.lastUpdatedMyTeam = LocalDateTime.now();
	}
}
