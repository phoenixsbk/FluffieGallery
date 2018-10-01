package com.fluffynx.fluffiegallery.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fluffynx.fluffiegallery.FluffiegalleryApplication;
import com.fluffynx.fluffiegallery.entity.Week;
import com.fluffynx.fluffiegallery.repos.WeekRepository;
import com.fluffynx.fluffiegallery.resources.model.ModelInclude;
import com.fluffynx.fluffiegallery.resources.model.WeekTo;
import com.fluffynx.fluffiegallery.resources.request.WeekRequest;
import com.fluffynx.fluffiegallery.utils.DateUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Component;

@Component
@Path("/week")
public class WeekResources {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  @Autowired
  private WeekRepository weekRepository;

  private File weekPhotoPath;

  @PostConstruct
  public void init() {
    ApplicationHome home = new ApplicationHome(FluffiegalleryApplication.class);
    weekPhotoPath = new File(home.getDir(), "static/weekphoto");
    if (!weekPhotoPath.exists() && !weekPhotoPath.mkdirs()) {
      throw new RuntimeException("Init failed");
    }
  }

  @Path("/weekid/{weekid}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public WeekTo getWeekById(@NotNull @PathParam("weekid") int weekid) {
    ModelInclude mi = new ModelInclude();
    mi.setIncludePainting(true);
    mi.setIncludePainter(true);
    return Optional.ofNullable(weekRepository.findById(weekid)).map(w -> WeekTo.fromWeek(w, mi))
        .orElse(null);
  }

  @Path("/all")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<WeekTo> getAllWeeks() {
    ModelInclude mi = new ModelInclude();
    mi.setIncludePainting(true);
    mi.setIncludePainter(true);
    List<Week> all = weekRepository.findByOrderByIdDesc();
    return Optional.ofNullable(all)
        .map(wks -> wks.stream().map(wk -> WeekTo.fromWeek(wk, mi)).collect(Collectors.toList()))
        .orElse(Collections.emptyList());
  }

  @Path("/latest")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public WeekTo getLatestWeek() {
    List<WeekTo> weeks = getAllWeeks();
    if (!weeks.isEmpty()) {
      return weeks.get(0);
    } else {
      return null;
    }
  }

  @POST
  @Path("/create")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createWeek(@FormDataParam("meta") FormDataBodyPart meta, @FormDataParam("photo")
      InputStream photostream, @FormDataParam("photo") FormDataContentDisposition photodetail) {
    if (meta == null || photostream == null) {
      throw new WebApplicationException(Response.status(Status.BAD_REQUEST).build());
    }

    WeekRequest pojo;
    try {
      pojo = MAPPER.readValue(meta.getValue(), WeekRequest.class);
    } catch (IOException e) {
      e.printStackTrace();
      throw new WebApplicationException(Response.status(Status.BAD_REQUEST).build());
    }

    if (pojo == null || pojo.getName() == null || pojo.getStartDate() == null) {
      throw new WebApplicationException(Response.status(Status.BAD_REQUEST).build());
    }

    String photoName = pojo.getName();
    String originName = photodetail.getFileName();
    String weekfilename = photoName + originName.substring(originName.lastIndexOf("."));
    byte[] buffer = new byte[8192];
    int len = -1;
    try (FileOutputStream fs = new FileOutputStream(new File(weekPhotoPath, weekfilename))) {
      while ((len = photostream.read(buffer, 0, 8192)) > 0) {
        fs.write(buffer, 0, len);
      }
      fs.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }

    Week week = new Week();
    week.setName(photoName);
    week.setDescription(pojo.getDescription());
    week.setStartDate(DateUtil.parseDate(pojo.getStartDate() + " 00:00:00"));
    week.setPhotoPath(weekfilename);
    week = weekRepository.save(week);

    try {
      return Response.status(Status.CREATED).entity(WeekTo.fromWeek(week, new ModelInclude()))
          .build();
    } catch (Throwable t) {
      throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
          .entity(Collections.singletonMap("message", t.getMessage())).build());
    }
  }
}
