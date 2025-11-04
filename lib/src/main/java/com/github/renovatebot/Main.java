package com.github.renovatebot;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.bouncycastle.bcpg.ArmoredInputStream;
import org.bouncycastle.openpgp.PGPException;
import org.teavm.jso.JSExport;

public final class Main {

  private Main() {
    // Prevent instantiation
  }

  @JSExport
  public static String decrypt(String key, String msg) throws IOException, PGPException {
    final var builder = ArmoredInputStream.builder().setIgnoreCRC(true);
    final var bytes = msg.getBytes(StandardCharsets.UTF_8);
    final var input = builder.build(new ByteArrayInputStream(bytes));

    input.close();

    return "test -> " + bytes.length;
  }
}
