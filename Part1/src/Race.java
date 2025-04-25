import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingUtilities;

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
    private Map<Horse, Integer> horseResultSteps;
    private int currentStep;
    private boolean raceFinished;
    private boolean allFallen;
    private String winnerName;

    /**
     * Constructor for objects of class Race Initially there are no horses in the lanes
     *
     * @param distance the length of the racetrack (in metres/yards...)
     */
    public Race(int distance, int lanes) {
        raceLength = distance;
        this.laneCount = lanes;
        this.horseLanes = new Horse[lanes];
        this.laneIndexList = new ArrayList<>();
        this.trackShape = "Oval";
        this.trackCondition = "Dry";
        this.horseResultSteps = new HashMap<>();
        this.currentStep = 0;
        this.raceFinished = false;
        this.allFallen = false;
        this.winnerName = null;
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
        if (laneIndexList.isEmpty()) { return; }

        this.raceFinished = false;
        this.allFallen = false;
        this.winnerName = null;
        boolean firstRepaintDone = false;

        while (!this.raceFinished) {
            this.currentStep++;

            if (trackPanel != null && !firstRepaintDone) {
                trackPanel.repaint();
                firstRepaintDone = true;
            }

            for (int laneNumber : laneIndexList) {
                if (laneNumber >= 1 && laneNumber < horseLanes.length && horseLanes[laneNumber] != null) {
                    Horse theHorse = horseLanes[laneNumber];
                    if (!theHorse.hasFallen()){
                        theHorse.move(trackShape, trackCondition);
                    }
                }
            }

            for (int laneNumber : laneIndexList) {
                if (laneNumber >= 1 && laneNumber < horseLanes.length && horseLanes[laneNumber] != null) {
                    Horse theHorse = horseLanes[laneNumber];
                    if (!horseResultSteps.containsKey(theHorse)) {
                        if (theHorse.getDistanceTravelled() >= Race.raceLength) {
                            horseResultSteps.put(theHorse, currentStep);
                        } else if (theHorse.hasFallen()) {
                            horseResultSteps.put(theHorse, -currentStep);
                        }
                    }
                }
            }

            printRace();
            if (trackPanel != null) { trackPanel.repaint(); }

            boolean winnerFound = false;
            for (Map.Entry<Horse, Integer> entry : horseResultSteps.entrySet()) {
                if (entry.getValue() > 0) {
                    this.raceFinished = true;
                    this.winnerName = entry.getKey().getName() + " (Lane " + findLaneNumber(entry.getKey()) + ")";
                    System.out.println("And the winner is " + entry.getKey().getName());
                    winnerFound = true;
                    break;
                }
            }

            if (!this.raceFinished) {
                boolean checkAllFallen = true;
                int activeCount = 0;
                int resultCount = 0;
                for(int laneNumber : laneIndexList){
                    if (laneNumber >= 1 && laneNumber < horseLanes.length && horseLanes[laneNumber] != null) {
                        activeCount++;
                        Horse h = horseLanes[laneNumber];
                        if(horseResultSteps.containsKey(h)){
                            resultCount++;
                            if(horseResultSteps.get(h) > 0) {
                                checkAllFallen = false;
                                break;
                            }
                        } else {
                            checkAllFallen = false;
                            break;
                        }
                    }
                }
                if (activeCount > 0 && activeCount == resultCount && checkAllFallen) {
                    this.raceFinished = true;
                    this.allFallen = true;
                    System.out.println("All horses have fallen! The race is over.");
                }
            }

            if (!this.raceFinished) {
                try { TimeUnit.MILLISECONDS.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); this.raceFinished = true; } catch (Exception e) { this.raceFinished = true;}
            }

        }

        if (trackPanel != null) { SwingUtilities.invokeLater(trackPanel::repaint); }

        System.out.println("Race thread finished.");
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

    public void prepareForRaceStart() {
        this.raceFinished = false;
        this.allFallen = false;
        this.winnerName = null;
        this.currentStep = 0;
        this.horseResultSteps.clear();
        for (int laneNumber : laneIndexList) {
            if (laneNumber >= 1 && laneNumber < horseLanes.length && horseLanes[laneNumber] != null) {
                horseLanes[laneNumber].goBackToStart();
            }
        }
    }

    public Map<Horse, Integer> getHorseResultSteps() {
        return this.horseResultSteps;
    }

    private int findLaneNumber(Horse horseToFind) {
        for(int laneNum : laneIndexList){
            if(laneNum >= 1 && laneNum < horseLanes.length && horseLanes[laneNum] == horseToFind){
                return laneNum;
            }
        }
        return -1;
    }

    public boolean isFinished() {
        return this.raceFinished;
    }
    public boolean didAllFall(){
        return this.allFallen;
    }
    public String getWinnerName() {
        return this.winnerName;
    }

    public void setRaceTrackPanel(RaceTrackPanel panel) { this.trackPanel = panel; }
}