package com.github.renovatebot.teavm.classlib;

import java.io.IOException;

import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

/**
 * Substitution for {@code org.bouncycastle.crypto.util.XmssKeyUtil} and
 * {@code org.bouncycastle.crypto.util.LmsKeyUtil}, which BouncyCastle 1.86 introduced and wired
 * into the key factories. Their hash based signature key handling doesn't survive TeaVM: XMSS
 * deserializes BDS state through {@code java.io.ObjectInputStream}, which TeaVM doesn't emulate.
 * <p>
 * Every method answers null, "not a key of mine", which lets the key factories fall through to
 * their own unrecognised-algorithm handling. This mirrors the twins BouncyCastle keeps in
 * {@code core/src/main/jdk1.4} for the distributions that exclude XMSS and LMS. Keep the method set
 * in step with the base copies.
 */
public final class PqcKeyUtilSubstitution {
  private PqcKeyUtilSubstitution() {
  }

  public static SubjectPublicKeyInfo createSubjectPublicKeyInfo(AsymmetricKeyParameter publicKey) throws IOException {
    return null;
  }

  public static PrivateKeyInfo createPrivateKeyInfo(AsymmetricKeyParameter privateKey, ASN1Set attributes)
      throws IOException {
    return null;
  }

  public static AsymmetricKeyParameter createPublicKey(SubjectPublicKeyInfo keyInfo) throws IOException {
    return null;
  }

  public static AsymmetricKeyParameter createPrivateKey(PrivateKeyInfo keyInfo) throws IOException {
    return null;
  }
}
