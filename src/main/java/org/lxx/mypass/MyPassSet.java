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
import org.lxx.mypass.aes.GcmCipher;
import org.lxx.mypass.repository.ObjectDAO;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.Console;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

/**
 * 密码管理软件
 *
 * @author lixinxing.world
 * @date 01/24/2022
 **/
public class MyPassSet {

    public static final String LINE = "----------------------------";
    public static final String FILE_NAME = "PASS_SET.txt";
    private static final ObjectDAO DAO = new ObjectDAO("");
    public static final MyPassSet INSTANCE = new MyPassSet();

    private final List<OnePass> passSet = Optional.ofNullable(DAO.<List<OnePass>>getObject(FILE_NAME)).orElse(new ArrayList<>());

    public void add(String param) {
        String[] args = getArgs(param, 3);
        if (args == null) {
            return;
        }
        OnePass onePass = new OnePass();
        onePass.setName(args[0]);
        onePass.setLength(Integer.parseInt(args[1]));
        onePass.setDesc(args[2]);
        passSet.add(onePass);

        System.out.println(LINE);
        System.out.println("success");
        System.out.println(LINE);

        DAO.saveObject(passSet, FILE_NAME);
    }

    public void list(String param) {
        System.out.println(LINE);
        for (int i = 0; i < passSet.size(); i++) {
            OnePass onePass = passSet.get(i);
            System.out.printf("%d - %s %s\n", i, onePass.getName(), onePass.getDesc());
        }
        System.out.println(LINE);
    }

    public void get(String param) {
        String[] args = getArgs(param, 1);
        if (args == null) {
            return;
        }
        Console cons = System.console();
        if(cons == null){
            System.out.println("Couldn't get Console instance, maybe you're running this from within an IDE?");
            System.exit(0);
        }
        int index = Integer.parseInt(args[0]);
        String secret = new String(cons.readPassword("input password:"));
        OnePass onePass = passSet.get(index);

        try {
            String encoded = hash(secret, onePass.getName());
            // 获取系统剪贴板
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            // 封装文本内容
            Transferable trans = new StringSelection(encoded.substring(0, onePass.getLength()));
            // 把文本内容设置到系统剪贴板
            clipboard.setContents(trans, null);

            System.out.println(LINE);
            System.out.println("success, copy to clipboard.");
            System.out.println(LINE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String gcm(String secret, String content) throws NoSuchAlgorithmException {
        GcmCipher gcmCipher = new GcmCipher(secret);
        return gcmCipher.encrypt(content);
    }

    private String hash(String secret, String content) {
        byte[] bytes = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, secret).hmac(content);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public void delete(String param) {
        String[] args = getArgs(param, 1);
        if (args == null) {
            return;
        }
        String name = args[0];
        OnePass password = getPassword(name);
        if (password != null) {
            passSet.remove(password);
        }
        list("");
        DAO.saveObject(passSet, FILE_NAME);
    }

    private OnePass getPassword(String name) {
        for (OnePass onePass : passSet) {
            if (onePass.getName().equals(name)) {
                return onePass;
            }
        }
        return null;
    }

    private String[] getArgs(String param, int length) {
        String[] args = param.split("\\s");
        if (args.length != length) {
            System.out.println(LINE);
            System.out.println("args length should be " + length + "!");
            System.out.println(LINE);
            return null;
        }
        return args;
    }

}