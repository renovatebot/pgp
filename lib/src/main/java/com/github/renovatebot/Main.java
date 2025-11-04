package com.github.renovatebot;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.bouncycastle.bcpg.ArmoredInputStream;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.operator.bc.BcKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.bc.BcPublicKeyDataDecryptorFactory;
import org.bouncycastle.util.io.Streams;
import org.teavm.jso.JSExport;

public final class Main {

  private Main() {
    // Prevent instantiation
  }

  @JSExport
  public static String decrypt(String key, String msg) throws IOException, PGPException {
    final var builder = ArmoredInputStream.builder().setIgnoreCRC(true);
    final var input = builder.build(new ByteArrayInputStream(msg.getBytes(StandardCharsets.UTF_8)));
    final var pgpFactory = new PGPObjectFactory(input, new BcKeyFingerprintCalculator());

    var firstObject = pgpFactory.nextObject();
    if (!(firstObject instanceof PGPEncryptedDataList)) {
      firstObject = pgpFactory.nextObject();
    }

    final var keyStream = builder.build(new ByteArrayInputStream(key.getBytes(StandardCharsets.UTF_8)));
    final var keyRing = new PGPSecretKeyRingCollection(
        keyStream,
        new BcKeyFingerprintCalculator());

    PGPPrivateKey keyToUse = null;
    PGPPublicKeyEncryptedData encryptedData = null;
    final var encObjects = ((PGPEncryptedDataList) firstObject).getEncryptedDataObjects();

    while (keyToUse == null && encObjects.hasNext()) {
      encryptedData = (PGPPublicKeyEncryptedData) encObjects.next();
      final var k = keyRing.getSecretKey(encryptedData.getKeyIdentifier().getKeyId());
      if (k != null) {
        keyToUse = k.extractPrivateKey(null);
        break;
      }
    }

    if (keyToUse == null) {
      throw new PGPException("Cannot find secret key for message.");
    }

    final var clearText = encryptedData.getDataStream(new BcPublicKeyDataDecryptorFactory(keyToUse));
    var message = new PGPObjectFactory(clearText, new BcKeyFingerprintCalculator()).nextObject();
    String result = null;

    if (message instanceof PGPCompressedData data) {
      message = new PGPObjectFactory(data.getDataStream(), new BcKeyFingerprintCalculator()).nextObject();
    }

    if (message instanceof PGPLiteralData literalData) {
      final var outputStream = new ByteArrayOutputStream();
      Streams.pipeAll(literalData.getInputStream(), outputStream);
      result = outputStream.toString(StandardCharsets.UTF_8);
      outputStream.close();
    } else {
      throw new PGPException("Message is not encoded correctly.");
    }

    if (encryptedData.isIntegrityProtected() && !encryptedData.verify()) {
      throw new PGPException("Message failed integrity check!");
    }

    input.close();
    keyStream.close();
    clearText.close();

    return result;
  }
}
