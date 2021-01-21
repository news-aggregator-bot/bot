package bepicky.bot.client.message.handler.list;

import bepicky.bot.client.message.EntityType;
import bepicky.bot.client.message.button.InlineMarkupBuilder;
import bepicky.bot.client.message.command.ChatCommand;
import bepicky.bot.client.message.command.CommandType;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.bot.client.service.INewsService;
import bepicky.common.domain.dto.NewsNoteDto;
import bepicky.common.domain.response.NewsSearchResponse;
import com.google.common.collect.ImmutableMap;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NewsSearchMessageHandler extends AbstractListMessageHandler {

    @Value("${search.news.pageSize}")
    private int searchPageSize;

    @Autowired
    private INewsService newsService;

    @Override
    public HandleResult handle(ChatCommand command) {
        String key = (String) command.getId();
        NewsSearchResponse searchResponse = newsService.search(
            command.getChatId(),
            key,
            command.getPage(),
            searchPageSize
        );

        List<TemplateNewsNote> templateDtos = searchResponse.getList().stream()
            .map(TemplateNewsNote::new)
            .collect(Collectors.toList());
        String text = templateContext.processTemplate(
            TemplateUtils.SEARCH_NOTE, searchResponse.getReader().getLang(),
            ImmutableMap.<String, Object>builder()
                .put("key", key)
                .put("page", command.getPage())
                .put("total_pages", searchResponse.getTotalPages())
                .put("total", searchResponse.getTotalElements())
                .put("notes", templateDtos)
                .build()
        );

        InlineMarkupBuilder markup = new InlineMarkupBuilder();
        List<InlineMarkupBuilder.InlineButton> pagination = pagination(
            command.getPage(),
            searchResponse,
            markup,
            key
        );
        markup.addButtons(pagination);
        return new HandleResult(text, markup.build(), false);
    }

    @Override
    public EntityType entityType() {
        return EntityType.NEWS_NOTE;
    }

    @Override
    public CommandType commandType() {
        return CommandType.SEARCH;
    }

    @Data
    public static class TemplateNewsNote {

        private final String title;
        private final String url;
        private final String source;
        private final Date date;
        private final String region;
        private final String common;

        public TemplateNewsNote(NewsNoteDto dto) {
            this.title = dto.getTitle();
            this.url = dto.getUrl();
            this.source = dto.getSourcePages().get(0).getSourceName();
            this.date = dto.getDate();
            this.region = dto.getRegions();
            this.common = dto.getCommons();
        }
    }
}
