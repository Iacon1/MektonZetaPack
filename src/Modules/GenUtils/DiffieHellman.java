// By Iacon1
// Created 11/27/2021
// Diffie-Hellman Key Exchange
// Steps:
// 1. Start
// 2. Send them your publicComponent
// 3. Run end with their publicComponent
// https://www.programcreek.com/java-api-examples/?api=javax.crypto.KeyAgreement
// https://stackoverflow.com/questions/10900643/how-can-i-construct-a-java-security-publickey-object-from-a-base64-encoded-strin

package Modules.GenUtils;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.KeyAgreement;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import GameEngine.Net.ConnectionPairThread;
import Utils.Logging;
import Utils.StringCipher;

public final class DiffieHellman
{
	private static final String algorithm = "DiffieHellman";
	private static final int encodeRadix = 16; // Radix for encoding mixes to strings
	private static final int keyLength = 16; // Key length in bytes

	private String stringFromKey(PublicKey key)
	{
		return new BigInteger(key.getEncoded()).toString(encodeRadix);
	}
	private PublicKey keyFromString(String string)
	{
		try
		{
			byte[] bytes = new BigInteger(string, encodeRadix).toByteArray();
			KeyFactory factory = KeyFactory.getInstance(algorithm);
			X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(bytes);
			return factory.generatePublic(encodedKeySpec);
		}
		catch (Exception e) {Logging.logException(e); return null;}
	}
	
	private DHParameterSpec getParams() 
	{
		// https://www.rfc-editor.org/rfc/rfc3526
		BigInteger modulus = new BigInteger(
				"FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD1" +
				"29024E088A67CC74020BBEA63B139B22514A08798E3404DD" +
				"EF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245" +
				"E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7ED" +
				"EE386BFB5A899FA5AE9F24117C4B1FE649286651ECE45B3D" +
				"C2007CB8A163BF0598DA48361C55D39A69163FA8FD24CF5F" +
				"83655D23DCA3AD961C62F356208552BB9ED529077096966D" +
				"670C354E4ABC9804F1746C08CA18217C32905E462E36CE3B" +
				"E39E772C180E86039B2783A2EC07A28FB5C55DF06F4C52C9" +
				"DE2BCBF6955817183995497CEA956AE515D2261898FA0510" +
				"15728E5A8AACAA68FFFFFFFFFFFFFFFF", 16);
		BigInteger base = new BigInteger("2", 16);
		
		return new DHParameterSpec(modulus, base);
	}
	
	private String publicComponent = null;
	private KeyAgreement keyAgreement = null;

	/** Starts the key-generating process.
	 * 
	 */
	public void start()
	{
		KeyPairGenerator keyGen;
		try
		{
			keyGen = KeyPairGenerator.getInstance(algorithm);
			DHParameterSpec params = getParams();
			keyGen.initialize(params);
			KeyPair keyPair = keyGen.generateKeyPair();
		
			keyAgreement = KeyAgreement.getInstance(algorithm);
			keyAgreement.init(keyPair.getPrivate());
			
			publicComponent = stringFromKey(keyPair.getPublic());
		}
		catch (Exception e) {Logging.logException(e);}
	}
	/** Returns the public mix of the base, modulus, and secret.
	 *	@return The public mix.
	 */
	public String getPublicMix() {return publicComponent;}
	/** Ends the key-generating process, giving the thread an encryption key.
	 *	@param mix The other party's public mix.
	 *	@param thread The thread to give the resulting key to.
	 */
	public void end(String mix, ConnectionPairThread thread)
	{
		try
		{
			if (mix == null) return; // TODO why does this happen?

			keyAgreement.doPhase(keyFromString(mix), true);
		
			byte[] secret = keyAgreement.generateSecret();
			thread.setKey(new SecretKeySpec(secret, 0, keyLength, "AES"));
		}
		catch (Exception e) {Logging.logException(e);}
	}
}
