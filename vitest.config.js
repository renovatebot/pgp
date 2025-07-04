import { resolve } from 'node:path';
import { defineConfig } from 'vitest/config';

const ci = !!process.env.CI;

console.info('CI:', ci);

// https://vitejs.dev/config/
export default defineConfig(() => {
  return {
    test: {
      pool: ci ? 'forks' : 'threads',
      environment: 'node',
      // setupFiles: ['./Scripts/test-setup.ts'],
      include: ['test/**/*.spec.ts'],
      reporters: ci
        ? [['default', { summary: false }], 'junit']
        : ['default', 'junit'],
      outputFile: {
        junit: 'coverage/junit.xml',
      },
      alias: {
        'sp.ribbon': resolve('./__mocks__/sp.ribbon.js'),
      },
      coverage: {
        ignoreEmptyLines: true,
        provider: 'v8',
        reporter: ci
          ? ['text-summary', 'cobertura']
          : ['text-summary', 'html', 'cobertura'],
        enabled: ci ? true : undefined,
        include: ['index.js', 'dist/**/*.js'],
      },
    },
  };
});
