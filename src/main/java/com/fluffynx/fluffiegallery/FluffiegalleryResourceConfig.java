package com.fluffynx.fluffiegallery;

import com.fluffynx.fluffiegallery.resources.PaintingResources;
import com.fluffynx.fluffiegallery.resources.WeekResources;
import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
@ApplicationPath("/")
public class FluffiegalleryResourceConfig extends ResourceConfig {
  public FluffiegalleryResourceConfig() {
    register(PaintingResources.class);
    register(WeekResources.class);
  }
}
