package com.fluffynx.fluffiegallery.resources.model;

import com.fluffynx.fluffiegallery.entity.Painter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PainterTo {
  private int id;

  private List<PaintingTo> artifacts;

  private String userId;

  private String avatar;

  private String name;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public List<PaintingTo> getArtifacts() {
    return artifacts;
  }

  public void setArtifacts(
      List<PaintingTo> artifacts) {
    this.artifacts = artifacts;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public static PainterTo fromPainter(Painter painter, ModelInclude include) {
    PainterTo pt = new PainterTo();
    pt.setId(painter.getId());
    pt.setName(painter.getName());
    pt.setUserId(painter.getUserId());
    pt.setAvatar(painter.getAvatar());
    if (include.isIncludePainting()) {
      include.setIncludePainter(false);
      pt.setArtifacts(Optional.ofNullable(painter.getArtifacts())
          .map(pts -> pts.stream().map(p -> PaintingTo.fromPainting(p, include)).collect(
              Collectors.toList())).orElse(null));
    }
    return pt;
  }
}
