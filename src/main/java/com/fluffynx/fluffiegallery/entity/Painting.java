package com.fluffynx.fluffiegallery.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Painting extends Model {

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "weekId")
  private Week week;

  private String name;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "painterId")
  private Painter painter;

  private String filePath;

  public Week getWeek() {
    return week;
  }

  public void setWeek(Week week) {
    this.week = week;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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
}
