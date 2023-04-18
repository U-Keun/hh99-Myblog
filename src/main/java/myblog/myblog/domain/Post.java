package myblog.myblog.domain;

import jakarta.persistence.*;
import lombok.*;
import myblog.myblog.dto.PostRequestDTO;

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

    //RequestDTO 를 Post로 변환
    public Post(PostRequestDTO requestDTO) {
        this.title = requestDTO.getTitle();
        this.content = requestDTO.getContent();
    }

    public void update(PostRequestDTO reqDTO) {
        this.title = reqDTO.getTitle();
        this.content = reqDTO.getContent();
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
