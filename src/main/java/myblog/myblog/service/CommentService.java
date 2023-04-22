package myblog.myblog.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myblog.myblog.domain.*;
import myblog.myblog.dto.BasicResponseDTO;
import myblog.myblog.dto.comment.CommentRequestDTO;
import myblog.myblog.dto.comment.CommentResponseDTO;
import myblog.myblog.exception.custom_exeption.CommentException;
import myblog.myblog.exception.custom_exeption.PostException;
import myblog.myblog.repository.CommentLikesRepository;
import myblog.myblog.repository.CommentRepository;
import myblog.myblog.repository.PostRepository;
import myblog.myblog.util.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentLikesRepository commentLikesRepository;

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
        log.info("댓글 삭제");
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

    /**
     * 댓글 좋아요
     */
    public ResponseEntity updateLikes(Long id, Member member) {
        // 댓글 존재 여부 확인
        Comment comment = validateComment(id);

        // 게시글에 현재 유저의 좋아요 유무 확인
        if (commentLikesRepository.existsByCommentIdAndMemberId(id, member.getId())){
            CommentLikes commentLikes = commentLikesRepository.findByCommentIdAndMemberId(id, member.getId());
            commentLikesRepository.delete(commentLikes);
            comment.updateLikes(false);
        } else { // 현재 유저의 좋아요 흔적 없음 -> 좋아요
            commentLikesRepository.save(new CommentLikes(comment, member));
            comment.updateLikes(true);
        }
        BasicResponseDTO basicResponseDTO = BasicResponseDTO.setSuccess("likes update success", null);
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
