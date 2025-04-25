import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A three-horse race, each horse running in its own lane for a given distance
 *
 * @author sunny_1118
 * @version 20250416
 */
public class Race {

    public static int raceLength;

    private Horse[] horseLanes;
    private List<Integer> laneIndexList;
    private String trackShape;
    private String trackCondition;
    private int laneCount;

    private RaceTrackPanel trackPanel;

    /**
     * Constructor for objects of class Race Initially there are no horses in the lanes
     *
     * @param distance the length of the racetrack (in metres/yards...)
     */
    public Race(int distance, int lanes) {
        raceLength = distance;
        this.laneCount = lanes;
        horseLanes = new Horse[lanes];
        laneIndexList = new ArrayList<>();
        trackShape = "Oval";
        trackCondition = "Dry";
    }

    /**
     * Adds a horse to the race in a given lane
     *
     * @param theHorse   the horse to be added to the race
     * @param laneNumber the lane that the horse will be added to
     */
    public void addHorse(Horse theHorse, int laneNumber) {
        if (laneNumber >= 1 && laneNumber <= horseLanes.length) {
            horseLanes[laneNumber] = theHorse;
            laneIndexList.add(laneNumber);
        }
    }

    public void setTrackShape(String shape) {
        if (shape.equals("Oval") || shape.equals("Figure-eight") || shape.equals("Custom")) {
            this.trackShape = shape;
        }
    }

    public int getAddedHorseCount() {
        return this.laneIndexList.size();
    }

    public void setTrackCondition(String condition) {
        if (condition.equals("Dry") || condition.equals("Muddy") || condition.equals("Icy")) {
            this.trackCondition = condition;
        }
    }


    public int getLaneCount() {
        return laneCount;
    }

    /**
     * Start the race The horse are brought to the start and then repeatedly moved forward until the
     * race is finished
     */
    public void startRace() {
        boolean finished = false;
        boolean firstRepaintDone = false;

        for (int i = 0; i < laneIndexList.size(); i++) {
            int laneNumber = laneIndexList.get(i);
            if (laneNumber >= 1 && laneNumber < horseLanes.length
                && horseLanes[laneNumber] != null) {
                horseLanes[laneNumber].goBackToStart();
            }
        }

        while (!finished) {
            if (trackPanel != null && !firstRepaintDone) {
                trackPanel.repaint();
                firstRepaintDone = true;
            }

            for (int i = 0; i < laneIndexList.size(); i++) {
                int laneNumber = laneIndexList.get(i);
                if (laneNumber >= 1 && laneNumber < horseLanes.length
                    && horseLanes[laneNumber] != null) {
                    Horse theHorse = horseLanes[laneNumber];
                    if (!theHorse.hasFallen()) {
                        theHorse.move(trackShape, trackCondition);
                    }
                }
            }

            printRace();
            if (trackPanel != null) {
                trackPanel.repaint();
            }
            for (int i = 0; i < laneIndexList.size(); i++) {
                int laneNumber = laneIndexList.get(i);
                if (laneNumber >= 1 && laneNumber < horseLanes.length
                    && horseLanes[laneNumber] != null) {
                    Horse theHorse = horseLanes[laneNumber];
                    if (theHorse.getDistanceTravelled() >= Race.raceLength) {
                        finished = true;
                        System.out.println("And the winner is " + theHorse.getName());
                        break;
                    }
                }
            }

            if (!finished) {
                boolean allFallen = true;
                int activeCount = 0;
                for (int i = 0; i < laneIndexList.size(); i++) {
                    int laneNumber = laneIndexList.get(i);
                    if (laneNumber >= 1 && laneNumber < horseLanes.length
                        && horseLanes[laneNumber] != null) {
                        activeCount++;
                        if (!horseLanes[laneNumber].hasFallen()) {
                            allFallen = false;
                            break;
                        }
                    }
                }
                if (activeCount > 0 && allFallen) {
                    finished = true;
                    System.out.println("All horses have fallen! The race is over.");
                }
            }

            if (!finished) {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    finished = true;
                } catch (Exception e) {
                    finished = true;
                }
            }
        }

        if (trackPanel != null) {
            trackPanel.repaint();
        }

    }

    /***
     * Print the race on the terminal
     */
    private void printRace() {
        System.out.print('\u000C');  //clear the terminal window

        multiplePrint('=', raceLength + 3); //top edge of track
        System.out.println();

        for (int i = 0; i < laneIndexList.size(); i++) {
            int laneIndex = laneIndexList.get(i);
            Horse theHorse = horseLanes[laneIndex];
            printLane(theHorse);
            System.out.println();
        }

        multiplePrint('=', raceLength + 3); //bottom edge of track
        System.out.println();
    }

    /**
     * print a horse's lane during the race for example |           X                      | to show
     * how far the horse has run
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

    public Horse[] getHorseLanes() {
        return horseLanes;
    }

    public List<Integer> getLaneIndexList() {
        return laneIndexList;
    }

    public void setTrackPanel(RaceTrackPanel trackPanel) {
        this.trackPanel = trackPanel;
    }

    public String getTrackShape() {
        return trackShape;
    }

    public String getTrackCondition() {
        return trackCondition;
    }

    public RaceTrackPanel getTrackPanel() {
        return trackPanel;
    }
}