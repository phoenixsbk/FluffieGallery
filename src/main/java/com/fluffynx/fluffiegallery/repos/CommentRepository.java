package com.fluffynx.fluffiegallery.repos;

import com.fluffynx.fluffiegallery.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
