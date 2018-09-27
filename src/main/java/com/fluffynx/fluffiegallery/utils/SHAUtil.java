package com.fluffynx.fluffiegallery.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public final class SHAUtil {

  private MessageDigest digest;

  @PostConstruct
  public void init() {
    try {
      digest = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }

  public String hash(String password) {
    byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
    return Base64.getEncoder().encodeToString(encodedhash);
  }
}
