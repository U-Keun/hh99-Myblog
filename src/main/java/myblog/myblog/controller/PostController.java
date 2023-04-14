package myblog.myblog.controller;

import lombok.RequiredArgsConstructor;
import myblog.myblog.dto.PostResponseDTO;
import myblog.myblog.service.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping()
    @ResponseBody
    public List<PostResponseDTO> list() {
        List<PostResponseDTO> posts = postService.findAll();
        return posts;
    }
}
