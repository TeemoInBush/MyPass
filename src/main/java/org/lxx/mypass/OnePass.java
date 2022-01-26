/*
 * *****************************************************
 * Copyright (C) 2022 bytedance.com. All Rights Reserved
 * This file is part of bytedance EA project.
 * Unauthorized copy of this file, via any medium is strictly prohibited.
 * Proprietary and Confidential.
 * ****************************************************
 */
package org.lxx.mypass;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Pattern;

/**
 * 一个密码
 *
 * @author lixinxing.world
 * @date 01/24/2022
 **/
public class OnePass implements Serializable {

    private static final long serialVersionUID = 1;
    public static final Pattern NUM_PATTERN = Pattern.compile("[0-9]");
    public static final Pattern CHAT_PATTERN = Pattern.compile("[A-z]");

    private final String name;

    private final String desc;

    private final int length;

    private int mode;

    public OnePass(String name, int length, int mode, String desc) {
        if (length > 20 && length < 4) {
            throw new RuntimeException("length should between 4 and 20 !");
        }
        this.name = name;
        this.desc = desc;
        this.length = length;
        this.mode = mode;
    }

    public String getPassword(String secret) {
        switch (mode) {
            case 0:
                return getPasswordWithCharAndNumber(secret);
            case 1:
                return getPasswordOnlyNumber(secret);
            case 2:
                return getPasswordWithCharNumberAndSymbol(secret);
            default:
                System.out.println("mode [" + mode + "] is illegal");
                return null;
        }
    }

    private String getPasswordWithCharAndNumber(String secret) {
        String password = "";
        HmacUtils hmacUtils = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, secret);
        do {
            byte[] bytes = hmacUtils.hmac(name + password);
            password =  Base64.getEncoder().encodeToString(bytes).replace("+", "").substring(0, length);
        } while (!NUM_PATTERN.matcher(password).find() || !CHAT_PATTERN.matcher(password).find());
        return password;
    }

    private String getPasswordOnlyNumber(String secret) {
        StringBuilder password = new StringBuilder();
        byte[] bytes = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, secret).hmac(name);
        for (byte b : bytes) {
            password.append(Math.abs(b % 100));
            if (password.length() >= length) {
                break;
            }
        }
        return password.toString();
    }

    private String getPasswordWithCharNumberAndSymbol(String secret) {
        String password = getPasswordWithCharAndNumber(secret);
        byte[] bytes = password.getBytes(StandardCharsets.UTF_8);
        byte b = bytes[secret.length() % bytes.length];
        int index = Math.abs(b) % bytes.length;
        String result;
        do {
            result = new StringBuilder(password).replace(index, index, "_").toString();
            index = (index + 1) / bytes.length;
        } while (!NUM_PATTERN.matcher(result).find() || !CHAT_PATTERN.matcher(result).find());
        return result;
    }

    public String print() {
        return String.format("%s （%s）", name, desc);
    }

    public String getName() {
        return name;
    }
}