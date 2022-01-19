package com.numble.karrot.config;

import com.numble.karrot.member.domain.Member;
import com.numble.karrot.member.domain.MemberRole;
import com.numble.karrot.member.exception.MemberNotFoundException;
import com.numble.karrot.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * 회원이 시큐리티의 form 로그인 시 호출되는 서비스 입니다.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException());

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        if ("admin".equals(email)) {
            grantedAuthorities.add(new SimpleGrantedAuthority(MemberRole.ROLE_ADMIN.getValue()));
        } else {
            grantedAuthorities.add(new SimpleGrantedAuthority(MemberRole.ROLE_USER.getValue()));
        }

        return new User(findMember.getEmail(), findMember.getPassword(), grantedAuthorities);

    }

}
