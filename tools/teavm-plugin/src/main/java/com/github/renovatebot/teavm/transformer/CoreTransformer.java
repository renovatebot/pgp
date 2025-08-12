package com.github.renovatebot.teavm.transformer;

import java.io.FileInputStream;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.teavm.model.ClassHolder;
import org.teavm.model.ClassHolderTransformer;
import org.teavm.model.ClassHolderTransformerContext;
import org.teavm.model.ClassReader;
import org.teavm.model.instructions.StringConstantInstruction;
import org.teavm.model.util.ModelUtils;
import com.github.renovatebot.teavm.transformer.classlib.FileInputStreamSubstitude;
import com.github.renovatebot.teavm.transformer.classlib.RuntimeSubstitude;

public class CoreTransformer implements ClassHolderTransformer {
  private static final Logger LOG = Logger.getLogger(CoreTransformer.class.getName());

  @Override
  public void transformClass(ClassHolder cls, ClassHolderTransformerContext context) {
    if (cls.getName().equals(Runtime.class.getName())) {
      LOG.log(Level.INFO, "Found class: {0}", cls.getName());
      var hierarchy = context.getHierarchy();
      var subst = hierarchy.getClassSource().get(RuntimeSubstitude.class.getName());
      copyMethods(cls, subst);
    }
    if (cls.getName().equals(FileInputStream.class.getName())) {
      LOG.log(Level.INFO, "Found class: {0}", cls.getName());
      var hierarchy = context.getHierarchy();
      var subst = hierarchy.getClassSource().get(FileInputStreamSubstitude.class.getName());
      copyMethods(cls, subst);
    }

    if (cls.getName().equals(org.pgpainless.key.parsing.KeyRingReader.class.getName())
        || cls.getName().equals(org.pgpainless.decryption_verification.OpenPgpInputStream.class.getName())) {
      LOG.log(Level.INFO, "Found class: {0}", cls.getName());

      var cinit = cls.getMethods().stream()
          .filter(m -> m.getName().equals("<clinit>"))
          .findFirst()
          .get();
      LOG.log(Level.INFO, "Found method: {0}", cinit.getName());

      for (var b : cinit.getProgram().getBasicBlocks()) {
        for (var i : b) {
          if (i instanceof StringConstantInstruction sc) {
            if (sc.getConstant().equals("UTF8")) {
              LOG.log(Level.INFO, "  Replacing string constant: {0}", sc.getConstant());
              sc.setConstant(("UTF-8"));
            }
          }
        }
      }
    }
  }

  private void copyMethods(ClassHolder cls, ClassReader subst) {
    for (var method : subst.getMethods()) {
      if (method.getName().equals("<init>")) {
        continue; // skip constructor
      }
      LOG.log(Level.INFO, "Adding method: {0}", method.getName());
      cls.addMethod(ModelUtils.copyMethod(method));
    }
  }
}
