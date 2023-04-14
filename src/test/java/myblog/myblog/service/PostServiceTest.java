package myblog.myblog.service;

import jakarta.transaction.Transactional;
import myblog.myblog.domain.Post;
import myblog.myblog.dto.PostRequestDTO;
import myblog.myblog.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class PostServiceTest {
    @Autowired PostService postService;
    @Autowired PostRepository postRepository;

    @Test
    @DisplayName("전체 게시글 조회")
    public void findAll() {
        //given
        long id = 1L;
        Post post = new Post(id, "스프링", "김무무", "스프링 재미있다", "1234");
        postRepository.save(post);

        //when
        Long findId = postService.list().get(0).getId();

        //then
        Assertions.assertThat(id).isEqualTo(findId);
    }

    @Test
    @DisplayName("게시글 등록")
    public void register() {
        //given
        PostRequestDTO dto = new PostRequestDTO(null, "스프링", "김무무", "스프링 재미있다", "1234");

        //when
        Long getId = postService.register(dto).getId();

        //then
        Assertions.assertThat(1L).isEqualTo(getId);
    }

    @Test
    @DisplayName("특정 게시글 조회")
    public void findPostById() {
        //given
        PostRequestDTO dto1 = new PostRequestDTO(null, "스프링1", "김무무", "스프링 재미있다", "1234");
        postRepository.save(dto1.toEntity());
        PostRequestDTO dto2 = new PostRequestDTO(null, "스프링2", "김무무", "스프링 재미있다", "1234");
        postRepository.save(dto2.toEntity());

        //when
        String findTitle = postService.findPostById(2L).getTitle();

        //then
        Assertions.assertThat("스프링2").isEqualTo(findTitle);
    }

    @Test
    @DisplayName("특정 게시글이 존재하지 않으면 에러 발생")
    public void findPostByIdException() {
        //given
        PostRequestDTO dto1 = new PostRequestDTO(null, "스프링1", "김무무", "스프링 재미있다", "1234");
        postRepository.save(dto1.toEntity());

        //when
        //then
        assertThrows(NoSuchElementException.class, () -> postService.findPostById(2L));
    }

    @Test
    @DisplayName("게시글 삭제")
    public void delete() {
        //given
        PostRequestDTO dto = new PostRequestDTO(null, "스프링", "김무무", "스프링 재미있다", "1234");
        Long savedId = postRepository.save(dto.toEntity()).getId();

        //when
        dto.setId(1L);
        postService.delete(savedId, "1234");

        //then
        Assertions.assertThat(postRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("게시글 수정")
    public void update() {
        //given
        PostRequestDTO dto = new PostRequestDTO(null, "스프링", "김무무", "스프링 재미있다", "1234");
        postRepository.save(dto.toEntity());

        PostRequestDTO updateDTO = new PostRequestDTO(null, "스프링 수정", "김무무 수정", "스프링 재미있다 수정", "1234");

        //when
        postService.update(1L, updateDTO);
        String getTitle = postRepository.findById(1L).get().getTitle();

        //then
        Assertions.assertThat(getTitle).isEqualTo("스프링 수정");
    }
}