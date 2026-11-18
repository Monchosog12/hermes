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

/**
 * Utilidad criptografica para el cifrado y el descifrdo simétrico de datos
 * utilizados AES en modo GCM, asi como funciones de resumen criptografico como
 * el hashing mediante algoritmos estandar como MD5,SHA
 * 
 * <p>
 * Incluye soperte de IVs derivados dinamicamente mediante el HMAC_SHA254 o
 * previstos de forma manual.
 *</p>
 */
public class AESUtil {

	/** Algoritmo de cifrado simétrico base. */
	private final static String ALGORITMO = "AES";

	/** Esquema y modo de cifrado utilizado (AES con GCM y sin relleno). */
	private final static String TIPOCIFRADO = "AES/GCM/NoPadding";

	/**
	 * Cifra un texto utilizando AES/GCM con un Vector de Inicialización (IV) y
	 * llave personalizados.
	 * 
	 * @param llave Llave simétrica en formato de cadena.
	 * @param iv    Vector de inicialización en formato de cadena.
	 * @param texto Texto plano que se desea cifrar.
	 * @return Cadena resultante codificada en Base64 con el texto cifrado.
	 */
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

	/**
	 * Descifra un texto previamente cifrado utilizando AES/GCM con llave e IV
	 * personalizados.
	 * 
	 * @param llave     Llave simétrica en formato de cadena.
	 * @param iv        Vector de inicialización en formato de cadena.
	 * @param encrypted Texto cifrado codificado en Base64.
	 * @return El texto plano descifrado, o una cadena vacía en caso de error.
	 */
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

	/** Tamaño del IV recomendado para AES/GCM (12 bytes). */
	private final static int LARGO_IV = 12;

	/** Tamaño de la etiqueta de autenticación de GCM: 128 bits = 16 bytes. */
	private final static int LARGO_TAG = 16;

	/**
	 * Lee y valida una variable de entorno que contiene la llave criptográfica.
	 * 
	 * @param nombre        Nombre de la variable de entorno.
	 * @param largosValidos Longitudes válidas permitidas en bytes (ej. 16, 24, 32).
	 * @return La llave en formato de cadena si es válida.
	 * @throws IllegalStateException si la variable no existe, está vacía o su
	 *                               longitud es incorrecta.
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
	 * Cifra un texto plano con AES/GCM utilizando la llave obtenida de la variable
	 * de entorno {@code AES_KEY}.
	 * <p>
	 * El IV no es fijo: se calcula como los primeros 12 bytes de HMAC-SHA256(llave,
	 * texto) y se antepone al resultado. El formato de salida final es Base64(iv +
	 * cifrado).
	 * </p>
	 * 
	 * @param plainText Texto plano a cifrar.
	 * @return Cadena en Base64 con el IV y el texto cifrado concatenados.
	 * @throws IllegalStateException si ocurre un error durante el proceso de
	 *                               cifrado o falta la llave.
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
	 * Descifra un texto generado previamente por {@link #encrypt(String)}.
	 * <p>
	 * Lee el IV de los primeros 12 bytes del arreglo decodificado y descifra el
	 * resto. Devuelve una cadena vacía si el valor no es válido o no corresponde al
	 * esquema actual.
	 * </p>
	 * 
	 * @param encrypted Texto cifrado en formato Base64.
	 * @return El texto plano original, o {@code ""} si el descifrado falla.
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

	/**
	 * Calcula el código de autenticación de mensajes basado en Hash (HMAC)
	 * utilizando SHA-256.
	 * 
	 * @param key   Llave secreta para el cálculo de HMAC.
	 * @param texto Arreglo de bytes sobre el cual se aplicará la función.
	 * @return Arreglo de bytes resultante del hash HMAC-SHA256.
	 * @throws GeneralSecurityException si el algoritmo no está disponible o falla
	 *                                  la inicialización.
	 */
	private static byte[] hmacSha256(String key, byte[] texto) throws GeneralSecurityException {
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(new SecretKeySpec(key.getBytes(), "HmacSHA256"));
		return mac.doFinal(texto);
	}

	/**
	 * Genera un resumen criptográfico (hash) del contenido utilizando el algoritmo
	 * MD5.
	 * 
	 * @param content Contenido en texto plano a transformar.
	 * @return Cadena hexadecimal con el hash MD5.
	 */
	public static String hashingToMD5(String content) {
		return DigestUtils.md5Hex(content);
	}

	/**
	 * Genera un resumen criptográfico (hash) del contenido utilizando el algoritmo
	 * SHA-1.
	 * 
	 * @param content Contenido en texto plano a transformar.
	 * @return Cadena hexadecimal con el hash SHA-1.
	 */
	public static String hashingToSHA1(String content) {
		return DigestUtils.sha1Hex(content);
	}

	/**
	 * Genera un resumen criptográfico (hash) del contenido utilizando el algoritmo
	 * SHA-256.
	 * 
	 * @param content Contenido en texto plano a transformar.
	 * @return Cadena hexadecimal con el hash SHA-256.
	 */
	public static String hashingToSHA256(String content) {
		return DigestUtils.sha256Hex(content);
	}

	/**
	 * Genera un resumen criptográfico (hash) del contenido utilizando el algoritmo
	 * SHA-384.
	 * 
	 * @param content Contenido en texto plano a transformar.
	 * @return Cadena hexadecimal con el hash SHA-384.
	 */
	public static String hashingToSHA384(String content) {
		return DigestUtils.sha384Hex(content);
	}

	/**
	 * Genera un resumen criptográfico (hash) del contenido utilizando el algoritmo
	 * SHA-512.
	 * 
	 * @param content Contenido en texto plano a transformar.
	 * @return Cadena hexadecimal con el hash SHA-512.
	 */
	public static String hashingToSHA512(String content) {
		return DigestUtils.sha512Hex(content);
	}

	/**
	 * Método principal de prueba y demostración de las funciones de cifrado,
	 * descifrado y hashing.
	 * 
	 * @param args Argumentos de la línea de comandos.
	 */
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
