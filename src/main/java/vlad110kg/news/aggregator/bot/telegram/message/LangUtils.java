package vlad110kg.news.aggregator.bot.telegram.message;

import com.google.common.collect.ImmutableList;

public class LangUtils {
    private LangUtils(){}

    public static final String DEFAULT = "en";

    private static final ImmutableList<String> SUPPORTED = ImmutableList.of(DEFAULT, "ru", "ua");

    public static String getLang(String userLang) {
        return SUPPORTED.contains(userLang) ? userLang : DEFAULT;
    }
}
