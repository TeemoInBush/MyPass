package org.lxx.mypass;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * 命令枚举
 *
 * @author lixinxing.world
 * @date 01/25/2022
 **/
public class CommandManager {

    private final List<Command> commands;

    public CommandManager(MyPassSet myPassSet) {
        this.commands = Arrays.asList(
                new Command("add", myPassSet::add, "新增密码 add name(host/username) length mode desc\n\tmode 0-Char and number; 1-Only number; 2-Char number and symbol"),
                new Command("get", myPassSet::get, "获取一个密码 get index"),
                new Command("list", myPassSet::list, "打印密码列表 list"),
                new Command("del", myPassSet::delete, "删除密码 del name"),
                new Command("update", myPassSet::update, "更新密码 update name length mode desc"),
                new Command("quit", this::quit, "退出 quit"),
                new Command("help", this::help, "帮助 help"));
    }

    private void quit(String input) {
        System.exit(0);
    }

    private void help(String input) {
        System.out.println(MyPassSet.LINE);
        for (Command command : commands) {
            System.out.printf("%s - %s\n", command.prefix, command.desc);
        }
        System.out.println(MyPassSet.LINE);
    }

    public void execute(String input) {
        input = input.trim();
        for (Command command : commands) {
            if (input.startsWith(command.prefix)) {
                String param = input.replaceFirst(command.prefix, "").trim();
                command.handler.accept(param);
            }
        }
    }

    public static class Command {
        String prefix;

        Consumer<String> handler;

        String desc;

        public Command(String prefix, Consumer<String> handler, String desc) {
            this.prefix = prefix;
            this.handler = handler;
            this.desc = desc;
        }
    }

}
