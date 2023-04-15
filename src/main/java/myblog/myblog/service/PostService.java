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
        //작성날짜 기준 내림차순
        //Post 객체를 PostResponseDTO 타입으로 변경하여 리스트로 반환
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * 게시글 등록
     */
    @Transactional
    public PostResponseDTO savePost(PostRequestDTO postRequestDTO) {
        //PostRequestDTO 타입을 POST 타입으로 변환하여 DB에 저장
        Post savedPost = postRepository.save(postRequestDTO.toEntity());
        return new PostResponseDTO(savedPost);
    }

    /**
     * 특정 게시글 조회
     */
    public PostResponseDTO findPostById(Long id) {
        //게시글 존재 여부 확인
        Post post = checkPost(id);
        return new PostResponseDTO(post);
    }

    /**
     * 게시글 삭제
     */
    @Transactional
    public String deletePost(Long id, String reqPassword) {
        //게시글 존재 여부 확인
        Post post = checkPost(id);

        String savedPassword = post.getPassword();

        //비밀번호 일치 여부 확인
        checkPassword(reqPassword, savedPassword);
        postRepository.deleteById(id);

        return "게시글 삭제 성공";
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public PostResponseDTO updatePost(Long id, PostRequestDTO reqDTO) {
        Post post = checkPost(id);

        String savedPassword = post.getPassword();
        String reqPassword = reqDTO.getPassword();

        //비밀번호 일치 여부 확인
        checkPassword(reqPassword, savedPassword);

        //더디체킹을 통해 변경 내용 적용
        //게시글 변경 내용 적용
        post.update(reqDTO);
        return new PostResponseDTO(post);
    }

    //게시글 존재 여부 확인
    public Post checkPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("게시글이 존재하지 않습니다.")
        );
        return post;
    }

    //게시글 비밀번호 일치 여부 확인
    public void checkPassword(String reqPassword, String savedPassword) {
        if (!savedPassword.equals(reqPassword)) {
            throw new NoSuchElementException("게시글 비밀번호가 다릅니다.");
        }
    }
}
