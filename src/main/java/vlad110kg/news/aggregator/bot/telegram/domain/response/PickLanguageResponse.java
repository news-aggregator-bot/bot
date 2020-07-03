package vlad110kg.news.aggregator.bot.telegram.domain.response;

import lombok.Data;
import vlad110kg.news.aggregator.bot.telegram.domain.Language;

@Data
public class PickLanguageResponse {

    private Language language;
    private String lang;
    private ErrorResponse error;

    public boolean isError() {
        return error != null;
    }
}
