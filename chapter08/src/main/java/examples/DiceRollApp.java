package examples;

public class DiceRollApp {
    public static void main(String[] args) {
        new DiceRollApp().run();
    }

    private void run() {
        var rnd = new RandomlyGeneratedNumbers();
        var roll = new DiceRoll(rnd);

        System.out.println(roll.asText());
    }
}

