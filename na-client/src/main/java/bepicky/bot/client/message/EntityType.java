package bepicky.bot.client.message;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum EntityType {
    CATEGORY("c"), LANGUAGE("l"), REGION("r"), SOURCE("s"), NEWS_NOTE("nn"), TRANSITION("t");
    private static final Map<String, EntityType> VALUES = Arrays.stream(values())
        .collect(Collectors.toMap(EntityType::getKey, Function.identity()));

    private final String key;

    EntityType(String key) {
        this.key = key;
    }

    public static EntityType of(String key) {
        return VALUES.get(key);
    }
}
