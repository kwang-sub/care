package com.example.care.user.domain;

import com.example.care.membership.domain.MembershipHistory;
import com.example.care.reserve.domain.Reserve;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String username;
    private String nickname;
    private String password;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    @CreationTimestamp
    private LocalDateTime regDate;
    private String provider;

    @OneToMany(mappedBy = "user")
    private List<Reserve> reserveList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<MembershipHistory> membershipHistoryList = new ArrayList<>();

    @Builder
    public User(Long id, String username, String nickname, String password, String email, Role role,
                String provider, List<Reserve> reserveList, List<MembershipHistory> membershipHistoryList) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.reserveList = reserveList;
        this.membershipHistoryList = membershipHistoryList;
    }


    public String getRoleKey() {
        return this.role.getKey();
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }
}
