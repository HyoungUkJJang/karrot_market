package com.numble.karrot.member_image.domain;

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
public class MemberImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

}
