package com.fluffynx.fluffiegallery.entity;

import java.sql.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class Week extends Model {

  @Column(unique = true)
  private String name;

  private Date startDate;

  private String photoPath;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "week")
  private List<Painting> paintings;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public String getPhotoPath() {
    return photoPath;
  }

  public void setPhotoPath(String photoPath) {
    this.photoPath = photoPath;
  }

  public List<Painting> getPaintings() {
    return paintings;
  }

  public void setPaintings(List<Painting> paintings) {
    this.paintings = paintings;
  }
}
