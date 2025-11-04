package com.github.renovatebot.teavm.transformer;

import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.teavm.model.ClassHolder;
import org.teavm.model.ClassHolderTransformer;
import org.teavm.model.ClassHolderTransformerContext;
import org.teavm.model.ClassReader;
import org.teavm.model.util.ModelUtils;

import com.github.renovatebot.teavm.classlib.FileInputStreamSubstitude;
import com.github.renovatebot.teavm.classlib.RuntimeSubstitude;

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
