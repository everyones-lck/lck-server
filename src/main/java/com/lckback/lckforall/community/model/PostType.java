package com.lckback.lckforall.community.model;

import com.lckback.lckforall.base.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostType extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 20)
	private String type; //잡담, 응원, 질문, FA, 후기, 거래

	@OneToMany(mappedBy = "postType")
	private List<Post> posts = new ArrayList<>();
}
