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
    public static final Pattern SYMBOL_PATTERN = Pattern.compile("[^0-9A-z]");

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
            password =  Base64.getEncoder().encodeToString(bytes).substring(0, length).replaceAll("[^0-9A-z]", "");
        } while (!NUM_PATTERN.matcher(password).find() || !CHAT_PATTERN.matcher(password).find());
        return password;
    }

    private String getPasswordOnlyNumber(String secret) {
        StringBuilder password = new StringBuilder();
        byte[] bytes = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, secret).hmac(name);
        for (byte b : bytes) {
            password.append(Math.abs(b % 10));
            if (password.length() >= length) {
                break;
            }
        }
        return password.toString();
    }

    private String getPasswordWithCharNumberAndSymbol(String secret) {
        String password = "";
        HmacUtils hmacUtils = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, secret);
        do {
            byte[] bytes = hmacUtils.hmac(name + password);
            password =  Base64.getEncoder().encodeToString(bytes).substring(0, length).replace("+", "_");
        } while (!NUM_PATTERN.matcher(password).find() || !CHAT_PATTERN.matcher(password).find() || !SYMBOL_PATTERN.matcher(password).find());
        return password;
    }

    public static void main(String[] args) {
        OnePass onePass = new OnePass("测试", 10, 2, "");
        System.out.println(onePass.getPassword("aaa"));
        System.out.println(onePass.getPassword("aaa"));
        System.out.println(onePass.getPassword("aaa1"));
        System.out.println(onePass.getPassword("aaa2"));
        System.out.println(onePass.getPassword("aaa3"));
        System.out.println(onePass.getPassword("aaa4"));
        System.out.println(onePass.getPassword("aaa5"));
        System.out.println(onePass.getPassword("aaa6"));
        System.out.println(onePass.getPassword("aaa7"));
    }

    public String print() {
        return String.format("%s （%s）", name, desc);
    }

    public String getName() {
        return name;
    }
}