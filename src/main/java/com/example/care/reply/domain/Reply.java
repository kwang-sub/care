package com.example.care.reply.domain;

import com.example.care.board.domain.Board;
import com.example.care.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REPLY_ID")
    private Long id;

    private String text;

    @CreationTimestamp
    private LocalDateTime regDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Reply parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    private List<Reply> children = new ArrayList<>();

    @Builder
    public Reply(Long id, String text, User user, Board board, Reply parent) {
        this.id = id;
        this.text = text;
        this.user = user;
        this.board = board;
        this.parent = parent;
    }

    public void changeReply(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Reply{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", regDate=" + regDate +
                ", user=" + user +
                ", board=" + board +
                ", children=" + children +
                '}';
    }
}
