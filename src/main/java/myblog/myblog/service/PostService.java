package myblog.myblog.service;

import lombok.RequiredArgsConstructor;
import myblog.myblog.domain.Member;
import myblog.myblog.domain.Post;
import myblog.myblog.domain.PostLikes;
import myblog.myblog.dto.BasicResponseDto;
import myblog.myblog.dto.post.PostRequestDTO;
import myblog.myblog.dto.post.PostResponseDTO;
import myblog.myblog.exception.custom_exeption.PostException;
import myblog.myblog.repository.PostLikesRepository;
import myblog.myblog.repository.PostRepository;
import myblog.myblog.util.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final PostLikesRepository postLikesRepository;

    /**
     * 전체 게시글 조회
     */
    public ResponseEntity list() {
        //수정날짜 기준 내림차순
        //Post 객체를 PostResponseDTO 타입으로 변경하여 리스트로 반환
        List<PostResponseDTO> postResponseDTOS = postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());
        BasicResponseDto basicResponseDTO = BasicResponseDto.setSuccess("list success", postResponseDTOS);
        return new ResponseEntity(basicResponseDTO, HttpStatus.OK);
    }

    /**
     * 게시글 등록
     */
    @Transactional
    public ResponseEntity savePost(PostRequestDTO postRequestDTO, Member member) {
        Post post = new Post(postRequestDTO);
        post.setMember(member);

        postRepository.save(post);
        BasicResponseDto basicResponseDTO = BasicResponseDto.setSuccess("save success", new PostResponseDTO(post));
        return new ResponseEntity(basicResponseDTO, HttpStatus.OK);
    }

    /**
     * 특정 게시글 조회
     */
    public ResponseEntity findPostById(Long id) {
        //게시글 존재 여부 확인
        Post post = validatePost(id);
        BasicResponseDto basicResponseDTO = BasicResponseDto.setSuccess("findOne success", new PostResponseDTO(post));
        return new ResponseEntity(basicResponseDTO, HttpStatus.OK);
    }

    /**
     * 게시글 삭제
     */
    @Transactional
    public ResponseEntity deletePost(Long id, Member member) {
        //게시글 존재 여부 확인
        Post post = validatePost(id);

        //작성자의 게시글인지 확인
        isPostAuthor(member, post);

        postRepository.deleteById(id);
        BasicResponseDto basicResponseDTO = BasicResponseDto.setSuccess("delete success", null);
        return new ResponseEntity(basicResponseDTO, HttpStatus.OK);
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public ResponseEntity updatePost(Long id, PostRequestDTO postRequestDTO, Member member) {
        // 게시글 존재 여부 확인
        Post post = validatePost(id);

        //작성자의 게시글인지 확인, ADMIN 여부 확인
        isPostAuthor(member, post);

        post.update(postRequestDTO);
        BasicResponseDto basicResponseDTO = BasicResponseDto.setSuccess("update success", new PostResponseDTO(post));
        return new ResponseEntity(basicResponseDTO, HttpStatus.OK);
    }

    /**
     * 게시글 좋아요
     */
    @Transactional
    public ResponseEntity updateLikes(Long id, Member member) {
        // 게시글 존재 여부 확인
        Post post = validatePost(id);

        // 게시글에 현재 유저의 좋아요 유무 확인
        if (postLikesRepository.existsByPostIdAndMemberId(id, member.getId())){
            PostLikes postLikes = postLikesRepository.findByPostIdAndMemberId(id, member.getId());
            postLikesRepository.delete(postLikes);
            post.updateLikes(false);
        } else { // 현재 유저의 좋아요 흔적 없음 -> 좋아요
            postLikesRepository.save(new PostLikes(post, member));
            post.updateLikes(true);
        }
        BasicResponseDto basicResponseDTO = BasicResponseDto.setSuccess("likes update success", null);
        return new ResponseEntity(basicResponseDTO, HttpStatus.OK);
    }

    //게시글 존재 여부 확인
    private Post validatePost(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new PostException(ExceptionMessage.NO_SUCH_BOARD_EXCEPTION.getMessage())
        );
    }

    //작성자 일치 여부 판단
    private void isPostAuthor(Member member, Post post) {
        if (!post.getMember().getUsername().equals(member.getUsername())) {
            if (member.isAdmin()) return;
            throw new PostException(ExceptionMessage.NO_AUTHORIZATION_EXCEPTION.getMessage());
        }
    }
}
