package com.fluffynx.fluffiegallery.entity;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class Week extends Model {

  @Column(unique = true)
  private String name;

  private String description;

  private LocalDateTime startDate;

  private String photoPath;

  @OneToMany(mappedBy = "week")
  private List<Painting> paintings;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDateTime getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDateTime startDate) {
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
