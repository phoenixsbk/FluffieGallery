package com.fluffynx.fluffiegallery.repos;

import com.fluffynx.fluffiegallery.entity.Week;
import java.sql.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeekRepository extends JpaRepository<Week, Integer> {
  Week findById(int id);

  public Week findByName(String name);

  public Week findByStartDate(Date startDate);

  public List<Week> findByOrderByIdDesc();
}
