package myblog.myblog.domain;

import jakarta.persistence.*;
import lombok.*;
import myblog.myblog.dto.post.PostRequestDTO;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    @OrderBy("createdAt desc")
    private List<Comment> commentList = new ArrayList<>();

    @Column(nullable = false)
    private int likes;

    //RequestDTO 를 Post로 변환
    public Post(PostRequestDTO requestDTO) {
        this.title = requestDTO.getTitle();
        this.content = requestDTO.getContent();
        this.likes = 0;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    // ============연관 관계 편의 메서드============
    public void addComment(Comment comment) {
        commentList.add(comment);
        comment.setPost(this);
    }

    // ============ 비즈니스 메서드============
    public void update(PostRequestDTO reqDTO) {
        this.title = reqDTO.getTitle();
        this.content = reqDTO.getContent();
    }

    public void updateLikes(boolean addOrNot){
        this.likes = addOrNot? this.likes+1 : this.likes -1;
    }
}
