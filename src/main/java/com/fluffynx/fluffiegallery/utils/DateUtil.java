package com.fluffynx.fluffiegallery.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public final class DateUtil {

  private static final DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
      .withZone(ZoneId.of("GMT+8"));

  private static final DateTimeFormatter TIMEFORMATTER = DateTimeFormatter
      .ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("GMT+8"));

  private static final long EXP_MIN = 120L;

  public static String formatDate(LocalDateTime date, boolean includeTime) {
    return Optional.ofNullable(date).map(d -> {
      if (includeTime) {
        return TIMEFORMATTER.format(date);
      } else {
        return DATEFORMATTER.format(date);
      }
    }).orElse(null);
  }

  public static LocalDateTime parseDate(String date) {
    return Optional.ofNullable(date).map(d -> LocalDateTime.parse(d, TIMEFORMATTER)).orElse(null);
  }

  public static boolean isTokenExpired(LocalDateTime genTime) {
    return LocalDateTime.now().minus(EXP_MIN, ChronoUnit.MINUTES).compareTo(genTime) > 0;
  }
}
