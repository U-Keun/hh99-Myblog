package myblog.myblog.controller;

import lombok.RequiredArgsConstructor;
import myblog.myblog.dto.BasicResponseDTO;
import myblog.myblog.dto.comment.CommentRequestDTO;
import myblog.myblog.security.UserDetailsImpl;
import myblog.myblog.service.CommentService;
import org.springframework.http.HttpStatus;
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
        ResponseEntity responseEntity;
        try {
            responseEntity = commentService.saveComment(id, dto, userDetails.getMember());
        } catch (Exception e) {
            BasicResponseDTO basicResponseDTO = BasicResponseDTO.setBadRequest(e.getMessage());
            responseEntity = new ResponseEntity(basicResponseDTO, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        ResponseEntity responseEntity;
        try {
            responseEntity = commentService.deleteComment(id, userDetails.getMember());
        } catch (Exception e) {
            BasicResponseDTO basicResponseDTO = BasicResponseDTO.setBadRequest(e.getMessage());
            responseEntity = new ResponseEntity(basicResponseDTO, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @PutMapping("/{id}")
    public ResponseEntity update(
            @PathVariable Long id,
            @RequestBody CommentRequestDTO requestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        ResponseEntity responseEntity;
        try {
            responseEntity = commentService.updateComment(id, requestDTO, userDetails.getMember());
        } catch (Exception e) {
            BasicResponseDTO basicResponseDTO = BasicResponseDTO.setBadRequest(e.getMessage());
            responseEntity = new ResponseEntity(basicResponseDTO, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }
}
