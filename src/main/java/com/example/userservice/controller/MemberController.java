package com.example.userservice.controller;

import com.example.userservice.dto.ContributionRes;
import com.example.userservice.service.MemberService;
import com.example.userservice.service.OAuthService;
import com.example.userservice.config.Constant.SocialLoginType;
import com.example.userservice.dto.GetSocialOAuthRes;
import com.example.userservice.dto.MemberDto;
import com.example.userservice.dto.PostMemberRes;
import com.example.userservice.vo.RequestContribution;
import com.example.userservice.vo.ResponseTeamMember;
import com.example.userservice.vo.UserIdList;
import com.thoughtworks.xstream.core.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
@Slf4j
@RequiredArgsConstructor
public class MemberController {
    private final OAuthService oauthService;
    private final MemberService memberService;

    @GetMapping("/health_check")
    public String healthCheck(){

        return "hello";
    }
    /* list에 있는 userId에 해당하는 user 정보 모두 반환 */
    @PostMapping("/users")
    public ResponseEntity<List<ResponseTeamMember>> getUsersInfo(@RequestBody UserIdList userIdList){
        List<ResponseTeamMember> result = null;
        System.out.println(userIdList);
        try{
            result = memberService.getTeamMembers(userIdList.getUserIdList());

        } catch(Exception ex){
            log.error(ex.getMessage());
        }
        return ResponseEntity.status(200).body(result);
    }

    /* 팀 멤버 정보와 기여도를 함께 반환 */
    @PostMapping("/users/contribution")
    public ResponseEntity<List<ContributionRes>> getUsersContribution(@RequestBody List<RequestContribution> userList){
        List<ContributionRes> result = new ArrayList<>();

        try{
            result = memberService.getContributions(userList);

        } catch(Exception ex){
            log.error(ex.getMessage());
        }
        return ResponseEntity.status(200).body(result);
    }

    @GetMapping("/accounts/oauth2/{socialLoginType}")
    public void socialLoginRedirect(@PathVariable(name = "socialLoginType") String socialLoginPath) throws IOException {
        SocialLoginType socialLoginType = SocialLoginType.valueOf(socialLoginPath.toUpperCase());
        oauthService.redirectTo(socialLoginType);
    }

    @GetMapping("/accounts/oauth2/{socialLoginType}/callback")
    public ResponseEntity<GetSocialOAuthRes> socialLogin (@PathVariable(name = "socialLoginType") String socialLoginPath,
                                                          @RequestParam(name = "code") String code) {
        try{
            SocialLoginType socialLoginType = SocialLoginType.valueOf(socialLoginPath.toUpperCase());
            GetSocialOAuthRes getSocialOAuthRes = oauthService.oAuthLogin(socialLoginType, code);
            return ResponseEntity.status(HttpStatus.OK).body(getSocialOAuthRes);
        } catch(BaseException exception){
            log.error(exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/accounts/signup")
    public ResponseEntity<PostMemberRes> signUp (@RequestBody MemberDto memberDto) {

        PostMemberRes postMemberRes = memberService.registerUser(memberDto);

        if (postMemberRes != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(postMemberRes);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

}
