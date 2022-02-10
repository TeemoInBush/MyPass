/*
 * *****************************************************
 * Copyright (C) 2022 bytedance.com. All Rights Reserved
 * This file is part of bytedance EA project.
 * Unauthorized copy of this file, via any medium is strictly prohibited.
 * Proprietary and Confidential.
 * ****************************************************
 */
package org.lxx.mypass.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.lxx.mypass.OnePass;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.List;

/**
 * 持久化对象
 *
 * @author lixinxing.world
 * @date 01/25/2022
 **/
public class ObjectDAO {

    /**
     * Gson配置下划线转驼峰，根据使用的Json工具设置
     */
    private static final Gson GSON = new GsonBuilder().create();
    private final String rootPath;

    public ObjectDAO(String rootPath) {
        this.rootPath = rootPath;
        System.out.println("文件存储在：" + Paths.get(rootPath).toAbsolutePath());
    }

    public List<OnePass> getObject(String fileName) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(rootPath + fileName))) {
            String json = bufferedReader.readLine();
            return GSON.fromJson(json, new TypeToken<List<OnePass>>() {}.getType());
        } catch (Exception e) {
            System.out.println("read file failed! " + e);
            return null;
        }
    }

    public void saveObject(List<OnePass> object, String fileName) {
        try (BufferedWriter outputStream = new BufferedWriter(new FileWriter(rootPath + fileName))) {
            outputStream.write(GSON.toJson(object));
        } catch (Exception e) {
            System.out.println("write file failed! " + e);
        }
    }

}