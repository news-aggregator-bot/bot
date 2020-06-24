package vlad110kg.news.aggregator.bot.telegram.message.button;

import org.springframework.stereotype.Component;

@Component
public class CommandBuilder {
    public static final String LIST = "list";
    public static final String PICK = "pick";

    private static final String PICK_PATTERN = PICK + ":%s:%s";
    private static final String LIST_PATTERN = LIST + ":%s:%d";
    private static final String LIST_ENTITY_PATTERN = LIST + ":%s:%s:%d";


    public String pick(String entity, String name) {
        return String.format(PICK_PATTERN, entity, name);
    }

    public String pick(String entity, long id) {
        return String.format(PICK_PATTERN, entity, id);
    }

    public String list(String entity) {
        return String.format(LIST_PATTERN, entity, 1);
    }

    public String list(String entity, int page) {
        return String.format(LIST_PATTERN, entity, page);
    }

    public String list(String entity, String value, int page) {
        return String.format(LIST_ENTITY_PATTERN, entity, value, page);
    }

    public String list(String entity, long id, int page) {
        return String.format(LIST_ENTITY_PATTERN, entity, id, page);
    }
}
