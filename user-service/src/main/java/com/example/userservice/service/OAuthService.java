package com.example.userservice.service;

import com.example.userservice.config.Constant.SocialLoginType;
import com.example.userservice.dto.GetSocialOAuthRes;
import com.example.userservice.dto.GoogleOAuthToken;
import com.example.userservice.dto.GoogleUser;
import com.example.userservice.entity.Member;
import com.example.userservice.utils.JwtService;
import com.thoughtworks.xstream.core.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
@Slf4j
public class OAuthService {

    private final GoogleOauth googleOauth;
    private final HttpServletResponse response;
    private final HttpServletRequest request;
    private final MemberService memberService;
    private final JwtService jwtService;

    public void redirectTo(SocialLoginType socialLoginType) throws IOException{
        String redirectURL;
        switch (socialLoginType){
            case GOOGLE : {
                redirectURL = googleOauth.getOauthRedirectURL();
            } break;
            default : {
                throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
            }
        }

        //각 소셜 로그인을 요청하면 소셜 로그인 페이지로 리다이렉트 해주는 프로세스
        response.sendRedirect(redirectURL);
    }

    public GetSocialOAuthRes oAuthLogin(SocialLoginType socialLoginType, String code) throws BaseException {
        try{
            switch (socialLoginType){
                case GOOGLE: {
                    //Authorization code를 통해 AccessToken 획득
                    GoogleOAuthToken oAuthToken = googleOauth.getAccessToken(code);
                    //액세스 토큰을 다시 구글로 보내 구글에 저장된 사용자 정보가 담긴 응답 객체를 받아온다.
                    ResponseEntity<String> userInfoResponse = googleOauth.requestUserInfo(oAuthToken);
                    //다시 JSON 형식의 응답 객체를 자바 객체로 역직렬화한다.
                    GoogleUser googleUser = googleOauth.getUserInfo(userInfoResponse);

                    String user_email = googleUser.getEmail();
                    String picture_url = googleUser.getPicture();
                    //우리 서버의 db와 대조하여 해당 User가 존재하는지 확인
                    Member member = memberService.getUser(user_email);

                    if (member != null){ //존재하는 경우
                        Long user_id = member.getId();
                        String role = member.getRole();
                        String jwtToken = jwtService.createJwt(user_id, role);
                        GetSocialOAuthRes getSocialOAuthRes = new GetSocialOAuthRes(jwtToken, user_id, user_email, picture_url, role);
                        return getSocialOAuthRes;
                    } else {
                        GetSocialOAuthRes getSocialOAuthRes = new GetSocialOAuthRes(null, null, user_email, picture_url, null);
                        return getSocialOAuthRes;
                    }
                }
                default: {
                    log.error("알 수 없는 소셜 로그인 형식입니다.");
                    throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
                }
            }
        }catch(Exception ex){
            log.error(ex.getMessage());
            return null;
        }
    }
}
