package org.slf4j;

public final class LoggerFactory {
  public static Logger getLogger(Class<?> forClass)
	{
		return new Logger();
	}
}
