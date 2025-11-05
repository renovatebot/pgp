import { beforeAll, bench, describe } from 'vitest';
import { decrypt } from '../index.js';
import { fixMessage, readFixture } from './util.js';
import * as kbpgp from '@renovatebot/kbpgp';
import * as openpgp from 'openpgp';

const key = await readFixture(`private-pgp.pem`);

const msg = fixMessage(
  'wcFMAw+4H7SgaqGOAQ/+Lz6RlbEymbnmMhrktuaGiDPWRNPEQFuMRwwYM6/B/r0JMZa9tskAA5RpyYKxGmJJeuRtlA8GkTw02GoZomlJf/KXJZ95FwSbkXMSRJRD8LJ2402Hw2TaOTaSvfamESnm8zhNo8cok627nkKQkyrpk64heVlU5LIbO2+UgYgbiSQjuXZiW+QuJ1hVRjx011FQgEYc59+22yuKYqd8rrni7TrVqhGRlHCAqvNAGjBI4H7uTFh0sP4auunT/JjxTeTkJoNu8KgS/LdrvISpO67TkQziZo9XD5FOzSN7N3e4f8vO4N4fpjgkIDH/9wyEYe0zYz34xMAFlnhZzqrHycRqzBJuMxGqlFQcKWp9IisLMoVJhLrnvbDLuwwcjeqYkhvODjSs7UDKwTE4X4WmvZr0x4kOclOeAAz/pM6oNVnjgWJd9SnYtoa67bZVkne0k6mYjVhosie8v8icijmJ4OyLZUGWnjZCRd/TPkzQUw+B0yvsop9FYGidhCI+4MVx6W5w7SRtCctxVfCjLpmU4kWaBUUJ5YIQ5xm55yxEYuAsQkxOAYDCMFlV8ntWStYwIG1FsBgJX6VPevXuPPMjWiPNedIpJwBH2PLB4blxMfzDYuCeaIqU4daDaEWxxpuFTTK9fLdJKuipwFG6rwE3OuijeSN+2SLszi834DXtUjQdikHSTQG392+oTmZCFPeffLk/OiV2VpdXF3gGL7sr5M9hOWIZ783q0vW1l6nAElZ7UA//kW+L6QRxbnBVTJK5eCmMY6RJmL76zjqC1jQ0FC10',
);

describe('decrypt', () => {
  beforeAll(async () => {
    // warm up
    await decrypt(key, msg, {
      runtime: 'js-java',
    });
    await decrypt(key, msg, {
      runtime: 'wasm-java',
    });
    await decrypt(key, msg, {
      runtime: 'wasm-dotnet',
    });
  });

  bench('js-java', async () => {
    await decrypt(key, msg, {
      runtime: 'js-java',
    });
  });

  bench('wasm-java', async () => {
    await decrypt(key, msg, {
      runtime: 'wasm-java',
    });
  });

  bench('wasm-dotnet', async () => {
    await decrypt(key, msg, {
      runtime: 'wasm-dotnet',
    });
  });

  bench('kbpgp', async () => {
    const pk = await new Promise<kbpgp.KeyManager>((resolve, reject) => {
      kbpgp.KeyManager.import_from_armored_pgp(
        {
          armored: key,
        },
        (err: Error, pk) => {
          if (err) {
            reject(err);
          } else {
            resolve(pk);
          }
        },
      );
    });

    const ring = new kbpgp.keyring.KeyRing();
    ring.add_key_manager(pk);

    await new Promise<kbpgp.Literal>((resolve, reject) => {
      kbpgp.unbox(
        {
          keyfetch: ring,
          armored: msg,
        },
        (err: Error, literals: any) => {
          if (err) {
            reject(err);
          } else {
            resolve(literals[0].toString());
          }
        },
      );
    });
  });

  bench('openpgp', async () => {
    const pk = await openpgp.readPrivateKey({
      armoredKey: key,
    });

    const message = await openpgp.readMessage({
      armoredMessage: msg,
    });
    await openpgp.decrypt({
      message,
      decryptionKeys: pk,
    });
  });
});
