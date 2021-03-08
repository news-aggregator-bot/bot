package bepicky.bot.core.cmd;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
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
    PAUSE_READER("pr"),
    SETTINGS_READER("str"),
    STATUS_READER("sr"),
    SETTINGS("st"),
    GO_NEXT("gn"),
    GO_PREVIOUS("gp"),
    UPDATE("upd"),
    CHOICE("ce"),
    SEARCH("s");

    public static final Set<CommandType> UTIL = ImmutableSet.of(
        ENABLE_READER,
        DISABLE_READER,
        PAUSE_READER,
        SETTINGS_READER,
        STATUS_READER,
        SETTINGS,
        GO_NEXT,
        GO_PREVIOUS,
        UPDATE
    );
    private static final Map<String, CommandType> VALUES = Arrays.stream(values())
        .collect(Collectors.toMap(CommandType::getKey, Function.identity()));
    private final String key;

    CommandType(String key) {
        this.key = key;
    }

    public static CommandType of(String key) {
        return VALUES.get(key);
    }

    @Override
    public String toString() {
        return key;
    }
}
