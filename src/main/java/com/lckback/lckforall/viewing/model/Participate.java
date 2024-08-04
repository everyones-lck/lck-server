package com.lckback.lckforall.viewing.model;

import com.lckback.lckforall.base.model.BaseEntity;
import com.lckback.lckforall.user.model.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Participate extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VIEWING_PARTY_ID", nullable = false)
	private ViewingParty viewingParty;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = false)
	private User user;

	public void setUser(User user) {
		if(this.user != null){
			user.getParticipatingViewingParties().remove(this);
		}
		this.user = user;
		user.getParticipatingViewingParties().add(this);
	}
	public void setViewingParty(ViewingParty viewingParty) {
		this.viewingParty = viewingParty;
	}
}
