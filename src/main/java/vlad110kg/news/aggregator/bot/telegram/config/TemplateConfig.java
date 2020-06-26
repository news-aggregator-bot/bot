package vlad110kg.news.aggregator.bot.telegram.config;

import com.google.common.io.Resources;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

@Configuration
public class TemplateConfig {

    @Bean("localTemplateConfig")
    public freemarker.template.Configuration templateConfiguration() throws IOException, URISyntaxException {

        freemarker.template.Configuration cfg = new freemarker.template.Configuration(new Version(2, 3, 20));
        cfg.setTemplateLoader(new FileTemplateLoader(new File(Resources.getResource("templates").toURI())));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return cfg;
    }
}
