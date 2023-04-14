package myblog.myblog.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Post extends TimeStamped {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Builder
    public Post(Long id, String title, String author, String content, String password) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.content = content;
        this.password = password;
    }
}
