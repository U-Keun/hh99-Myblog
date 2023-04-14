package myblog.myblog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import myblog.myblog.domain.Post;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDTO {

    private Long id;
    private String title;
    private String author;
    private String content;
    private String password;

    public Post toEntity() {
        return Post.builder()
                .title(title)
                .author(author)
                .content(content)
                .password(password)
                .build();
    }
}
