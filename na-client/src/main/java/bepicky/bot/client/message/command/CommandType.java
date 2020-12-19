package bepicky.bot.client.message.command;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum CommandType {
    LIST("l"),
    SUBLIST("sl"),
    PICK("p"),
    PICK_ALL("pa"),
    REMOVE("r"),
    REMOVE_ALL("ra"),
    ENABLE_READER("er"),
    DISABLE_READER("dr"),
    SETTINGS("s"),
    GO_NEXT("gn"),
    GO_PREVIOUS("gp");
    private static final Map<String, CommandType> VALUES = Arrays.stream(values())
        .collect(Collectors.toMap(CommandType::getKey, Function.identity()));
    private final String key;

    CommandType(String key) {
        this.key = key;
    }

    public static CommandType of(String key) {
        return VALUES.get(key);
    }
}
