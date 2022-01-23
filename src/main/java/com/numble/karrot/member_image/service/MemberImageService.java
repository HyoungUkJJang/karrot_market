package com.numble.karrot.member_image.service;

import com.numble.karrot.member_image.domain.MemberImage;

/**
 * 회원 이미지와 관련된 서비스를 제공하는 클래스
 */
public interface MemberImageService {

    /**
     * 회원 이미지 도메인을 저장합니다.
     * @param memberImage 저장 할 이미지 정보
     * @return 저장 된 이미지 정보
     */
    MemberImage save(MemberImage memberImage);

    /**
     * 회원의 이미지 정보를 불러옵니다.
     * @param memberId 회원의 아이디
     * @return 이미지 정보
     */
    MemberImage findMemberImage(Long memberId);

    /**
     * 회원 이미지를 업데이트 합니다.
     * @param memberImage
     * @param newFilePath
     * @param newOriginalFileName
     * @param newServerFileName
     */
    void updateMemberImage(MemberImage memberImage, String newFilePath, String newOriginalFileName, String newServerFileName);

}
