package com.fluffynx.fluffiegallery.repos;

import com.fluffynx.fluffiegallery.entity.Painter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PainterRepository extends JpaRepository<Painter, Integer> {
  Painter findById(int id);

  Painter findByUserId(String userId);
}
