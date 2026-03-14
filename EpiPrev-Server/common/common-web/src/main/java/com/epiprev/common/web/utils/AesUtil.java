package com.epiprev.common.web.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import io.micrometer.common.util.StringUtils;

import java.nio.charset.StandardCharsets;

public class AesUtil {

//    static void main() {
//        String password = "admin";
//        System.out.println(AesUtil.encrypt(password));
//    }

    private static final AES aes;

    static {
        // 固定 key → 固定密文（方便等值匹配）
        String key = "2026恭喜发财,代码全过!!!";
        byte[] keyBytes = SecureUtil.generateKey(
                SymmetricAlgorithm.AES.getValue(),
                key.getBytes(StandardCharsets.UTF_8)
        ).getEncoded();

        aes = SecureUtil.aes(keyBytes);
    }

    /**
     * 加密（Base64）
     */
    public static String encrypt(String plainText) {
        if (StringUtils.isBlank(plainText)) {
            return plainText;
        }
        return aes.encryptBase64(plainText);
    }

    /**
     * 解密（Base64）
     */
    public static String decrypt(String cipherText) {
        if (StringUtils.isBlank(cipherText)) {
            return cipherText;
        }
        return aes.decryptStr(cipherText, StandardCharsets.UTF_8);
    }
}