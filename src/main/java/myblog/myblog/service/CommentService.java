package myblog.myblog.service;

import lombok.RequiredArgsConstructor;
import myblog.myblog.domain.Comment;
import myblog.myblog.domain.Member;
import myblog.myblog.domain.Post;
import myblog.myblog.dto.BasicResponseDTO;
import myblog.myblog.dto.comment.CommentRequestDTO;
import myblog.myblog.dto.comment.CommentResponseDTO;
import myblog.myblog.exception.custom_exeption.CommentException;
import myblog.myblog.exception.custom_exeption.PostException;
import myblog.myblog.repository.CommentRepository;
import myblog.myblog.repository.PostRepository;
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

    /**
     * 댓글 등록
     */
    @Transactional
    public ResponseEntity saveComment(Long postId, CommentRequestDTO commentRequestDTO, Member member) {
        Comment comment = new Comment(commentRequestDTO);

        //게시글 존재 여부 확인
        Post post = validatePost(postId);

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
    public ResponseEntity deleteComment(Long commentId, Member member) {
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
    public ResponseEntity updateComment(Long commentId, CommentRequestDTO commentRequestDTO, Member member) {
        // 댓글 존재 여부 확인
        Comment comment = validateComment(commentId);

        //작성자의 댓글인지 확인
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

    //작성자 일치 여부 판단
    private void isCommentAuthor(Member member, Comment comment) {
        if (!comment.getMember().getUsername().equals(member.getUsername())) {
            if (member.isAdmin()) return;
            throw new CommentException(ExceptionMessage.NO_AUTHORIZATION_EXCEPTION.getMessage());
        }
    }
}
