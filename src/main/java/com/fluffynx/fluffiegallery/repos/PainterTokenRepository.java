package com.fluffynx.fluffiegallery.repos;

import com.fluffynx.fluffiegallery.entity.Painter;
import com.fluffynx.fluffiegallery.entity.PainterToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PainterTokenRepository extends JpaRepository<PainterToken, Integer> {
  PainterToken findByPainter(Painter painter);
}
