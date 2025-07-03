package com.github.renovatebot.teavm.transformer;

import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.teavm.model.ClassHolder;
import org.teavm.model.ClassHolderTransformer;
import org.teavm.model.ClassHolderTransformerContext;
import org.teavm.model.MethodDescriptor;
import org.teavm.model.MethodHolder;
import org.teavm.model.instructions.StringConstantInstruction;
import org.teavm.model.util.ModelUtils;
import org.teavm.model.util.ProgramUtils;

import com.github.renovatebot.teavm.transformer.classlib.CharsetSubstitude;
import com.github.renovatebot.teavm.transformer.classlib.FileInputStreamSubstitude;
import com.github.renovatebot.teavm.transformer.classlib.RuntimeSubstitude;

public class CoreTransformer implements ClassHolderTransformer {
  private static final Logger LOG = Logger.getLogger(CoreTransformer.class.getName());

  @Override
  public void transformClass(ClassHolder cls, ClassHolderTransformerContext context) {
    if (cls.getName().equals(Runtime.class.getName())) {
      var hierarchy = context.getHierarchy();
      var subst = hierarchy.getClassSource().get(RuntimeSubstitude.class.getName());
      for (var method : subst.getMethods()) {
        LOG.log(Level.INFO, "Adding method: {0}", method.getName());
        cls.addMethod(ModelUtils.copyMethod(method));
      }
    }
    if (cls.getName().equals(FileInputStream.class.getName())) {
      var hierarchy = context.getHierarchy();
      var subst = hierarchy.getClassSource().get(FileInputStreamSubstitude.class.getName());
      for (var method : subst.getMethods()) {
        LOG.log(Level.INFO, "Adding method: {0}", method.getName());
        cls.addMethod(ModelUtils.copyMethod(method));
      }
    }
    // if (cls.getName().equals(Charset.class.getName())) {
    // var hierarchy = context.getHierarchy();
    // var subst =
    // hierarchy.getClassSource().get(CharsetSubstitude.class.getName());

    // var forNameOrig = cls.getMethods().stream().filter(m ->
    // m.getName().equals("forName"))
    // .findFirst()
    // .get();
    // var forNameNew = ModelUtils.copyMethod(subst.getMethod(new
    // MethodDescriptor("forName", forNameOrig.getSignature())));

    // LOG.log(Level.INFO, "Replacing method: {0}", forNameNew.getName());
    // cls.removeMethod(forNameOrig);

    // cls.addMethod(renameMethod(forNameOrig, "forNameOrig"));
    // cls.addMethod(forNameNew);

    // // for (var b : forNameNew.getProgram().getBasicBlocks()) {
    // // LOG.log(Level.WARNING, "Block: {0}", b);
    // // for(var i : b) {
    // // LOG.log(Level.WARNING, " Instruction: {0}", i);
    // // }
    // // }

    // // LOG.log(Level.WARNING, "Inner classes: {0}", cls.getInnerClasses());

    // throw new UnsupportedOperationException("Dummy");
    // }

    if (cls.getName().equals(org.pgpainless.key.parsing.KeyRingReader.class.getName())
        || cls.getName().equals(org.pgpainless.decryption_verification.OpenPgpInputStream.class.getName())) {
      LOG.log(Level.WARNING, "Found class: {0}", cls.getName());

      var cinit = cls.getMethods().stream()
          .filter(m -> m.getName().equals("<clinit>"))
          .findFirst()
          .get();
      LOG.log(Level.WARNING, "Found method: {0}", cinit.getName());

      for (var b : cinit.getProgram().getBasicBlocks()) {
        LOG.log(Level.WARNING, " Block: {0}", b);
        for (var i : b) {
          LOG.log(Level.WARNING, "  Instruction: {0}", i);

          if (i instanceof StringConstantInstruction sc) {
            if (sc.getConstant().equals("UTF8")) {
              LOG.log(Level.WARNING, "  Replacing string constant: {0}", sc.getConstant());
              sc.setConstant(("UTF-8"));
            }
          }
        }
      }
    }
  }
}
