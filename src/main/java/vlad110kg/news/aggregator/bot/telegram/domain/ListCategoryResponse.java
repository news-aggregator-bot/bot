package vlad110kg.news.aggregator.bot.telegram.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ListCategoryResponse {

    private List<Category> categories;
    @JsonProperty("total_amount")
    private int totalAmount;
    private String language;
    private String error;

    public boolean isError() {
        return error != null;
    }
}
