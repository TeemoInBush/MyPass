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

    private final MyPassSet myPassSet;

    private final List<Command> commands;

    public CommandManager(MyPassSet myPassSet) {
        this.myPassSet = myPassSet;
        this.commands = Arrays.asList(
                new Command("add", myPassSet::add, "新增密码 add name length desc"),
                new Command("get", myPassSet::get, "获取一个密码 get index"),
                new Command("list", myPassSet::list, "打印密码列表 list"),
                new Command("del", myPassSet::delete, "删除密码 del name"),
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
