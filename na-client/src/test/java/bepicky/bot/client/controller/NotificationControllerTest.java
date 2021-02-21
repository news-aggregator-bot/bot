package bepicky.bot.client.controller;

import bepicky.bot.client.config.TemplateConfig;
import bepicky.bot.client.message.LangUtils;
import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.router.PickyNewsBot;
import bepicky.bot.client.service.IReaderService;
import bepicky.bot.client.service.IValueNormalisationService;
import bepicky.bot.client.service.ValueNormalisationService;
import bepicky.common.domain.dto.CategoryDto;
import bepicky.common.domain.dto.NewsNoteDto;
import bepicky.common.domain.dto.SourcePageDto;
import bepicky.common.domain.request.NotifyNewsRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class NotificationControllerTest {

    private static final String COMMON_TYPE = "COMMON";
    private static final String REGION_TYPE = "REGION";
    private static final long CHAT_ID = 1L;

    @Autowired
    private NotificationController newsController;

    @MockBean
    private PickyNewsBot bot;

    @Autowired
    private MessageTemplateContext templateContext;

    @MockBean
    private IReaderService readerService;

    @Autowired
    private IValueNormalisationService normalisationService;

    @Test
    public void notifyNews_NoteContainsRegionsCommonsAuthor_ShouldNotifyWithCorrectMessage()
        throws TelegramApiException {
        ArgumentCaptor<SendMessage> sendMsgAc = ArgumentCaptor.forClass(SendMessage.class);

        NewsNoteDto noteRequest = newNoteReq("title", "url", "author");
        CategoryDto regionUSA = region("USA");
        CategoryDto regionUSSR = region("USSR");
        CategoryDto politics = common("Politics");
        CategoryDto finance = common("Finance");
        SourcePageDto pageRequest = new SourcePageDto();

        pageRequest.setCategories(Arrays.asList(regionUSA, regionUSSR, politics, finance));
        noteRequest.setSourcePages(Arrays.asList(pageRequest));

        NotifyNewsRequest request = notifyNewsReq(Arrays.asList(noteRequest));

        newsController.notifyNews(request);

        verify(bot).execute(sendMsgAc.capture());

        SendMessage value = sendMsgAc.getValue();
        assertTrue(value.getDisableNotification());
        assertEquals(CHAT_ID, Long.parseLong(value.getChatId()));
        assertEquals("<a href=\"url\">title</a>\n\n" +
            "USA, USSR / Finance, Politics\n" +
            "Author: author", value.getText());
    }

    @Test
    public void notifyNews_NoteContainsCommonsAuthor_ShouldNotifyWithCorrectMessage() throws TelegramApiException {
        ArgumentCaptor<SendMessage> sendMsgAc = ArgumentCaptor.forClass(SendMessage.class);

        NewsNoteDto noteRequest = newNoteReq("title", "url", "author");
        CategoryDto politics = common("Politics");
        CategoryDto finance = common("Finance");
        SourcePageDto pageRequest = new SourcePageDto();

        pageRequest.setCategories(Arrays.asList(politics, finance));
        noteRequest.setSourcePages(Arrays.asList(pageRequest));

        NotifyNewsRequest request = notifyNewsReq(Arrays.asList(noteRequest));

        newsController.notifyNews(request);

        verify(bot).execute(sendMsgAc.capture());

        SendMessage value = sendMsgAc.getValue();
        assertTrue(value.getDisableNotification());
        assertEquals(CHAT_ID, Long.parseLong(value.getChatId()));
        assertEquals("<a href=\"url\">title</a>\n\n" +
            " / Finance, Politics\n" +
            "Author: author", value.getText());
    }

    @Test
    public void notifyNews_NoteContainsCommons_ShouldNotifyWithCorrectMessage() throws TelegramApiException {
        ArgumentCaptor<SendMessage> sendMsgAc = ArgumentCaptor.forClass(SendMessage.class);

        NewsNoteDto noteRequest = newNoteReq("title", "url", null);
        CategoryDto politics = common("Politics");
        CategoryDto finance = common("Finance");
        SourcePageDto pageRequest = new SourcePageDto();

        pageRequest.setCategories(Arrays.asList(politics, finance));
        noteRequest.setSourcePages(Arrays.asList(pageRequest));

        NotifyNewsRequest request = notifyNewsReq(Arrays.asList(noteRequest));

        newsController.notifyNews(request);

        verify(bot).execute(sendMsgAc.capture());

        SendMessage value = sendMsgAc.getValue();
        assertEquals(CHAT_ID, Long.parseLong(value.getChatId()));
        assertEquals("<a href=\"url\">title</a>\n\n" +
            " / Finance, Politics", value.getText());
    }

    @Test
    public void notifyNews_NoteContainsTitleUrlCommon_ShouldNotifyWithCorrectMessage() throws TelegramApiException {
        ArgumentCaptor<SendMessage> sendMsgAc = ArgumentCaptor.forClass(SendMessage.class);

        NewsNoteDto noteRequest = newNoteReq("title", "url", null);
        SourcePageDto pageRequest = new SourcePageDto();

        pageRequest.setCategories(Arrays.asList(common("Finance")));
        noteRequest.setSourcePages(Arrays.asList(pageRequest));

        NotifyNewsRequest request = notifyNewsReq(Arrays.asList(noteRequest));

        newsController.notifyNews(request);

        verify(bot).execute(sendMsgAc.capture());

        SendMessage value = sendMsgAc.getValue();
        assertTrue(value.getDisableNotification());
        assertEquals(CHAT_ID, Long.parseLong(value.getChatId()));
        assertEquals("<a href=\"url\">title</a>\n\n" +
            " / Finance", value.getText());
    }

    @Test(expected = NullPointerException.class)
    public void notifyNews_NoteNotContainsUrl_ShouldThrowAndException() {
        NewsNoteDto noteRequest = newNoteReq("title", null, null);
        SourcePageDto pageRequest = new SourcePageDto();

        pageRequest.setCategories(Collections.emptyList());
        noteRequest.setSourcePages(Arrays.asList(pageRequest));

        NotifyNewsRequest request = notifyNewsReq(Arrays.asList(noteRequest));

        newsController.notifyNews(request);
    }

    @Test(expected = NullPointerException.class)
    public void notifyNews_NoteNotContainsTitle_ShouldThrowAndException() {
        NewsNoteDto noteRequest = newNoteReq(null, "url", null);
        SourcePageDto pageRequest = new SourcePageDto();

        pageRequest.setCategories(Collections.emptyList());
        noteRequest.setSourcePages(Arrays.asList(pageRequest));

        NotifyNewsRequest request = notifyNewsReq(Arrays.asList(noteRequest));

        newsController.notifyNews(request);
    }

    private NotifyNewsRequest notifyNewsReq(List<NewsNoteDto> newsNotes) {
        NotifyNewsRequest request = new NotifyNewsRequest();
        request.setChatId(CHAT_ID);
        request.setLang(LangUtils.DEFAULT);
        request.setNotes(newsNotes);
        return request;
    }

    private NewsNoteDto newNoteReq(String title, String url, String author) {
        NewsNoteDto noteRequest = new NewsNoteDto();
        noteRequest.setTitle(title);
        noteRequest.setUrl(url);
        noteRequest.setAuthor(author);
        return noteRequest;
    }

    private CategoryDto region(String localised) {
        return category(localised, REGION_TYPE);
    }

    private CategoryDto common(String localised) {
        return category(localised, COMMON_TYPE);
    }

    private CategoryDto category(String localised, String type) {
        CategoryDto r = new CategoryDto();
        r.setType(type);
        r.setLocalised(localised);
        return r;
    }

    @TestConfiguration
    @EntityScan(basePackages = "bepicky.bot.client")
    @Import({TemplateConfig.class, MessageTemplateContext.class})
    static class NotificationControllerTestConfig {

        @Bean
        public NotificationController newsController() {
            return new NotificationController();
        }

        @Bean
        public IValueNormalisationService valueNormalisationService() {
            return new ValueNormalisationService();
        }
    }
}