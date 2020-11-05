package bepicky.bot.client.message;

public enum EntityType {
    CATEGORY, LANGUAGE, REGION, SOURCE, TRANSITION;

    public String low() {
        return this.name().toLowerCase();
    }
}
