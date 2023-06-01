package PostService.repository;

import PostService.models.Post;
import PostService.repository.specification.PostSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor {
    Optional<Post> findByIdAndAuthorId(Long id, Long authorId);

    Post findByAuthorId(Long id);

    default List<Post> findAllByFilter(ZonedDateTime publishTime, Long id) {
        Specification<Post> specification = Specification.where(PostSpecification.byPublishTime(publishTime))
                .and(PostSpecification.byAuthorId(id));
        return this.findAll(specification);
    }
}
