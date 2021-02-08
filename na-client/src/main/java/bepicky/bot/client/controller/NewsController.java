package bepicky.bot.client.controller;

import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.bot.client.router.PickyNewsBot;
import bepicky.bot.client.service.IReaderService;
import bepicky.bot.client.service.IValueNormalisationService;
import bepicky.common.domain.dto.NewsNoteDto;
import bepicky.common.domain.request.NotifyNewsRequest;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class NewsController {

    @Autowired
    private PickyNewsBot bot;

    @Autowired
    private MessageTemplateContext templateContext;

    @Autowired
    private IReaderService readerService;

    @Autowired
    private IValueNormalisationService normalisationService;

    @PutMapping("/notify/news")
    public void notifyNews(@RequestBody NotifyNewsRequest request) {
        for (NewsNoteDto note : request.getNotes()) {
            String regions = note.getRegions();
            String categories = note.getCommons();
            Map<String, Object> params = ImmutableMap.<String, Object>builder()
                .put("title", note.getTitle())
                .put("url", note.getUrl())
                .put("region", normalisationService.normalise(regions))
                .put("category", normalisationService.normalise(categories))
                .put("author", normalisationService.normalise(note.getAuthor()))
                .build();
            String noteMsg = templateContext.processTemplate(
                TemplateUtils.NEWS_NOTE,
                request.getLang(),
                params
            ).trim();
            try {
                bot.execute(new SendMessage()
                    .enableMarkdownV2(true)
                    .setChatId(request.getChatId())
                    .disableNotification()
                    .enableHtml(true)
                    .setText(noteMsg));
            } catch (TelegramApiException e) {
                if (e instanceof TelegramApiRequestException) {
                    TelegramApiRequestException requestException = (TelegramApiRequestException) e;
                    if (requestException.getErrorCode() == 403) {
                        log.info("reader:disabled:{}", request.getChatId());
                        readerService.disable(request.getChatId());
                    }
                } else {
                    log.warn("reader:notify:failed:{}", e.getMessage());
                }
            }
        }
    }
}
