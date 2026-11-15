package co.edu.unbosque.hermes.util;

import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;

public class AESUtil {

	private final static String ALGORITMO = "AES";

	private final static String TIPOCIFRADO = "AES/GCM/NoPadding";

	public static String encrypt(String llave, String iv, String texto) {
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(TIPOCIFRADO);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}

		SecretKeySpec secretKeySpec = new SecretKeySpec(llave.getBytes(), ALGORITMO);
		GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv.getBytes());
		try {
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec);
		} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}

		byte[] encrypted = null;
		try {
			encrypted = cipher.doFinal(texto.getBytes());
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}

		return new String(encodeBase64(encrypted));
	}

	public static String decrypt(String llave, String iv, String encrypted) {
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(TIPOCIFRADO);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}

		SecretKeySpec secretKeySpec = new SecretKeySpec(llave.getBytes(), ALGORITMO);
		GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv.getBytes());
		try {
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);
		} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {

			e.printStackTrace();
		}

		byte[] enc = decodeBase64(encrypted);
		byte[] decrypted = null;
		try {
			decrypted = cipher.doFinal(enc);
			return new String(decrypted);
		} catch (IllegalBlockSizeException | BadPaddingException e) {

			e.printStackTrace();
		}
		return "";
	}

	/** Tamaño del IV recomendado para AES/GCM. */
	private final static int LARGO_IV = 12;

	/** Tamaño de la etiqueta de autenticación de GCM: 128 bits = 16 bytes. */
	private final static int LARGO_TAG = 16;

	/**
	 * La llave se lee de la variable de entorno AES_KEY, nunca se escribe en el
	 * código. Debe tener 16, 24 o 32 caracteres (AES-128, 192 o 256). Todos los que
	 * usen la misma base de datos deben tener la misma llave, o no podrán leer lo
	 * que guardaron los demás.
	 */
	private static String leerVariable(String nombre, int... largosValidos) {
		String valor = System.getenv(nombre);
		if (valor == null || valor.isBlank()) {
			throw new IllegalStateException("Falta la variable de entorno " + nombre);
		}
		for (int largo : largosValidos) {
			if (valor.getBytes().length == largo) {
				return valor;
			}
		}
		throw new IllegalStateException("La variable de entorno " + nombre + " tiene " + valor.getBytes().length
				+ " bytes; debe tener " + java.util.Arrays.toString(largosValidos));
	}

	/**
	 * Cifra con AES/GCM usando la llave de AES_KEY. El IV no es fijo: se calcula
	 * como los primeros 12 bytes de HMAC-SHA256(llave, texto) y se guarda delante
	 * del cifrado. El resultado es Base64(iv + cifrado).
	 *
	 * - El mismo texto siempre produce el mismo resultado, así que se puede buscar
	 *   por un campo cifrado (findByUsername(encrypt("admin"))).
	 * - Textos distintos usan IVs distintos. Con un IV fijo, GCM deja ver el XOR
	 *   de dos textos cifrados con la misma llave.
	 */
	public static String encrypt(String plainText) {
		String key = leerVariable("AES_KEY", 16, 24, 32);
		try {
			byte[] texto = plainText.getBytes(StandardCharsets.UTF_8);
			byte[] iv = Arrays.copyOf(hmacSha256(key, texto), LARGO_IV);

			Cipher cipher = Cipher.getInstance(TIPOCIFRADO);
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), ALGORITMO),
					new GCMParameterSpec(LARGO_TAG * 8, iv));
			byte[] cifrado = cipher.doFinal(texto);

			byte[] resultado = new byte[iv.length + cifrado.length];
			System.arraycopy(iv, 0, resultado, 0, iv.length);
			System.arraycopy(cifrado, 0, resultado, iv.length, cifrado.length);
			return new String(encodeBase64(resultado));
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("No se pudo cifrar el texto", e);
		}
	}

	/**
	 * Descifra lo que produjo {@link #encrypt(String)}: lee el IV de los primeros
	 * 12 bytes y descifra el resto. Devuelve "" si el valor no es un texto cifrado
	 * con la llave actual (por ejemplo, datos guardados en texto plano o con el
	 * esquema anterior de IV fijo).
	 */
	public static String decrypt(String encrypted) {
		String key = leerVariable("AES_KEY", 16, 24, 32);
		byte[] todo = decodeBase64(encrypted);
		if (todo.length < LARGO_IV + LARGO_TAG) {
			return "";
		}
		byte[] iv = Arrays.copyOfRange(todo, 0, LARGO_IV);
		byte[] cifrado = Arrays.copyOfRange(todo, LARGO_IV, todo.length);
		try {
			Cipher cipher = Cipher.getInstance(TIPOCIFRADO);
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), ALGORITMO),
					new GCMParameterSpec(LARGO_TAG * 8, iv));
			return new String(cipher.doFinal(cifrado), StandardCharsets.UTF_8);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
			return "";
		}
	}

	private static byte[] hmacSha256(String key, byte[] texto) throws GeneralSecurityException {
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(new SecretKeySpec(key.getBytes(), "HmacSHA256"));
		return mac.doFinal(texto);
	}


	public static String hashingToMD5(String content) {
		return DigestUtils.md5Hex(content);
	}

	public static String hashingToSHA1(String content) {
		return DigestUtils.sha1Hex(content);
	}

	public static String hashingToSHA256(String content) {
		return DigestUtils.sha256Hex(content);
	}

	public static String hashingToSHA384(String content) {
		return DigestUtils.sha384Hex(content);
	}

	public static String hashingToSHA512(String content) {
		return DigestUtils.sha512Hex(content);
	}

	public static void main(String[] args) {
		String text = "sapohijodeperra";
		System.out.println(text);

		String encoded = encrypt(text);
		System.out.println(encoded);

		String decoded = decrypt(encoded);
		System.out.println(decoded);

		String contrasena = "soyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseñasoyunacontraseña";
		System.out.println(hashingToMD5(contrasena));
		System.out.println(hashingToSHA1(contrasena));
		System.out.println(hashingToSHA256(contrasena));
		System.out.println(hashingToSHA384(contrasena));
		System.out.println(hashingToSHA512(contrasena));
		
	}
}
