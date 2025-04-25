import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class RaceGUI {

    private static Race race;
    private static final String[] RANDOM_NAMES = {"Thunder", "Horsy", "Unicorn", "Dash",
        "Usain Bolt"};
    private static final Random random = new Random();

    private static void setRandomHorseDefaults(JTextField nameField, JTextField confidenceField) {
        String randomName = RANDOM_NAMES[random.nextInt(RANDOM_NAMES.length)];
        double randomConfidence = Math.round(random.nextDouble() * 100.0) / 100.0;
        nameField.setText(randomName);
        confidenceField.setText(String.format("%.1f", randomConfidence));
    }

    public static void startRaceGUI() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Horse Race Simulator - GUI");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 700);
            frame.setLocationRelativeTo(null);

            JPanel trackPanel = new JPanel(new GridLayout(5, 2, 10, 10));
            trackPanel.setBorder(BorderFactory.createTitledBorder("Track Configuration"));
            JLabel laneLabel = new JLabel("Number of Lanes (1-1000):");
            JTextField laneField = new JTextField(10);
            laneField.setText("10");

            JLabel lengthLabel = new JLabel("Track Length (meters):");
            JTextField lengthField = new JTextField(10);
            lengthField.setText("20");

            JLabel shapeLabel = new JLabel("Track Shape:");
            JComboBox<String> shapeBox = new JComboBox<>(
                new String[]{"Oval", "Figure-eight", "Custom"});
            shapeBox.setPreferredSize(new Dimension(150, 25));

            JLabel conditionLabel = new JLabel("Track Condition:");
            JComboBox<String> conditionBox = new JComboBox<>(new String[]{"Dry", "Muddy", "Icy"});
            conditionBox.setPreferredSize(new Dimension(150, 25));

            JButton saveTrackButton = new JButton("Save Track Settings");

            saveTrackButton.addActionListener(e -> {
                int lanes = Integer.parseInt(laneField.getText());
                int length = Integer.parseInt(lengthField.getText());
                String shape = (String) shapeBox.getSelectedItem();
                String condition = (String) conditionBox.getSelectedItem();

                if (lanes > 0 && lanes <= 1000 && length > 0) {
                    race = new Race(length, lanes);
                    race.setTrackShape(shape);
                    race.setTrackCondition(condition);
                    JOptionPane.showMessageDialog(frame,
                        "Track set: " + lanes + " lanes, " + length + "m, " + shape + ", "
                            + condition + ". Ready to add horses.");
                } else {
                    JOptionPane.showMessageDialog(frame,
                        "Enter valid lane count (1-1000) and positive length.");
                }

            });

            trackPanel.add(laneLabel);
            trackPanel.add(laneField);
            trackPanel.add(lengthLabel);
            trackPanel.add(lengthField);
            trackPanel.add(shapeLabel);
            trackPanel.add(shapeBox);
            trackPanel.add(conditionLabel);
            trackPanel.add(conditionBox);
            trackPanel.add(new JLabel());
            trackPanel.add(saveTrackButton);

            JPanel horsePanel = new JPanel(new GridLayout(8, 2, 10, 10));
            horsePanel.setBorder(
                BorderFactory.createTitledBorder("Horse Customization & Lane Assignment"));

            JLabel nameLabel = new JLabel("Horse Name:");
            JTextField nameField = new JTextField(10);
            horsePanel.add(nameLabel);
            horsePanel.add(nameField);

            JLabel confidenceLabel = new JLabel("Confidence (0-1):");
            JTextField confidenceField = new JTextField(10);
            horsePanel.add(confidenceLabel);
            horsePanel.add(confidenceField);

            setRandomHorseDefaults(nameField, confidenceField);

            JLabel breedLabel = new JLabel("Breed:");
            JComboBox<String> breedBox = new JComboBox<>(
                new String[]{"Thoroughbred", "Arabian", "Quarter Horse"});
            breedBox.setPreferredSize(new Dimension(150, 25));
            horsePanel.add(breedLabel);
            horsePanel.add(breedBox);

            JLabel colorLabel = new JLabel("Coat Color:");
            JComboBox<String> colorBox = new JComboBox<>(
                new String[]{"Brown", "Black", "Grey", "White"});
            colorBox.setPreferredSize(new Dimension(150, 25));
            horsePanel.add(colorLabel);
            horsePanel.add(colorBox);

            JLabel symbolLabel = new JLabel("Symbol:");
            String[] horseSymbols = {"♞", "♘", "#", "@", "*", "%", "$", "+", "&"};
            JComboBox<String> symbolBox = new JComboBox<>(horseSymbols);
            symbolBox.setPreferredSize(new Dimension(150, 25));
            horsePanel.add(symbolLabel);
            horsePanel.add(symbolBox);

            JLabel equipmentLabel = new JLabel("Equipment:");
            JComboBox<String> equipmentBox = new JComboBox<>(
                new String[]{"Standard Saddle, Standard Horseshoe",
                    "Light Saddle, Light Horseshoe"});
            equipmentBox.setPreferredSize(new Dimension(150, 25));
            horsePanel.add(equipmentLabel);
            horsePanel.add(equipmentBox);

            JLabel laneAssignLabel = new JLabel("Assign to Lane:");
            JTextField laneAssignField = new JTextField(5);
            horsePanel.add(laneAssignLabel);
            horsePanel.add(laneAssignField);

            JButton addHorseButton = new JButton("Add Horse to Lane");
            horsePanel.add(new JLabel());
            horsePanel.add(addHorseButton);

            addHorseButton.addActionListener(e -> {
                if (race == null) {
                    JOptionPane.showMessageDialog(frame, "Please save track settings first.");
                    return;
                }

                String name = nameField.getText();
                double confidence = Double.parseDouble(confidenceField.getText());
                String breed = (String) breedBox.getSelectedItem();
                String color = (String) colorBox.getSelectedItem();
                String selectedSymbol = (String) symbolBox.getSelectedItem();
                if (selectedSymbol == null) {
                    JOptionPane.showMessageDialog(frame, "Please select a symbol.");
                    return;
                }
                char symbolChar = selectedSymbol.charAt(0);

                String equipment = (String) equipmentBox.getSelectedItem();
                String[] equipmentParts =
                    equipment != null ? equipment.split(",\\s*") : new String[0];
                String saddle = equipmentParts.length > 0 ? equipmentParts[0] : "Standard Saddle";
                String horseshoe =
                    equipmentParts.length > 1 ? equipmentParts[1] : "Standard Horseshoe";

                int targetLane = Integer.parseInt(laneAssignField.getText());

                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter a horse name.");
                    return;
                }
                if (confidence < 0 || confidence > 1) {
                    JOptionPane.showMessageDialog(frame, "Confidence must be between 0.0 and 1.0.");
                    return;
                }
                if (targetLane < 1 || targetLane > race.getLaneCount()) {
                    JOptionPane.showMessageDialog(frame,
                        "Invalid Lane Number. Must be between 1 and " + race.getLaneCount() + ".");
                    return;
                }

                Horse horse = new Horse(symbolChar, name, confidence, breed, color, saddle,
                    horseshoe);

                race.addHorse(horse, targetLane);

                JOptionPane.showMessageDialog(frame,
                    "Added " + name + " (" + symbolChar + ") to Lane " + targetLane + ".");
                setRandomHorseDefaults(nameField, confidenceField);
                laneAssignField.setText("");
                symbolBox.setSelectedIndex(0);

            });

            JPanel controlPanel = new JPanel();
            JButton startRaceButton = new JButton("Start Race");

            startRaceButton.addActionListener(e -> {
                if (race == null) {
                    JOptionPane.showMessageDialog(frame, "Please save track settings first.");
                    return;
                }

                if (race.getAddedHorseCount() == 0) {
                    JOptionPane.showMessageDialog(frame,
                        "Please add at least one horse to a lane.");
                    return;
                }

                System.out.println("Starting race...");
                new Thread(() -> race.startRace()).start();

            });
            controlPanel.add(startRaceButton);

            frame.add(trackPanel, BorderLayout.NORTH);
            frame.add(horsePanel, BorderLayout.CENTER);
            frame.add(controlPanel, BorderLayout.SOUTH);

            frame.setMinimumSize(new Dimension(600, 650));
            frame.setVisible(true);
        });
    }

    public static void main(String[] args) {
        startRaceGUI();
    }
}