package com.fluffynx.fluffiegallery.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class PainterToken extends Model {

  @OneToOne
  @JoinColumn(name = "painterId")
  private Painter painter;

  private String token;

  private LocalDateTime genTime;

  public Painter getPainter() {
    return painter;
  }

  public void setPainter(Painter painter) {
    this.painter = painter;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public LocalDateTime getGenTime() {
    return genTime;
  }

  public void setGenTime(LocalDateTime genTime) {
    this.genTime = genTime;
  }
}
