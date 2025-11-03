let decryptDotnet, decryptWasmJava, decryptJsJava;

export async function decrypt(key, data, options) {
  switch (options?.runtime) {
    case 'js-java':
      if (!decryptJsJava) {
        decryptJsJava = (await import('./dist/teavm/lib.js')).decrypt;
      }
      return decryptJsJava(key, data);
    case 'wasm-java':
      if (!decryptWasmJava) {
        const { load } = await import('./dist/teavm/lib.wasm-runtime.js');
        const teavm = await load(
          new URL(`./dist/teavm/lib.wasm`, import.meta.url),
        );
        decryptWasmJava = teavm.exports.decrypt;
      }
      return decryptWasmJava(key, data);
    case 'wasm-dotnet':
      if (!decryptDotnet) {
        decryptDotnet = (await import('./dist/dotnet/main.mjs')).decrypt;
      }
      return decryptDotnet(key, data);
    default:
      throw new Error('Unsupported runtime specified in options');
  }
}
