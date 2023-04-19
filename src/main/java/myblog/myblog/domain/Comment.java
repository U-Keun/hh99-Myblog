package myblog.myblog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import myblog.myblog.dto.comment.CommentRequestDTO;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment(CommentRequestDTO commentRequestDTO, Member member) {
        this.username = member.getUsername();
        this.comment = commentRequestDTO.getComment();
    }

    public void update(CommentRequestDTO commentRequestDTO) {
        this.comment = commentRequestDTO.getComment();
    }

    public void setMember(Member member) { this.member = member; }

    public void setPost(Post post) {
        this.post = post;
    }
}
