package vlad110kg.news.aggregator.bot.client.domain.request;

import lombok.Data;
import vlad110kg.news.aggregator.bot.client.domain.Category;
import vlad110kg.news.aggregator.bot.client.domain.Language;

import java.util.List;

@Data
public class SourcePageRequest {

    private String name;

    private String url;

    private Language language;

    private List<Category> categories;
}
