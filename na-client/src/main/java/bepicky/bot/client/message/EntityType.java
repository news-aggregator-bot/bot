package bepicky.bot.client.message;

public enum EntityType {
    CATEGORY, SUBCATEGORY, LANGUAGE, REGION, SOURCE, TRANSITION;

    public String lower() {
        return this.name().toLowerCase();
    }
}
