package com.ramnelov.backend.unit.security;





import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import com.ramnelov.backend.security.Jwks;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwksTest {

    @Test
    public void testGenerateRsa() throws JOSEException {
        RSAKey rsaKey = Jwks.generateRsa();

        assertNotNull(rsaKey.toPublicKey());
        assertNotNull(rsaKey.toPrivateKey());
        assertNotNull(rsaKey.getKeyID());
    }
}
