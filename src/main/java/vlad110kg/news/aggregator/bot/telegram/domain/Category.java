package vlad110kg.news.aggregator.bot.telegram.domain;

import lombok.Data;

import java.util.List;

@Data
public class Category {
    private long id;
    private String name;
    private String localised;
    private Category parent;
    private List<Category> children;
}
