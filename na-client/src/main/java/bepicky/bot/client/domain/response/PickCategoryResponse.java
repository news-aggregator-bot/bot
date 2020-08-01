package bepicky.bot.client.domain.response;

import bepicky.bot.client.domain.Category;
import lombok.Data;

@Data
public class PickCategoryResponse {

    private Category category;
    private String language;
    private ErrorResponse error;

    public boolean isError() {
        return error != null;
    }
}
