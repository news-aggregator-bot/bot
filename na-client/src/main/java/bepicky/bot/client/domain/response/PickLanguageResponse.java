package bepicky.bot.client.domain.response;

import lombok.Data;
import bepicky.bot.client.domain.Language;

@Data
public class PickLanguageResponse {

    private Language language;
    private String lang;
    private ErrorResponse error;

    public boolean isError() {
        return error != null;
    }
}
