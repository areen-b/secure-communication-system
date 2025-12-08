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

To generate the sender/receiver public/private keypairs. Distribute the keys as necessary, though the demo will work with both keys on a single computer.

In `message.txt`, create any text message to be encrypted and sent to the receiver. Then run:

```bash
java Sender
```

to AES encrypt the message with the RSA keypair, attach a MAC, and write the data to `Transmitted_Data.txt`.

To decrypt, run:

```bash
java Receiver
```

and the message should be successfully decrypted and printed to output.