package org.gy.framework.util;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class PasswordHelper {
    private static final RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    private static final String                algorithmName         = "md5";
    private static final int                   hashIterations        = 2;

    public static String generateSalt() {
        return randomNumberGenerator.nextBytes().toHex();
    }

    public static String generatePassword(String source,
                                          String salt) {
        return new SimpleHash(algorithmName, source, ByteSource.Util.bytes(salt), hashIterations).toHex();
    }
}
