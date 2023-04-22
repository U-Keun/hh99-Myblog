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
    @Column(name = "commentId")
    private Long id;

    @Column(nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
    private Post post;

    @Column(nullable = false)
    private int likes;

    public Comment(CommentRequestDTO commentRequestDTO) {
        this.comment = commentRequestDTO.getComment();
        this.likes = 0;
    }

    public void update(CommentRequestDTO commentRequestDTO) {
        this.comment = commentRequestDTO.getComment();
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void updateLikes(boolean addOrNot){
        this.likes = addOrNot? this.likes + 1 : this.likes - 1;
    }
}
