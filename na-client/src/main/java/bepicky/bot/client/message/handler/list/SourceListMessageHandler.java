package bepicky.bot.client.message.handler.list;

import bepicky.bot.client.message.EntityUtils;
import bepicky.bot.client.message.LangUtils;
import bepicky.bot.client.message.MessageUtils;
import bepicky.bot.client.message.button.MarkupBuilder;
import bepicky.bot.client.message.handler.context.ChatFlowContext;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.bot.client.service.ISourceService;
import bepicky.common.domain.dto.ReaderDto;
import bepicky.common.domain.dto.SourceDto;
import bepicky.common.domain.response.SourceListResponse;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.stream.Collectors;

import static com.vdurmont.emoji.EmojiParser.parseToUnicode;

@Component
public class SourceListMessageHandler extends AbstractListMessageHandler {

    @Autowired
    private ISourceService sourceService;

    @Autowired
    private ChatFlowContext flowContext;

    @Override
    public HandleResult handle(Message message, String data) {
        String[] split = MessageUtils.parse(data);
        int page = Integer.parseInt(split[2]);
        SourceListResponse response = sourceService.list(message.getChatId(), page, PAGE_SIZE);
        if (response.isError()) {
            return error(response.getError().getEntity());
        }
        List<SourceDto> sources = response.getList();

        MarkupBuilder markup = new MarkupBuilder();
        List<MarkupBuilder.Button> buttons = sources.stream()
            .map(l -> buildButton(response.getReader(), l))
            .collect(Collectors.toList());

        List<MarkupBuilder.Button> navigation = navigation(page, trigger(), response, markup);
        List<List<MarkupBuilder.Button>> partition = Lists.partition(buttons, 3);
        partition.forEach(markup::addButtons);
        markup.addButtons(navigation);

        String listSourcesText = parseToUnicode(templateContext.processTemplate(
            TemplateUtils.LIST_SOURCES,
            response.getReader().getLang(),
            TemplateUtils.page(page)
        ));
        return new HandleResult(listSourcesText, markup.build());
    }

    private MarkupBuilder.Button buildButton(ReaderDto r, SourceDto s) {
        String textKey = s.isPicked() ? TemplateUtils.REMOVE : TemplateUtils.PICK;
        String command = s.isPicked() ?
            commandBuilder.remove(trigger(), s.getId()) :
            commandBuilder.pick(trigger(), s.getId());
        return MarkupBuilder.Button.builder()
            .text(buildText(s, textKey))
            .command(command)
            .build();
    }

    @Override
    public String trigger() {
        return EntityUtils.SOURCE;
    }

    private String buildText(SourceDto s, String textKey) {
        return parseToUnicode(templateContext.processTemplate(
            textKey,
            LangUtils.DEFAULT,
            TemplateUtils.name(s.getName())
        ));
    }
}
