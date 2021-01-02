package bepicky.bot.client.message.template;

import bepicky.bot.client.message.EntityType;
import bepicky.bot.client.message.command.CommandType;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class TemplateUtils {


    private TemplateUtils() {}

    public static final String HELP = "help";
    public static final String OPTIONS = "options";
    public static final String SETTINGS = "settings";

    public static final String SETTINGS_UPDATE = "settings_update";

    public static final String PICK_REGION_SUCCESS = "pick_region_success";
    public static final String PICK_CATEGORY_SUCCESS = "pick_category_success";
    public static final String PICK_LANGUAGE_SUCCESS = "pick_language_success";
    public static final String PICK_SOURCE_SUCCESS = "pick_source_success";
    public static final String PICK_ALL_SUBCATEGORIES = "pick_all_subcategories";

    public static final String REMOVE_REGION_SUCCESS = "rm_region_success";
    public static final String REMOVE_CATEGORY_SUCCESS = "rm_category_success";
    public static final String REMOVE_LANGUAGE_SUCCESS = "rm_language_success";
    public static final String REMOVE_SOURCE_SUCCESS = "rm_source_success";
    public static final String REMOVE_ALL_SUBCATEGORIES = "rm_all_subcategories";

    public static final String WELCOME_LIST_REGION = "welcome_list_regions";
    public static final String WELCOME_LIST_CATEGORY = "welcome_list_categories";
    public static final String WELCOME_LIST_LANGUAGES = "welcome_list_language";

    public static final String LIST_CATEGORY = "list_categories";
    public static final String LIST_REGIONS = "list_regions";
    public static final String LIST_SUBCATEGORIES = "list_subcategories";
    public static final String LIST_SUBREGIONS = "list_subregions";
    public static final String LIST_LANGUAGES = "list_languages";
    public static final String LIST_SUBCATEGORY = "list_subcategory";
    public static final String LIST_SOURCES = "list_sources";

    public static final String NEWS_NOTE = "news_note";

    public static final String ENABLE = "enable";
    public static final String ENABLE_READER = "enable_reader";
    public static final String DISABLE_READER = "disable_reader";


    public static final Map<EntityType, String> RM_ENTITY_CONTAINER = Map.of(
        EntityType.REGION,
        REMOVE_REGION_SUCCESS,
        EntityType.CATEGORY,
        REMOVE_CATEGORY_SUCCESS,
        EntityType.LANGUAGE,
        REMOVE_LANGUAGE_SUCCESS,
        EntityType.SOURCE,
        REMOVE_SOURCE_SUCCESS
    );
    public static final Map<EntityType, String> PICK_ENTITY_CONTAINER = Map.of(
        EntityType.REGION,
        PICK_REGION_SUCCESS,
        EntityType.CATEGORY,
        PICK_CATEGORY_SUCCESS,
        EntityType.LANGUAGE,
        PICK_LANGUAGE_SUCCESS,
        EntityType.SOURCE,
        PICK_SOURCE_SUCCESS
    );
    private static final Map<CommandType, Map<EntityType, String>> UPDATE_MSG_CONTAINER =
        ImmutableMap.<CommandType, Map<EntityType, String>>builder()
            .put(CommandType.PICK, PICK_ENTITY_CONTAINER)
            .put(CommandType.PICK_ALL, PICK_ENTITY_CONTAINER)
            .put(CommandType.REMOVE, RM_ENTITY_CONTAINER)
            .put(CommandType.REMOVE_ALL, RM_ENTITY_CONTAINER)
            .build();

    public static String getTemplate(CommandType c, EntityType e) {
        return UPDATE_MSG_CONTAINER.get(c).get(e);
    }


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
