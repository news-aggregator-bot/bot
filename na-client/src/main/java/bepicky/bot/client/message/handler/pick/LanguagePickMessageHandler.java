package bepicky.bot.client.message.handler.pick;

import bepicky.bot.client.message.LangUtils;
import bepicky.bot.client.message.MessageUtils;
import bepicky.bot.client.message.button.CommandBuilder;
import bepicky.bot.client.message.button.MarkupBuilder;
import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.bot.client.service.ILanguageService;
import bepicky.common.domain.response.LanguageResponse;
import bepicky.common.domain.response.PickLanguageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

import static bepicky.bot.client.message.template.TemplateUtils.DIR_CONTINUE;

@Component
public class LanguagePickMessageHandler implements PickMessageHandler {

    public static final String LANGUAGE = "language";

    @Autowired
    private CommandBuilder commandBuilder;

    @Autowired
    private MessageTemplateContext templateContext;

    @Autowired
    private ILanguageService languageService;

    @Override
    public BotApiMethod<Message> handle(Message message, String data) {
        String[] split = MessageUtils.parse(data);
        String lang = split[2];
        PickLanguageResponse response = languageService.pick(message.getChatId(), lang);
        if (response.isError()) {
            String errorText = templateContext.errorTemplate(LangUtils.DEFAULT);// TODO: 28.06.2020 add normal error
            // handling
            return new SendMessage().setChatId(message.getChatId()).setText(errorText);
        }

        LanguageResponse language = response.getLanguage();
        MarkupBuilder markup = new MarkupBuilder();

        List<MarkupBuilder.Button> navigation = new ArrayList<>();
        navigation.add(buildContinueButton(response.getLang(), markup));
        markup.addButtons(navigation);

        String langPickText = templateContext.processTemplate(
            TemplateUtils.PICK_LANGUAGE_SUCCESS,
            response.getLang(),
            TemplateUtils.name(language.getLocalized())
        );
        return new SendMessage()
            .setChatId(message.getChatId())
            .setText(langPickText)
            .setReplyMarkup(markup.build());
    }

    private MarkupBuilder.Button buildContinueButton(String lang, MarkupBuilder markup) {
        String buttonText = templateContext.processTemplate(DIR_CONTINUE, lang);
        return markup.button(buttonText, commandBuilder.list(LANGUAGE, 1));
    }

    @Override
    public String trigger() {
        return LANGUAGE;
    }
}
