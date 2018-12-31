package com.fluffynx.fluffiegallery.resources.request;

public class CommentRequest {

  private int paintingId;

  private String comment;

  public int getPaintingId() {
    return paintingId;
  }

  public void setPaintingId(int paintingId) {
    this.paintingId = paintingId;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }
}
