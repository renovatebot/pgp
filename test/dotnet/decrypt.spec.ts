import { describe, expect, it } from 'vitest';
import { decrypt } from '../../dist/dotnet/main.mjs';
import { fixMessage, readFixture } from '../util';

describe('dotnet/decrypt', () => {
  it('armor error', async () => {
    const key = await readFixture(`private-pgp.pem`);

    // Invalid armor error ????
    const msg =
      'wcFMAw+4H7SgaqGOAQ//Yk4RTQoLEhO0TKxN2IUBrCi88ts+CG1SXKeL06sJ2qikN/3n2JYAGGKgkHRICfu5dOnsjyFdLJ1XWUrbsM3XgVWikMbrmzD1Xe7N5DsoZXlt4Wa9pZ+IkZuE6XcKKu9whIJ22ciEwCzFwDmk/CBshdCCVVQ3IYuM6uibEHn/AHQ8K15XhraiSzF6DbJpevs5Cy7b5YHFyE936H25CVnouUQnMPsirpQq3pYeMq/oOtV/m4mfRUUQ7MUxvtrwE4lq4hLjFu5n9rwlcqaFPl7I7BEM++1c9LFpYsP5mTS7hHCZ9wXBqER8fa3fKYx0bK1ihCpjP4zUkR7P/uhWDArXamv7gHX2Kj/Qsbegn7KjTdZlggAmaJl/CuSgCbhySy+E55g3Z1QFajiLRpQ5+RsWFDbbI08YEgzyQ0yNCaRvrkgo7kZ1D95rEGRfY96duOQbjzOEqtvYmFChdemZ2+f9Kh/JH1+X9ynxY/zYe/0p/U7WD3QNTYN18loc4aXiB1adXD5Ka2QfNroLudQBmLaJpJB6wASFfuxddsD5yRnO32NSdRaqIWC1x6ti3ZYJZ2RsNwJExPDzjpQTuMOH2jtpu3q7NHmW3snRKy2YAL2UjI0YdeKIlhc/qLCJt9MRcOxWYvujTMD/yGprhG44qf0jjMkJBu7NjuVIMONujabl9b7SUQGfO/t+3rMuC68bQdCGLlO8gf3hvtD99utzXphi6idjC0HKSW/9KzuMkm+syGmIAYq/0L3EFvpZ38uq7z8KzwFFQHI3sBA34bNEr5zpU5OMWg';

    expect(() => decrypt(key, fixMessage(msg))).toThrow('invalid armor');
  });

  it('works', async () => {
    const key = await readFixture(`private-pgp.pem`);

    const msg =
      'wcFMAw+4H7SgaqGOAQ/+Lz6RlbEymbnmMhrktuaGiDPWRNPEQFuMRwwYM6/B/r0JMZa9tskAA5RpyYKxGmJJeuRtlA8GkTw02GoZomlJf/KXJZ95FwSbkXMSRJRD8LJ2402Hw2TaOTaSvfamESnm8zhNo8cok627nkKQkyrpk64heVlU5LIbO2+UgYgbiSQjuXZiW+QuJ1hVRjx011FQgEYc59+22yuKYqd8rrni7TrVqhGRlHCAqvNAGjBI4H7uTFh0sP4auunT/JjxTeTkJoNu8KgS/LdrvISpO67TkQziZo9XD5FOzSN7N3e4f8vO4N4fpjgkIDH/9wyEYe0zYz34xMAFlnhZzqrHycRqzBJuMxGqlFQcKWp9IisLMoVJhLrnvbDLuwwcjeqYkhvODjSs7UDKwTE4X4WmvZr0x4kOclOeAAz/pM6oNVnjgWJd9SnYtoa67bZVkne0k6mYjVhosie8v8icijmJ4OyLZUGWnjZCRd/TPkzQUw+B0yvsop9FYGidhCI+4MVx6W5w7SRtCctxVfCjLpmU4kWaBUUJ5YIQ5xm55yxEYuAsQkxOAYDCMFlV8ntWStYwIG1FsBgJX6VPevXuPPMjWiPNedIpJwBH2PLB4blxMfzDYuCeaIqU4daDaEWxxpuFTTK9fLdJKuipwFG6rwE3OuijeSN+2SLszi834DXtUjQdikHSTQG392+oTmZCFPeffLk/OiV2VpdXF3gGL7sr5M9hOWIZ783q0vW1l6nAElZ7UA//kW+L6QRxbnBVTJK5eCmMY6RJmL76zjqC1jQ0FC10';
    const expected = '{"o":"abc","r":"","v":"123"}';
    const decrypted = decrypt(key, fixMessage(msg));
    expect(decrypted).toEqual(expected);
  });

  it('works with pgp 2.4 and ecc', async () => {
    const key = await readFixture(`private-pgp-2.4-ecc.pem`);

    const msg = await readFixture(`test-ecc.txt.asc`);

    const decrypted = decrypt(key, msg);
    expect(decrypted.trim()).toEqual('test');
  });

  it('works with pgp 2.4 and rsa', async () => {
    const key = await readFixture(`private-pgp-2.4-rsa.pem`);

    const msg = await readFixture(`test-rsa.txt.asc`);

    const decrypted = decrypt(key, msg);
    expect(decrypted.trim()).toEqual('test');
  }, 15000);
});
