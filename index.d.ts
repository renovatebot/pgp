/**
 * Decrypt data with .NET wasm implementations of Bouncy Castle.
 * @param key private key in PEM format
 * @param data encrypted data in base64 format
 * @param options runtime options
 */
export async function decrypt(
  key: string,
  data: string,
  options: { runtime: 'js-java' | 'wasm-java' | 'wasm-dotnet' },
): Promise<string>;
