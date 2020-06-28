package vlad110kg.news.aggregator.bot.telegram.domain.response;

import lombok.Data;
import vlad110kg.news.aggregator.bot.telegram.domain.Category;

@Data
public class PickCategoryResponse {

    private Category category;
    private String language;
    private ErrorResponse error;

    public boolean isError() {
        return error != null;
    }
}
