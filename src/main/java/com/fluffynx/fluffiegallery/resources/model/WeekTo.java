package com.fluffynx.fluffiegallery.resources.model;

import com.fluffynx.fluffiegallery.entity.Week;
import com.fluffynx.fluffiegallery.utils.DateUtil;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WeekTo {

  private int id;

  private String name;

  private String startDate;

  private String photoPath;

  private List<PaintingTo> paintings;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getPhotoPath() {
    return photoPath;
  }

  public void setPhotoPath(String photoPath) {
    this.photoPath = photoPath;
  }

  public List<PaintingTo> getPaintings() {
    return paintings;
  }

  public void setPaintings(
      List<PaintingTo> paintings) {
    this.paintings = paintings;
  }

  public static WeekTo fromWeek(Week week, ModelInclude include) {
    WeekTo wt = new WeekTo();
    wt.setId(week.getId());
    wt.setName(week.getName());
    wt.setPhotoPath(week.getPhotoPath());
    wt.setStartDate(DateUtil.formatDate(week.getStartDate(), false));
    if (include.isIncludePainting()) {
      include.setIncludeWeek(false);
      List<PaintingTo> paintings = Optional.ofNullable(week.getPaintings())
          .map(pts -> pts.stream().map(p -> PaintingTo.fromPainting(p, include)).collect(
              Collectors.toList())).orElse(null);
      wt.setPaintings(paintings);
    }
    return wt;
  }
}
