package com.github.renovatebot.teavm.transformer;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.teavm.model.ClassHolder;
import org.teavm.model.ClassHolderTransformer;
import org.teavm.model.ClassHolderTransformerContext;

/**
 * BouncyCastle 1.86 made its key parameter classes, {@code RSAKeyParameters} and
 * {@code ECPrivateKeyParameters} among them, implement {@code javax.security.auth.Destroyable}.
 * TeaVM doesn't emulate that interface, and its WasmGC backend fails with a
 * {@code NullPointerException} while building the interface virtual tables for a class whose
 * interface it can't resolve.
 * <p>
 * We never destroy keys, so drop the marker interface. The {@code destroy()} and
 * {@code isDestroyed()} methods stay on the classes and are simply never reached.
 */
public class DestroyableTransformer implements ClassHolderTransformer {
  private static final Logger LOG = Logger.getLogger(DestroyableTransformer.class.getName());

  private static final String DESTROYABLE = "javax.security.auth.Destroyable";

  @Override
  public void transformClass(ClassHolder cls, ClassHolderTransformerContext context) {
    if (cls.getInterfaces().remove(DESTROYABLE)) {
      LOG.log(Level.INFO, "Removed {0} from class: {1}", new Object[] { DESTROYABLE, cls.getName() });
    }
  }
}
