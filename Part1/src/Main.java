//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Horse horse1 = new Horse('#', "horse1", 0.9);
        Horse horse2 = new Horse('!', "horse2", 0.9);
        Horse horse3 = new Horse('@', "horse3", 0.9);
        Horse horse4 = new Horse('%', "horse4", 0.9);
        Horse horse5 = new Horse('*', "horse5", 0.9);
        Race race = new Race(20, 20);

        race.addHorse(horse1, 1);
        race.addHorse(horse2, 2);
        race.addHorse(horse3, 3);
        race.addHorse(horse4, 5);
        race.addHorse(horse5, 19);
        race.startRace();


    }
}