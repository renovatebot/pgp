let decryptDotnet, decryptWasmJava, decryptJsJava;

export async function decrypt(key, data, options) {
  switch (options?.runtime) {
    case 'js-java':
      if (!decryptJsJava) {
        decryptJsJava = import('./dist/teavm/lib.js');
      }
      return (await decryptJsJava).decrypt(key, data);
    case 'wasm-java':
      if (!decryptWasmJava) {
        decryptWasmJava = import('./dist/teavm/lib.wasm-runtime.js').then(
          ({ load }) =>
            load(new URL(`./dist/teavm/lib.wasm`, import.meta.url)).then(
              (teavm) => teavm.exports,
            ),
        );
      }
      return (await decryptWasmJava).decrypt(key, data);
    case 'wasm-dotnet':
      if (!decryptDotnet) {
        decryptDotnet = import('./dist/dotnet/main.mjs');
      }
      return (await decryptDotnet).decrypt(key, data);
    default:
      throw new Error('Unsupported runtime specified in options');
  }
}
