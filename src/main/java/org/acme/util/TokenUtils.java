package org.acme.util;

import java.security.PrivateKey;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import io.smallrye.jwt.build.Jwt;
import java.io.InputStream;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.KeyFactory;
import java.util.Base64;

public class TokenUtils
{
	public static String generateToken(final String username, final long duration, final String issuer) throws Exception
	{
		String privateKeyLocation = "/privateKey.pem";
		PrivateKey privateKey = readPrivateKey(privateKeyLocation);
		JwtClaimsBuilder claimsBuilder = Jwt.claims();
		long currentTimeInSecs = currentTimeInSecs();
		claimsBuilder.issuer(issuer);
		claimsBuilder.subject(username);
		claimsBuilder.issuedAt(currentTimeInSecs);
		claimsBuilder.expiresAt(currentTimeInSecs + duration);
		claimsBuilder.groups("USER");
		return claimsBuilder.jws().signatureKeyId(privateKeyLocation).sign(privateKey);
	}

	public static PrivateKey readPrivateKey(final String pemResName)
	{
		try (InputStream contentIS = TokenUtils.class.getResourceAsStream(pemResName))
		{
			byte[] tmp = new byte[4096];
			int length = contentIS.read(tmp);
			return decodePrivateKey(new String(tmp, 0, length, "UTF-8"));
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public static PrivateKey decodePrivateKey(final String pemEncoded) throws Exception
	{
		byte[] encodedBytes = toEncodedBytes(pemEncoded);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePrivate(keySpec);
	}

	public static byte[] toEncodedBytes(final String pemEncoded)
	{
		return Base64.getDecoder().decode(pemEncoded);
	}

	public static long currentTimeInSecs()
	{
		return (long) (System.currentTimeMillis() / 1000);
	}
}