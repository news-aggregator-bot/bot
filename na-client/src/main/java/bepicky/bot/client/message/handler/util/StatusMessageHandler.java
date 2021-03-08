package bepicky.bot.client.message.handler.util;

import bepicky.bot.core.cmd.ChatCommand;
import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.core.message.handler.UtilMessageHandler;
import bepicky.bot.core.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateNames;
import bepicky.bot.client.service.IReaderService;
import bepicky.bot.client.service.IValueNormalisationService;
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

    @Autowired
    private IValueNormalisationService normalisationService;

    @Override
    public HandleResult handle(ChatCommand command) {
        StatusReaderDto status = readerService.getStatus(command.getChatId());
        String name =
            normalisationService.normalise(status.getFirstName()) + " " +
                normalisationService.normalise(status.getLastName());
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
            TemplateNames.STATUS_READER,
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
