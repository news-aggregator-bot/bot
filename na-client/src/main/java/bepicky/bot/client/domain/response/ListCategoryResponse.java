package bepicky.bot.client.domain.response;

import bepicky.bot.client.domain.Category;
import lombok.Data;

import java.util.List;

@Data
public class ListCategoryResponse {

    private List<Category> categories;
    private boolean last;
    private String language;
    private ErrorResponse error;

    public boolean isError() {
        return error != null;
    }
}
