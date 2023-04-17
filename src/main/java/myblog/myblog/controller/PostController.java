package myblog.myblog.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import myblog.myblog.dto.PostRequestDTO;
import myblog.myblog.dto.PostResponseDTO;
import myblog.myblog.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //전체 게시글 조회
    @GetMapping()
    public List<PostResponseDTO> list() {
        List<PostResponseDTO> posts = postService.list();
        return posts;
    }
    //게시글 등록
    @PostMapping()
    public PostResponseDTO  register(@RequestBody PostRequestDTO dto, HttpServletRequest request) {
        PostResponseDTO savedPost = postService.savePost(dto, request);
        return savedPost;
    }

    //특정 게시글 조회
    @GetMapping("/{id}")
    public PostResponseDTO  findPost(@PathVariable Long id) {
        return postService.findPostById(id);
    }

    //게시글 삭제
    @DeleteMapping("/delete/{id}")
    public PostResponseDTO  delete(@PathVariable Long id, HttpServletRequest request) {
        return postService.deletePost(id, request);
    }

    //게시글 수정
    @PutMapping("/update/{id}")
    public PostResponseDTO update(@PathVariable Long id, @RequestBody PostRequestDTO reqDTO, HttpServletRequest request) {
        return postService.updatePost(id, reqDTO, request);
    }
}
