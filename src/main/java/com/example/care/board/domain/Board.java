package com.example.care.board.domain;

import com.example.care.reply.domain.Reply;
import com.example.care.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long id;

    private String title;
    @Lob
    private String content;
    private long view;

    @CreationTimestamp
    @Column(name = "REG_DATE", updatable = false)
    private LocalDateTime regDate;

    @UpdateTimestamp
    private LocalDateTime modDate;

    private int replyCnt;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Reply> replyList = new ArrayList<>();

    @Builder
    public Board(Long id, String title, String content, LocalDateTime regDate, LocalDateTime modDate, User user) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.regDate = regDate;
        this.modDate = modDate;
        this.user = user;
    }

    public void changeBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void read() {
        this.view++;
    }

    public void replyPlus() {
        this.replyCnt++;
    }

    public void replyMinus() {
        this.replyCnt--;
    }
}
