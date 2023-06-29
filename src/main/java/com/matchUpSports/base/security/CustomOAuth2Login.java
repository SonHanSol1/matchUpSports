package com.matchUpSports.base.security;

import com.matchUpSports.boundedContext.member.entity.Member;
import com.matchUpSports.boundedContext.member.service.MemberService;
import com.matchUpSports.base.security.social.SocialUserFactory;
import com.matchUpSports.base.security.social.inter.DivideOAuth2User;
import com.matchUpSports.base.security.social.SocialUserFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2Login extends DefaultOAuth2UserService {

    private final MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        String providerName = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
        OAuth2User oAuth2User = super.loadUser(userRequest);

        DivideOAuth2User customOAuth2User = SocialUserFactory.create(providerName, oAuth2User);

        Member member = memberService.saveOAuth2Member(customOAuth2User);

//        return new CustomOAuth2User(member.getUsername(), member.getPassword(), member.getAuthorities());
        return new CustomOAuth2User(member.getUsername(), member.getAuthorities());
    }



}