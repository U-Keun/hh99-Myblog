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
    @Transactional
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
    @Transactional
    public String delete(Long id, String reqPassword) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("게시글이 존재하지 않습니다.")
        );

        String savedPassword = post.getPassword();

        //입력한 비밀번호와 저장된 비밀번호가 같으면 게시글 삭제
        if (!savedPassword.equals(reqPassword)) {
            throw new NoSuchElementException("게시글 비밀번호가 다릅니다.");
        }
        postRepository.deleteById(id);

        return "게시글 삭제 성공";
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public PostResponseDTO update(Long id, PostRequestDTO reqDTO) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("게시글이 존재하지 않습니다.")
        );

        String savedPassword = post.getPassword();
        String reqPassword = reqDTO.getPassword();

        //입력한 비밀번호와 저장된 비밀번호가 같으면 게시글 수정
        if (!savedPassword.equals(reqPassword)) {
            throw new NoSuchElementException("게시글 비밀번호가 다릅니다.");
        }

        //영속성 컨텍스트로 관리되므로 DB에 변경내용이 반영됨
        //게시글 변경 내용 적용
        post.update(reqDTO);
        return new PostResponseDTO(post);
    }
}
