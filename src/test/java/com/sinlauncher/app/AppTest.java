package com.sinlauncher.app;

import static org.junit.Assert.assertTrue;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.junit.Test;
import org.springframework.context.annotation.Bean;

/**
 * Unit test for simple com.App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

@Test
    public void testEncryptionKey() {
            PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
            SimpleStringPBEConfig config = new SimpleStringPBEConfig();
            config.setPassword(System.getenv("ENCRYPTION_PASSWORD"));
            config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
            config.setKeyObtentionIterations("1000");
            config.setPoolSize(1);
            config.setProviderName("SunJCE");
            config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
            config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
            config.setStringOutputType("base64");
            encryptor.setConfig(config);

            // FOR TESTING PURPOSE WILL REMOVE THIS LATER
            // String plainText = "???";
//            System.out.println("Encrypted value is : " + encryptor.encrypt(plainText));
//            System.out.println(encryptor.decrypt("vXpoQcbIV1xysEBikQQLUgj3sTQnQX3QdnRiszkimtpMM6C/DFDlrhpi3qqj3wF6"));
    }
}
