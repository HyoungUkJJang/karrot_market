package com.numble.karrot.member.domain;

import com.numble.karrot.member_image.domain.MemberImage;
import com.numble.karrot.trade.domain.Trade;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 캐럿마켓 회원 도메인입니다.
 */
@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(length = 30, nullable = false)
    private String name;
    @Column(length = 20,nullable = false)
    private String phone;
    private String nickName;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @OneToOne(mappedBy = "member")
    private MemberImage memberImage;

    @OneToOne(mappedBy = "member")
    private Trade trade;

    @Builder
    public Member(String email, String password, String name, String phone, String nickName,
                  MemberRole memberRole) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.nickName = nickName;
        this.memberRole = memberRole;
    }

}
