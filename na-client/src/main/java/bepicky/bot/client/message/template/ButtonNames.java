package bepicky.bot.client.message.template;

public class ButtonNames {

    private ButtonNames() {}

    private static final String BUTTON_DIR = "button/";
    private static final String READER_DIR = BUTTON_DIR + "reader/";
    private static final String SETTINGS_DIR = BUTTON_DIR + "settings/";
    private static final String DIRECTION_DIR = BUTTON_DIR + "direction/";

    public static final String REMOVE = BUTTON_DIR + "rm";
    public static final String PICK = BUTTON_DIR + "pick";
    public static final String SETTINGS = BUTTON_DIR + "settings";
    public static final String CLOSE = BUTTON_DIR + "close";

    public static final String SETTINGS_ADD = SETTINGS_DIR + "add";
    public static final String SETTINGS_REMOVE = SETTINGS_DIR + "rm";
    public static final String SETTINGS_REGION = SETTINGS_DIR + "region";
    public static final String SETTINGS_CATEGORY = SETTINGS_DIR + "category";
    public static final String SETTINGS_LANGUAGE = SETTINGS_DIR + "language";
    public static final String SETTINGS_SOURCE = SETTINGS_DIR + "src";

    public static final String DIR_DONE = DIRECTION_DIR + "done";
    public static final String DIR_BACK = DIRECTION_DIR + "back";
    public static final String DIR_CONTINUE = DIRECTION_DIR + "continue";
    public static final String DIR_NEXT = DIRECTION_DIR + "next";
    public static final String DIR_PREV = DIRECTION_DIR + "prev";

    public static final String ENABLE_READER = READER_DIR + "enable";
    public static final String DISABLE_READER = READER_DIR + "disable";
}
