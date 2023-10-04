package com.example.userservice.service;

import com.example.userservice.dto.ContributionRes;
import com.example.userservice.dto.MemberDto;
import com.example.userservice.dto.PostMemberRes;
import com.example.userservice.dto.UserInfoRes;
import com.example.userservice.entity.Member;
import com.example.userservice.repository.MemberRepository;
import com.example.userservice.utils.JwtService;
import com.example.userservice.vo.RequestContribution;
import com.example.userservice.vo.ResponseTeamMember;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    public List<ResponseTeamMember> getTeamMembers (List<Long> userIdList){
        List<ResponseTeamMember> teamMembers = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        userIdList.forEach(v -> {
            Optional<Member> member = memberRepository.findById(v);
            if(member.isPresent()){
                teamMembers.add(modelMapper.map(member.get(), ResponseTeamMember.class));
            }
        });
        return teamMembers;
    }

    public List<ContributionRes> getContributions (List<RequestContribution> userList){
        List<ContributionRes> teamMembers = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        userList.forEach(v -> {
            Optional<Member> member = memberRepository.findById(v.getId());
            if(member.isPresent()){
                ContributionRes memberInfo = modelMapper.map(member.get(), ContributionRes.class);
                memberInfo.setContribution(v.getContribution());
                teamMembers.add(memberInfo);
            }
        });
        return teamMembers;
    }

    /**
     * 이메일로 찾은 사용자 id(PK) 반환
     */
    public Member getUser(String email){
        Optional <Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()){
            return member.get();
        } else {
            return null;
        }
    }

    public PostMemberRes registerUser(MemberDto memberDto){
        if(getUser(memberDto.getUser_email()) != null){
            log.error("이미 가입된 회원입니다.");
            return null;
        }

        Member member = Member.builder()
                .email(memberDto.getUser_email())
                .name(memberDto.getUser_name())
                .role(memberDto.getRole())
                .department(memberDto.getDepartment())
                .university(memberDto.getUniversity())
                .studentNumber(memberDto.getStudent_number())
                .pictureUrl(memberDto.getPicture_url())
                .build();

        Member postMember = memberRepository.save(member);

        Long id = postMember.getId();
        String role = member.getRole();

        String jwtToken = jwtService.createJwt(id, role);
        PostMemberRes postMemberRes = new PostMemberRes(jwtToken, id, role);
        return postMemberRes;
    }

    public UserInfoRes findUser(Long userId){
        Optional<Member> member = memberRepository.findById(userId);

        if(member.isPresent()){
            ModelMapper modelmapper = new ModelMapper();
            return modelmapper.map(member.get(), UserInfoRes.class);
        }
        return null;
    }
}
