// package com.lckback.lckforall.user.dto;
//
// import org.springframework.web.multipart.MultipartFile;
//
// import lombok.Builder;
// import lombok.Getter;
//
// @Getter
// public class SignupUserDataDto {
//
//     private MultipartFile profileImage;
//     private SignupUserData signupUserData;
//
//     @Getter
//     @Builder
//     public static class SignupUserData {
//         private String kakaoUserId;
//         private String nickName;
//         private String role;
//         private Long teamId;
//     }
// }

package com.lckback.lckforall.user.dto;

import lombok.Builder;
import lombok.Getter;

public class SignupUserDataDto {

    @Getter
    @Builder
    public static class SignupUserData {

        private String kakaoUserId;
        private String nickName;
        private String role;
        private String tier;
        private Long teamId;
    }

}