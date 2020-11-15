package bepicky.bot.client.controller;

import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.bot.client.router.PickyNewsBot;
import bepicky.common.domain.dto.CategoryDto;
import bepicky.common.domain.request.NewsNoteRequest;
import bepicky.common.domain.request.NotifyNewsRequest;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class NewsController {

    @Autowired
    private PickyNewsBot bot;

    @Autowired
    private MessageTemplateContext templateContext;

    @PutMapping("/notify/news")
    public void notifyNews(@RequestBody NotifyNewsRequest request) {
        List<String> newsNotes = new ArrayList<>(request.getNotes().size());
        for (NewsNoteRequest note : request.getNotes()) {
            String regions = note.getSourcePage().getRegions().stream()
                .map(CategoryDto::getLocalised).collect(Collectors.joining(", "));
            String categories = note.getSourcePage().getCommons().stream()
                .map(CategoryDto::getLocalised).collect(Collectors.joining(", "));
            Map<String, Object> params = ImmutableMap.<String, Object>builder()
                .put("title", note.getTitle())
                .put("url", note.getUrl())
                .put("region", normaliseValue(regions))
                .put("category", normaliseValue(categories))
                .put("author", normaliseValue(note.getAuthor()))
                .build();
            newsNotes.add(templateContext.processTemplate(TemplateUtils.NEWS_NOTE, request.getLang(), params).trim());
        }
        try {
            bot.execute(new SendMessage()
                .enableMarkdownV2(true)
                .setChatId(request.getChatId())
                .setText(String.join("\n", newsNotes)));
        } catch (TelegramApiException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private String normaliseValue(String value) {
        return StringUtils.isBlank(value) ? "" : value;
    }
}
