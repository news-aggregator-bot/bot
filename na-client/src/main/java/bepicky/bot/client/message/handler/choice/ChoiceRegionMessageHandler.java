package bepicky.bot.client.message.handler.choice;

import bepicky.bot.core.message.EntityType;
import org.springframework.stereotype.Component;

@Component
public class ChoiceRegionMessageHandler extends AbstractChoiceMessageHandler {

    @Override
    protected String categoryType() {
        return "REGION";
    }

    @Override
    public EntityType entityType() {
        return EntityType.REGION;
    }
}
