package myblog.myblog.domain;

import jakarta.persistence.*;
import lombok.*;
import myblog.myblog.dto.PostRequestDTO;

@Getter
@Setter
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
    private String author;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String password;

    //RequestDTO 를 Post로 변환
    @Builder
    public Post(PostRequestDTO requestDTO) {
        this.title = requestDTO.getTitle();
        this.author = requestDTO.getAuthor();
        this.content = requestDTO.getContent();
        this.password = requestDTO.getPassword();
    }

    public void update(PostRequestDTO reqDTO) {
        this.title = reqDTO.getTitle();
        this.author = reqDTO.getAuthor();
        this.content = reqDTO.getContent();
    }
}
