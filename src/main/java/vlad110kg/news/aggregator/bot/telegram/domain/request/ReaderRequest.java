package vlad110kg.news.aggregator.bot.telegram.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ReaderRequest {

    @JsonProperty("chat_id")
    private Long chatId;

    private String username;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private String platform;

    @JsonProperty("primary_language")
    private String primaryLanguage;
}
