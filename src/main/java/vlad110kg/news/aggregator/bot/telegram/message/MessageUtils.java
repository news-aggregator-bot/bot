package vlad110kg.news.aggregator.bot.telegram.message;

public class MessageUtils {
    private MessageUtils(){}

    public static String[] parse(String message) {
        return message.split(":");
    }
}
