package com.lckback.lckforall.community.model;

import com.lckback.lckforall.base.model.BaseEntity;
import com.lckback.lckforall.report.model.CommentReport;
import com.lckback.lckforall.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@Table(name = "COMMENTS")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "POST_ID", nullable = false)
	private Post post;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = false)
	private User user;

	@OneToMany(mappedBy = "comment", orphanRemoval = true)
	private List<CommentReport> commentReports = new ArrayList<>();
}
