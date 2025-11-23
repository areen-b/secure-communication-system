import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class Sender {
    public static void main(String[] args) throws Exception {
        // load receiver publci key
        // (https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/security/spec/X509EncodedKeySpec.html)
        byte[] receiverPublicBytes = Files.readAllBytes(Paths.get("ReceiverPublic.key"));
        X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(receiverPublicBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey receiverPublicKey = keyFactory.generatePublic(publicSpec);

        // read message to send
        String message = new String(Files.readAllBytes(Paths.get("message.txt")));

        // generate aes256 key
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        SecretKey aesKey = keyGenerator.generateKey();

        // generate IV (16bytes)
        // (https://howtodoinjava.com/java/java-security/aes-256-encryption-decryption/)
        byte[] ivBytes = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(ivBytes);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

        // aes encrypt the message
        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey, ivParameterSpec);
        byte[] encryptedMessage = aesCipher.doFinal(message.getBytes());

        // encrypt aes key with rsa
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        rsaCipher.init(Cipher.ENCRYPT_MODE, receiverPublicKey);
        byte[] encryptedAESKey = rsaCipher.doFinal(aesKey.getEncoded());

        // create MAC
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(aesKey);
        byte[] macBytes = mac.doFinal(encryptedMessage);

        // write data
        String output = Base64.getEncoder().encodeToString(encryptedAESKey) + "\n" +
                Base64.getEncoder().encodeToString(ivBytes) + "\n" +
                Base64.getEncoder().encodeToString(encryptedMessage) + "\n" +
                Base64.getEncoder().encodeToString(macBytes);
        Files.write(Paths.get("Transmitted_Data.txt"), output.getBytes());
        System.out.println("data written to Transmitted_Data.txt");
    }
}