package com.fluffynx.fluffiegallery.resources;

import com.fluffynx.fluffiegallery.FluffiegalleryApplication;
import com.fluffynx.fluffiegallery.entity.Painter;
import com.fluffynx.fluffiegallery.repos.PainterRepository;
import com.fluffynx.fluffiegallery.resources.request.PainterRequest;
import com.fluffynx.fluffiegallery.utils.SHAUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Path("/painter")
public class PainterResources {

  @Autowired
  private PainterRepository painterRepository;

  @Autowired
  private SHAUtil shaUtil;

  private File avatarFolder;

  @PostConstruct
  public void init() {
    ApplicationHome home = new ApplicationHome(FluffiegalleryApplication.class);
    avatarFolder = new File(home.getDir(), "static/avatar");
    if (!avatarFolder.exists() && !avatarFolder.mkdirs()) {
      throw new RuntimeException("Init failed");
    }
  }

  @GET
  @Path("/all")
  @Produces(MediaType.APPLICATION_JSON)
  public List<Painter> getAllPainters() {
    return painterRepository.findAll();
  }

  @POST
  @Path("/create")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createPainter(PainterRequest p) {
    if (p == null || StringUtils.isEmpty(p.getName()) || StringUtils.isEmpty(p.getUserId())
        || StringUtils.isEmpty(p.getPasswd())) {
      throw new BadRequestException();
    }

    Painter pp = new Painter();
    pp.setName(p.getName());
    pp.setUserId(p.getUserId());
    pp.setPasswd(shaUtil.hash(p.getPasswd()));
    Painter out = painterRepository.save(pp);
    out.setPasswd(null);
    return Response.status(Status.CREATED).entity(out).build();
  }

  @POST
  @Path("/avatar")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response uploadAvatar(@FormDataParam("avatar") InputStream ai, @FormDataParam("avatar")
      FormDataContentDisposition fileinfo, @FormDataParam("pid") int painterid) {
    Painter p = painterRepository.findById(painterid);
    if (p == null) {
      throw new BadRequestException();
    }

    String fname = fileinfo.getFileName();
    String extname = fname.substring(fname.lastIndexOf("."));
    byte[] buffer = new byte[8192];
    int len = -1;
    try (FileOutputStream fo = new FileOutputStream(new File(avatarFolder, p.getName() + extname))) {
      while ((len = ai.read(buffer, 0, 8192)) > 0) {
        fo.write(buffer, 0, len);
      }
      fo.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }

    p.setAvatar(p.getName() + extname);
    painterRepository.save(p);
    return Response.status(Status.CREATED).build();
  }
}
