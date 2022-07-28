package sk.skv4ro.fullhenhouse;

public enum GameItem {
    EGG,
    CHICK,
    HEN,
    DICE,
    SKIP;

    public String nameWithArticle(boolean lowerCase) {
        String article = this == EGG ? "an " : "a ";
        return article + (lowerCase ? name().toLowerCase() : name());
    }

    public String nameWithArticle() {
        return nameWithArticle(true);
    }
}
