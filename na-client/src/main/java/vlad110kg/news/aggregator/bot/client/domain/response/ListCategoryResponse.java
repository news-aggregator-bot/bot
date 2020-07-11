package vlad110kg.news.aggregator.bot.client.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import vlad110kg.news.aggregator.bot.client.domain.Category;

import java.util.List;

@Data
public class ListCategoryResponse {

    private List<Category> categories;
    @JsonProperty("total_amount")
    private int totalAmount;
    private String language;
    private ErrorResponse error;

    public boolean isError() {
        return error != null;
    }
}
