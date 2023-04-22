package myblog.myblog.repository;

import myblog.myblog.domain.CommentLikes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikesRepository extends JpaRepository<CommentLikes, Long> {
    boolean existsByCommentIdAndMemberId(Long comment_id, Long member_id);
    CommentLikes findByCommentIdAndMemberId(Long comment_id, Long member_id);
}
