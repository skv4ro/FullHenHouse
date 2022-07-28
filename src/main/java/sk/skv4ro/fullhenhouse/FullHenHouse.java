package sk.skv4ro.fullhenhouse;

public class FullHenHouse {

    private final PlayerAdapter playerAdapter;
    private final Dice dice = new Dice(6);
    private final Player[] players;
    private int currentPlayer = 0;
    private boolean endGame = false;
    private int rounds = 0;

    public FullHenHouse(PlayerAdapter playerAdapter, int players) {
        this.playerAdapter = playerAdapter;
        this.players = new Player[players];
        for(int i = 0; i < players; i++) {
            this.players[i] = new Player(i);
        }
    }

    public FullHenHouse(PlayerAdapter playerAdapter, Player... players) {
        this.playerAdapter = playerAdapter;
        this.players = players;
    }

    private void reward(Player player, int num) {
        if(num == 1 || num == 2 || num == 3) player.addEgg();
        else if(num == 4 || num == 5) player.addChick();
        else if(num == 6) player.addHen();
    }

    private void nextPlayer() {
        if(currentPlayer >= players.length - 1) currentPlayer = 0;
        else currentPlayer++;
    }

    private Player checkWinner() {
        for(Player player : players) {
            if(player.getHens() >= 9) return player;
        }
        return null;
    }

    private GameItem promptItem(Player player) {
        GameItem changeItem;
        boolean block = true;
        do {
            changeItem = playerAdapter.itemPrompt(player);
            if(changeItem == GameItem.EGG) {
                if(player.canChangeEggs()) block = false;
                else playerAdapter.onError(player, 0);
            } else if (changeItem == GameItem.CHICK) {
                if(player.canChangeChicks()) block = false;
                else playerAdapter.onError(player, 1);
            } else if (changeItem == GameItem.HEN) {
                if(player.canChangeHens()) block = false;
                else playerAdapter.onError(player, 2);
            } else block = false;
        } while(block);
        return changeItem;
    }

    private void turn(Player player) {
        GameItem gameItem = promptItem(player);
        if(gameItem == GameItem.EGG) player.changeEggs();
        else if(gameItem == GameItem.CHICK) player.changeChicks();
        else if(gameItem == GameItem.HEN) player.changeHens();
        else if(gameItem == GameItem.DICE) {
            Dice.DoubleRoll doubleRoll = playerAdapter.doubleRollPrompt(player, dice);
            int d1 = doubleRoll.getRoll1();
            int d2 = doubleRoll.getRoll2();
            if((d1 == 1 && d2 == 1) && player.canGiveEgg()) {
                Player other = playerAdapter.playerPrompt(player, players);
                player.giveEgg(other);
            } else if((d1 == 2 && d2 == 2) && player.canGiveChick()) {
                Player other = playerAdapter.playerPrompt(player, players);
                player.giveChick(other);
            } else if((d1 == 3 && d2 == 3) && player.canGiveHen()) {
                Player other = playerAdapter.playerPrompt(player, players);
                player.giveHen(other);
            } else if(d1 == 4 && d2 == 4) {
                player.dropEggs();
            } else if(d1 == 5 && d2 == 5) {
                int d = playerAdapter.singleRollPrompt(player, dice, doubleRoll);
                if(d == 1 || d == 2 || d == 3) player.setBlocked(true);
                else if(d == 4) player.dropEggs();
                else if(d == 5) player.dropChicks();
                else if(d == 6) player.dropHens();
            } else if(d1 == 6 && d2 == 6) {
                if(!player.hasRooster()) {
                    int sum = player.getHens() + player.getChicks() + player.getEggs();
                    if (sum == 1) player.fox(1);
                    else if (sum > 0) {
                        int d = playerAdapter.singleRollPrompt(player, dice, doubleRoll);
                        player.fox(d);
                    }
                }
            } else {
                reward(player, d1);
                reward(player, d2);
            }
        }
    }

    private void round() {
        Player winner = checkWinner();
        if(winner != null) {
            playerAdapter.onWin(players, winner, rounds);
            endGame = true;
        } else {
            Player player = players[currentPlayer];
            playerAdapter.onBeforeTurn(player);
            if(player.isBlocked()) player.setBlocked(false);
            else turn(player);
            rounds++;
            playerAdapter.onAfterTurn(player);
            nextPlayer();
        }
    }

    public void startGame() {
        playerAdapter.onGameStart(players);
        while (!endGame) round();
    }
}
