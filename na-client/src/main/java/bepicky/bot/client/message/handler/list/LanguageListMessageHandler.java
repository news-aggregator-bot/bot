package bepicky.bot.client.message.handler.list;

import bepicky.bot.client.message.EntityType;
import bepicky.bot.client.message.LangUtils;
import bepicky.bot.client.message.MessageUtils;
import bepicky.bot.client.message.button.MarkupBuilder;
import bepicky.bot.client.message.handler.context.ChatFlowContext;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.bot.client.service.ILanguageService;
import bepicky.common.domain.dto.LanguageDto;
import bepicky.common.domain.dto.ReaderDto;
import bepicky.common.domain.response.LanguageListResponse;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.stream.Collectors;

import static com.vdurmont.emoji.EmojiParser.parseToUnicode;

@Component
public class LanguageListMessageHandler extends AbstractListMessageHandler {

    @Autowired
    private ILanguageService languageService;

    @Autowired
    private ChatFlowContext flowContext;

    @Override
    public HandleResult handle(Message message, String data) {
        String[] split = MessageUtils.parse(data);
        int page = Integer.parseInt(split[2]);
        LanguageListResponse response = languageService.list(message.getChatId(), page, PAGE_SIZE);
        if (response.isError()) {
            return error(response.getError().getEntity());
        }
        List<LanguageDto> languages = response.getList();
        flowContext.updateLanguage(response.getReader().getChatId());

        MarkupBuilder markup = new MarkupBuilder();
        List<MarkupBuilder.Button> buttons = languages.stream()
            .map(l -> buildButton(response.getReader(), l))
            .collect(Collectors.toList());

        List<MarkupBuilder.Button> navigation = navigation(page, trigger(), response, markup);
        List<List<MarkupBuilder.Button>> partition = Lists.partition(buttons, 3);
        partition.forEach(markup::addButtons);
        markup.addButtons(navigation);

        String listSubcategoryText = parseToUnicode(templateContext.processTemplate(
            TemplateUtils.LIST_LANGUAGES,
            response.getReader().getLang(),
            TemplateUtils.page(page)
        ));
        return new HandleResult(listSubcategoryText, markup.build());
    }

    private MarkupBuilder.Button buildButton(ReaderDto r, LanguageDto l) {
        boolean langPicked = r.getLanguages().contains(l);
        String textKey = langPicked ? TemplateUtils.REMOVE : TemplateUtils.PICK;
        String command = langPicked ?
            commandBuilder.remove(trigger(), l.getLang()) :
            commandBuilder.pick(trigger(), l.getLang());
        return MarkupBuilder.Button.builder()
            .text(buildText(l, textKey))
            .command(command)
            .build();
    }

    @Override
    public String trigger() {
        return EntityType.LANGUAGE.lower();
    }

    private String buildText(LanguageDto l, String textKey) {
        return parseToUnicode(templateContext.processTemplate(
            textKey,
            LangUtils.DEFAULT,
            TemplateUtils.name(l.getLocalized())
        ));
    }
}
