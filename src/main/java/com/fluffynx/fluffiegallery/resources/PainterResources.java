package com.fluffynx.fluffiegallery.resources;

import com.fluffynx.fluffiegallery.entity.Painter;
import com.fluffynx.fluffiegallery.repos.PainterRepository;
import java.util.List;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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

  @GET
  @Path("/all")
  @Produces(MediaType.APPLICATION_JSON)
  public List<Painter> getAllPainters() {
    return painterRepository.findAll();
  }

  @POST
  @Path("/create/{name}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response createPainter(@PathParam("name") String name) {
    if (StringUtils.isEmpty(name)) {
      throw new BadRequestException();
    }

    Painter p = new Painter();
    p.setName(name);
    p = painterRepository.save(p);
    return Response.status(Status.CREATED).entity(p).build();
  }

}
