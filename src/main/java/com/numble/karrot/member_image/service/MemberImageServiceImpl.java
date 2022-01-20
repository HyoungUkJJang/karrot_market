package com.numble.karrot.member_image.service;

import com.numble.karrot.member_image.domain.MemberImage;
import com.numble.karrot.member_image.repository.MemberImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberImageServiceImpl implements MemberImageService{

    private final MemberImageRepository memberImageRepository;

    @Override
    @Transactional
    public MemberImage save(MemberImage memberImage) {
        return memberImageRepository.save(memberImage);
    }

    @Override
    public MemberImage findMemberImage(Long memberId) {
        return memberImageRepository.findByMemberId(memberId);
    }

    @Override
    @Transactional
    public void updateMemberImage(MemberImage memberImage, String newFilePath, String newOriginalFileName, String newServerFileName) {
        memberImage.updateMemberImage(newFilePath, newOriginalFileName, newServerFileName);
    }

}
