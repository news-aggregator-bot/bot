package bepicky.bot.client.message.handler.list;

import bepicky.bot.client.message.EntityType;
import bepicky.bot.client.message.LangUtils;
import bepicky.bot.client.message.button.InlineMarkupBuilder;
import bepicky.bot.client.message.command.ChatCommand;
import bepicky.bot.client.message.command.CommandType;
import bepicky.bot.client.message.handler.context.ChatChainManager;
import bepicky.bot.client.message.template.ButtonNames;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.bot.client.service.ILanguageService;
import bepicky.common.domain.dto.LanguageDto;
import bepicky.common.domain.dto.ReaderDto;
import bepicky.common.domain.response.LanguageListResponse;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.vdurmont.emoji.EmojiParser.parseToUnicode;

@Component
public class LanguageListMessageHandler extends AbstractListMessageHandler {

    @Autowired
    private ILanguageService languageService;

    @Autowired
    private ChatChainManager chainManager;

    @Override
    public HandleResult handle(ChatCommand cc) {
        int page = cc.getPage();
        LanguageListResponse response = languageService.list(cc.getChatId(), page, SIX_PAGE_SIZE);
        if (response.isError()) {
            return error(response.getError().getEntity());
        }
        List<LanguageDto> languages = response.getList();
        chainManager.languageUpdateChain(response.getReader().getChatId());

        InlineMarkupBuilder markup = new InlineMarkupBuilder();
        List<InlineMarkupBuilder.InlineButton> buttons = languages.stream()
            .map(l -> buildButton(response.getReader(), l, page))
            .collect(Collectors.toList());

        List<InlineMarkupBuilder.InlineButton> pagination = pagination(page, response, markup);
        List<InlineMarkupBuilder.InlineButton> navigation = navigation(markup, response.getReader());
        List<List<InlineMarkupBuilder.InlineButton>> partition = Lists.partition(buttons, 3);
        partition.forEach(markup::addButtons);
        markup.addButtons(pagination);
        markup.addButtons(navigation);

        String listSubcategoryText = parseToUnicode(templateContext.processTemplate(
            TemplateUtils.LIST_LANGUAGES,
            response.getReader().getLang(),
            TemplateUtils.page(page)
        ));
        return new HandleResult(listSubcategoryText, markup.build());
    }

    private InlineMarkupBuilder.InlineButton buildButton(ReaderDto r, LanguageDto l, int page) {
        boolean langPicked = r.getLanguages().contains(l);
        String textKey = langPicked ? ButtonNames.REMOVE : ButtonNames.PICK;
        String command = langPicked ?
            cmdMngr.remove(entityType(), l.getLang()) :
            cmdMngr.pick(entityType(), l.getLang());
        String list = cmdMngr.list(entityType(), page);
        return new InlineMarkupBuilder.InlineButton(buildText(l, textKey), Arrays.asList(command, list));
    }

    private String buildText(LanguageDto l, String textKey) {
        return parseToUnicode(templateContext.processTemplate(
            textKey,
            LangUtils.DEFAULT,
            TemplateUtils.name(l.getLocalized())
        ));
    }

    @Override
    public CommandType commandType() {
        return CommandType.LIST;
    }

    @Override
    public EntityType entityType() {
        return EntityType.LANGUAGE;
    }
}
