package bepicky.bot.client.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NewsNoteRequest {

    private String title;

    private String url;

    private String description;

    private String author;

    @JsonProperty("source_page")
    private SourcePageRequest sourcePage;
}
