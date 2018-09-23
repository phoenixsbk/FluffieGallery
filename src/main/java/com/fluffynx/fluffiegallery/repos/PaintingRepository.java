package com.fluffynx.fluffiegallery.repos;

import com.fluffynx.fluffiegallery.entity.Painter;
import com.fluffynx.fluffiegallery.entity.Painting;
import com.fluffynx.fluffiegallery.entity.Week;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaintingRepository extends JpaRepository<Painting, Integer> {
  public List<Painting> findByWeek(Week week);

  public List<Painting> findByPainter(Painter painter);
}
