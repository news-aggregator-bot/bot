package bepicky.bot.client.message.handler.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static bepicky.bot.client.message.template.TemplateUtils.WELCOME_LIST_CATEGORY;
import static bepicky.bot.client.message.template.TemplateUtils.WELCOME_LIST_LANGUAGES;
import static bepicky.bot.client.message.template.TemplateUtils.WELCOME_LIST_REGION;

@Component
public class ChatChainManager {

    private static final int CHAIN_SIZE = 100;

    private final Map<Long, ChatChain> chains = new ConcurrentHashMap<>(CHAIN_SIZE);

    @Autowired
    private ChainLinkFactory chainFactory;

    public ChatChain welcomeChain(Long chatId) {
        ChatChainLink listCategory = chainFactory.listCategories(WELCOME_LIST_CATEGORY);
        ChatChainLink listRegion = chainFactory.listRegions(WELCOME_LIST_REGION);
        ChatChainLink listLanguage = chainFactory.listLanguages(WELCOME_LIST_LANGUAGES);
        ChatChain chatChain = new ChatChain(listLanguage, listRegion, listCategory, chainFactory.getActivateReader());
        chains.put(chatId, chatChain);
        return chatChain;
    }

    public ChatChain getChain(Long chatId) {
        return chains.get(chatId);
    }

    public ChatChainLink languageUpdateChain(Long chatId) {
        ChatChain chain = chains.get(chatId);
        if (chain != null) {
            return chain.current();
        }
        ChatChainLink listLanguage = chainFactory.listLanguages(WELCOME_LIST_LANGUAGES);
        ChatChain chatChain = new ChatChain(listLanguage);
        chains.put(chatId, chatChain);
        return chatChain.current();
    }

    public ChatChain updateChain(Long chatId, ChatChainLink updateLink) {
        ChatChainLink settings = chainFactory.settings();
        ChatChain cc = new ChatChain(settings, updateLink, chainFactory.getActivateReader());
        chains.put(chatId, cc);
        return cc;
    }

    public void updatePage(Long chatId, int page) {
        ChatChain currentChain = getChain(chatId);
        currentChain.current().setPage(page);
    }

    public ChatChainLink current(Long chatId) {
        return Optional.ofNullable(chains.get(chatId)).map(ChatChain::current).orElse(chainFactory.getActivateReader());
    }

    public ChatChainLink next(Long chatId) {
        return Optional.ofNullable(chains.get(chatId)).map(ChatChain::next).orElse(chainFactory.getActivateReader());
    }

    public void clean(Long chatId) {
        chains.remove(chatId);
    }

}
