package bepicky.bot.client.message.handler;

import bepicky.bot.client.message.LangUtils;
import bepicky.bot.client.message.MessageUtils;
import bepicky.bot.client.message.button.CommandBuilder;
import bepicky.bot.client.message.button.MarkupBuilder;
import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.bot.client.service.ISourceService;
import bepicky.common.domain.dto.SourceDto;
import bepicky.common.domain.response.SourceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

import static bepicky.bot.client.message.EntityType.SOURCE;
import static bepicky.bot.client.message.template.TemplateUtils.DIR_CONTINUE;

public abstract class AbstractSourceMessageHandler implements CallbackMessageHandler {

    @Autowired
    protected CommandBuilder commandBuilder;

    @Autowired
    protected MessageTemplateContext templateContext;

    @Autowired
    protected ISourceService sourceService;

    @Override
    public HandleResult handle(Message message, String data) {
        String[] split = MessageUtils.parse(data);
        long srcId = Long.parseLong(split[2]);
        SourceResponse response = handle(message.getChatId(), srcId);
        if (response.isError()) {
            String errorText = templateContext.errorTemplate(LangUtils.DEFAULT);
            // handling
            return new HandleResult(errorText, null);
        }

        SourceDto src = response.getSource();
        MarkupBuilder markup = new MarkupBuilder();
        String readerLang = response.getReader().getLang();

        List<MarkupBuilder.Button> navigation = new ArrayList<>();
        navigation.add(buildContinueButton(readerLang, markup));
        markup.addButtons(navigation);

        String text = templateContext.processTemplate(
            textKey(),
            readerLang,
            TemplateUtils.name(src.getName())
        );
        return new HandleResult(text, markup.build());
    }

    private MarkupBuilder.Button buildContinueButton(String lang, MarkupBuilder markup) {
        String buttonText = templateContext.processTemplate(DIR_CONTINUE, lang);
        return markup.button(buttonText, commandBuilder.list(trigger(), 1));
    }

    @Override
    public String trigger() {
        return SOURCE.lower();
    }

    protected abstract String textKey();

    protected abstract SourceResponse handle(Long chatId, Long sourceId);
}
