package vlad110kg.news.aggregator.bot.client.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import vlad110kg.news.aggregator.bot.client.domain.Language;

import java.util.List;

@Data
public class ListLanguageResponse {

    private List<Language> languages;
    @JsonProperty("total_amount")
    private int totalAmount;
    private String language;
    private ErrorResponse error;

    public boolean isError() {
        return error != null;
    }
}
