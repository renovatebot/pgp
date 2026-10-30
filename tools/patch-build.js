import fs from 'node:fs/promises';

function resolve(path) {
  return `${import.meta.dirname}/../${path}`;
}

await (async () => {
  const file = resolve('dist/dotnet/_framework/dotnet.js');
  let data = await fs.readFile(file, 'utf8');
  data = data.replace('//# sourceMappingURL=dotnet.js.map', '');
  await fs.writeFile(file, data);
})();

await (async () => {
  const file = resolve('dist/dotnet/_framework/dotnet.runtime.js');
  let data = await fs.readFile(file, 'utf8');
  data = data.replace('//# sourceMappingURL=dotnet.runtime.js.map', '');
  await fs.writeFile(file, data);
})();
