package vlad110kg.news.aggregator.bot.telegram.message.template;

import com.google.common.collect.ImmutableMap;

public class TemplateUtils {
    private TemplateUtils(){}
    public static final String DIR_BACK = "direction_back";
    public static final String DIR_CONTINUE = "direction_continue";
    public static final String DIR_NEXT = "direction_next";
    public static final String DIR_PREV = "direction_previous";

    public static final String PICK_CATEGORY = "pick_category";

    public static final String LIST_CATEGORY = "list_categories";
    public static final String WELCOME_LIST_CATEGORY = "welcome_list_categories";
    public static final String LIST_SUBCATEGORY = "list_subcategories";

    public static final String ALL_SUBCATEGORIES = "all_subcategories";


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
