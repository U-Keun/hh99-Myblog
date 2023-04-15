package myblog.myblog.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostRequestDTO {

    private Long id;
    private String title;
    private String author;
    private String content;
    private String password;

    public PostRequestDTO(String title, String author, String content, String password) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.password = password;
    }
}
