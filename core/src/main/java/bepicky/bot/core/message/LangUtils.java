package bepicky.bot.core.message;

import com.google.common.collect.ImmutableList;

public class LangUtils {

    private LangUtils() {}

    public static final String DEFAULT = "en";

    public static final String ALL = "all";

    public static final ImmutableList<String> SUPPORTED = ImmutableList.of(DEFAULT, "ru", "ukr");

    public static String getLang(String userLang) {
        return SUPPORTED.contains(userLang) ? userLang : DEFAULT;
    }
}
