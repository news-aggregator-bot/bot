package bepicky.bot.client.message.command;

import bepicky.bot.client.message.EntityType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static bepicky.bot.client.message.command.CommandType.GO_NEXT;
import static bepicky.bot.client.message.command.CommandType.GO_PREVIOUS;
import static bepicky.bot.client.message.command.CommandType.LIST;
import static bepicky.bot.client.message.command.CommandType.PICK;
import static bepicky.bot.client.message.command.CommandType.PICK_ALL;
import static bepicky.bot.client.message.command.CommandType.REMOVE;
import static bepicky.bot.client.message.command.CommandType.REMOVE_ALL;

@Component
public class CommandManager {

    @Autowired
    private ObjectMapper om;


    public String pick(EntityType entity, String name) {
        return toCmd(ChatCommand.of(PICK, entity, name));
    }

    public String pick(EntityType entity, long id) {
        return toCmd(ChatCommand.of(PICK, entity, id));
    }

    public String pickAll(EntityType entity, long id) {
        return toCmd(ChatCommand.of(PICK_ALL, entity, id));
    }

    public String remove(EntityType entity, String name) {
        return toCmd(ChatCommand.of(REMOVE, entity, name));
    }

    public String remove(EntityType entity, long id) {
        return toCmd(ChatCommand.of(REMOVE, entity, id));
    }

    public String removeAll(EntityType entity, long id) {
        return toCmd(ChatCommand.of(REMOVE_ALL, entity, id));
    }

    public String list(EntityType entity) {
        return list(entity, 1);
    }

    public String list(EntityType entity, int page) {
        return toCmd(ChatCommand.of(LIST, entity, page));
    }

    public String sublist(EntityType entity, long parent) {
        return sublist(entity, parent, 1);
    }

    public String sublist(EntityType entity, long parent, int page) {
        return toCmd(ChatCommand.of(CommandType.SUBLIST, entity, page, parent));
    }

    public String generic(CommandType c, EntityType e, int page, Object id) {
        return toCmd(ChatCommand.of(c, e, page, id));
    }

    public String util(CommandType c) {
        return toCmd(ChatCommand.of(c));
    }

    public String update(EntityType e) {
        return toCmd(ChatCommand.of(CommandType.UPDATE, e, 1));
    }

    public String status() {
        return toCmd(ChatCommand.of(CommandType.STATUS_READER));
    }

    public String goNext() {
        return toCmd(ChatCommand.of(GO_NEXT));
    }

    public String goPrevious() {
        return toCmd(ChatCommand.of(GO_PREVIOUS));
    }

    private String toCmd(ChatCommand cc) {
        return cc.toString();
    }

}
