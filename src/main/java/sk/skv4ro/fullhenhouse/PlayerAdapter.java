package sk.skv4ro.fullhenhouse;

public interface PlayerAdapter {
    GameItem itemPrompt(Player player);
    Player playerPrompt(Player player, Player[] players);
    int singleRollPrompt(Player player, Dice dice, Dice.DoubleRoll preRoll);
    Dice.DoubleRoll doubleRollPrompt(Player player, Dice dice);
    void onError(Player player, int errorCode);
    void onWin(Player[] players, Player winner, int totalRounds);
    void onBeforeTurn(Player player);
    void onAfterTurn(Player player);
    void onGameStart(Player[] players);
}
