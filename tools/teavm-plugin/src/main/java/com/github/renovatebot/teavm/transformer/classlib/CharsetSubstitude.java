package com.github.renovatebot.teavm.transformer.classlib;

import java.nio.charset.Charset;

public abstract class CharsetSubstitude {
  public static Charset forName(String charsetName) {
    if (charsetName == null) {
      throw new IllegalArgumentException("charsetName is null");
    }
    if (charsetName == "UTF8") {
      charsetName = "UTF-8";
    }
    return forNameOrig(charsetName);
  }

  public static Charset forNameOrig(String charsetName) {
    return null;
  }
}
