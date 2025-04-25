import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import javax.swing.JPanel;

public class RaceTrackPanel extends JPanel {

    private Race race;

    public RaceTrackPanel() {
        setBackground(new Color(210, 180, 140));
    }

    public void setRace(Race race) {
        this.race = race;
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (race == null) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int laneCount = race.getLaneCount();
        int raceLength = Race.raceLength;

        if (laneCount <= 0 || raceLength <= 0 || panelWidth <= 60 || panelHeight <= 0) {
            return;
        }

        double laneHeight = (double) panelHeight / laneCount;
        double scaleX = (double) (panelWidth - 60) / raceLength;
        int trackStartX = 30;
        int trackEndX = panelWidth - 30;

        g2d.setColor(new Color(139, 69, 19));
        for (int i = 0; i <= laneCount; i++) {
            int y = (int) (i * laneHeight);
            if (y >= 0 && y <= panelHeight) {
                g2d.drawLine(trackStartX, y, trackEndX, y);
            }
        }

        g2d.setColor(Color.WHITE);
        g2d.drawLine(trackStartX, 0, trackStartX, panelHeight);
        g2d.drawLine(trackEndX, 0, trackEndX, panelHeight);

        Font horseFont = new Font("SansSerif", Font.BOLD, Math.max(10, (int) (laneHeight * 0.6)));
        g2d.setFont(horseFont);

        Horse[] horses = race.getHorseLanes();
        List<Integer> activeLanes = race.getLaneIndexList();

        if(horses == null || activeLanes == null) return;

        for (int laneNumber : activeLanes) {
            if (laneNumber < 1 || laneNumber >= horses.length) {
                continue;
            }
            Horse horse = horses[laneNumber];
            if (horse != null) {
                int laneIndex = laneNumber -1;
                int y = (int) ((laneIndex + 0.5) * laneHeight);
                int distance = horse.getDistanceTravelled();
                distance = Math.min(distance, raceLength);
                distance = Math.max(0, distance);
                int x = trackStartX + (int) (distance * scaleX);

                if (y > 0 && y < panelHeight + horseFont.getSize()) {
                    if (horse.hasFallen()) {
                        g2d.setColor(Color.RED);
                        g2d.drawString("X", x, y);
                    } else {
                        g2d.setColor(Color.BLACK);
                        g2d.drawString(String.valueOf(horse.getSymbol()), x, y);
                    }
                }
            }
        }
    }
}