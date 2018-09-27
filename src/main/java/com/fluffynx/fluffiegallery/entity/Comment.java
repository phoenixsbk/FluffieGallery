package com.fluffynx.fluffiegallery.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Comment extends Model {
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "paintingId")
  @JsonIgnore
  private Painting painting;

  private String text;

  private Date date;

  @OneToOne
  private Painter commenter;

  public Painting getPainting() {
    return painting;
  }

  public void setPainting(Painting painting) {
    this.painting = painting;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Painter getCommenter() {
    return commenter;
  }

  public void setCommenter(Painter commenter) {
    this.commenter = commenter;
  }
}
