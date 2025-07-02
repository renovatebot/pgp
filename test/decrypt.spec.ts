import { decrypt } from '..';
import { describe, expect, it } from 'vitest';
import fs from 'node:fs/promises';

describe('decrypt', () => {
  it('works', async () => {
    const key = await fs.readFile(`${__dirname}/private-pgp.pem`, {
      encoding: 'utf-8',
    });
    const msg =
      'wcFMAw+4H7SgaqGOAQ//Yk4RTQoLEhO0TKxN2IUBrCi88ts+CG1SXKeL06sJ2qikN/3n2JYAGGKgkHRICfu5dOnsjyFdLJ1XWUrbsM3XgVWikMbrmzD1Xe7N5DsoZXlt4Wa9pZ+IkZuE6XcKKu9whIJ22ciEwCzFwDmk/CBshdCCVVQ3IYuM6uibEHn/AHQ8K15XhraiSzF6DbJpevs5Cy7b5YHFyE936H25CVnouUQnMPsirpQq3pYeMq/oOtV/m4mfRUUQ7MUxvtrwE4lq4hLjFu5n9rwlcqaFPl7I7BEM++1c9LFpYsP5mTS7hHCZ9wXBqER8fa3fKYx0bK1ihCpjP4zUkR7P/uhWDArXamv7gHX2Kj/Qsbegn7KjTdZlggAmaJl/CuSgCbhySy+E55g3Z1QFajiLRpQ5+RsWFDbbI08YEgzyQ0yNCaRvrkgo7kZ1D95rEGRfY96duOQbjzOEqtvYmFChdemZ2+f9Kh/JH1+X9ynxY/zYe/0p/U7WD3QNTYN18loc4aXiB1adXD5Ka2QfNroLudQBmLaJpJB6wASFfuxddsD5yRnO32NSdRaqIWC1x6ti3ZYJZ2RsNwJExPDzjpQTuMOH2jtpu3q7NHmW3snRKy2YAL2UjI0YdeKIlhc/qLCJt9MRcOxWYvujTMD/yGprhG44qf0jjMkJBu7NjuVIMONujabl9b7SUQGfO/t+3rMuC68bQdCGLlO8gf3hvtD99utzXphi6idjC0HKSW/9KzuMkm+syGmIAYq/0L3EFvpZ38uq7z8KzwFFQHI3sBA34bNEr5zpU5OMWg';

    const decrypted = decrypt(key, fixMessage(msg));

    expect(decrypted).toEqual('test');
  });
});

function fixMessage(msg) {
  const startBlock = '-----BEGIN PGP MESSAGE-----\n\n';
  const endBlock = '\n-----END PGP MESSAGE-----';

  let armoredMessage = msg.trim();
  if (!armoredMessage.startsWith(startBlock)) {
    armoredMessage = `${startBlock}${armoredMessage}`;
  }
  if (!armoredMessage.endsWith(endBlock)) {
    armoredMessage = `${armoredMessage}${endBlock}`;
  }
  return armoredMessage;
}
