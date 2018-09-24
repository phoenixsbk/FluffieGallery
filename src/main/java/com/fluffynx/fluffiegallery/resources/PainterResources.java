package com.fluffynx.fluffiegallery.resources;

import com.fluffynx.fluffiegallery.entity.Painter;
import com.fluffynx.fluffiegallery.repos.PainterRepository;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
}
