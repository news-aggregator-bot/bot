package bepicky.bot.client.message.handler.context;

import bepicky.bot.client.message.button.CommandBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static bepicky.bot.client.message.EntityUtils.CATEGORY;
import static bepicky.bot.client.message.EntityUtils.LANGUAGE;
import static bepicky.bot.client.message.template.TemplateUtils.DIR_NEXT;
import static bepicky.bot.client.message.template.TemplateUtils.ENABLE;
import static bepicky.bot.client.message.template.TemplateUtils.ENABLE_READER;
import static bepicky.bot.client.message.template.TemplateUtils.WELCOME_LIST_CATEGORY;
import static bepicky.bot.client.message.template.TemplateUtils.WELCOME_LIST_LANGUAGES;

@Component
public class ChatFlowContext {

    private final ChatFlow activateReader = flow(ENABLE, ENABLE_READER, CommandBuilder.ENABLE_READER, null);

    private final Map<Long, ChatFlow> readerFlowContext = new ConcurrentHashMap<>();

    @Autowired
    protected CommandBuilder commandBuilder;

    public ChatFlow goNext(Long chatId) {
        ChatFlow next = current(chatId).getNext();
        if (next == null) {
            readerFlowContext.remove(chatId);
            return activateReader;
        }
        readerFlowContext.replace(chatId, next);
        return next;
    }

    public ChatFlow current(Long chatId) {
        return readerFlowContext.getOrDefault(chatId, activateReader);
    }

    public void clean(Long chatId) {
        readerFlowContext.remove(chatId);
    }

    public ChatFlow welcome(Long chatId) {
        ChatFlow listCategory = flow(DIR_NEXT, WELCOME_LIST_CATEGORY, commandBuilder.list(CATEGORY), activateReader);
        ChatFlow listLanguage = flow(DIR_NEXT, WELCOME_LIST_LANGUAGES, commandBuilder.list(LANGUAGE), listCategory);
        readerFlowContext.put(chatId, listLanguage);
        return listLanguage;
    }

    public ChatFlow updateLanguage(Long chatId) {
        ChatFlow current = readerFlowContext.get(chatId);
        if (current != null) {
            return current;
        }
        ChatFlow listLanguage = flow(DIR_NEXT, WELCOME_LIST_LANGUAGES, commandBuilder.list(LANGUAGE), activateReader);
        readerFlowContext.put(chatId, listLanguage);
        return listLanguage;
    }

    public ChatFlow updateCategory(Long chatId) {
        ChatFlow current = readerFlowContext.get(chatId);
        if (current != null) {
            return current;
        }
        ChatFlow listCategory = flow(DIR_NEXT, WELCOME_LIST_CATEGORY, commandBuilder.list(CATEGORY), activateReader);
        readerFlowContext.put(chatId, listCategory);
        return listCategory;
    }

    private ChatFlow flow(String buttonKey, String msgKey, String command, ChatFlow next) {
        return ChatFlow.builder()
            .buttonKey(buttonKey)
            .msgKey(msgKey)
            .command(command)
            .next(next)
            .build();
    }

}
