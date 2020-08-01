package bepicky.bot.client.domain.request;

import lombok.Data;
import bepicky.bot.client.domain.Category;
import bepicky.bot.client.domain.Language;

import java.util.List;

@Data
public class SourcePageRequest {

    private String name;

    private String url;

    private Language language;

    private List<Category> categories;
}
