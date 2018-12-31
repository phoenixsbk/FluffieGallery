package com.fluffynx.fluffiegallery.resources;

import com.fluffynx.fluffiegallery.entity.Comment;
import com.fluffynx.fluffiegallery.entity.Painter;
import com.fluffynx.fluffiegallery.entity.Painting;
import com.fluffynx.fluffiegallery.repos.CommentRepository;
import com.fluffynx.fluffiegallery.repos.PainterRepository;
import com.fluffynx.fluffiegallery.repos.PaintingRepository;
import com.fluffynx.fluffiegallery.resources.request.CommentRequest;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/comment")
public class CommentResources {

  @Autowired
  private CommentRepository commentRepo;

  @Autowired
  private PaintingRepository paintingRepo;

  @Autowired
  private PainterRepository painterRepo;

  @POST
  @Path("/add")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addComment(CommentRequest commentreq, @Context ContainerRequestContext context) {
    int paintingid = commentreq.getPaintingId();
    if (paintingid <= 0) {
      throw new WebApplicationException(Response.status(Status.BAD_REQUEST).build());
    }

    Painting painting = paintingRepo.findById(paintingid);
    if (painting == null) {
      throw new WebApplicationException(Response.status(Status.BAD_REQUEST).build());
    }

    String painterid = Optional.ofNullable(context.getCookies().get("pid")).map(Cookie::getValue)
        .orElseThrow(
            () -> new WebApplicationException(Response.status(Status.BAD_REQUEST).build()));

    Painter painter = painterRepo.findByUserId(painterid);
    if (painter == null) {
      throw new WebApplicationException(Response.status(Status.BAD_REQUEST).build());
    }

    Comment comment = new Comment();
    comment.setPainting(painting);
    comment.setCommenter(painter);
    comment.setDate(LocalDateTime.now());
    comment.setText(commentreq.getComment());

    commentRepo.save(comment);
    return Response.status(Status.CREATED).build();
  }
}
