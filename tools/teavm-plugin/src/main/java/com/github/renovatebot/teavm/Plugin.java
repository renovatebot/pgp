package com.github.renovatebot.teavm;

import org.teavm.vm.spi.TeaVMHost;
import org.teavm.vm.spi.TeaVMPlugin;

import com.github.renovatebot.teavm.transformer.CoreTransformer;
import com.github.renovatebot.teavm.transformer.classlib.Slf4jLoggerFactoryTransformer;

public final class Plugin implements TeaVMPlugin {
  @Override
  public void install(TeaVMHost host) {
    host.add(new CoreTransformer());
    host.add(new Slf4jLoggerFactoryTransformer());
  }
}
