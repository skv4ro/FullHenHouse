package sk.skv4ro.fullhenhouse;

public class Main {
    public static void main(String[] args) {
        FullHenHouse game = new FullHenHouse(
                new PlayerAdapterImpl(),
                new Player(0),
                new Player(1),
                new Player(2)
        );
        game.startGame();
    }
}
