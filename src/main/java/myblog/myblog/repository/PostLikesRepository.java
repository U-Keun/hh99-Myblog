package myblog.myblog.repository;

import myblog.myblog.domain.Member;
import myblog.myblog.domain.PostLikes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikesRepository extends JpaRepository<PostLikes, Long> {
    boolean existsByPostIdAndMemberId(Long post_id, Long member_id);
    PostLikes findByPostIdAndMemberId(Long post_id, Long member_id);
}
