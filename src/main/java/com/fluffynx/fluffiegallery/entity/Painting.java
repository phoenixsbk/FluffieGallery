package com.fluffynx.fluffiegallery.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Painting extends Model {

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "weekId")
  @JsonIgnore
  private Week week;

  private String description;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "painterId")
  @JsonIgnore
  private Painter painter;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "painting")
  private List<Comment> comments;

  private String filePath;

  public Week getWeek() {
    return week;
  }

  public void setWeek(Week week) {
    this.week = week;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Painter getPainter() {
    return painter;
  }

  public void setPainter(Painter painter) {
    this.painter = painter;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public List<Comment> getComments() {
    return comments;
  }

  public void setComments(List<Comment> comments) {
    this.comments = comments;
  }
}
