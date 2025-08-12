import fs from 'node:fs/promises';

/**
 *
 * @param {string} msg
 * @returns {string}
 */
export function fixMessage(msg) {
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

/**
 *
 * @param {string} msg
 * @returns {Promise<string>}
 */
export async function readFixture(path) {
  return await fs.readFile(new URL(`fixtures/${path}`, import.meta.url), {
    encoding: 'utf-8',
  });
}
