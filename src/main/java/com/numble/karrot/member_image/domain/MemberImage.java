package com.numble.karrot.member_image.domain;

import com.numble.karrot.common.BaseEntity;
import com.numble.karrot.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 회원 프로필 이미지정보 도메인입니다.
 */
@Entity
@Getter
@NoArgsConstructor
public class MemberImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_image_id")
    private Long id;

    private String filePath;
    private String originalFileName;
    private String serverFileName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public MemberImage(String filePath, String originalFileName, String serverFileName, Member member) {
        this.filePath = filePath;
        this.originalFileName = originalFileName;
        this.serverFileName = serverFileName;
        this.member = member;
    }

    /**
     * 회원 이미지 프로필을 업데이트 합니다.
     * @param filePath 새로운 패스
     * @param originalFileName 새로운 이미지 원본 이름
     * @param serverFileName 새로운 서버 이미지 이름
     */
    public void updateMemberImage(String filePath, String originalFileName, String serverFileName) {
        this.filePath = filePath;
        this.originalFileName = originalFileName;
        this.serverFileName = serverFileName;
    }

}
