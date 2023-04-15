package myblog.myblog.controller;

import lombok.RequiredArgsConstructor;
import myblog.myblog.dto.PostRequestDTO;
import myblog.myblog.dto.PostResponseDTO;
import myblog.myblog.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //전체 게시글 조회
    @GetMapping()
    @ResponseBody
    public List<PostResponseDTO> list() {
        List<PostResponseDTO> posts = postService.list();
        return posts;
    }

    //게시글 등록
    @PostMapping()
    @ResponseBody
    public PostResponseDTO register(@RequestBody PostRequestDTO dto) {
        PostResponseDTO savedPost = postService.savePost(dto);
        return savedPost;
    }

    //특정 게시글 조회
    @GetMapping("/{id}")
    @ResponseBody
    public PostResponseDTO findPost(@PathVariable Long id) {
        PostResponseDTO findDTO = postService.findPostById(id);
        return findDTO;
    }

    //게시글 삭제
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public String delete(@PathVariable Long id, @RequestBody Map<String, String> password) {
        return postService.deletePost(id, password.get("password"));
    }

    //게시글 수정
    @PutMapping("/update/{id}")
    @ResponseBody
    public PostResponseDTO update(@PathVariable Long id, @RequestBody PostRequestDTO reqDTO) {
        return postService.updatePost(id, reqDTO);
    }
}
