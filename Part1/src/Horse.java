
/**
 * Write a description of class Horse here.
 *
 * @author sunny_118
 * @version 20250416
 */
public class Horse {
    //Fields of class Horse
    private String name;
    private double confidence;
    private int distanceTravelled;
    private char symbol;
    private boolean isFallen;
    //Constructor of class Horse

    /**
     * Constructor for objects of class Horse
     */
    public Horse(char a, String b, double c) {
        this.symbol = a;
        this.name = b;
        this.setConfidence(c);
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
        this.isFallen = false;
    }

    public boolean hasFallen() {
        return this.isFallen;
    }

    public void moveForward() {
        this.distanceTravelled = this.distanceTravelled + 1;
    }

    public void setConfidence(double newConfidence) {
        if (newConfidence >= 0 && newConfidence <= 1) {
            this.confidence = newConfidence;
        }
    }

    public void setSymbol(char newSymbol) {
        this.symbol = newSymbol;
    }

    public void move(String trackShape, String trackCondition) {
        if (!this.hasFallen()) {
            double moveProbability = this.getConfidence();
            double fallProbability = 0.1 * this.getConfidence() * this.getConfidence();

            if (trackShape.equals("Figure-eight")) {
                moveProbability *= 0.9;
            } else if (trackShape.equals("Custom")) {
                moveProbability *= 0.85;
            }

            if (trackCondition.equals("Muddy")) {
                moveProbability *= 0.8;
                fallProbability *= 1.2;
            } else if (trackCondition.equals("Icy")) {
                moveProbability *= 0.7;
                fallProbability *= 1.5;
                if (this.hasFallen()) {
                    this.confidence = Math.round((this.confidence - 0.05) * 100.0) / 100.0;
                }
            }

            if (Math.random() < moveProbability) {
                this.moveForward();
                if (this.raceWonBy()) {
                    this.confidence = Math.round((this.confidence + 0.1) * 100.0) / 100.0;
                }
            }

            //the probability that the horse will fall is very small (max is 0.1)
            //but will also will depends exponentially on confidence
            //so if you double the confidence, the probability that it will fall is *2
            if (Math.random() < fallProbability) {
                this.fall();
                this.confidence = Math.round((this.confidence - 0.1) * 100.0) / 100.0;
            }
        }
    }

    public boolean raceWonBy() {
        return this.distanceTravelled == Race.raceLength;
    }

}
