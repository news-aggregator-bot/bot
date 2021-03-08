package bepicky.bot.core.message.template;

import bepicky.bot.core.message.LangUtils;
import com.vdurmont.emoji.EmojiParser;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class MessageTemplateContext {

    public static final String ERROR = "error";

    @Autowired
    @Qualifier("localTemplateConfig")
    private Configuration configuration;

    public String processEmojiTemplate(String dir, String lang) {
        return EmojiParser.parseToUnicode(processTemplate(dir, lang));
    }

    public String processTemplate(String dir, String lang) {
        return processTemplate(dir, lang, Collections.emptyMap());
    }

    public String processTemplate(String dir, String lang, Map<String, Object> params) {
        StringWriter stringWriter = new StringWriter();
        String language = Optional.ofNullable(lang).orElse(LangUtils.DEFAULT);
        try {
            Template template = configuration.getTemplate(dir + "/" + language + ".ftl");
            template.process(params, stringWriter);
            return stringWriter.toString();
        } catch (IOException | TemplateException e) {
            if (!LangUtils.ALL.equals(lang)) {
                return processTemplate(dir, LangUtils.ALL, params);
            }
            log.error("Template processing failed {}", e.getMessage());
            return processTemplate(ERROR, language, Collections.emptyMap());
        }
    }

    public String errorTemplate(String lang, Map<String, Object> params) {
        return processTemplate(ERROR, lang, params);
    }

    public String errorTemplate(String lang) {
        return processTemplate(ERROR, lang, Collections.emptyMap());
    }

}
