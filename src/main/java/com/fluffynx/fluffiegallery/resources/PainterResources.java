package com.fluffynx.fluffiegallery.resources;

import com.fluffynx.fluffiegallery.FluffiegalleryApplication;
import com.fluffynx.fluffiegallery.entity.Painter;
import com.fluffynx.fluffiegallery.entity.PainterToken;
import com.fluffynx.fluffiegallery.repos.PainterRepository;
import com.fluffynx.fluffiegallery.repos.PainterTokenRepository;
import com.fluffynx.fluffiegallery.resources.model.ModelInclude;
import com.fluffynx.fluffiegallery.resources.model.PainterTo;
import com.fluffynx.fluffiegallery.resources.request.PainterRequest;
import com.fluffynx.fluffiegallery.utils.SHAUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
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
  private PainterTokenRepository painterTokenRepository;

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

  @POST
  @Path("/login")
  @Produces(MediaType.APPLICATION_JSON)
  public Response loginPainter(@NotNull @FormParam("uid") String uid,
      @NotNull @FormParam("pwd") String pwd) {
    Painter p = painterRepository.findByUserId(uid);
    if (p == null) {
      throw new BadRequestException();
    }

    String pwdd = shaUtil.hash(pwd);
    if (pwdd != null && pwdd.equals(p.getPasswd())) {
      String genToken = UUID.randomUUID().toString();
      PainterToken token = painterTokenRepository.findByPainter(p);
      if (token != null) {
        token.setGenTime(LocalDateTime.now());
        token.setToken(genToken);
      } else {
        token = new PainterToken();
        token.setPainter(p);
        token.setToken(genToken);
        token.setGenTime(LocalDateTime.now());
      }
      painterTokenRepository.save(token);
      return Response.ok().cookie(new NewCookie("pid", uid, "/", null, null, -1, false),
          new NewCookie("ptk", genToken, "/", null, null, -1, false)).build();
    } else {
      return Response.status(Status.UNAUTHORIZED).build();
    }
  }

  @POST
  @Path("/logout")
  public Response logout(@CookieParam("pid") String uid) {
    Painter p = painterRepository.findByUserId(uid);
    if (p == null) {
      throw new BadRequestException();
    }

    PainterToken token = painterTokenRepository.findByPainter(p);
    if (token != null) {
      token.setToken(null);
      painterTokenRepository.save(token);
    }

    return Response.ok().cookie(new NewCookie("pid", null, "/", null, null, -1, false),
        new NewCookie("ptk", null, "/", null, null, -1, false)).build();
  }

  @GET
  @Path("/painterid/{painterid}")
  @Produces(MediaType.APPLICATION_JSON)
  public PainterTo getPainter(@NotNull @PathParam("painterid") int painterid) {
    Painter painter = painterRepository.findById(painterid);
    if (painter == null) {
      throw new BadRequestException();
    }

    ModelInclude mi = new ModelInclude();
    mi.setIncludePainting(true);
    mi.setIncludeWeek(true);
    return PainterTo.fromPainter(painter, mi);
  }

  @GET
  @Path("/all")
  @Produces(MediaType.APPLICATION_JSON)
  public List<PainterTo> getAllPainters() {
    List<Painter> painters = painterRepository.findAll();
    if (painters != null) {
      ModelInclude mi = new ModelInclude();
      mi.setIncludePainting(true);
      return painters.stream().map(p -> PainterTo.fromPainter(p, mi)).collect(Collectors.toList());
    } else {
      return Collections.emptyList();
    }
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
    pp.setDescription(p.getDescription());
    Painter out = painterRepository.save(pp);
    out.setPasswd(null);
    return Response.status(Status.CREATED).entity(PainterTo.fromPainter(out, new ModelInclude())).build();
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
    try (FileOutputStream fo = new FileOutputStream(
        new File(avatarFolder, p.getName() + extname))) {
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
