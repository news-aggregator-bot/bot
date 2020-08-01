package bepicky.bot.client.domain.response;

import bepicky.bot.client.domain.Language;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

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
