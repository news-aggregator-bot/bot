package bepicky.bot.core.message.template;

import com.google.common.collect.ImmutableMap;

public class TemplateUtils {

    private TemplateUtils() {}

    public static ImmutableMap<String, Object> name(String name) {
        return params("name", name);
    }

    public static ImmutableMap<String, Object> page(int page) {
        return params("page", page);
    }

    public static ImmutableMap<String, Object> params(String key, Object value) {
        return ImmutableMap.<String, Object>builder()
            .put(key, value)
            .build();
    }

    public static ImmutableMap<String, Object> params(String key1, Object value1, String key2, Object value2) {
        return ImmutableMap.<String, Object>builder()
            .put(key1, value1)
            .put(key2, value2)
            .build();
    }
}
