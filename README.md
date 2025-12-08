# CS4600-Fall2025 Final Project

## Group: 

Arin Boyadjian, Reim Nouh, Alexander Leang

## How to run:

Download the repository as a zip. Unzip and navigate to the extracted folder.

Compile all files with a terminal:

```bash
javac RSAKeyGenerator.java Sender.java Receiver.java
```

## Demo

Run:

```bash
java RSAKeyGenerator
```

to generate the sender/receiver public/private keypairs. Distribute the keys as necessary, although the demo will work with both keys on a single computer.

In `message.txt`, create any text message to be encrypted and sent to the receiver. Then run:

```bash
java Sender
```

to AES encrypt the message with the RSA keypair, generate and attach a MAC, and write the data to `Transmitted_Data.txt`.

To decrypt, run:

```bash
java Receiver
```

and the message should successfully be decrypted and printed to output.

### Scenario A: MAC Verified

This is the default scenario, when the message has been encrypted, transmitted, and decrypted without being modified or read by an unauthorized party.

### Scenario B: MAC Failed

If the encrypted message in `Transmitted_Data.txt` has been tampered with (data changed or decrypted then re-encrypted), the HMAC that is computed with the message will change. Upon decryption by the receiving party, the resulting HMAC will be different than the one computed by the receiver, and a warning message will print. 

**Example:** After running the `Sender` Java binary, modify `Transmitted_Data.txt` by changing many or just one character in the file. Then, run the `Receiver` binary. The output should be:

```bash
mac verification failed.
```
