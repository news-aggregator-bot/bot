package bepicky.bot.client.message.handler.context;

import bepicky.bot.client.message.button.CommandBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static bepicky.bot.client.message.EntityUtils.CATEGORY;
import static bepicky.bot.client.message.EntityUtils.LANGUAGE;
import static bepicky.bot.client.message.handler.common.HelpMessageHandler.HELP;
import static bepicky.bot.client.message.template.TemplateUtils.DIR_NEXT;
import static bepicky.bot.client.message.template.TemplateUtils.DIR_OPTS;
import static bepicky.bot.client.message.template.TemplateUtils.DONE;
import static bepicky.bot.client.message.template.TemplateUtils.WELCOME_LIST_CATEGORY;
import static bepicky.bot.client.message.template.TemplateUtils.WELCOME_LIST_LANGUAGES;

@Component
public class ChatFlowContext {

    private final ChatFlow doneFlow = ChatFlow.builder().buttonKey(DIR_OPTS).msgKey(DONE).command(HELP).build();

    private final Map<Long, ChatFlow> readerFlowContext = new ConcurrentHashMap<>();

    @Autowired
    protected CommandBuilder commandBuilder;

    public ChatFlow goNext(Long chatId) {
        ChatFlow next = current(chatId).getNext();
        if (next == null) {
            readerFlowContext.remove(chatId);
            return doneFlow;
        }
        readerFlowContext.replace(chatId, next);
        return next;
    }

    public ChatFlow current(Long chatId) {
        return readerFlowContext.getOrDefault(chatId, doneFlow);
    }

    public ChatFlow welcome(Long chatId) {
        ChatFlow listCategory = create(DIR_NEXT, WELCOME_LIST_CATEGORY, commandBuilder.list(CATEGORY), doneFlow);
        ChatFlow listLanguage = create(DIR_NEXT, WELCOME_LIST_LANGUAGES, commandBuilder.list(LANGUAGE), listCategory);
        readerFlowContext.put(chatId, listLanguage);
        return listLanguage;
    }

    private ChatFlow create(String buttonKey, String msgKey, String command, ChatFlow next) {
        return ChatFlow.builder()
            .buttonKey(buttonKey)
            .msgKey(msgKey)
            .command(command)
            .next(next)
            .build();
    }

}
