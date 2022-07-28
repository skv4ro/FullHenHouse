package sk.skv4ro.fullhenhouse;

public class FoxLoot {
    private final int hens;
    private final int chicks;
    private final int eggs;

    public FoxLoot(int hens, int chicks, int eggs) {
        this.hens = hens;
        this.chicks = chicks;
        this.eggs = eggs;
    }

    public int getChicks() {
        return chicks;
    }

    public int getEggs() {
        return eggs;
    }

    public int getHens() {
        return hens;
    }

    @Override
    public String toString() {
        return hens + "-" + chicks + "-" + eggs;
    }
}
