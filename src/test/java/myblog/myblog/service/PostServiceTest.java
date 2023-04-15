package myblog.myblog.service;

import jakarta.transaction.Transactional;
import myblog.myblog.domain.Post;
import myblog.myblog.dto.PostRequestDTO;
import myblog.myblog.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;

    @AfterEach
    public void clear() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("전체 게시글 조회")
    public void findAll() {
        //given
        PostRequestDTO dto = new PostRequestDTO(null, "스프링", "김무무", "스프링 재미있다", "1234");
        Long savedId = postRepository.save(new Post(dto)).getId();

        //when
        //then
        Assertions.assertThat(1).isEqualTo(postService.list().size());
    }

    @Test
    @DisplayName("게시글 등록")
    public void register() {
        //given
        PostRequestDTO dto = new PostRequestDTO(null, "스프링", "김무무", "스프링 재미있다", "1234");

        //when
        Long savedId = postService.savePost(dto).getId();
        Long findId = postRepository.findById(savedId).get().getId();

        //then
        Assertions.assertThat(findId).isEqualTo(savedId);
    }

    @Test
    @DisplayName("특정 게시글 조회")
    public void findPostById() {
        //given
        PostRequestDTO dto1 = new PostRequestDTO(null, "스프링1", "김무무", "스프링 재미있다", "1234");
        postRepository.save(new Post(dto1));
        PostRequestDTO dto2 = new PostRequestDTO(null, "스프링2", "김무무", "스프링 재미있다", "1234");
        Long savedId = postRepository.save(new Post(dto2)).getId();

        //when
        String findTitle = postService.findPostById(savedId).getTitle();

        //then
        Assertions.assertThat("스프링2").isEqualTo(findTitle);
    }

    @Test
    @DisplayName("특정 게시글이 존재하지 않으면 에러 발생")
    public void findPostByIdException() {
        //when
        //then
        assertThrows(NoSuchElementException.class,
                () -> postService.findPostById(2L));
    }

    @Test
    @DisplayName("게시글 삭제")
    public void delete() {
        //given
        PostRequestDTO dto = new PostRequestDTO(null, "스프링", "김무무", "스프링 재미있다", "1234");
        Long savedId = postRepository.save(new Post(dto)).getId();

        //when
        dto.setId(1L);
        postService.deletePost(savedId, "1234");

        //then
        Assertions.assertThat(postRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("게시글 비밀번호와 일치하지 않으면 에러 발생")
    public void passwordException() {
        //given
        PostRequestDTO dto = new PostRequestDTO(null, "스프링", "김무무", "스프링 재미있다", "1234");
        String savedId = postRepository.save(new Post(dto)).getPassword();
        String reqPw = "123456";

        //when
        //then
        assertThrows(NoSuchElementException.class,
                () -> postService.checkPassword(savedId, reqPw));
    }

    @Test
    @DisplayName("게시글 수정")
    public void update() {
        //given
        PostRequestDTO dto = new PostRequestDTO(null, "스프링", "김무무", "스프링 재미있다", "1234");
        Long savedId = postRepository.save(new Post(dto)).getId();

        PostRequestDTO updateDTO = new PostRequestDTO(null, "스프링 수정", "김무무 수정", "스프링 재미있다 수정", "1234");

        //when
        postService.updatePost(savedId, updateDTO);
        String getTitle = postRepository.findById(savedId).get().getTitle();

        //then
        Assertions.assertThat(getTitle).isEqualTo("스프링 수정");
    }
}