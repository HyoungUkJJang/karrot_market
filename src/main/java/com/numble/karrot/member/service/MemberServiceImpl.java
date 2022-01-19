package com.numble.karrot.member.service;

import com.numble.karrot.member.domain.Member;
import com.numble.karrot.member.exception.MemberNotFoundException;
import com.numble.karrot.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member join(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public Member findMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException());
    }

    @Override
    public Member update(String email, String newNickName) {
        Member findMember = findMember(email);
        return findMember.userUpdate(newNickName);
    }

    @Override
    public boolean duplicateEmailCheck(String email) {
        return memberRepository.existsByEmail(email);
    }

}
