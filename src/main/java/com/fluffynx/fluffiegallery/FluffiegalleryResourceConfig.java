package com.fluffynx.fluffiegallery;

import com.fluffynx.fluffiegallery.config.AuthFilter;
import com.fluffynx.fluffiegallery.resources.CommentResources;
import com.fluffynx.fluffiegallery.resources.PainterResources;
import com.fluffynx.fluffiegallery.resources.PaintingResources;
import com.fluffynx.fluffiegallery.resources.WeekResources;
import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ApplicationPath("/")
public class FluffiegalleryResourceConfig extends ResourceConfig {

  public FluffiegalleryResourceConfig() {
    register(PaintingResources.class);
    register(WeekResources.class);
    register(PainterResources.class);
    register(CommentResources.class);
    register(MultiPartFeature.class);
    register(AuthFilter.class);
    property(ServletProperties.FILTER_FORWARD_ON_404, true);
  }
}
