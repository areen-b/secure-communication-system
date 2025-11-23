import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class RSAKeyGenerator {
    public static void main(String[] args) throws Exception {

        // generate sender RSA key pair (from oracle ch11)
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair senderPair = kpg.generateKeyPair();

        Files.write(Paths.get("SenderPublic.key"), senderPair.getPublic().getEncoded());
        Files.write(Paths.get("SenderPrivate.key"), senderPair.getPrivate().getEncoded());

        // generate receiver RSA key pair
        KeyPair receiverPair = kpg.generateKeyPair();

        Files.write(Paths.get("ReceiverPublic.key"), receiverPair.getPublic().getEncoded());
        Files.write(Paths.get("ReceiverPrivate.key"), receiverPair.getPrivate().getEncoded());

        System.out.println("RSA key pairs generated");
    }

}
