package myblog.myblog.dto.post;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostRequestDTO {

    private Long id;
    private String title;
    private String content;
}
