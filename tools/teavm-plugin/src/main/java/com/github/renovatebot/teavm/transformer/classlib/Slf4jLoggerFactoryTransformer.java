package com.github.renovatebot.teavm.transformer.classlib;

import org.slf4j.LoggerFactory;
import org.teavm.model.ClassHolder;
import org.teavm.model.ClassHolderTransformer;
import org.teavm.model.ClassHolderTransformerContext;
import org.teavm.model.ClassReader;
import org.teavm.model.FieldHolder;
import org.teavm.model.FieldReader;
import org.teavm.model.MethodHolder;
import org.teavm.model.MethodReader;
import org.teavm.model.util.ModelUtils;

public class Slf4jLoggerFactoryTransformer implements ClassHolderTransformer {
  @Override
  public void transformClass(ClassHolder cls, ClassHolderTransformerContext context) {
    if (!cls.getName().equals(LoggerFactory.class.getName())) {
      return;
    }
    ClassReader subst = context.getHierarchy().getClassSource().get(NopLoggerFactorySubstitution.class.getName());
    for (FieldHolder field : cls.getFields().toArray(new FieldHolder[0])) {
      cls.removeField(field);
    }
    for (MethodHolder method : cls.getMethods().toArray(new MethodHolder[0])) {
      cls.removeMethod(method);
    }
    for (FieldReader field : subst.getFields()) {
      cls.addField(ModelUtils.copyField(field));
    }
    for (MethodReader method : subst.getMethods()) {
      cls.addMethod(ModelUtils.copyMethod(method));
    }
  }
}
