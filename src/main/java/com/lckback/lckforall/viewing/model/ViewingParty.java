package com.lckback.lckforall.viewing.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.lckback.lckforall.base.model.BaseEntity;
import com.lckback.lckforall.user.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "VIEWING_PARTY")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ViewingParty extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 20)
	private String name;

	@Column(nullable = false)
	private LocalDateTime date;

	@Column(nullable = false)
	private Double latitude;

	@Column(nullable = false)
	private Double longitude;

	@Column(nullable = false)
	private Integer price;

	@Column(nullable = false)
	private Integer lowParticipate;

	@Column(nullable = false)
	private Integer highParticipate;

	@Column(nullable = false, length = 100)
	private String partyQualify;

	@Column(length = 1000)
	private String etc;

	@Column(length = 30)
	private String location;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = false)
	private User user;

	@OneToMany(mappedBy = "viewingParty")
	private List<Participate> participates = new ArrayList<>();

	public void setUser(User user) {
		if(this.user != null) {
			user.getHostingViewingParties().remove(this);
		}
		this.user = user;
		user.getHostingViewingParties().add(this);
	}
}
