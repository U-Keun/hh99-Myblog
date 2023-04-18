package myblog.myblog.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import myblog.myblog.dto.PostRequestDTO;
import myblog.myblog.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //전체 게시글 조회
    @GetMapping()
    public ResponseEntity list() {
        return postService.list();
    }

    //게시글 등록
    @PostMapping()
    public ResponseEntity register(@RequestBody PostRequestDTO dto, HttpServletRequest request) {
        return postService.savePost(dto, request);
    }

    //특정 게시글 조회
    @GetMapping("/{id}")
    public ResponseEntity findPost(@PathVariable Long id) {
        return postService.findPostById(id);
    }

    //게시글 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable Long id, HttpServletRequest request) {
        return postService.deletePost(id, request);
    }

    //게시글 수정
    @PutMapping("/update/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody PostRequestDTO reqDTO, HttpServletRequest request) {
        return postService.updatePost(id, reqDTO, request);
    }
}
