package bepicky.bot.client.message.command;

import bepicky.bot.client.message.EntityType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatCommand {

    public static final String DELIMITER = ":";

    private CommandType commandType;
    private EntityType entityType;
    private Integer page;
    private Object id;
    private long chatId;

    public static ChatCommand of(CommandType type) {
        ChatCommand c = new ChatCommand();
        c.setCommandType(type);
        return c;
    }

    public static ChatCommand of(CommandType ct, EntityType e, Integer page) {
        ChatCommand c = of(ct);
        c.setEntityType(e);
        c.setPage(page);
        return c;
    }

    public static ChatCommand of(CommandType ct, EntityType e, Object id) {
        ChatCommand c = of(ct);
        c.setEntityType(e);
        c.setId(id);
        return c;
    }

    public static ChatCommand of(CommandType ct, EntityType e, Integer page, Object id) {
        ChatCommand c = of(ct, e, page);
        c.setId(id);
        return c;
    }

    public Object getId() {
        if (id instanceof String) {
            try {
                return Long.parseLong((String) id);
            } catch (NumberFormatException e) {
                return id;
            }
        }
        return id;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(commandType.getKey());
        if (entityType != null) {
            b.append(DELIMITER).append(entityType.getKey());
            b.append(DELIMITER).append(page == null ? 0 : page);
            b.append(DELIMITER).append(id == null ? 0 : id);
        }
        return b.toString();
    }

    public static ChatCommand fromText(String cc) {
        String[] parts = cc.split(DELIMITER);
        CommandType ct = CommandType.of(parts[0]);
        if (parts.length > 1) {
            EntityType et = EntityType.of(parts[1]);
            int page = Integer.parseInt(parts[2]);
            Object id = parts[3];
            return of(ct, et, page, id);
        }
        return of(ct);
    }
}
