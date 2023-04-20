package myblog.myblog.controller;

import lombok.RequiredArgsConstructor;
import myblog.myblog.dto.comment.CommentRequestDTO;
import myblog.myblog.security.UserDetailsImpl;
import myblog.myblog.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{id}")
    public ResponseEntity register(
            @PathVariable Long id,
            @RequestBody CommentRequestDTO dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return commentService.saveComment(id, dto, userDetails.getMember());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(id, userDetails.getMember());
    }

    @PutMapping("/{id}")
    public ResponseEntity update(
            @PathVariable Long id,
            @RequestBody CommentRequestDTO requestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return commentService.updateComment(id, requestDTO, userDetails.getMember());
    }
}
