package myblog.myblog.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import myblog.myblog.domain.Comment;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String username;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.username = comment.getMember().getUsername();
        this.comment = comment.getComment();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
