import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class RaceGUI {
    public static void startRaceGUI() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Horse Race Simulator - GUI");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
            JLabel laneLabel = new JLabel("Number of Lanes (1-1000):");
            JTextField laneField = new JTextField(5);
            JLabel lengthLabel = new JLabel("Track Length (meters):");
            JTextField lengthField = new JTextField(5);
            JLabel shapeLabel = new JLabel("Track Shape:");
            JComboBox<String> shapeBox = new JComboBox<>(new String[]{"Oval", "Figure-eight", "Custom"});
            JLabel conditionLabel = new JLabel("Track Condition:");
            JComboBox<String> conditionBox = new JComboBox<>(new String[]{"Dry", "Muddy", "Icy"});
            JButton saveButton = new JButton("Save Settings");

            saveButton.addActionListener(e -> {
                try {
                    int lanes = Integer.parseInt(laneField.getText());
                    int length = Integer.parseInt(lengthField.getText());
                    String shape = (String) shapeBox.getSelectedItem();
                    String condition = (String) conditionBox.getSelectedItem();
                    if (lanes > 0 && lanes <= 1000 && length > 0) {
                        Race race = new Race(length, lanes);
                        race.setTrackShape(shape);
                        race.setTrackCondition(condition);
                        JOptionPane.showMessageDialog(frame, "Track set: " + lanes + " lanes, " + length + "m, " + shape + ", " + condition);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Enter valid lane count (1-1000) and positive length.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter numbers.");
                }
            });

            panel.add(laneLabel);
            panel.add(laneField);
            panel.add(lengthLabel);
            panel.add(lengthField);
            panel.add(shapeLabel);
            panel.add(shapeBox);
            panel.add(conditionLabel);
            panel.add(conditionBox);
            panel.add(new JLabel());
            panel.add(saveButton);

            frame.add(panel, BorderLayout.NORTH);
            frame.setVisible(true);
        });
    }

    public static void main(String[] args) {
        startRaceGUI();
    }
}