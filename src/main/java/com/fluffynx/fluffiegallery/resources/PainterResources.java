package com.fluffynx.fluffiegallery.resources;

import com.fluffynx.fluffiegallery.entity.Painter;
import com.fluffynx.fluffiegallery.repos.PainterRepository;
import com.fluffynx.fluffiegallery.utils.SHAUtil;
import java.util.List;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Path("/painter")
public class PainterResources {

  @Autowired
  private PainterRepository painterRepository;

  @Autowired
  private SHAUtil shaUtil;

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
  public Response createPainter(PainterPojo p) {
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
}
