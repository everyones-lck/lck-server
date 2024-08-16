package com.lckback.lckforall.community.model;

import com.lckback.lckforall.base.model.BaseEntity;
import com.lckback.lckforall.report.model.PostReport;
import com.lckback.lckforall.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@Table(name = "POSTS")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 20)
	private String title;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "POST_TYPE_ID", nullable = false)
	private PostType postType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = false)
	private User user;

	@OneToMany(mappedBy = "post")
	private List<Comment> comments = new ArrayList<>();

	@OneToMany(mappedBy = "post")
	private List<PostFile> postFiles = new ArrayList<>();

	@OneToMany(mappedBy = "post")
	private List<PostReport> postReports = new ArrayList<>();


	// 파일은 파라미터로 일단 안받았음. 어떻게 하는지 모르겠어 질문
	public void update(String title, String content,PostType postType) {
		//유의미한 이름을 가진 메서드를 만들어
		this.title = title;
		this.content = content;
		this.postType = postType;
	}
}
