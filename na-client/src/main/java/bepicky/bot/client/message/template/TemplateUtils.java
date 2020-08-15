package bepicky.bot.client.message.template;

import com.google.common.collect.ImmutableMap;

public class TemplateUtils {
    private TemplateUtils(){}

    public static final String DIR_DONE = "direction_done";
    public static final String DIR_BACK = "direction_back";
    public static final String DIR_CONTINUE = "direction_continue";
    public static final String DIR_NEXT = "direction_next";
    public static final String DIR_PREV = "direction_previous";
    public static final String DIR_OPTS = "direction_opts";

    public static final String PICK = "pick";
    public static final String PICK_CATEGORY_SUCCESS = "pick_category_success";
    public static final String PICK_LANGUAGE_SUCCESS = "pick_language_success";
    public static final String REMOVE = "remove";
    public static final String REMOVE_CATEGORY_SUCCESS = "rm_category_success";
    public static final String REMOVE_LANGUAGE_SUCCESS = "rm_language_success";

    public static final String WELCOME_LIST_CATEGORY = "welcome_list_categories";
    public static final String WELCOME_LIST_LANGUAGES = "welcome_list_languages";

    public static final String LIST_CATEGORY = "list_categories";
    public static final String LIST_SUBCATEGORIES = "list_subcategories";
    public static final String LIST_LANGUAGES = "list_languages";
    public static final String LIST_SUBCATEGORY = "list_subcategory";

    public static final String NEWS_NOTE = "news_note";

    public static final String DONE = "done";
    public static final String ENABLE = "enable";
    public static final String ENABLE_READER = "enable_reader";
    public static final String ALL_SUBCATEGORIES = "all_subcategories";

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
