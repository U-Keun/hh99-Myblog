package myblog.myblog.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import myblog.myblog.dto.MessageDTO;
import myblog.myblog.domain.StatusCode;
import myblog.myblog.dto.PostRequestDTO;
import myblog.myblog.dto.PostResponseDTO;
import myblog.myblog.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //전체 게시글 조회
    @GetMapping()
    @ResponseBody
    public ResponseEntity list() {
        List<PostResponseDTO> posts = postService.list();
        MessageDTO messageDTO = new MessageDTO(StatusCode.OK, "list success", posts);
        return new ResponseEntity(messageDTO, HttpStatus.OK);
    }

    //게시글 등록
    @PostMapping()
    @ResponseBody
    public ResponseEntity register(@RequestBody PostRequestDTO dto, HttpServletRequest request) {
        MessageDTO messageDTO = postService.savePost(dto, request);
        String code = messageDTO.getStatus().getCode();
        return new ResponseEntity(messageDTO, HttpStatus.OK);
    }

    //특정 게시글 조회
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity findPost(@PathVariable Long id) {
        MessageDTO messageDTO = postService.findPostById(id);
        return new ResponseEntity(messageDTO, HttpStatus.OK);
    }

    //게시글 삭제
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity delete(@PathVariable Long id, HttpServletRequest request) {
        MessageDTO messageDTO = postService.deletePost(id, request);
        return new ResponseEntity(messageDTO, HttpStatus.OK);
    }

    //게시글 수정
    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity update(@PathVariable Long id, @RequestBody PostRequestDTO reqDTO, HttpServletRequest request) {
        MessageDTO messageDTO = postService.updatePost(id, reqDTO, request);
        return new ResponseEntity(messageDTO, HttpStatus.OK);
    }
}
