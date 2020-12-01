package bepicky.bot.client.controller;

import bepicky.bot.client.config.TemplateConfig;
import bepicky.bot.client.message.LangUtils;
import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.router.PickyNewsBot;
import bepicky.common.domain.dto.CategoryDto;
import bepicky.common.domain.request.NewsNoteRequest;
import bepicky.common.domain.request.NotifyNewsRequest;
import bepicky.common.domain.request.SourcePageRequest;
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
public class NewsControllerTest {

    private static final String COMMON_TYPE = "COMMON";
    private static final String REGION_TYPE = "REGION";
    private static final long CHAT_ID = 1L;

    @Autowired
    private NewsController newsController;

    @MockBean
    private PickyNewsBot bot;

    @Autowired
    private MessageTemplateContext templateContext;

    @Test
    public void notifyNews_NoteContainsRegionsCommonsAuthor_ShouldNotifyWithCorrectMessage()
        throws TelegramApiException {
        ArgumentCaptor<SendMessage> sendMsgAc = ArgumentCaptor.forClass(SendMessage.class);

        NewsNoteRequest noteRequest = newNoteReq("title", "url", "author");
        CategoryDto regionUSA = region("USA");
        CategoryDto regionUSSR = region("USSR");
        CategoryDto politics = common("Politics");
        CategoryDto finance = common("Finance");
        SourcePageRequest pageRequest = new SourcePageRequest();

        pageRequest.setCategories(Arrays.asList(regionUSA, regionUSSR, politics, finance));
        noteRequest.setSourcePage(pageRequest);

        NotifyNewsRequest request = notifyNewsReq(Arrays.asList(noteRequest));

        newsController.notifyNews(request);

        verify(bot).execute(sendMsgAc.capture());

        SendMessage value = sendMsgAc.getValue();
        assertTrue(value.getDisableNotification());
        assertEquals(CHAT_ID, Long.parseLong(value.getChatId()));
        assertEquals("<a href=\"url\">title</a>\n\n" +
            "Region: <b>USA, USSR</b>\n" +
            "Category: <b>Politics, Finance</b>\n" +
            "Author: author", value.getText());
    }

    @Test
    public void notifyNews_NoteContainsCommonsAuthor_ShouldNotifyWithCorrectMessage() throws TelegramApiException {
        ArgumentCaptor<SendMessage> sendMsgAc = ArgumentCaptor.forClass(SendMessage.class);

        NewsNoteRequest noteRequest = newNoteReq("title", "url", "author");
        CategoryDto politics = common("Politics");
        CategoryDto finance = common("Finance");
        SourcePageRequest pageRequest = new SourcePageRequest();

        pageRequest.setCategories(Arrays.asList(politics, finance));
        noteRequest.setSourcePage(pageRequest);

        NotifyNewsRequest request = notifyNewsReq(Arrays.asList(noteRequest));

        newsController.notifyNews(request);

        verify(bot).execute(sendMsgAc.capture());

        SendMessage value = sendMsgAc.getValue();
        assertTrue(value.getDisableNotification());
        assertEquals(CHAT_ID, Long.parseLong(value.getChatId()));
        assertEquals("<a href=\"url\">title</a>\n\n" +
            "Category: <b>Politics, Finance</b>\n" +
            "Author: author", value.getText());
    }

    @Test
    public void notifyNews_NoteContainsCommons_ShouldNotifyWithCorrectMessage() throws TelegramApiException {
        ArgumentCaptor<SendMessage> sendMsgAc = ArgumentCaptor.forClass(SendMessage.class);

        NewsNoteRequest noteRequest = newNoteReq("title", "url", null);
        CategoryDto politics = common("Politics");
        CategoryDto finance = common("Finance");
        SourcePageRequest pageRequest = new SourcePageRequest();

        pageRequest.setCategories(Arrays.asList(politics, finance));
        noteRequest.setSourcePage(pageRequest);

        NotifyNewsRequest request = notifyNewsReq(Arrays.asList(noteRequest));

        newsController.notifyNews(request);

        verify(bot).execute(sendMsgAc.capture());

        SendMessage value = sendMsgAc.getValue();
        assertEquals(CHAT_ID, Long.parseLong(value.getChatId()));
        assertEquals("<a href=\"url\">title</a>\n\n" +
            "Category: <b>Politics, Finance</b>", value.getText());
    }

    @Test
    public void notifyNews_NoteContainsTitleUrlCommon_ShouldNotifyWithCorrectMessage() throws TelegramApiException {
        ArgumentCaptor<SendMessage> sendMsgAc = ArgumentCaptor.forClass(SendMessage.class);

        NewsNoteRequest noteRequest = newNoteReq("title", "url", null);
        SourcePageRequest pageRequest = new SourcePageRequest();

        pageRequest.setCategories(Arrays.asList(common("Finance")));
        noteRequest.setSourcePage(pageRequest);

        NotifyNewsRequest request = notifyNewsReq(Arrays.asList(noteRequest));

        newsController.notifyNews(request);

        verify(bot).execute(sendMsgAc.capture());

        SendMessage value = sendMsgAc.getValue();
        assertTrue(value.getDisableNotification());
        assertEquals(CHAT_ID, Long.parseLong(value.getChatId()));
        assertEquals("<a href=\"url\">title</a>\n\n" +
            "Category: <b>Finance</b>", value.getText());
    }

    @Test(expected = NullPointerException.class)
    public void notifyNews_NoteNotContainsUrl_ShouldThrowAndException() {
        NewsNoteRequest noteRequest = newNoteReq("title", null, null);
        SourcePageRequest pageRequest = new SourcePageRequest();

        pageRequest.setCategories(Collections.emptyList());
        noteRequest.setSourcePage(pageRequest);

        NotifyNewsRequest request = notifyNewsReq(Arrays.asList(noteRequest));

        newsController.notifyNews(request);
    }

    @Test(expected = NullPointerException.class)
    public void notifyNews_NoteNotContainsTitle_ShouldThrowAndException() {
        NewsNoteRequest noteRequest = newNoteReq(null, "url", null);
        SourcePageRequest pageRequest = new SourcePageRequest();

        pageRequest.setCategories(Collections.emptyList());
        noteRequest.setSourcePage(pageRequest);

        NotifyNewsRequest request = notifyNewsReq(Arrays.asList(noteRequest));

        newsController.notifyNews(request);
    }

    private NotifyNewsRequest notifyNewsReq(List<NewsNoteRequest> newsNotes) {
        NotifyNewsRequest request = new NotifyNewsRequest();
        request.setChatId(CHAT_ID);
        request.setLang(LangUtils.DEFAULT);
        request.setNotes(newsNotes);
        return request;
    }

    private NewsNoteRequest newNoteReq(String title, String url, String author) {
        NewsNoteRequest noteRequest = new NewsNoteRequest();
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
    static class NewsControllerTestConfig {

        @Bean
        public NewsController newsController() {
            return new NewsController();
        }
    }
}