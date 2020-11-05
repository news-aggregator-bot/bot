package bepicky.bot.client.message.handler.context;

import bepicky.bot.client.message.EntityType;
import bepicky.bot.client.message.button.CommandType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static bepicky.bot.client.message.template.TemplateUtils.WELCOME_LIST_CATEGORY;
import static bepicky.bot.client.message.template.TemplateUtils.WELCOME_LIST_LANGUAGES;
import static bepicky.bot.client.message.template.TemplateUtils.WELCOME_LIST_REGION;

@Component
public class ChatFlowManager {

    private final Map<Long, ChatFlow> context = new ConcurrentHashMap<>();

    @Autowired
    private ChatFlowFactory flowFactory;

    public ChatFlow welcomeFlow(Long chatId) {
        ChatFlow listCategory = flowFactory.listCategories(WELCOME_LIST_CATEGORY);
        ChatFlow listRegion = flowFactory.listRegions(WELCOME_LIST_REGION, listCategory);
        ChatFlow listLanguage = flowFactory.listLanguages(WELCOME_LIST_LANGUAGES, listRegion);
        context.put(chatId, listLanguage);
        return listLanguage;
    }

    public ChatFlow languageUpdateFlow(Long chatId) {
        ChatFlow current = context.get(chatId);
        if (current != null) {
            return current;
        }
        ChatFlow listLanguage = flowFactory.listLanguages(WELCOME_LIST_LANGUAGES);
        context.put(chatId, listLanguage);
        return listLanguage;
    }

    public void updateFlow(Long chatId, String m, EntityType e, CommandType c) {
        ChatFlow current = current(chatId);
        context.put(chatId, flowFactory.flow(m, e, c, current.getNext()));
    }


    public ChatFlow goNext(Long chatId) {
        ChatFlow next = current(chatId).getNext();
        if (next == null) {
            context.remove(chatId);
            return flowFactory.getActivateReader();
        }
        context.replace(chatId, next);
        return next;
    }

    public ChatFlow current(Long chatId) {
        return context.getOrDefault(chatId, flowFactory.getActivateReader());
    }

    public void clean(Long chatId) {
        context.remove(chatId);
    }

}
