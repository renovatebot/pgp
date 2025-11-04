package com.github.renovatebot;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.bouncycastle.bcpg.ArmoredInputStream;

import org.teavm.jso.dom.html.HTMLDocument;

public final class Test {

  private Test() {
    // Prevent instantiation
  }

  public static void main(String[] args) throws IOException {
    var document = HTMLDocument.current();
    var div = document.createElement("div");
    div.appendChild(document.createTextNode("TeaVM started"));
    document.getBody().appendChild(div);
    final var builder = ArmoredInputStream.builder().setIgnoreCRC(true);
    final var input = builder.build(new ByteArrayInputStream(args[0].getBytes(StandardCharsets.UTF_8)));
    div.appendChild(document.createTextNode("TeaVM done"));
  }
}
