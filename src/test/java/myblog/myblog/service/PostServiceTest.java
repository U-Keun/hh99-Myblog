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
}