package br.com.api.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import br.com.api.exception.Sha512Exception;

public class Utils {

  public static void argumentNotEmpty(String field, String errMessage) {
    if (field == null || field.isEmpty())
      throw new IllegalArgumentException(errMessage);
  }

  public static void argumentNotNull(Object obj, String errMessage) {
    if (obj == null)
      throw new IllegalArgumentException(errMessage);
  }

  public static String getSHA512(String input) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-512");
      digest.reset();
      digest.update(input.getBytes("utf8"));
      return String.format("%040x", new BigInteger(1, digest.digest()));
    } catch (Exception e) {
      throw new Sha512Exception("Error ao gerar SHA512." + e.getMessage());
    }
  }
}
