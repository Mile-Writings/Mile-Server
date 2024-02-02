package com.mile.curious.repository;
import com.mile.curious.domain.Curious;
import com.mile.post.domain.Post;
import com.mile.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CuriousRepository extends JpaRepository<Curious, Long> {
    boolean existsByPostAndUser(Post post, User user);
    Curious findByPostAndUser(Post post, User user);

    @Modifying
    @Query("delete from Curious c where c.post = :post")
    void deleteAllByPost(@Param("post") final Post post);
}
