package com.lckback.lckforall.match;

import com.lckback.lckforall.base.type.PlayerRole;
import com.lckback.lckforall.base.type.UserRole;
import com.lckback.lckforall.base.type.UserStatus;
import com.lckback.lckforall.match.dto.PogDto;
import com.lckback.lckforall.match.model.Match;
import com.lckback.lckforall.match.repository.MatchRepository;
import com.lckback.lckforall.match.service.PogService;
import com.lckback.lckforall.player.model.Player;
import com.lckback.lckforall.player.model.repository.PlayerRepository;
import com.lckback.lckforall.team.model.Team;
import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.vote.model.MatchPogVote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class PogServiceTest {
    @Mock
    private PlayerRepository playerRepository; // PlaerRepository를 Mocking
    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private PogService pogService; // PogService에 Mock을 주입

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Mock 객체 초기화
    }

    @Test
    public void testGetTodayMatches() {
        // Given
        Team team1 = Team.builder()
                .id(1L)
                .teamName("team1")
                .teamLogoUrl("url")
                .season("season")
                .winningPoint(3)
                .build();

        Team team2 = Team.builder()
                .id(2L)
                .teamName("team2")
                .teamLogoUrl("url")
                .season("season")
                .winningPoint(3)
                .build();

        User user1 = User.builder()
                .id(1L)
                .nickname("user1")
                .profileImageUrl("url")
                .role(UserRole.ROLE_USER)
                .status(UserStatus.ACTIVE)
                .team(team1)
                .build();

        User user2 = User.builder()
                .id(2L)
                .nickname("user2")
                .profileImageUrl("url")
                .role(UserRole.ROLE_USER)
                .status(UserStatus.ACTIVE)
                .team(team1)
                .build();
        User user3 = User.builder()
                .id(3L)
                .nickname("user3")
                .profileImageUrl("url")
                .role(UserRole.ROLE_USER)
                .status(UserStatus.ACTIVE)
                .team(team1)
                .build();

        Match match1 = Match.builder()
                .id(1L)
                .matchDate(LocalDateTime.now())
                .team1(team1)
                .team2(team2)
                .build();

        Match match2 = Match.builder()
                .id(2L)
                .matchDate(LocalDateTime.now())
                .team1(team2)
                .team2(team1)
                .build();

        Player player1 = Player.builder()
                .id(1L)
                .name("player1")
                .ProfileImageUrl("imgUrl")
                .role(PlayerRole.LCK_ROSTER)
                .team(team1)
                .build();
        Player player2 = Player.builder()
                .id(2L)
                .name("player2")
                .ProfileImageUrl("imgUrl")
                .role(PlayerRole.LCK_ROSTER)
                .team(team1)
                .build();
        MatchPogVote vote1 = MatchPogVote.builder()
                .id(1L).match(match1).player(player2).user(user1).build();
        MatchPogVote vote2 = MatchPogVote.builder()
                .id(2L).match(match1).player(player1).user(user2).build();
        MatchPogVote vote3 = MatchPogVote.builder()
                .id(3L).match(match1).player(player2).user(user3).build();
        List<MatchPogVote> voteList = new ArrayList<>();
        voteList.add(vote1); voteList.add(vote2); voteList.add(vote3);
        match1.testSetPogVote(voteList);

        //player2가 pog인 경우
        when(playerRepository.findById(2L)).thenReturn(Optional.ofNullable(player2));// Mock의 동작 정의
        when(matchRepository.findById(1L)).thenReturn(Optional.ofNullable(match1)); // 1번 match에 대해 정의

        // When
        PogDto.PogServiceDto dto = new PogDto.PogServiceDto(1L);
        PogDto.MatchPogResponse response = pogService.matchPog(dto);

        // Then
        assertEquals(2L, response.getId()); // 결과 검증
        System.out.println(response.getId() + " " + response.getName() + " " + response.getProfileImageUrl());
    }
}
