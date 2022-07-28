package sk.skv4ro.fullhenhouse;

public class Player {
    private final int id;
    private String name;
    private int eggs = 0;
    private int chicks = 0;
    private int hens = 0;
    private boolean rooster = false;
    private boolean blocked = false;

    public Player(int id) {
        this(id, "Player " + id);
    }

    public Player(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getEggs() {
        return eggs;
    }

    public int getChicks() {
        return chicks;
    }

    public int getHens() {
        return hens;
    }

    public boolean hasRooster() {
        return rooster;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean canChangeEggs() {
        return eggs > 2;
    }

    public boolean canChangeChicks() {
        return chicks > 2;
    }

    public boolean canChangeHens() {
        return hens > 2 && !rooster;
    }

    public boolean canGiveEgg() {
        return eggs > 0;
    }

    public boolean canGiveChick() {
        return chicks > 0;
    }

    public boolean canGiveHen() {
        return hens > 0;
    }

    public void changeEggs() {
        if(canChangeEggs()) {
            eggs--;
            eggs--;
            eggs--;
            chicks++;
        }
    }

    public void changeChicks() {
        if(canChangeChicks()) {
            chicks--;
            chicks--;
            chicks--;
            hens++;
        }
    }

    public void changeHens() {
        if(canChangeHens()) {
            hens --;
            hens --;
            hens --;
            rooster = true;
        }
    }

    public void giveEgg(Player other) {
        if(eggs > 0) {
            eggs--;
            other.eggs++;
        }
    }

    public void giveChick(Player other) {
        if(chicks > 0) {
            chicks--;
            other.chicks++;
        }
    }

    public void giveHen(Player other) {
        if(hens > 0) {
            hens--;
            other.hens++;
        }
    }

    public void addEgg() {
        eggs++;
    }

    public void addChick() {
        chicks++;
    }

    public void addHen() {
        hens++;
    }

    public void dropEggs() {
        eggs = 0;
    }

    public void dropChicks() {
        chicks = 0;
    }

    public void dropHens() {
        hens = 0;
    }

    public FoxLoot computeFoxLoot(int loot) {
        int h = 0;
        int c = 0;
        int e = 0;
        if (loot > 0) {
            if (loot < hens) h = hens - (hens - loot);
            else {
                h = hens;
                int chickLoot = loot - h;
                if (chickLoot > 0) {
                    if (chickLoot < chicks) c = chicks - (chicks - chickLoot);
                    else {
                        c = chicks;
                        int eggLoot = chickLoot - c;
                        if (eggLoot > 0) {
                            if (chickLoot < eggs) e = eggs - (eggs - eggLoot);
                            else e = eggs;
                        }
                    }
                }
            }
        }
        return new FoxLoot(h, c, e);
    }

    public void fox(int loot) {
        FoxLoot foxLoot = computeFoxLoot(loot);
        hens = hens - foxLoot.getHens();
        chicks = chicks - foxLoot.getChicks();
        eggs = eggs - foxLoot.getEggs();
    }

    @Override
    public String toString() {
        return "H-" + hens + " C-" + chicks + " E-" + eggs + " R-" + (rooster ? "1" : "0");
    }
}
