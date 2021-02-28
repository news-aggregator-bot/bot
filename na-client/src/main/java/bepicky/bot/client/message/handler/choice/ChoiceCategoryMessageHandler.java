package bepicky.bot.client.message.handler.choice;

import bepicky.bot.client.message.EntityType;
import org.springframework.stereotype.Component;

@Component
public class ChoiceCategoryMessageHandler extends AbstractChoiceMessageHandler {

    @Override
    protected String categoryType() {
        return "COMMON";
    }

    @Override
    public EntityType entityType() {
        return EntityType.CATEGORY;
    }
}
