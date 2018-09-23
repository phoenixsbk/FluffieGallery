package com.fluffynx.fluffiegallery.resources;

import com.fluffynx.fluffiegallery.entity.Painter;
import com.fluffynx.fluffiegallery.entity.Painting;
import com.fluffynx.fluffiegallery.entity.Week;
import com.fluffynx.fluffiegallery.repos.PainterRepository;
import com.fluffynx.fluffiegallery.repos.PaintingRepository;
import com.fluffynx.fluffiegallery.repos.WeekRepository;
import java.util.Collections;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/paintings")
public class PaintingResources {

  @Autowired
  private PaintingRepository paintingRepository;

  @Autowired
  private WeekRepository weekRepository;

  @Autowired
  private PainterRepository painterRepository;

  @Path("/week/{weekid}")
  @GET
  public List<Painting> getPaintingsByWeek(@NotNull @PathParam("weekid") int weekid) {
    Week week = weekRepository.getOne(weekid);
    if (week != null) {
      return paintingRepository.findByWeek(week);
    } else {
      return Collections.emptyList();
    }
  }

  @Path("/painter/{painterid}")
  @GET
  public List<Painting> getPaintingsByPainterid(@NotNull @PathParam("painterid") int painterid) {
    Painter painter = painterRepository.getOne(painterid);
    if (painter != null) {
      return paintingRepository.findByPainter(painter);
    } else {
      return Collections.emptyList();
    }
  }
}
