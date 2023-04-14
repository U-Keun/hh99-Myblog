package myblog.myblog.service;

import jakarta.transaction.Transactional;
import myblog.myblog.domain.Post;
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
    @DisplayName("전체 회원 조회")
    public void findAll() {
        //given
        long id = 1L;
        Post post = new Post(id, "스프링", "김무무", "스프링 재미있다", "1234");
        postRepository.save(post);

        //when
        Long findId = postService.findAll().get(0).getId();

        //then
        Assertions.assertThat(id).isEqualTo(findId);
    }
}