//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Horse horse1 = new Horse('#', "horse1", 0.5);
        Horse horse2 = new Horse('!', "horse2", 0.9);
        Horse horse3 = new Horse('@', "horse3", 0.3);
        Race race = new Race(10);

        race.addHorse(horse1, 1);
        race.addHorse(horse2, 2);
        race.addHorse(horse3, 3);
        race.startRace();


    }
}