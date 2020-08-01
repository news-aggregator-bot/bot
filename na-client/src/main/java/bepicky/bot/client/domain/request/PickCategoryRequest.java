package bepicky.bot.client.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PickCategoryRequest {

    @JsonProperty("chat_id")
    private long chatId;

    @JsonProperty("category_id")
    private long categoryId;
}
