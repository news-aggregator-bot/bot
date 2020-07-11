package vlad110kg.news.aggregator.bot.client.domain.request;

import lombok.Data;

import java.util.List;

@Data
public class NotifyReaderRequest {

    private long chatId;

    private String lang;

    private List<NewsNoteRequest> notes;
}
