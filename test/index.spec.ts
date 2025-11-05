import { describe, expect, it } from 'vitest';
import { decrypt, isSupportedRuntime, SupportedRuntimes } from '../index.js';

describe('index', () => {
  it.each(SupportedRuntimes)(
    'SupportedRuntimes are supported: %s',
    (runtime) => {
      expect(isSupportedRuntime(runtime)).toBe(true);
    },
  );

  it('throws for unsupported runtime', async () => {
    // @ts-expect-error testing invalid runtime
    await expect(decrypt('test', 'test')).rejects.toThrow(
      'Unsupported runtime: ',
    );
  });
});
