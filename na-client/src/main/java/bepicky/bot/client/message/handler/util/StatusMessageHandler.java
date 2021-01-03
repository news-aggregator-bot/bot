package bepicky.bot.client.message.handler.util;

import bepicky.bot.client.message.command.ChatCommand;
import bepicky.bot.client.message.command.CommandType;
import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.bot.client.service.IReaderService;
import bepicky.common.domain.dto.CategoryDto;
import bepicky.common.domain.dto.LanguageDto;
import bepicky.common.domain.dto.SourceDto;
import bepicky.common.domain.dto.StatusReaderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StatusMessageHandler implements UtilMessageHandler {

    @Autowired
    private IReaderService readerService;

    @Autowired
    private MessageTemplateContext templateContext;

    @Override
    public HandleResult handle(ChatCommand command) {
        StatusReaderDto status = readerService.getStatus(command.getChatId());
        String name = status.getFirstName() + " " + status.getLastName();
        String langs = status.getLanguages()
            .stream()
            .map(LanguageDto::getLocalized)
            .collect(Collectors.joining(", "));
        String region = status.getRegions().stream().map(CategoryDto::getLocalised).collect(Collectors.joining(", "));
        String category = status.getCommons().stream().map(CategoryDto::getLocalised).collect(Collectors.joining(", "));
        String srcs = status.getSources().stream().map(SourceDto::getName).collect(Collectors.joining(", "));
        Map<String, Object> params = Map.of(
            "name", name,
            "languages", langs,
            "region", region,
            "category", category,
            "sources", srcs
        );

        String msg = templateContext.processTemplate(
            TemplateUtils.STATUS_READER,
            status.getPrimaryLanguage().getLang(),
            params
        );
        return new HandleResult(msg, null);
    }

    @Override
    public CommandType commandType() {
        return CommandType.STATUS_READER;
    }
}
