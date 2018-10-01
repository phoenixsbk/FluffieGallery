package com.fluffynx.fluffiegallery.resources;

import com.fluffynx.fluffiegallery.FluffiegalleryApplication;
import com.fluffynx.fluffiegallery.entity.Painter;
import com.fluffynx.fluffiegallery.entity.Painting;
import com.fluffynx.fluffiegallery.entity.Week;
import com.fluffynx.fluffiegallery.repos.PainterRepository;
import com.fluffynx.fluffiegallery.repos.PaintingRepository;
import com.fluffynx.fluffiegallery.repos.WeekRepository;
import com.fluffynx.fluffiegallery.resources.model.ModelInclude;
import com.fluffynx.fluffiegallery.resources.model.PaintingTo;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Component;

@Component
@Path("/painting")
public class PaintingResources {

  @Context
  private ServletContext context;

  @Autowired
  private PaintingRepository paintingRepository;

  @Autowired
  private WeekRepository weekRepository;

  @Autowired
  private PainterRepository painterRepository;

  private File galleryFolder;

  @PostConstruct
  public void init() {
    ApplicationHome home = new ApplicationHome(FluffiegalleryApplication.class);
    galleryFolder = new File(home.getDir(), "static/gallery");
    if (!galleryFolder.exists() && !galleryFolder.mkdirs()) {
      throw new RuntimeException("Init failed");
    }
  }

  @Path("/paintingid/{paintingid}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public PaintingTo getPaintingById(@NotNull @PathParam("paintingid") int paintingid) {
    ModelInclude mi = new ModelInclude();
    mi.setIncludePainter(true);
    mi.setIncludeWeek(true);
    mi.setIncludeComment(true);

    return Optional.ofNullable(paintingRepository.findById(paintingid))
        .map(p -> PaintingTo.fromPainting(p, mi)).orElse(null);
  }

  @Path("/week/{weekid}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<PaintingTo> getPaintingsByWeek(@NotNull @PathParam("weekid") int weekid) {
    Week week = weekRepository.findById(weekid);
    if (week != null) {
      ModelInclude mi = new ModelInclude();
      mi.setIncludePainter(true);
      mi.setIncludeWeek(true);
      mi.setIncludeComment(true);
      return Optional.ofNullable(paintingRepository.findByWeek(week))
          .map(ps -> ps.stream().map(p -> PaintingTo.fromPainting(p, mi)).collect(
              Collectors.toList())).orElse(null);
    } else {
      return Collections.emptyList();
    }
  }

  @Path("/painter/{painterid}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<Painting> getPaintingsByPainterid(@NotNull @PathParam("painterid") int painterid) {
    Painter painter = painterRepository.findById(painterid);
    if (painter != null) {
      return paintingRepository.findByPainter(painter);
    } else {
      return Collections.emptyList();
    }
  }

  @POST
  @Path("/upload")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  public Response uploadPainting(@FormDataParam("weekid") int weekid,
      @FormDataParam("painterid") int painterid, @FormDataParam("desc") String description,
      @FormDataParam("painting") InputStream pstream,
      @FormDataParam("painting")
          FormDataContentDisposition filedetail) {
    Week week = weekRepository.findById(weekid);
    if (week == null) {
      throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
          .entity(Collections.singletonMap("message", "Week not found")).build());
    }
    Painter painter = painterRepository.findById(painterid);
    if (painter == null) {
      throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
          .entity(Collections.singletonMap("message", "Painter not found")).build());
    }

    File weekDir = new File(galleryFolder, "week_" + week.getName());
    if (!weekDir.exists() && !weekDir.mkdirs()) {
      throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).build());
    }

    String ext = filedetail.getFileName();
    int extid = ext.lastIndexOf(".");
    if (extid != -1) {
      ext = ext.substring(extid + 1);
    }

    String filename = painter.getName() + "." + ext;
    byte[] buffer = new byte[8192];
    int len = -1;
    try (FileOutputStream fo = new FileOutputStream(new File(weekDir, filename))) {
      while ((len = pstream.read(buffer, 0, 8192)) > 0) {
        fo.write(buffer, 0, len);
      }
      fo.flush();

    } catch (IOException e) {
      e.printStackTrace();
    }

    Painting painting = new Painting();
    painting.setDescription(description);
    painting.setPainter(painter);
    painting.setFilePath(filename);
    painting.setWeek(week);
    paintingRepository.save(painting);
    return Response.status(Status.CREATED).entity(Collections.singletonMap("filename", filename))
        .build();
  }
}
