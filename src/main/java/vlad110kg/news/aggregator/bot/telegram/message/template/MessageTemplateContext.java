package vlad110kg.news.aggregator.bot.telegram.message.template;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;

@Component
@Slf4j
public class MessageTemplateContext {
    public static final String ERROR = "error";

    @Autowired
    private Configuration configuration;

    public String processTemplate(String dir, String lang) {
        return processTemplate(dir, lang, Collections.emptyMap());
    }

    public String processTemplate(String dir, String lang, Map<String, Object> params) {
        StringWriter stringWriter = new StringWriter();
        try {
            Template template = configuration.getTemplate(dir + "/" + lang + ".ftl");
            template.process(params, stringWriter);
            return stringWriter.toString();
        } catch (IOException | TemplateException e) {
            log.error("Template processing failed {}", e.getMessage());
            return processTemplate(ERROR, lang, Collections.emptyMap());
        }
    }
}
