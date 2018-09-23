package com.fluffynx.fluffiegallery.entity;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class Painter extends Model {

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "painter")
  private List<Painting> artifacts;

  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
