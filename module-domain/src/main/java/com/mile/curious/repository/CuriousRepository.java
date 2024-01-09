package com.mile.curious.repository;

import com.mile.curious.domain.Curious;
import com.mile.post.domain.Post;
import com.mile.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuriousRepository extends JpaRepository<Curious, Long> {
    boolean existsByPostAndUser(Post post, User user);
    Curious findByPostAndUser(Post post, User user);
}