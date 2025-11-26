import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Receiver {
    public static void main(String[] args) throws Exception {

        // load receiver private key
        byte[] privateKeyBytes = Files.readAllBytes(Paths.get("ReceiverPrivate.key"));
        PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey receiverPrivateKey = keyFactory.generatePrivate(privateSpec);

        // read the .txt file (code from the attached Oracle link)
        String[] parts = new String(
                Files.readAllBytes(Paths.get("Transmitted_Data.txt"))).split("\n");
        byte[] encryptedAESKey = Base64.getDecoder().decode(parts[0]);
        byte[] ivBytes = Base64.getDecoder().decode(parts[1]);
        byte[] encryptedMessage = Base64.getDecoder().decode(parts[2]);
        byte[] macReceived = Base64.getDecoder().decode(parts[3]);

        // decrypt aes keyt using rsa
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        rsaCipher.init(Cipher.DECRYPT_MODE, receiverPrivateKey);
        byte[] aesKeyBytes = rsaCipher.doFinal(encryptedAESKey);

        SecretKey aesKey = new SecretKeySpec(aesKeyBytes, "AES");

        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(aesKey);
        byte[] macCalculated = mac.doFinal(encryptedMessage);

        if (!MessageDigest.isEqual(macReceived, macCalculated)) {
            System.out.println("mac verification failed."); // message has been tempered with
            return;
        }
        System.out.println("mac verified!"); // message integrity confirmed

        // decrypt message using aes
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesCipher.init(Cipher.DECRYPT_MODE, aesKey, ivSpec);

        byte[] decryptedBytes = aesCipher.doFinal(encryptedMessage);
        String decryptedMessage = new String(decryptedBytes);

        // display the original text
        System.out.println("decrypted message:\n" + decryptedMessage);
    }
}
