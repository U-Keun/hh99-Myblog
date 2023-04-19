package myblog.myblog.controller;

import lombok.RequiredArgsConstructor;
import myblog.myblog.service.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("board")
@RequiredArgsConstructor
public class BoardController {

    private final PostService postService;

    //전체 게시글 조회
    @GetMapping()
    public ResponseEntity list() {
        return postService.list();
    }

    //특정 게시글 조회
    @GetMapping("/{id}")
    public ResponseEntity findPost(@PathVariable Long id) {
        return postService.findPostById(id);
    }
}
