import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Map;
import java.util.Random;
import javax.swing.*;

public class RaceGUI {

    private static Race race;
    private static final String[] RANDOM_NAMES = {"Thunder", "Horsy", "Unicorn", "Dash",
        "Usain Bolt"};
    private static final Random random = new Random();

    private JFrame frame;
    private RaceTrackPanel raceTrackPanel;
    private JButton startRaceButton;
    private JButton addHorseButton;
    private JButton saveTrackButton;
    private JTextField nameField;
    private JTextField confidenceField;
    private JTextField laneAssignField;
    private JComboBox<String> symbolBox;
    private JComboBox<String> breedBox;
    private JComboBox<String> colorBox;
    private JComboBox<String> equipmentBox;
    private JTextField laneField;
    private JTextField lengthField;


    private static void setRandomHorseDefaults(JTextField nameField, JTextField confidenceField) {
        String randomName = RANDOM_NAMES[random.nextInt(RANDOM_NAMES.length)];
        double randomConfidence = Math.round(random.nextDouble() * 100.0) / 100.0;
        nameField.setText(randomName);
        confidenceField.setText(String.format("%.1f", randomConfidence));
    }

    public void createAndShowGUI() {
        frame = new JFrame("Horse Race Simulator - GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        JPanel trackPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        trackPanel.setBorder(BorderFactory.createTitledBorder("Track Configuration"));
        JLabel laneLabel = new JLabel("Number of Lanes (1-1000):");
        laneField = new JTextField(10);
        laneField.setText("10");
        JLabel lengthLabel = new JLabel("Track Length (meters):");
        lengthField = new JTextField(10);
        lengthField.setText("20");
        JLabel shapeLabel = new JLabel("Track Shape:");
        JComboBox<String> shapeBox = new JComboBox<>(new String[]{"Oval", "Figure-eight", "Custom"});
        shapeBox.setPreferredSize(new Dimension(150, 25));
        JLabel conditionLabel = new JLabel("Track Condition:");
        JComboBox<String> conditionBox = new JComboBox<>(new String[]{"Dry", "Muddy", "Icy"});
        conditionBox.setPreferredSize(new Dimension(150, 25));
        saveTrackButton = new JButton("Save Track Settings");

        saveTrackButton.addActionListener(e -> {
            try {
                int lanes = Integer.parseInt(laneField.getText());
                int length = Integer.parseInt(lengthField.getText());
                String shape = (String) shapeBox.getSelectedItem();
                String condition = (String) conditionBox.getSelectedItem();

                if (lanes > 0 && lanes <= 1000 && length > 0) {
                    race = new Race(length, lanes);

                    race.setTrackShape(shape);
                    race.setTrackCondition(condition);
                    raceTrackPanel.setRace(race);
                    JOptionPane.showMessageDialog(frame,
                        "Track set: " + lanes + " lanes, " + length + "m, " + shape + ", "
                            + condition + ". Ready to add horses.");
                    addHorseButton.setEnabled(true);
                    startRaceButton.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(frame,
                        "Enter valid lane count (1-1000) and positive length.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter valid numbers for lanes and length.");
            }
        });

        trackPanel.add(laneLabel); trackPanel.add(laneField);
        trackPanel.add(lengthLabel); trackPanel.add(lengthField);
        trackPanel.add(shapeLabel); trackPanel.add(shapeBox);
        trackPanel.add(conditionLabel); trackPanel.add(conditionBox);
        trackPanel.add(new JLabel()); trackPanel.add(saveTrackButton);

        JPanel horsePanel = new JPanel(new GridLayout(8, 2, 5, 5));
        horsePanel.setBorder(BorderFactory.createTitledBorder("Horse Customization & Lane Assignment"));
        JLabel nameLabel = new JLabel("Horse Name:");
        nameField = new JTextField(10);
        JLabel confidenceLabel = new JLabel("Confidence (0-1):");
        confidenceField = new JTextField(10);
        JLabel breedLabel = new JLabel("Breed:");
        breedBox = new JComboBox<>(new String[]{"Thoroughbred", "Arabian", "Quarter Horse"});
        JLabel colorLabel = new JLabel("Coat Color:");
        colorBox = new JComboBox<>(new String[]{"Brown", "Black", "Grey", "White"});
        JLabel symbolLabel = new JLabel("Symbol:");
        String[] horseSymbols = {"♞", "♘", "H", "%", "$", "&"};
        symbolBox = new JComboBox<>(horseSymbols);
        JLabel equipmentLabel = new JLabel("Equipment:");
        equipmentBox = new JComboBox<>(new String[]{"Standard Saddle, Standard Horseshoe", "Light Saddle, Light Horseshoe"});
        JLabel laneAssignLabel = new JLabel("Assign to Lane:");
        laneAssignField = new JTextField(5);
        addHorseButton = new JButton("Add Horse to Lane");

        setRandomHorseDefaults(nameField, confidenceField);

        addHorseButton.addActionListener(e -> {
            if (race == null) {
                JOptionPane.showMessageDialog(frame, "Please save track settings first.");
                return;
            }
            try {
                String name = nameField.getText();
                double confidence = Double.parseDouble(confidenceField.getText());
                String breed = (String) breedBox.getSelectedItem();
                String color = (String) colorBox.getSelectedItem();
                String selectedSymbol = (String) symbolBox.getSelectedItem();
                if (selectedSymbol == null) { JOptionPane.showMessageDialog(frame, "Select symbol."); return; }
                char symbolChar = selectedSymbol.charAt(0);
                String equipment = (String) equipmentBox.getSelectedItem();
                String[] equipmentParts = equipment != null ? equipment.split(",\\s*") : new String[0];
                String saddle = equipmentParts.length > 0 ? equipmentParts[0] : "Standard Saddle";
                String horseshoe = equipmentParts.length > 1 ? equipmentParts[1] : "Standard Horseshoe";
                int targetLane = Integer.parseInt(laneAssignField.getText());

                if (name.isEmpty()) { JOptionPane.showMessageDialog(frame, "Enter name."); return; }
                if (confidence < 0 || confidence > 1) { JOptionPane.showMessageDialog(frame, "Conf must be 0-1."); return; }
                if (race == null || targetLane < 1 || targetLane > race.getLaneCount()) {
                    JOptionPane.showMessageDialog(frame, "Invalid Lane (1-" + (race != null ? race.getLaneCount() : "?") + ")."); return;
                }

                Horse horse = new Horse(symbolChar, name, confidence, breed, color, saddle, horseshoe);

                if (targetLane >=1 && targetLane <= race.getLaneCount()){
                    race.addHorse(horse, targetLane);
                    setRandomHorseDefaults(nameField, confidenceField);
                    laneAssignField.setText("");
                    symbolBox.setSelectedIndex(0);
                    raceTrackPanel.repaint();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid Lane Number.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid number for Confidence or Lane.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (ArrayIndexOutOfBoundsException ex) {
                JOptionPane.showMessageDialog(frame, "Error adding horse - check lane number: " + laneAssignField.getText(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        horsePanel.add(nameLabel); horsePanel.add(nameField);
        horsePanel.add(confidenceLabel); horsePanel.add(confidenceField);
        horsePanel.add(breedLabel); horsePanel.add(breedBox);
        horsePanel.add(colorLabel); horsePanel.add(colorBox);
        horsePanel.add(symbolLabel); horsePanel.add(symbolBox);
        horsePanel.add(equipmentLabel); horsePanel.add(equipmentBox);
        horsePanel.add(laneAssignLabel); horsePanel.add(laneAssignField);
        horsePanel.add(new JLabel()); horsePanel.add(addHorseButton);

        raceTrackPanel = new RaceTrackPanel();
        raceTrackPanel.setPreferredSize(new Dimension(600, 400));

        JPanel controlPanel = new JPanel();
        startRaceButton = new JButton("Start Race");

        startRaceButton.addActionListener(e -> {
            if (race == null) {
                JOptionPane.showMessageDialog(frame, "Please save track settings first.");
                return;
            }
            if (race.getAddedHorseCount() == 0) {
                JOptionPane.showMessageDialog(frame, "Please add at least one horse to a lane.");
                return;
            }

            race.setTrackPanel(this.raceTrackPanel);
            race.prepareForRaceStart();

            addHorseButton.setEnabled(false);
            startRaceButton.setEnabled(false);
            saveTrackButton.setEnabled(false);

            System.out.println("Starting race thread...");

            Thread raceThread = new Thread(() -> race.startRace());

            Thread completionWaiterThread = new Thread(() -> {
                try {
                    raceThread.join();

                    SwingUtilities.invokeLater(() -> {
                        displayRaceResult();
                    });
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    System.err.println("Completion waiter thread interrupted.");
                    SwingUtilities.invokeLater(() -> setRaceControlsEnabled(true));
                }
            });

            raceThread.start();
            completionWaiterThread.start();
        });
        controlPanel.add(startRaceButton);

        frame.add(trackPanel, BorderLayout.NORTH);
        frame.add(horsePanel, BorderLayout.WEST);
        frame.add(raceTrackPanel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        addHorseButton.setEnabled(false);
        startRaceButton.setEnabled(false);
        saveTrackButton.setEnabled(true);
    }

    private void displayRaceResult() {
        if (race == null) return;

        StringBuilder summary = new StringBuilder("=== Race Results ===\n");
        Map<Horse, Integer> results = race.getHorseResultSteps();
        long delay = 100;

        for (int laneNum : race.getLaneIndexList()) {
            if(laneNum >= 1 && laneNum < race.getHorseLanes().length) {
                Horse horse = race.getHorseLanes()[laneNum];
                if (horse != null) {
                    summary.append(String.format("Lane %d: %s ", laneNum, horse.getName()));

                    if (results.containsKey(horse)) {
                        int steps = results.get(horse);
                        if (steps > 0) {
                            double finishTime = (double) steps * delay / 1000.0;
                            double avgSpeed = (finishTime > 0) ? (double) Race.raceLength / finishTime : 0;
                            summary.append(String.format("[Finished: %.2fs | Avg Speed: %.2fm/s | Conf: %.1f]\n",
                                finishTime, avgSpeed, horse.getConfidence()));
                        } else {
                            summary.append(String.format("[Fell at step %d | Conf: %.1f]\n",
                                -steps, horse.getConfidence()));
                        }
                    } else {
                        summary.append(String.format("[Did Not Finish | Conf: %.1f]\n", horse.getConfidence()));
                    }
                }
            }
        }
        summary.append("===================\n");

        String winnerInfo = race.getWinnerName();
        boolean allFell = race.didAllFall();

        if (winnerInfo != null) {
            summary.append("Winner: ").append(winnerInfo).append("\n");
        } else if (allFell) {
            summary.append("All horses have fallen.\n");
        } else if (race.isFinished()){
            summary.append("Race finished without a clear winner or all falling.\n");
        }

        JTextArea resultArea = new JTextArea(summary.toString());
        resultArea.setEditable(false);
        resultArea.setCaretPosition(0);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setPreferredSize(new Dimension(450, 300));

        JOptionPane.showMessageDialog(frame, scrollPane, "Race Result", JOptionPane.INFORMATION_MESSAGE);

        setRaceControlsEnabled(true);
    }

    private void setRaceControlsEnabled(boolean enabled) {
        addHorseButton.setEnabled(enabled);
        startRaceButton.setEnabled(enabled);
        saveTrackButton.setEnabled(enabled);
    }

    public static void main(String[] args) {
        RaceGUI raceApp = new RaceGUI();
        SwingUtilities.invokeLater(raceApp::createAndShowGUI);
    }
}