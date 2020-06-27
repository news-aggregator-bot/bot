package vlad110kg.news.aggregator.bot.telegram.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Reader {

    private Long id;

    @JsonProperty("chat_id")
    private Long chatId;

    private String username;

    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;

    private String status;

    private String platform;

    @JsonProperty("primary_language")
    private Language primaryLanguage;
}
