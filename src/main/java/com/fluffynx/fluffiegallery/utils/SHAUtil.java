package com.fluffynx.fluffiegallery.utils;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public final class SHAUtil {

  private static final String PUB = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCoQTii+2JAEHlHkKgluR0iVTzg7O7/3b9T1lPv80o6HBALZUIKrsSRs80kwBZSYNMQaFArq01K8Xbni4Wv0qR3Exezvn1lsgBBvrYNKFMF7jPNc0pKEUD1sQVVVPtfXReID80X3+4ewxcXcU5nt8OXfGLTA/E8fADP5ZKvXm/bswIDAQAB";

  private MessageDigest digest;

  private Cipher cipher;

  @PostConstruct
  public void init() {
    try {
      digest = MessageDigest.getInstance("SHA-256");

      byte[] pubbytes = Base64.getDecoder().decode(PUB.getBytes());
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubbytes);
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      PublicKey pubkey = keyFactory.generatePublic(keySpec);
      cipher = Cipher.getInstance("RSA");
      cipher.init(Cipher.DECRYPT_MODE, pubkey);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException e) {
      throw new RuntimeException("Security unsatisfied");
    }
  }

  public String hash(String password) {
    byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
    return Base64.getEncoder().encodeToString(encodedhash);
  }

  public String decrypt(String encrypted) {
    if (StringUtils.isEmpty(encrypted)) {
      return null;
    }

    try {
      return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)));
    } catch (IllegalBlockSizeException | BadPaddingException e) {
      return null;
    }
  }
}
