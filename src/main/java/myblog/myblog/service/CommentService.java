package myblog.myblog.service;

import lombok.RequiredArgsConstructor;
import myblog.myblog.domain.Comment;
import myblog.myblog.domain.Member;
import myblog.myblog.domain.Post;
import myblog.myblog.dto.BasicResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import myblog.myblog.dto.comment.CommentRequestDTO;
import myblog.myblog.dto.comment.CommentResponseDTO;
import myblog.myblog.exception.CommentException;
import myblog.myblog.exception.MemberException;
import myblog.myblog.exception.PostException;
import myblog.myblog.repository.CommentRepository;
import myblog.myblog.repository.MemberRepository;
import myblog.myblog.repository.PostRepository;
import myblog.myblog.jwt.TokenProvider;
import myblog.myblog.util.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    /**
     * 댓글 등록
     */
    @Transactional
    public ResponseEntity saveComment(Long postId, CommentRequestDTO commentRequestDTO, HttpServletRequest request) {
        String username = getUserInfoFromToken(request);
        Comment comment = new Comment(commentRequestDTO);

        //게시글 존재 여부 확인
        Post post = validatePost(postId);

        // 회원 여부 확인
        Member member = validateMember(username);

        // post의 댓글 리스트에 추가
        post.addComment(comment);
        comment.setMember(member);

        commentRepository.save(comment);
        BasicResponseDTO basicResponseDTO = BasicResponseDTO.setSuccess("save success", new CommentResponseDTO(comment));
        return new ResponseEntity(basicResponseDTO, HttpStatus.OK);
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public ResponseEntity deleteComment(Long commentId, HttpServletRequest request) {

        String username = getUserInfoFromToken(request);

        //회원 레포지토리에서 회원 가져오기
        Member member = validateMember(username);

        //댓글 존재 여부 확인
        Comment comment = validateComment(commentId);

        //작성자의 댓글인지 확인
        isCommentAuthor(member, comment);

        commentRepository.deleteById(commentId);
        BasicResponseDTO basicResponseDTO = BasicResponseDTO.setSuccess("delete success", null);
        return new ResponseEntity(basicResponseDTO, HttpStatus.OK);
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public ResponseEntity updateComment(Long commentId, CommentRequestDTO commentRequestDTO, HttpServletRequest request) {

        String username = getUserInfoFromToken(request);

        // 회원 레포지토리에서 회원 가져오기
        Member member = validateMember(username);

        // 댓글 존재 여부 확인
        Comment comment = validateComment(commentId);

        //작성자의 게시글인지 확인
        isCommentAuthor(member, comment);

        comment.update(commentRequestDTO);
        BasicResponseDTO basicResponseDTO = BasicResponseDTO.setSuccess("update success", new CommentResponseDTO(comment));
        return new ResponseEntity(basicResponseDTO, HttpStatus.OK);
    }

    //댓글 존재 여부 확인
    private Comment validateComment(Long id) {
        return commentRepository.findById(id).orElseThrow(
                () -> new CommentException(ExceptionMessage.NO_SUCH_COMMENT_EXCEPTION.getMessage())
        );
    }

    //게시글 존재 여부 확인
    private Post validatePost(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new PostException(ExceptionMessage.NO_SUCH_BOARD_EXCEPTION.getMessage())
        );
    }

    //회원 존재 여부 확인
    private Member validateMember(String username) {
        return memberRepository.findByUsername(username).orElseThrow(
                () -> new MemberException(ExceptionMessage.NO_SUCH_MEMBER_EXCEPTION.getMessage())
        );
    }

    //작성자 일치 여부 판단
    private void isCommentAuthor(Member member, Comment comment) {
        if (comment.getMember() != member) {
            if (member.isAdmin()) return;
            throw new CommentException(ExceptionMessage.NO_AUTHORIZATION_EXCEPTION.getMessage());
        }
    }

    //토큰에서 사용자 정보 가져오기
    private String getUserInfoFromToken(HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request, "Access");
        return tokenProvider.getUserInfoFromToken(token);
    }
}
