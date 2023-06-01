package PostService.repository.specification;

import PostService.models.Post;
import org.springframework.data.jpa.domain.Specification;

import java.time.ZonedDateTime;

public class PostSpecification {
    public static Specification<Post> byPublishTime(ZonedDateTime publishTime) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("publishTime"), publishTime));
    }

    public static Specification<Post> byAuthorId(Long id) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("authorId"), id);
    }
}
