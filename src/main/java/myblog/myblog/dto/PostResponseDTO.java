package myblog.myblog.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import myblog.myblog.domain.Post;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
public class PostResponseDTO {

    private Long id;
    private String title;
    private String author;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public PostResponseDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.author = post.getAuthor();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
    }
}
