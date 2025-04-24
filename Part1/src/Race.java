import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 *
 * @author sunny_1118
 * @version 20250416
 */
public class Race {
    public static int raceLength;
    private Horse[] horseLanes;
    private List<Integer> laneIndexList;

    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     *
     * @param distance the length of the racetrack (in metres/yards...)
     */
    public Race(int distance) {
        // initialise instance variables
        raceLength = distance;
        horseLanes = new Horse[1000];
        laneIndexList = new ArrayList<>();
    }

    /**
     * Adds a horse to the race in a given lane
     *
     * @param theHorse   the horse to be added to the race
     * @param laneNumber the lane that the horse will be added to
     */
    public void addHorse(Horse theHorse, int laneNumber) {
        horseLanes[laneNumber] = theHorse;
        laneIndexList.add(laneNumber);
    }

    /**
     * Start the race
     * The horse are brought to the start and
     * then repeatedly moved forward until the
     * race is finished
     */
    public void startRace() {
        //declare a local variable to tell us when the race is finished
        boolean finished = false;

        for(int i = 0; i < laneIndexList.size() ; i++){
            int laneIndex = laneIndexList.get(i);
            Horse theHorse = horseLanes[laneIndex];
            theHorse.goBackToStart();
        }


        while (!finished) {
            //move each horse
            for(int i = 0; i < laneIndexList.size() ; i++){
                int laneIndex = laneIndexList.get(i);
                Horse theHorse = horseLanes[laneIndex];
                theHorse.move();
            }

            printRace();

            for(int i = 0; i < laneIndexList.size() ; i++){
                int laneIndex = laneIndexList.get(i);
                Horse theHorse = horseLanes[laneIndex];
                if(theHorse.raceWonBy()){
                    finished = true;
                    System.out.println("And the winner is " + theHorse.getName());
                }
            }

            boolean allFallen = true;
            for(int i = 0; i < laneIndexList.size(); i++){
                int laneIndex = laneIndexList.get(i);
                Horse theHorse = horseLanes[laneIndex];
                if(!theHorse.hasFallen()){
                    allFallen = false;
                    break;
                }
            }
            if(allFallen){
                finished = true;
                System.out.println("All horses have fallen! The race is over.");
            }


            //wait for 100 milliseconds
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (Exception e) {
            }
        }
    }

    /***
     * Print the race on the terminal
     */
    private void printRace() {
        System.out.print('\u000C');  //clear the terminal window

        multiplePrint('=', raceLength + 3); //top edge of track
        System.out.println();

        for(int i = 0; i < laneIndexList.size() ; i++){
            int laneIndex = laneIndexList.get(i);
            Horse theHorse = horseLanes[laneIndex];
            printLane(theHorse);
            System.out.println();
        }

        multiplePrint('=', raceLength + 3); //bottom edge of track
        System.out.println();
    }

    /**
     * print a horse's lane during the race
     * for example
     * |           X                      |
     * to show how far the horse has run
     */
    private void printLane(Horse theHorse) {
        //calculate how many spaces are needed before
        //and after the horse
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - theHorse.getDistanceTravelled();

        //print a | for the beginning of the lane
        System.out.print('|');

        //print the spaces before the horse
        multiplePrint(' ', spacesBefore);

        //if the horse has fallen then print dead
        //else print the horse's symbol
        if (theHorse.hasFallen()) {
            System.out.print('\u2322');
        } else {
            System.out.print(theHorse.getSymbol());
        }

        //print the spaces after the horse
        multiplePrint(' ', spacesAfter);

        //print the | for the end of the track
        System.out.print('|');

        System.out.print(theHorse.getName());
        System.out.print("(Current confidence: " + theHorse.getConfidence() + ")");
    }


    /***
     * print a character a given number of times.
     * e.g. printmany('x',5) will print: xxxxx
     *
     * @param aChar the character to Print
     */
    private void multiplePrint(char aChar, int times) {
        int i = 0;
        while (i < times) {
            System.out.print(aChar);
            i = i + 1;
        }
    }
}
