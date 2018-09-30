package com.fluffynx.fluffiegallery.resources.model;

public final class ModelInclude {
  private boolean includeWeek = false;

  private boolean includePainter = false;

  private boolean includePainting = false;

  private boolean includeComment = false;

  public boolean isIncludeWeek() {
    return includeWeek;
  }

  public void setIncludeWeek(boolean includeWeek) {
    this.includeWeek = includeWeek;
  }

  public boolean isIncludePainter() {
    return includePainter;
  }

  public void setIncludePainter(boolean includePainter) {
    this.includePainter = includePainter;
  }

  public boolean isIncludePainting() {
    return includePainting;
  }

  public void setIncludePainting(boolean includePainting) {
    this.includePainting = includePainting;
  }

  public boolean isIncludeComment() {
    return includeComment;
  }

  public void setIncludeComment(boolean includeComment) {
    this.includeComment = includeComment;
  }
}
