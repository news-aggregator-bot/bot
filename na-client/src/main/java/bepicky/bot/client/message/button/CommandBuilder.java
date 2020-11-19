package bepicky.bot.client.message.button;

import bepicky.bot.client.message.EntityType;
import org.springframework.stereotype.Component;

import static bepicky.bot.client.message.button.CommandType.LIST;
import static bepicky.bot.client.message.button.CommandType.PICK;
import static bepicky.bot.client.message.button.CommandType.PICK_ALL;
import static bepicky.bot.client.message.button.CommandType.REMOVE;

@Component
public class CommandBuilder {

    private static final String UPDATE_PATTERN = "%s:%s";
    private static final String COMMON_PATTERN = "%s:%s:%d";
    private static final String SUBLIST_PATTERN = "%s:%s:%d:%d";
    private static final String PICK_PATTERN = PICK.name() + ":%s:%s";
    private static final String PICK_ALL_PATTERN = PICK_ALL.name() + ":%s:%s";
    private static final String REMOVE_PATTERN = REMOVE.name() + ":%s:%s";
    private static final String LIST_PATTERN = LIST.name() + ":%s:%d";
    private static final String LIST_ENTITY_PATTERN = LIST.name() + ":%s:%s:%d";


    public String pick(String entity, String name) {
        return String.format(PICK_PATTERN, entity, name);
    }

    public String pick(String entity, long id) {
        return String.format(PICK_PATTERN, entity, id);
    }

    public String pickAll(String entity, long id) {
        return String.format(PICK_ALL_PATTERN, entity, id);
    }

    public String remove(String entity, String name) {
        return String.format(REMOVE_PATTERN, entity, name);
    }

    public String remove(String entity, long id) {
        return String.format(REMOVE_PATTERN, entity, id);
    }

    public String update(EntityType entityType) {
        return String.format(UPDATE_PATTERN, CommandType.UPDATE, entityType.low());
    }

    public String list(String entity) {
        return list(LIST, entity);
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

    public String list(CommandType type, EntityType entity) {
        return list(type, entity.low());
    }

    public String list(CommandType type, String entity) {
        return list(type, entity, 1);
    }

    public String list(CommandType type, String entity, int page) {
        return String.format(COMMON_PATTERN, type.name(), entity, page);
    }

    public String sublist(CommandType command, String entity, long parent) {
        return String.format(SUBLIST_PATTERN, command.name(), entity, parent, 1);
    }

    public String sublist(CommandType command, String entity, long parent, int page) {
        return String.format(SUBLIST_PATTERN, command.name(), entity, parent, page);
    }

}
