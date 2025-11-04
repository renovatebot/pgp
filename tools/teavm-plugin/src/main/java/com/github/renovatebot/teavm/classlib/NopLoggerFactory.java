package com.github.renovatebot.teavm.classlib;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

public final class NopLoggerFactory implements ILoggerFactory  {

  static final NopLogger NOP_LOGGER = new NopLogger();

  @Override
  public Logger getLogger(String name) {
    return NOP_LOGGER;
  }

}
