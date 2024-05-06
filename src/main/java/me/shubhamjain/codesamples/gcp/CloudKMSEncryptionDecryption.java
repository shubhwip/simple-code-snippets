//package me.shubhamjain.codesamples.gcp;
//
//import java.io.IOException;
//
//public class CloudKMSEncryptionDecryption {
//        // TODO(developer): Replace these variables before running the sample.
//        static String projectId = "project-id";
//        // can be global or region name
//        static String locationId = "global";
//        static String keyRingId = "key-ring-name";
//        static String keyId = "key-name";
//        static String plaintext = "plaintext-to-encrypt";
//
//        public static void main(String[] args) throws IOException {
//            encryptSymmetric(projectId, locationId, keyRingId, keyId, plaintext);
//
//        }
//        public static void encryptSymmetric(
//                String projectId, String locationId, String keyRingId, String keyId, String plaintext)
//                throws IOException {
//            // Initialize client that will be used to send requests. This client only
//            // needs to be created once, and can be reused for multiple requests. After
//            // completing all of your requests, call the "close" method on the client to
//            // safely clean up any remaining background resources.
//            try (KeyManagementServiceClient client = KeyManagementServiceClient.create()) {
//                // Build the key version name from the project, location, key ring, key,
//                // and key version.
//                CryptoKeyName keyVersionName = CryptoKeyName.of(projectId, locationId, keyRingId, keyId);
//
//                // Encrypt the plaintext.
//                EncryptResponse response = client.encrypt(keyVersionName, ByteString.copyFromUtf8(plaintext));
//                System.out.printf("Ciphertext: %s%n", response.getCiphertext().toStringUtf8());
//            }
//        }
//
//    // Decrypt data that was encrypted using a symmetric key.
//    public void decryptSymmetric(
//            String projectId, String locationId, String keyRingId, String keyId, byte[] ciphertext)
//            throws IOException {
//        // Initialize client that will be used to send requests. This client only
//        // needs to be created once, and can be reused for multiple requests. After
//        // completing all of your requests, call the "close" method on the client to
//        // safely clean up any remaining background resources.
//        try (KeyManagementServiceClient client = KeyManagementServiceClient.create()) {
//            // Build the key version name from the project, location, key ring, and
//            // key.
//            CryptoKeyName keyName = CryptoKeyName.of(projectId, locationId, keyRingId, keyId);
//
//            // Decrypt the response.
//            DecryptResponse response = client.decrypt(keyName, ByteString.copyFrom(ciphertext));
//            System.out.printf("Plaintext: %s%n", response.getPlaintext().toStringUtf8());
//        }
//    }
//}
