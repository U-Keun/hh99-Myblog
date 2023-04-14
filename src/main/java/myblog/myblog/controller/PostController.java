package myblog.myblog.controller;

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

    @GetMapping()
    @ResponseBody
    public List<PostResponseDTO> list() {
        List<PostResponseDTO> posts = postService.list();
        return posts;
    }

    @PostMapping()
    @ResponseBody
    public PostResponseDTO register(PostRequestDTO dto) {
        PostResponseDTO savedPost = postService.register(dto);
        return savedPost;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public PostResponseDTO findPost(@PathVariable Long id) {
        PostResponseDTO findDTO = postService.findPostById(id);
        return findDTO;
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public String delete(@PathVariable Long id, @RequestParam String password) {
        return postService.delete(id, password);
    }
}
