package com.github.renovatebot.teavm.transformer;

import java.util.Set;

import org.teavm.model.ClassHolder;
import org.teavm.model.ClassHolderTransformer;
import org.teavm.model.ClassHolderTransformerContext;
import org.teavm.model.ClassReader;
import org.teavm.model.FieldHolder;
import org.teavm.model.MethodHolder;
import org.teavm.model.util.ModelUtils;

import com.github.renovatebot.teavm.classlib.PqcKeyUtilSubstitution;

public class PqcKeyUtilTransformer implements ClassHolderTransformer {
  // package private in BouncyCastle, so they can't be referenced by class literal
  private static final Set<String> KEY_UTILS = Set.of(
      "org.bouncycastle.crypto.util.XmssKeyUtil",
      "org.bouncycastle.crypto.util.LmsKeyUtil");

  @Override
  public void transformClass(ClassHolder cls, ClassHolderTransformerContext context) {
    if (!KEY_UTILS.contains(cls.getName())) {
      return;
    }
    ClassReader subst = context.getHierarchy().getClassSource().get(PqcKeyUtilSubstitution.class.getName());
    for (var field : cls.getFields().toArray(new FieldHolder[0])) {
      cls.removeField(field);
    }
    for (var method : cls.getMethods().toArray(new MethodHolder[0])) {
      cls.removeMethod(method);
    }
    for (var field : subst.getFields()) {
      cls.addField(ModelUtils.copyField(field));
    }
    for (var method : subst.getMethods()) {
      cls.addMethod(ModelUtils.copyMethod(method));
    }
  }
}
