package com.fluffynx.fluffiegallery.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fluffynx.fluffiegallery.FluffiegalleryApplication;
import com.fluffynx.fluffiegallery.entity.Week;
import com.fluffynx.fluffiegallery.repos.WeekRepository;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
      .withZone(ZoneId.of("GMT+8"));

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

  @Path("/all")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<Week> getAllWeeks() {
    List<Week> all = weekRepository.findByOrderByIdDesc();
    return Optional.ofNullable(all).orElse(Collections.emptyList());
  }

  @Path("/latest")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Week getLatestWeek() {
    List<Week> weeks = getAllWeeks();
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

    WeekPojo pojo;
    try {
      pojo = MAPPER.readValue(meta.getValue(), WeekPojo.class);
    } catch (IOException e) {
      e.printStackTrace();
      throw new WebApplicationException(Response.status(Status.BAD_REQUEST).build());
    }

    if (pojo == null || pojo.getName() == null || pojo.getStartDate() == null) {
      throw new WebApplicationException(Response.status(Status.BAD_REQUEST).build());
    }

    String photoName = pojo.getName();
    String originName = photodetail.getFileName();
    String weekfilename = photoName + originName.substring(originName.lastIndexOf(".") + 1);
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
    LocalDate ld = LocalDate.parse(pojo.getStartDate(), FORMATTER);
    week.setStartDate(Date.valueOf(ld));
    week.setPhotoPath(weekfilename);
    week = weekRepository.save(week);

    try {
      return Response.status(Status.CREATED).entity(week).build();
    } catch (Throwable t) {
      throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
          .entity(Collections.singletonMap("message", t.getMessage())).build());
    }
  }
}
