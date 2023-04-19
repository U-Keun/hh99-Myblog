package myblog.myblog.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import myblog.myblog.dto.comment.CommentRequestDTO;
import myblog.myblog.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{id}")
    public ResponseEntity register(@PathVariable Long id, @RequestBody CommentRequestDTO dto, HttpServletRequest request) {
        return commentService.saveComment(id, dto, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id, HttpServletRequest request) {
        return commentService.deleteComment(id, request);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody CommentRequestDTO requestDTO ,HttpServletRequest request) {
        return commentService.updateComment(id, requestDTO, request);
    }
}
