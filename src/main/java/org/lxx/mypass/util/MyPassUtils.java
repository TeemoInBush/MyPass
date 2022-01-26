/*
 * *****************************************************
 * Copyright (C) 2022 bytedance.com. All Rights Reserved
 * This file is part of bytedance EA project.
 * Unauthorized copy of this file, via any medium is strictly prohibited.
 * Proprietary and Confidential.
 * ****************************************************
 */
package org.lxx.mypass.util;

import java.time.LocalDate;

/**
 * 工具类
 *
 * @author lixinxing.world
 * @date 01/24/2022
 **/
public class MyPassUtils {

    public static String getWelcomeWord() {
        if (LocalDate.now().getMonth().getValue() == 12 && LocalDate.now().getDayOfMonth() >= 30
                || (LocalDate.now().getMonth().getValue() == 1 && LocalDate.now().getDayOfMonth() <= 4)) {
            return " | |__   __ _ _ __  _ __  _   _   _ __   _____      __  _   _  ___  __ _ _ __ \n" +
                    " | '_ \\ / _` | '_ \\| '_ \\| | | | | '_ \\ / _ \\ \\ /\\ / / | | | |/ _ \\/ _` | '__|\n" +
                    " | | | | (_| | |_) | |_) | |_| | | | | |  __/\\ V  V /  | |_| |  __/ (_| | |   \n" +
                    " |_| |_|\\__,_| .__/| .__/ \\__, | |_| |_|\\___| \\_/\\_/    \\__, |\\___|\\__,_|_|   \n" +
                    "             |_|   |_|    |___/                         |___/                ";
        }
        return "  _    _                           ______                        _             \n" +
                " | |  | |                         |  ____|                      | |            \n" +
                " | |__| | __ _ _ __  _ __  _   _  | |____   _____ _ __ _   _  __| | __ _ _   _ \n" +
                " |  __  |/ _` | '_ \\| '_ \\| | | | |  __\\ \\ / / _ \\ '__| | | |/ _` |/ _` | | | |\n" +
                " | |  | | (_| | |_) | |_) | |_| | | |___\\ V /  __/ |  | |_| | (_| | (_| | |_| |\n" +
                " |_|  |_|\\__,_| .__/| .__/ \\__, | |______\\_/ \\___|_|   \\__, |\\__,_|\\__,_|\\__, |\n" +
                "              | |   | |     __/ |                       __/ |             __/ |\n" +
                "              |_|   |_|    |___/                       |___/             |___/ ";
    }

}