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
    private String breed;
    private String coatColor;
    private String saddle;
    private String horseshoe;

    /**
     * Constructor for objects of class Horse
     */
    public Horse(char a, String b, double c) {
        this.symbol = a;
        this.name = b;
        this.setConfidence(c);
        this.breed = "";
        this.coatColor = "";
        this.saddle = "";
        this.horseshoe = "";
    }

    public Horse(char a, String b, double c, String breed, String coatColor, String saddle, String horseshoe) {
        this.symbol = a;
        this.name = b;
        this.setConfidence(c);
        this.breed = breed;
        this.coatColor = coatColor;
        this.saddle = saddle;
        this.horseshoe = horseshoe;
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

    public String getBreed() {
        return breed;
    }

    public String getCoatColor() {
        return coatColor;
    }

    public String getSaddle() {
        return saddle;
    }

    public String getHorseshoe() {
        return horseshoe;
    }

    public void move(String trackShape, String trackCondition) {
        if (!this.hasFallen()) {
            double moveProbability = this.getConfidence();
            double fallProbability = 0.1 * this.getConfidence() * this.getConfidence();

            // Track shape effects
            if (trackShape.equals("Figure-eight")) {
                moveProbability *= 0.9;
            } else if (trackShape.equals("Custom")) {
                moveProbability *= 0.85;
            }

            // Track condition effects
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

            // Breed effects
            if (breed.equals("Thoroughbred")) {
                moveProbability *= 1.1; // Faster
                fallProbability *= 1.1; // Slightly riskier
            } else if (breed.equals("Arabian")) {
                moveProbability *= 0.95; // Slightly slower
                fallProbability *= 0.9; // More stable
            } else if (breed.equals("Quarter Horse")) {
                moveProbability *= 1.05; // Balanced speed
            }

            // Equipment effects
            if (saddle.equals("Light Saddle")) {
                moveProbability *= 1.05; // Slightly faster
            }
            if (horseshoe.equals("Light Horseshoe")) {
                fallProbability *= 0.95; // Slightly safer
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