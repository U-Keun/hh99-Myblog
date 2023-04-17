package myblog.myblog.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import myblog.myblog.domain.Member;
import myblog.myblog.domain.Post;
import myblog.myblog.domain.StatusCode;
import myblog.myblog.dto.MessageDTO;
import myblog.myblog.dto.PostRequestDTO;
import myblog.myblog.dto.PostResponseDTO;
import myblog.myblog.jwt.JwtUtil;
import myblog.myblog.repository.MemberRepository;
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
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

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
    public MessageDTO savePost(PostRequestDTO postRequestDTO, HttpServletRequest request) {

        Post post = new Post(postRequestDTO);
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            // Token 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }
            // 존재하는 회원인지 확인
            Member member = checkMember(claims);

            post.setMember(member);
            postRepository.save(post);
            return new MessageDTO(StatusCode.OK, "savePost success", new PostResponseDTO(post));
        } else {
            return new MessageDTO(StatusCode.BAD_REQUEST, "savePost fail", null);
        }
    }

    /**
     * 특정 게시글 조회
     */
    public MessageDTO findPostById(Long id) {
        //게시글 존재 여부 확인
        Post post = checkPost(id);
        return new MessageDTO(StatusCode.OK, "findPost success", new PostResponseDTO(post));
    }

    /**
     * 게시글 삭제
     */
    @Transactional
    public MessageDTO deletePost(Long id, HttpServletRequest request) {

        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            // Token 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }
            //존재하는 회원인지 확인
            Member member = checkMember(claims);

            // 게시글 존재 여부 확인
            Post post = checkPost(id);

            post.setMember(member);
            postRepository.save(post);
            return new MessageDTO(StatusCode.OK, "savePost success", new PostResponseDTO(post));
        } else {
            return new MessageDTO(StatusCode.BAD_REQUEST, "savePost fail", null);
        }
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public MessageDTO updatePost(Long id, PostRequestDTO requestDTO, HttpServletRequest request) {

        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            // Token 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            //존재하는 회원인지 확인
            Member member = checkMember(claims);

            // 게시글 존재 여부 확인
            Post post = checkPost(id);

            post.update(requestDTO);
            return new MessageDTO(StatusCode.OK, "update success", new PostResponseDTO(post));

        } else {
            return new MessageDTO(StatusCode.BAD_REQUEST, "update fail", null);
        }
    }

    //게시글 존재 여부 확인
    private Post checkPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("게시글이 존재하지 않습니다.")
        );
        return post;
    }


    //회원 존재 여부 확인
    private Member checkMember(Claims claims) {
        Member member = memberRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
        );
        return member;
    }
}
