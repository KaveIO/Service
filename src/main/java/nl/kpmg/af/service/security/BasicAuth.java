package nl.kpmg.af.service.security;

import javax.xml.bind.DatatypeConverter;

/**
 * Created by fziliotto on 24-6-16. Decodes a HTTP Basic auth string encoded in BASE 64
 */
public class BasicAuth {
	/**
	 * Decode the basic auth and convert it to array login/password
	 * 
	 * @param auth
	 *            The string encoded
	 * @return The login (case 0), the password (case 1)
	 */
	public static String[] decode(String auth) {
		// Replacing "Basic THE_BASE_64" to "THE_BASE_64" directly
		auth = auth.replaceFirst("[B|b]asic ", "");

		// Decode the Base64 into byte[]
		byte[] decodedBytes = DatatypeConverter.parseBase64Binary(auth);

		// If the decode fails in any case
		if (decodedBytes == null || decodedBytes.length == 0) {
			return null;
		}

		// Now we can convert the byte[] into a splitted array :
		// - the first one is login,
		// - the second one password
		return new String(decodedBytes).split(":", 2);
	}
}