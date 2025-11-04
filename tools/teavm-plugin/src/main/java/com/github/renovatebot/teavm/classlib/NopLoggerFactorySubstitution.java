package com.github.renovatebot.teavm.classlib;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

public final class NopLoggerFactorySubstitution {
  private static final NopLoggerFactory instance = new NopLoggerFactory();

  private NopLoggerFactorySubstitution() {
    // Prevent instantiation
  }

  public static Logger getLogger(Class<?> forClass)
	{
		return NopLoggerFactory.NOP_LOGGER;
	}

  public static Logger getLogger(String name)
	{
		return NopLoggerFactory.NOP_LOGGER;
	}

   public static ILoggerFactory getILoggerFactory() {
        return instance;
    }
}
