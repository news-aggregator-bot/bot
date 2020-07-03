package vlad110kg.news.aggregator.bot.telegram.message.handler.list;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import vlad110kg.news.aggregator.bot.telegram.domain.Language;
import vlad110kg.news.aggregator.bot.telegram.domain.response.ListLanguageResponse;
import vlad110kg.news.aggregator.bot.telegram.message.LangUtils;
import vlad110kg.news.aggregator.bot.telegram.message.MessageUtils;
import vlad110kg.news.aggregator.bot.telegram.message.button.MarkupBuilder;
import vlad110kg.news.aggregator.bot.telegram.message.template.TemplateUtils;
import vlad110kg.news.aggregator.bot.telegram.service.ILanguageService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.vdurmont.emoji.EmojiParser.parseToUnicode;

@Component
public class LanguageListMessageHandler extends AbstractListMessageHandler {

    public static final String LANGUAGE = "language";

    @Autowired
    private ILanguageService languageService;

    @Override
    public BotApiMethod<Message> handle(Message message, String data) {
        String[] split = MessageUtils.parse(data);
        int page = Integer.parseInt(split[2]);
        ListLanguageResponse response = languageService.list(message.getChatId(), page, PAGE_SIZE);
        if (response.isError()) {
            return error(message.getChatId(), LangUtils.DEFAULT, response.getError().getEntity());
        }
        List<Language> languages = response.getLanguages();

        MarkupBuilder markup = new MarkupBuilder();
        List<MarkupBuilder.Button> buttons = languages.stream()
            .map(l -> MarkupBuilder.Button.builder()
                .text(buildText(l))
                .command(commandBuilder.pick(LANGUAGE, l.getLang()))
                .build())
            .collect(Collectors.toList());

        List<MarkupBuilder.Button> navigation = new ArrayList<>();
        if (needsNavigation(response.getTotalAmount())) {
            if (page > 1) {
                String prevText = prevButtonText(LangUtils.DEFAULT);
                navigation.add(markup.button(prevText, commandBuilder.list(LANGUAGE, page - 1)));
            }
        }
        navigation.add(markup.button(backButtonText(response.getLanguage()), "command"));

        if (needsNavigation(response.getTotalAmount())) {
            if (languages.size() == PAGE_SIZE) {
                String nextText = nextButtonText(LangUtils.DEFAULT);
                navigation.add(markup.button(nextText, commandBuilder.list(LANGUAGE, page + 1)));
            }
        }
        List<List<MarkupBuilder.Button>> partition = Lists.partition(buttons, 3);
        partition.forEach(markup::addButtons);
        markup.addButtons(navigation);

        String listSubcategoryText = parseToUnicode(templateContext.processTemplate(
            TemplateUtils.LIST_LANGUAGES,
            response.getLanguage(),
            TemplateUtils.page(page)
        ));
        return new SendMessage()
            .setChatId(message.getChatId())
            .setText(listSubcategoryText)
            .setReplyMarkup(markup.build());
    }

    @Override
    public String trigger() {
        return LANGUAGE;
    }

    private String buildText(Language l) {
        return parseToUnicode(templateContext.processTemplate(
            TemplateUtils.PICK,
            LangUtils.DEFAULT,
            TemplateUtils.name(l.getLocalized())
        ));
    }
}
