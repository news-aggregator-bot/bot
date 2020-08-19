package bepicky.bot.client.config;

import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TemplateConfig {

    @Bean("localTemplateConfig")
    public freemarker.template.Configuration templateConfiguration() {

        freemarker.template.Configuration cfg = new freemarker.template.Configuration(new Version(2, 3, 20));
        cfg.setClassForTemplateLoading(this.getClass(), "/templates");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return cfg;
    }
}
