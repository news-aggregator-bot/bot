package bepicky.bot.client.message.handler.update;

import bepicky.bot.client.message.EntityType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static bepicky.bot.client.message.EntityType.CATEGORY;

@Component
@Slf4j
public class CommonCategoryUpdateMessageHandler extends AbstractUpdateMessageHandler {

    @Override
    public String trigger() {
        return entityType().low();
    }

    @Override
    public EntityType entityType() {
        return CATEGORY;
    }
}
