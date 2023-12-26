package com.ramnelov.backend.unit.security;

import com.ramnelov.backend.security.KeyGeneratorUtils;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static org.junit.jupiter.api.Assertions.*;

public class KeyGeneratorUtilsTest {

    @Test
    public void testGenerateRsaKey() {
        // Call method to test
        KeyPair result = KeyGeneratorUtils.generateRsaKey();

        // Assert result
        assertNotNull(result);
        assertTrue(result.getPrivate() instanceof RSAPrivateKey);
        assertTrue(result.getPublic() instanceof RSAPublicKey);
    }

    @Test
    public void testGenerateRsaKeyException() {
        try (MockedStatic<KeyPairGenerator> mocked = Mockito.mockStatic(KeyPairGenerator.class)) {
            mocked.when(() -> KeyPairGenerator.getInstance("RSA")).thenThrow(NoSuchAlgorithmException.class);

            // Call method to test and assert exception
            assertThrows(IllegalStateException.class, KeyGeneratorUtils::generateRsaKey);
        }
    }
}