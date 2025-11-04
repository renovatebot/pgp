package com.github.renovatebot.teavm.transformer;

import org.slf4j.LoggerFactory;
import org.teavm.model.ClassHolder;
import org.teavm.model.ClassHolderTransformer;
import org.teavm.model.ClassHolderTransformerContext;
import org.teavm.model.ClassReader;
import org.teavm.model.FieldHolder;
import org.teavm.model.MethodHolder;
import org.teavm.model.util.ModelUtils;

import com.github.renovatebot.teavm.classlib.NopLoggerFactorySubstitution;

public class Slf4jLoggerFactoryTransformer implements ClassHolderTransformer {
  @Override
  public void transformClass(ClassHolder cls, ClassHolderTransformerContext context) {
    if (!cls.getName().equals(LoggerFactory.class.getName())) {
      return;
    }
    ClassReader subst = context.getHierarchy().getClassSource().get(NopLoggerFactorySubstitution.class.getName());
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
