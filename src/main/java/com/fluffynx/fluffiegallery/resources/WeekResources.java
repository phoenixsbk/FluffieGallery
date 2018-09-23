package com.fluffynx.fluffiegallery.resources;

import com.fluffynx.fluffiegallery.entity.Week;
import com.fluffynx.fluffiegallery.repos.WeekRepository;
import com.fluffynx.fluffiegallery.resources.model.WeekReq;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Path("/week")
public class WeekResources {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
      .withZone(ZoneId.of("GMT+8"));

  @Autowired
  private WeekRepository weekRepository;

  @Path("/all")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<Week> getAllWeeks() {
    List<Week> all = weekRepository.findByOrderByIdDesc();
    return Optional.ofNullable(all).orElse(Collections.emptyList());
  }

  @POST
  @Path("/create")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createWeek(WeekReq req) {
    if (StringUtils.isEmpty(req.getName()) || StringUtils.isEmpty(req.getStartDate())) {
      throw new WebApplicationException(Response.status(Status.BAD_REQUEST).build());
    }

    Week week = new Week();
    week.setName(req.getName());
    LocalDate ld = LocalDate.parse(req.getStartDate(), FORMATTER);
    week.setStartDate(Date.valueOf(ld));

    try {
      return Response.status(Status.CREATED).entity(weekRepository.save(week)).build();
    } catch (Throwable t) {
      throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
          .entity(Collections.singletonMap("message", t.getMessage())).build());
    }
  }
}
