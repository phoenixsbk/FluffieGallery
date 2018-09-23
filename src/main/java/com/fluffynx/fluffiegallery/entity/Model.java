package com.fluffynx.fluffiegallery.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Model {
  @Id
  @GeneratedValue
  protected int id;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
