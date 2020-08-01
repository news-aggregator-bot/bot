package bepicky.bot.client.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Language {

    @NotBlank
    private String lang;

    @NotBlank
    private String name;

    @NotBlank
    private String localized;
}
