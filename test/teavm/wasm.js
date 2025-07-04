import { fixMessage, readFixture } from '../util.js';
import fs from 'node:fs/promises';
import { load } from '../../dist/teavm/lib.wasm-runtime.js';

const decrypt = await (async function () {
  const fetch = globalThis.fetch;

  Object.assign(globalThis, {
    fetch: async (url) =>
      new Response(await fs.readFile(url), {
        headers: { 'Content-Type': 'application/wasm' },
      }),
  });

  const teavm = await load(
    new URL(`../../dist/teavm/lib.wasm`, import.meta.url),
  );
  Object.assign(globalThis, { fetch });
  return teavm.exports.decrypt;
})();

const msg =
  'wcFMAw+4H7SgaqGOAQ/+Lz6RlbEymbnmMhrktuaGiDPWRNPEQFuMRwwYM6/B/r0JMZa9tskAA5RpyYKxGmJJeuRtlA8GkTw02GoZomlJf/KXJZ95FwSbkXMSRJRD8LJ2402Hw2TaOTaSvfamESnm8zhNo8cok627nkKQkyrpk64heVlU5LIbO2+UgYgbiSQjuXZiW+QuJ1hVRjx011FQgEYc59+22yuKYqd8rrni7TrVqhGRlHCAqvNAGjBI4H7uTFh0sP4auunT/JjxTeTkJoNu8KgS/LdrvISpO67TkQziZo9XD5FOzSN7N3e4f8vO4N4fpjgkIDH/9wyEYe0zYz34xMAFlnhZzqrHycRqzBJuMxGqlFQcKWp9IisLMoVJhLrnvbDLuwwcjeqYkhvODjSs7UDKwTE4X4WmvZr0x4kOclOeAAz/pM6oNVnjgWJd9SnYtoa67bZVkne0k6mYjVhosie8v8icijmJ4OyLZUGWnjZCRd/TPkzQUw+B0yvsop9FYGidhCI+4MVx6W5w7SRtCctxVfCjLpmU4kWaBUUJ5YIQ5xm55yxEYuAsQkxOAYDCMFlV8ntWStYwIG1FsBgJX6VPevXuPPMjWiPNedIpJwBH2PLB4blxMfzDYuCeaIqU4daDaEWxxpuFTTK9fLdJKuipwFG6rwE3OuijeSN+2SLszi834DXtUjQdikHSTQG392+oTmZCFPeffLk/OiV2VpdXF3gGL7sr5M9hOWIZ783q0vW1l6nAElZ7UA//kW+L6QRxbnBVTJK5eCmMY6RJmL76zjqC1jQ0FC10';
const expected = '{"o":"abc","r":"","v":"123"}';

const key = await readFixture(`private-pgp.pem`);

const actual = decrypt(key, fixMessage(msg));
console.assert(actual === expected, 'Decryption failed');
console.log('Decryption successful:', actual);
