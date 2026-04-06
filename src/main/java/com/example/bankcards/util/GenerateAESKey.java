package com.example.bankcards.util;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

public class GenerateAESKey {
    public static void main(String[] args) throws Exception {
        var keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128); // Можно 256, если поддерживает JVM
        SecretKey secretKey = keyGen.generateKey();

        // Base64 представление
        String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("Сгенерированный ключ (Base64): " + base64Key);
    }
}
