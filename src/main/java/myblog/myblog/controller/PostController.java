package myblog.myblog.controller;

import lombok.RequiredArgsConstructor;
import myblog.myblog.dto.post.PostRequestDTO;
import myblog.myblog.security.UserDetailsImpl;
import myblog.myblog.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //게시글 등록
    @PostMapping()
    public ResponseEntity register(@RequestBody PostRequestDTO dto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.savePost(dto, userDetails.getMember());
    }

    //게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePost(id, userDetails.getMember());
    }

    //게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity update(
            @PathVariable Long id,
            @RequestBody PostRequestDTO reqDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return postService.updatePost(id, reqDTO, userDetails.getMember());
    }
}
