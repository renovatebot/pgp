package com.github.renovatebot.teavm.transformer.classlib;

import org.teavm.classlib.PlatformDetector;
import org.teavm.jso.crypto.Crypto;
import org.teavm.jso.typedarrays.Uint8Array;

public abstract class SecureRandomSubstitude {
  public void nextBytes(byte[] bytes) {
    if ((PlatformDetector.isJavaScript() || PlatformDetector.isWebAssemblyGC()) && Crypto.isSupported()) {
      var buffer = new Uint8Array(bytes.length);
      Crypto.current().getRandomValues(buffer);

      for (int i = 0; i < bytes.length; ++i) {
        bytes[i] = (byte) buffer.get(i);
      }
    } else {
      throw new UnsupportedOperationException(
          "SecureRandom is not supported on this platform");
    }
  }

}
