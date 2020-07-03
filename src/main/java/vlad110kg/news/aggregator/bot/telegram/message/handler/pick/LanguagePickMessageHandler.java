package vlad110kg.news.aggregator.bot.telegram.message.handler.pick;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import vlad110kg.news.aggregator.bot.telegram.domain.Language;
import vlad110kg.news.aggregator.bot.telegram.domain.response.PickLanguageResponse;
import vlad110kg.news.aggregator.bot.telegram.message.LangUtils;
import vlad110kg.news.aggregator.bot.telegram.message.MessageUtils;
import vlad110kg.news.aggregator.bot.telegram.message.button.CommandBuilder;
import vlad110kg.news.aggregator.bot.telegram.message.button.MarkupBuilder;
import vlad110kg.news.aggregator.bot.telegram.message.template.MessageTemplateContext;
import vlad110kg.news.aggregator.bot.telegram.message.template.TemplateUtils;
import vlad110kg.news.aggregator.bot.telegram.service.ILanguageService;

import java.util.ArrayList;
import java.util.List;

import static vlad110kg.news.aggregator.bot.telegram.message.template.TemplateUtils.DIR_CONTINUE;

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

        Language language = response.getLanguage();
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
