package com.caito.tokeninyector.components;

import com.caito.tokeninyector.api.exceptions.customs.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class KeyUtils {
    private static final Logger log = LoggerFactory.getLogger(KeyUtils.class);
    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;

    public KeyUtils()throws Exception{
        this.privateKey = loadPrivateKey("src/main/resources/keys/private_key.pem");
        this.publicKey = loadPublicKey("src/main/resources/keys/public_key.pem");
    }

    private RSAPrivateKey loadPrivateKey(String filename) throws Exception{
        try {
            String key = new String(Files.readAllBytes(Paths.get(filename)));
            String privateKeyPEM = key.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (IOException e) {
            log.error("--> ERROR: error leyendo clave privada");
            throw new NotFoundException("error en clave privada");
        }

    }

    private RSAPublicKey loadPublicKey(String filename) throws Exception{
        try {
            String key = new String(Files.readAllBytes(Paths.get(filename)));
            String publicKeyPEM = key.replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        }catch (IOException e) {
            log.error("--> ERROR: error leyendo clave publica");
            throw new NotFoundException("error en clave publica");
        }
    }

    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }
}
