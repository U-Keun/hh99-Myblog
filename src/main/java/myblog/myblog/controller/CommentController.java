package myblog.myblog.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import myblog.myblog.dto.BasicResponseDTO;
import myblog.myblog.dto.comment.CommentRequestDTO;
import myblog.myblog.exception.CommentException;
import myblog.myblog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{id}")
    public ResponseEntity register(@PathVariable Long id, @RequestBody CommentRequestDTO dto, HttpServletRequest request) {
        ResponseEntity responseEntity;
        try {
            responseEntity = commentService.saveComment(id, dto, request);
        } catch (Exception e) {
            BasicResponseDTO basicResponseDTO = BasicResponseDTO.setBadRequest(e.getMessage());
            responseEntity = new ResponseEntity(basicResponseDTO, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id, HttpServletRequest request) {
        ResponseEntity responseEntity;
        try {
            responseEntity = commentService.deleteComment(id, request);
        } catch (Exception e) {
            BasicResponseDTO basicResponseDTO = BasicResponseDTO.setBadRequest(e.getMessage());
            responseEntity = new ResponseEntity(basicResponseDTO, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }
    
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody CommentRequestDTO requestDTO ,HttpServletRequest request) {
        ResponseEntity responseEntity;
        try {
            responseEntity = commentService.updateComment(id, requestDTO, request);
        } catch (Exception e) {
            BasicResponseDTO basicResponseDTO = BasicResponseDTO.setBadRequest(e.getMessage());
            responseEntity = new ResponseEntity(basicResponseDTO, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }
}
