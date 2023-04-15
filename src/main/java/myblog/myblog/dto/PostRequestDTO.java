package myblog.myblog.dto;

import lombok.AllArgsConstructor;
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

    public PostRequestDTO(Long id, String title, String author, String content, String password) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.content = content;
        this.password = password;
    }
}
