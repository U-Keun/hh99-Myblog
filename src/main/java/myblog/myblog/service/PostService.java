package myblog.myblog.service;

import lombok.RequiredArgsConstructor;
import myblog.myblog.domain.Post;
import myblog.myblog.dto.PostRequestDTO;
import myblog.myblog.dto.PostResponseDTO;
import myblog.myblog.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    /**
     * 전체 게시글 조회
     */
    public List<PostResponseDTO> list() {
        return postRepository.findAll().stream()
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * 게시글 등록
     */
    public PostResponseDTO register(PostRequestDTO postRequestDTO) {
        Post savedPost = postRepository.save(postRequestDTO.toEntity());
        return new PostResponseDTO(savedPost);
    }

    /**
     * 특정 게시글 조회
     */
    public PostResponseDTO findPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("게시글이 존재하지 않습니다.")
        );
        return new PostResponseDTO(post);
    }

    /**
     * 게시글 삭제
     */
    public void delete(Long id, String reqPassword) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("게시글이 존재하지 않습니다.")
        );

        String savedPassword = post.getPassword();

        //입력한 비밀번호와 저장된 비밀번호가 같으면 게시글 삭제
        if (savedPassword.equals(reqPassword)) {
            postRepository.deleteById(id);
        }
    }
}
