package com.fluffynx.fluffiegallery.resources.model;

import com.fluffynx.fluffiegallery.entity.Comment;
import com.fluffynx.fluffiegallery.utils.DateUtil;
import java.util.Optional;

public class CommentTo {
  private int id;

  private PaintingTo painting;

  private String text;

  private String date;

  private PainterTo commenter;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public PaintingTo getPainting() {
    return painting;
  }

  public void setPainting(PaintingTo painting) {
    this.painting = painting;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public PainterTo getCommenter() {
    return commenter;
  }

  public void setCommenter(PainterTo commenter) {
    this.commenter = commenter;
  }

  public static CommentTo fromComment(Comment comment, ModelInclude include) {
    CommentTo ct = new CommentTo();
    ct.setId(comment.getId());
    ct.setDate(DateUtil.formatDate(comment.getDate(), true));
    ct.setText(comment.getText());
    if (include.isIncludePainter()) {
      include.setIncludeComment(false);
      ct.setCommenter(
          Optional.ofNullable(comment.getCommenter()).map(p -> PainterTo.fromPainter(p, include))
              .orElse(null));
    }

    if (include.isIncludePainting()) {
      include.setIncludeComment(false);
      ct.setPainting(
          Optional.ofNullable(comment.getPainting()).map(p -> PaintingTo.fromPainting(p, include))
              .orElse(null));
    }

    return ct;
  }
}
