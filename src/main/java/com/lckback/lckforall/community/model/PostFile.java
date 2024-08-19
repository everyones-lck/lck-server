package com.lckback.lckforall.community.model;

import com.lckback.lckforall.base.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@Table(name = "POST_FILE")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private Integer index;
    //사진인지 동영상인지
//	private String jpg;
//	private String mov;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID", nullable = true)
    private Post post;

    public void setPost(Post post) {
        if (this.post != null) {
            post.getPostFiles().remove(this);
        }
        this.post = post;
        post.getPostFiles().add(this);
    }
}
