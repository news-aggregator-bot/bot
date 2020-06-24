package vlad110kg.news.aggregator.bot.telegram.domain;

import lombok.Data;

import java.util.List;

@Data
public class ListCategoryResponse {
    private List<Category> categories;
    private int totalAmount;
    private String language;
    private boolean error;
}
