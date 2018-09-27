package com.fluffynx.fluffiegallery.entity;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class Painter extends Model {

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "painter")
  private List<Painting> artifacts;

  private String userId;

  private String passwd;

  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getPasswd() {
    return passwd;
  }

  public void setPasswd(String passwd) {
    this.passwd = passwd;
  }

  public List<Painting> getArtifacts() {
    return artifacts;
  }

  public void setArtifacts(List<Painting> artifacts) {
    this.artifacts = artifacts;
  }
}
