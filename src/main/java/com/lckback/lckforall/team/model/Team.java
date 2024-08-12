package com.lckback.lckforall.team.model;

import java.util.ArrayList;
import java.util.List;

import com.lckback.lckforall.base.model.BaseEntity;
import com.lckback.lckforall.match.model.Match;
import com.lckback.lckforall.match.model.Set;
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

@Getter
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

    @OneToMany(mappedBy = "team")
    private List<SeasonTeam> seasonTeams = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "winnerTeam")
    private List<Set> winSets = new ArrayList<>();

    @OneToMany(mappedBy = "loseTeam")
    private List<Set> loseSets = new ArrayList<>();

    /**
     * 팀이 참여한 모든 경기의 매치 투표를 전부 가져옴(경기를 구분하지 않고)
     * 때문에 양방향 연관관계가 필요가 없을것 같은데 혹시 몰라서 연결해 두었습니다
     */
    @OneToMany(mappedBy = "team")
    private List<MatchVote> matchVotes = new ArrayList<>();

    @OneToMany(mappedBy = "team1")
    private List<Match> matches1 = new ArrayList<>();

    @OneToMany(mappedBy = "team2")
    private List<Match> matches2 = new ArrayList<>();
}
