import fs from 'node:fs/promises';

async function resolve(path) {
  return `${import.meta.dirname}/../${path}`;
}

await (async () => {
  const file = await resolve('dist/teavm/lib.wasm-runtime.js');
  let data = await fs.readFile(file, 'utf8');
  data += `
import fs from 'node:fs/promises';
const fetch = async (url) => new Response(await fs.readFile(url), { headers: { 'Content-Type': 'application/wasm' } });
`;
  fs.writeFile(file, data);
})();

await (async () => {
  const file = await resolve('dist/dotnet/_framework/dotnet.js');
  let data = await fs.readFile(file, 'utf8');
  data = data.replace('//# sourceMappingURL=dotnet.js.map', '');
  fs.writeFile(file, data);
})();

await (async () => {
  const file = await resolve('dist/dotnet/_framework/dotnet.runtime.js');
  let data = await fs.readFile(file, 'utf8');
  data = data.replace('//# sourceMappingURL=dotnet.runtime.js.map', '');
  fs.writeFile(file, data);
})();
