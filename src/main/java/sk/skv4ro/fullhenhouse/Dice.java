package sk.skv4ro.fullhenhouse;

import java.util.Random;

public class Dice {

    private final int sides;
    private final Random random = new Random();

    public Dice(int sides) {
        this.sides = sides;
    }

    public int roll() {
        return random.nextInt(sides) + 1;
    }

    public static class DoubleRoll {
        private final int roll1;
        private final int roll2;

        public DoubleRoll(int roll1, int roll2) {
            this.roll1 = roll1;
            this.roll2 = roll2;
        }

        public int getRoll1() {
            return roll1;
        }

        public int getRoll2() {
            return roll2;
        }
    }
}
