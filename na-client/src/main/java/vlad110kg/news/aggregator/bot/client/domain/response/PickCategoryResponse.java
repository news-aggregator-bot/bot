package vlad110kg.news.aggregator.bot.client.domain.response;

import lombok.Data;
import vlad110kg.news.aggregator.bot.client.domain.Category;

@Data
public class PickCategoryResponse {

    private Category category;
    private String language;
    private ErrorResponse error;

    public boolean isError() {
        return error != null;
    }
}
