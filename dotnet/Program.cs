using System.IO;
using System.Linq;
using System.Runtime.InteropServices.JavaScript;
using System.Text;
using Org.BouncyCastle.Bcpg;
using Org.BouncyCastle.Bcpg.OpenPgp;
using Org.BouncyCastle.Utilities.IO;

public partial class Program
{
  public static void Main()
  {
    // Dummy to make compiler happy
  }

#pragma warning disable CA1416 // Validate platform compatibility

  [JSExport]
#pragma warning restore CA1416 // Validate platform compatibility

  public static string Decrypt(string key, string msg)
  {
    using var input = new ArmoredInputStream(new MemoryStream(Encoding.UTF8.GetBytes(msg)));
    var pgpFactory = new PgpObjectFactory(input);

    var firstObject = pgpFactory.NextPgpObject();
    if (firstObject is not PgpEncryptedDataList)
    {
      firstObject = pgpFactory.NextPgpObject();
    }

    PgpPrivateKey keyToUse = null;
    PgpSecretKeyRingBundle keyRing = null;

    using (var keyStream = new ArmoredInputStream(new MemoryStream(Encoding.UTF8.GetBytes(key))))
    {
      keyRing = new PgpSecretKeyRingBundle(keyStream);
    }

    var encryptedData = ((PgpEncryptedDataList)firstObject)
      .GetEncryptedDataObjects()
      .Cast<PgpPublicKeyEncryptedData>()
      .FirstOrDefault(x =>
      {
        var key = keyRing.GetSecretKey(x.KeyId);
        if (key != null)
        {
          keyToUse = key.ExtractPrivateKey([]);
          return true;
        }
        return false;
      });

    if (keyToUse == null)
    {
      throw new PgpException("Cannot find secret key for message.");
    }

    Stream clearText = encryptedData.GetDataStream(keyToUse);
    PgpObject message = new PgpObjectFactory(clearText).NextPgpObject();
    string result = null;

    if (message is PgpCompressedData data)
    {
      message = new PgpObjectFactory(data.GetDataStream()).NextPgpObject();
    }

    if (message is PgpLiteralData literalData)
    {
      using var outputStream = new MemoryStream();
      Streams.PipeAll(literalData.GetInputStream(), outputStream);
      result = Encoding.UTF8.GetString(outputStream.ToArray());
    }
    else
    {
      throw new PgpException("Message is not encoded correctly.");
    }

    if (encryptedData.IsIntegrityProtected() && !encryptedData.Verify())
    {
      throw new PgpException("Message failed integrity check!");
    }
    return result;
  }
}
