package com.fluffynx.fluffiegallery.resources.request;

public class WeekRequest {
  private String name;

  private String startDate;

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
}