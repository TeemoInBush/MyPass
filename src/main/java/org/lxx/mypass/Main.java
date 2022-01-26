/*
 * *****************************************************
 * Copyright (C) 2022 bytedance.com. All Rights Reserved
 * This file is part of bytedance EA project.
 * Unauthorized copy of this file, via any medium is strictly prohibited.
 * Proprietary and Confidential.
 * ****************************************************
 */
package org.lxx.mypass;

import org.lxx.mypass.util.MyPassUtils;

import java.util.Scanner;

/**
 * 启动类
 *
 * @author lixinxing.world
 * @date 01/25/2022
 **/
public class Main {

    public static final String EXIT = "exit";

    public static void main(String[] args) {
        System.out.println(MyPassUtils.getWelcomeWord());
        CommandManager commandManager = new CommandManager(new MyPassSet());
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                String input = scanner.nextLine();
                if (input.equals(EXIT)) {
                    break;
                }
                commandManager.execute(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}