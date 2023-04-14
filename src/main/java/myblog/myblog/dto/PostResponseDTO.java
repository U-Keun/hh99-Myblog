package myblog.myblog.dto;

import lombok.Getter;
import lombok.ToString;
import java.time.LocalDate;

@Getter
@ToString
public class PostResponseDTO {

    private Long id;
    private String title;
    private String author;
    private String content;
    private LocalDate createdAt;
}
