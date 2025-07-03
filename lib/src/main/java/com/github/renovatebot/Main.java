package com.github.renovatebot;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.util.io.Streams;
import org.pgpainless.PGPainless;
import org.pgpainless.decryption_verification.ConsumerOptions;
import org.pgpainless.key.protection.SecretKeyRingProtector;
import org.teavm.jso.JSExport;

public final class Main {
  private static final SecretKeyRingProtector keyProtector = SecretKeyRingProtector.unprotectedKeys();

  private Main() {
    // Prevent instantiation
  }

  @JSExport
  public static String decrypt(String key, String data) throws IOException, PGPException {

    // https://github.com/pgpainless/pgpainless/blob/f2cbde43bee0da523717b65f10f95defc4ad6f60/pgpainless-core/src/test/java/org/pgpainless/example/DecryptOrVerify.java

    // read the secret key
    var secretKey = PGPainless.readKeyRing().secretKeyRing(key);

    var consumerOptions = new ConsumerOptions()
        .addDecryptionKey(secretKey, keyProtector);// add the decryption key ring

    var plaintextOut = new ByteArrayOutputStream();
    var ciphertextIn = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));

    // The decryption stream is an input stream from which we read the decrypted
    // data
    var decryptionStream = PGPainless.decryptAndOrVerify()
        .onInputStream(ciphertextIn)
        .withOptions(consumerOptions);

    Streams.pipeAll(decryptionStream, plaintextOut);
    decryptionStream.close(); // remember to close the stream!

    // The output stream now contains the decrypted message
    return plaintextOut.toString();
  }
}
