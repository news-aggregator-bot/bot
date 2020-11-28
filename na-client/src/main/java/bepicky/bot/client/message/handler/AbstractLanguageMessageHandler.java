package bepicky.bot.client.message.handler;

import bepicky.bot.client.message.EntityType;
import bepicky.bot.client.message.LangUtils;
import bepicky.bot.client.message.MessageUtils;
import bepicky.bot.client.message.button.CommandBuilder;
import bepicky.bot.client.message.button.InlineMarkupBuilder;
import bepicky.bot.client.message.template.ButtonNames;
import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.bot.client.service.ILanguageService;
import bepicky.common.domain.dto.LanguageDto;
import bepicky.common.domain.response.LanguageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLanguageMessageHandler implements CallbackMessageHandler {

    @Autowired
    protected CommandBuilder commandBuilder;

    @Autowired
    protected MessageTemplateContext templateContext;

    @Autowired
    protected ILanguageService languageService;

    @Override
    public HandleResult handle(Message message, String data) {
        String[] split = MessageUtils.parse(data);
        String lang = split[2];
        LanguageResponse response = handle(message.getChatId(), lang);
        if (response.isError()) {
            String errorText = templateContext.errorTemplate(LangUtils.DEFAULT);
            // handling
            return new HandleResult(errorText, null);
        }

        LanguageDto language = response.getLanguage();
        InlineMarkupBuilder markup = new InlineMarkupBuilder();
        String readerLang = response.getReader().getLang();

        List<InlineMarkupBuilder.InlineButton> navigation = new ArrayList<>();
        navigation.add(buildContinueButton(readerLang, markup));
        markup.addButtons(navigation);

        String langRemoveText = templateContext.processTemplate(
            textKey(),
            readerLang,
            TemplateUtils.name(language.getLocalized())
        );
        return new HandleResult(langRemoveText, markup.build());
    }

    private InlineMarkupBuilder.InlineButton buildContinueButton(String lang, InlineMarkupBuilder markup) {
        String buttonText = templateContext.processTemplate(ButtonNames.DIR_CONTINUE, lang);
        return markup.button(buttonText, commandBuilder.list(trigger(), 1));
    }

    @Override
    public String trigger() {
        return EntityType.LANGUAGE.low();
    }

    protected abstract String textKey();

    protected abstract LanguageResponse handle(Long chatId, String lang);
}
