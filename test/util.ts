export function fixMessage(msg: string): string {
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
