package org.lxx.mypass.aes;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

/**
 * AES对称加密工具类
 *
 * @author 单红宇
 * @date 2019年7月18日
 */
public class GcmCipher {

    private static final String KEY_ALGORITHM = "AES";
    /**
     * 默认的加密算法
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/GCM/PKCS5Padding";
    private static final int AES_KEY_SIZE = 256;

    private final SecretKeySpec key;

    public GcmCipher(String key) throws NoSuchAlgorithmException {
        this.key = getSecretKey(key);
    }

    /**
     * AES 加密操作
     *
     * @param content     待加密内容
     * @return 返回Base64转码后的加密数据
     */
    public String encrypt(String content) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, "lixinxing".getBytes(StandardCharsets.UTF_8));
            cipher.init(1, key, gcmParameterSpec);
            // initialization vector 初始向量
            byte[] iv = cipher.getIV();
            assert iv.length == 12;
            byte[] encryptData = cipher.doFinal(content.getBytes());
            assert encryptData.length == content.getBytes().length + 16;

            return Base64.getEncoder().encodeToString(GcmCipher.concatAll(iv, encryptData));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] concatAll(byte[] first, byte[]... rest) {
        int totalLength = first.length;
        for (byte[] array : rest) {
            totalLength += array.length;
        }
        byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (byte[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    /**
     * 生成加密秘钥
     */
    private static SecretKeySpec getSecretKey(String encryptPass) throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        // 初始化密钥生成器，AES要求密钥长度为128位、192位、256位
        kg.init(AES_KEY_SIZE, new SecureRandom(encryptPass.getBytes()));
        SecretKey secretKey = kg.generateKey();
        // 转换为AES专用密钥
        return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
    }
}