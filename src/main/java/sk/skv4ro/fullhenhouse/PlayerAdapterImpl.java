package sk.skv4ro.fullhenhouse;

import java.util.ArrayList;
import java.util.List;

public class PlayerAdapterImpl implements PlayerAdapter {

    private String createFoxLootMsg(FoxLoot loot) {
        int h = loot.getHens();
        int c = loot.getChicks();
        int e = loot.getEggs();
        List<String> tokens = new ArrayList<>();
        if (h != 0) tokens.add(h + " hens");
        if (c != 0) tokens.add(c + " chick");
        if (e != 0) tokens.add(e + " eggs");
        StringBuilder sb = new StringBuilder();
        sb.append("  Fox has stolen: ");
        for (String token : tokens) sb.append(token).append(", ");
        sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    private String createRewardMsg(int roll1, int roll2) {
        GameItem r1 = resolveReward(roll1);
        GameItem r2 = resolveReward(roll2);
        if (r1 == GameItem.EGG && r2 == GameItem.EGG) return "  You got 2 eggs";
        if (r1 == GameItem.CHICK && r2 == GameItem.CHICK) return "  You got 2 chicks";
        return "  You got " + r1.nameWithArticle() + " and " + r2.nameWithArticle();
    }

    private GameItem resolveReward(int roll) {
        if (roll == 1 || roll == 2 || roll == 3) return GameItem.EGG;
        else if (roll == 4 || roll == 5) return GameItem.CHICK;
        else return GameItem.HEN;
    }

    @Override
    public GameItem itemPrompt(Player player) {
        if (player.canChangeEggs()) {
            System.out.println(" Changing 3 eggs for 1 chick");
            return GameItem.EGG;
        }
        if (player.canChangeChicks()) {
            System.out.println(" Changing 3 chicks for 1 hen");
            return GameItem.CHICK;
        }
        if (player.canChangeHens()) {
            System.out.println(" Changing 3 hens for a rooster");
            return GameItem.HEN;
        }
        return GameItem.DICE;
    }

    @Override
    public Player playerPrompt(Player player, Player[] players) {
        for (Player other : players) {
            if (other.getId() != player.getId()) {
                System.out.println("  You chose " + other.getName());
                return other;
            }
        }
        System.out.println("  You chose yourself");
        return player;
    }

    @Override
    public int singleRollPrompt(Player player, Dice dice, Dice.DoubleRoll preRoll) {
        int r = dice.roll();
        System.out.println("  You rolled " + r);
        int r1 = preRoll.getRoll1();
        int r2 = preRoll.getRoll2();
        if (r1 == 5 && r2 == 5) {
            if (r == 1 || r == 2 || r == 3) System.out.println("  You skip one round");
            else if (r == 4) System.out.println("  All your eggs have gone bad");
            else if (r == 5) System.out.println("  All your chick passed");
            else if (r == 6) System.out.println("  All your hens passed");
        } else if (r1 == 6 && r2 == 6) {
            FoxLoot loot = player.computeFoxLoot(r);
            System.out.println(createFoxLootMsg(loot));
        }

        return r;
    }

    @Override
    public Dice.DoubleRoll doubleRollPrompt(Player player, Dice dice) {
        int r1 = dice.roll();
        int r2 = dice.roll();
        System.out.println("  You rolled " + r1 + "-" + r2);
        if (r1 == 1 && r2 == 1) {
            if (player.getEggs() > 0) System.out.println("  Give one egg to another player");
            else System.out.println("  You have no eggs to give");
        } else if (r1 == 2 && r2 == 2) {
            if (player.getChicks() > 0) System.out.println("  Give one chick to another player");
            else System.out.println("  You have no chicks to give");
        } else if (r1 == 3 && r2 == 3) {
            if (player.getHens() > 0) System.out.println("  Give one hen to another player");
            else System.out.println("  You have no hens to give");
        } else if (r1 == 4 && r2 == 4) {
            System.out.println("  The farmer is in a misery and he must sell all the eggs");
            if (player.getEggs() == 0) System.out.println("  You have no eggs to sell");
        } else if (r1 == 5 && r2 == 5) {
            System.out.println("  Bird flu has come");
        } else if (r1 == 6 && r2 == 6) {
            if (player.hasRooster()) System.out.println("  You have a rooster");
            else {
                System.out.println("  Fox has come!");
                int sum = player.getHens() + player.getChicks() + player.getEggs();
                if (sum == 0) System.out.println("  Fox had nothing to steal");
                else if (sum == 1) System.out.println(createFoxLootMsg(player.computeFoxLoot(1)));
            }
        } else {
            System.out.println(createRewardMsg(r1, r2));
        }
        return new Dice.DoubleRoll(r1, r2);
    }

    @Override
    public void onError(Player player, int errorCode) {
        if (errorCode == 0) System.out.println("  You have no eggs to change!");
        else if (errorCode == 1) System.out.println("  You have no chicks to change!");
        else if (errorCode == 2) System.out.println("  You have no hens to change!");
        else System.out.println("  Error "  + errorCode);
    }

    @Override
    public void onWin(Player[] players, Player winner, int totalRounds) {
        System.out.println(winner.getName() + " has WON within " + totalRounds + " rounds");
        for(Player player : players) {
            System.out.println(player.getName() + ": " + player);
        }
    }

    @Override
    public void onBeforeTurn(Player player) {
        System.out.println(player.getName() + " turn ");
        System.out.println("> " + player);
        if(player.isBlocked()) System.out.println("  Skipping round");
    }

    @Override
    public void onAfterTurn(Player player) {
        System.out.println("> " + player);
        System.out.println();
    }

    @Override
    public void onGameStart(Player[] players) {
        System.out.println(">>> HEN HOUSE <<<");
        System.out.println("Players: ");
        for(Player player : players) System.out.println("  " + player.getName());
        System.out.println("Game started");
        System.out.println();
    }
}
