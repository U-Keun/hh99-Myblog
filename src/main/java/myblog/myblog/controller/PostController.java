package myblog.myblog.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import myblog.myblog.dto.BasicResponseDTO;
import myblog.myblog.dto.post.PostRequestDTO;
import myblog.myblog.exception.PostException;
import myblog.myblog.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //게시글 등록
    @PostMapping()
    public ResponseEntity register(@RequestBody PostRequestDTO dto, HttpServletRequest request) {
        ResponseEntity responseEntity;
        try {
            responseEntity = postService.savePost(dto, request);
        } catch (Exception e) {
            BasicResponseDTO basicResponseDTO = BasicResponseDTO.setBadRequest(e.getMessage());
            responseEntity = new ResponseEntity(basicResponseDTO, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    //게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id, HttpServletRequest request) {
        ResponseEntity responseEntity;
        try {
            responseEntity = postService.deletePost(id, request);
        } catch (Exception e) {
            BasicResponseDTO basicResponseDTO = BasicResponseDTO.setBadRequest(e.getMessage());
            responseEntity = new ResponseEntity(basicResponseDTO, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    //게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody PostRequestDTO reqDTO, HttpServletRequest request) {
        ResponseEntity responseEntity;
        try {
            responseEntity = postService.updatePost(id, reqDTO, request);
        } catch (Exception e) {
            BasicResponseDTO basicResponseDTO = BasicResponseDTO.setBadRequest(e.getMessage());
            responseEntity = new ResponseEntity(basicResponseDTO, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }
}
