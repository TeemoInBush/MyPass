/*
 * *****************************************************
 * Copyright (C) 2022 bytedance.com. All Rights Reserved
 * This file is part of bytedance EA project.
 * Unauthorized copy of this file, via any medium is strictly prohibited.
 * Proprietary and Confidential.
 * ****************************************************
 */
package org.lxx.mypass.repository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;

/**
 * 持久化对象
 *
 * @author lixinxing.world
 * @date 01/25/2022
 **/
public class ObjectDAO {

    private final String rootPath;

    public ObjectDAO(String rootPath) {
        this.rootPath = rootPath;
        System.out.println("文件存储在：" + Paths.get(rootPath).toAbsolutePath());
    }

    public <T> T getObject(String fileName) {
        try (final FileInputStream fileInputStream = new FileInputStream(rootPath + fileName)) {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (T) objectInputStream.readObject();
        } catch (Exception e) {
            System.out.println("read file failed! " + e);
            return null;
        }
    }

    public <T> void saveObject(T object, String fileName) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(rootPath + fileName))) {
            outputStream.writeObject(object);
        } catch (Exception e) {
            System.out.println("write file failed! " + e);
        }
    }

}