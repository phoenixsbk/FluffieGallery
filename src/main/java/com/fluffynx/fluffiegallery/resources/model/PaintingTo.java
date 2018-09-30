package com.fluffynx.fluffiegallery.resources.model;

import com.fluffynx.fluffiegallery.entity.Painting;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PaintingTo {
  private int id;

  private WeekTo week;

  private String name;

  private PainterTo painter;

  private List<CommentTo> comments;

  private String filePath;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public WeekTo getWeek() {
    return week;
  }

  public void setWeek(WeekTo week) {
    this.week = week;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public PainterTo getPainter() {
    return painter;
  }

  public void setPainter(PainterTo painter) {
    this.painter = painter;
  }

  public List<CommentTo> getComments() {
    return comments;
  }

  public void setComments(List<CommentTo> comments) {
    this.comments = comments;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public static PaintingTo fromPainting(Painting painting, ModelInclude include) {
    PaintingTo pt = new PaintingTo();
    pt.setId(painting.getId());
    pt.setName(painting.getName());
    pt.setFilePath(painting.getFilePath());
    if (include.isIncludeWeek()) {
      include.setIncludePainting(false);
      pt.setWeek(Optional.ofNullable(painting.getWeek()).map(wk -> WeekTo.fromWeek(wk, include))
          .orElse(null));
    }

    if (include.isIncludePainter()) {
      include.setIncludePainting(false);
      pt.setPainter(
          Optional.ofNullable(painting.getPainter()).map(p -> PainterTo.fromPainter(p, include))
              .orElse(null));
    }

    if (include.isIncludeComment()) {
      include.setIncludePainting(false);
      pt.setComments(Optional.ofNullable(painting.getComments())
          .map(cs -> cs.stream().map(c -> CommentTo.fromComment(c, include)).collect(
              Collectors.toList())).orElse(null));
    }

    return pt;
  }
}
