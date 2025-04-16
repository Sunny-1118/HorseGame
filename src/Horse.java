
/**
 * Write a description of class Horse here.
 *
 * @author sunny_118
 * @version 20250416
 */
public class Horse {
    //Fields of class Horse
    private String name;
    double confidence;
    int distanceTravelled;
    char symbol;
    boolean isFallen;
    //Constructor of class Horse

    /**
     * Constructor for objects of class Horse
     */
    public Horse(char horseSymbol, String horseName, double horseConfidence) {
        
    }


    //Other methods of class Horse
    public void fall() {
        this.isFallen = true;
    }

    public double getConfidence() {
        return this.confidence;
    }

    public int getDistanceTravelled() {
        return this.distanceTravelled;
    }

    public String getName() {
        return this.name;
    }

    public char getSymbol() {
        return this.symbol;
    }

    public void goBackToStart() {
        this.distanceTravelled = 0;
    }

    public boolean hasFallen() {
        return this.isFallen;
    }

    public void moveForward() {
        this.distanceTravelled = this.distanceTravelled + 1;
    }

    public void setConfidence(double newConfidence) {
        this.confidence = newConfidence;
    }

    public void setSymbol(char newSymbol) {
        this.symbol = newSymbol;
    }

}
