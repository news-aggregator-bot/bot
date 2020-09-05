package bepicky.bot.client.controller;

import bepicky.bot.client.message.button.CommandBuilder;
import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.bot.client.router.PickyNewsBot;
import bepicky.common.domain.request.NewsNoteRequest;
import bepicky.common.domain.request.NotifyReaderRequest;
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

@Slf4j
@RestController
public class NewsController {

    @Autowired
    private PickyNewsBot bot;

    @Autowired
    protected CommandBuilder commandBuilder;

    @Autowired
    protected MessageTemplateContext templateContext;

    @PutMapping("/notify-reader")
    public void notifyReader(@RequestBody NotifyReaderRequest request) {
        List<String> newsNotes = new ArrayList<>(request.getNotes().size());
        for (NewsNoteRequest note : request.getNotes()) {
            Map<String, Object> params = ImmutableMap.<String, Object>builder()
                .put("title", note.getTitle())
                .put("url", note.getUrl())
                .put("description", normaliseValue(note.getDescription()))
                .put("author", normaliseValue(note.getAuthor()))
                .build();
            newsNotes.add(templateContext.processTemplate(TemplateUtils.NEWS_NOTE, request.getLang(), params).trim());
        }
        try {
            bot.execute(new SendMessage()
                .setChatId(request.getChatId())
                .setText(String.join("\n", newsNotes)));
        } catch (TelegramApiException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private String normaliseValue(String value) {
        return StringUtils.isBlank(value) ? "" : value;
    }
}
