import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.util.List;
import javax.swing.JPanel;

public class RaceTrackPanel extends JPanel {

    private Race race;
    private final Color dryColor = new Color(210, 180, 140);
    private final Color muddyColor = new Color(160, 82, 45);
    private final Color icyColor = new Color(224, 255, 255);

    public RaceTrackPanel() {}

    public void setRace(Race race) {
        this.race = race;
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (race == null) { return; }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int laneCount = race.getLaneCount();
        int raceLength = Race.raceLength;
        String shape = race.getTrackShape();
        String condition = race.getTrackCondition();

        if (laneCount <= 0 || raceLength <= 0 || panelWidth <= 60 || panelHeight <= 0) { return; }

        Color bgColor;
        if ("Muddy".equals(condition)) bgColor = muddyColor;
        else if ("Icy".equals(condition)) bgColor = icyColor;
        else bgColor = dryColor;
        g2d.setColor(bgColor);
        g2d.fillRect(0, 0, panelWidth, panelHeight);

        int trackStartX = 30;
        int trackEndX = panelWidth - 30;
        int trackWidth = trackEndX - trackStartX;
        int trackVisualY = 5;
        int trackVisualHeight = panelHeight - 2 * trackVisualY;

        Color trackLineColor = new Color(139, 69, 19);
        g2d.setColor(trackLineColor);

        double laneFactor = 1.0 / laneCount;

        if ("Oval".equals(shape)) {
            for(int i=0; i < laneCount; i++) {
                double ratio = (double)i * laneFactor;
                double insetX = trackWidth * 0.05 * ratio;
                double insetY = trackVisualHeight * 0.1 * ratio;
                g2d.draw(new Ellipse2D.Double(trackStartX + insetX, trackVisualY + insetY, trackWidth - 2*insetX, trackVisualHeight - 2*insetY));
            }
        } else if ("Figure-eight".equals(shape)) {
            int ovalWidth = trackWidth / 2;
            for(int i=0; i < laneCount; i++) {
                double ratio = (double)i * laneFactor;
                double currentOvalWidth = ovalWidth * (1 - ratio * 0.2);
                double currentOvalHeight = trackVisualHeight * (1 - ratio * 0.2);
                double yOffset = (trackVisualHeight - currentOvalHeight) / 2.0;
                double xOffset = (ovalWidth - currentOvalWidth) / 2.0;
                g2d.draw(new Ellipse2D.Double(trackStartX + xOffset, trackVisualY + yOffset, currentOvalWidth, currentOvalHeight));
                g2d.draw(new Ellipse2D.Double(trackStartX + ovalWidth + xOffset, trackVisualY + yOffset, currentOvalWidth, currentOvalHeight));
            }
        } else {
            double laneHeight = (double) panelHeight / laneCount;
            for (int i = 0; i <= laneCount; i++) {
                int y = (int) (i * laneHeight);
                if (y >= 0 && y <= panelHeight) {
                    g2d.drawLine(trackStartX, y, trackEndX, y);
                }
            }
        }

        g2d.setColor(Color.WHITE);
        g2d.drawLine(trackStartX, 0, trackStartX, panelHeight);
        g2d.drawLine(trackEndX, 0, trackEndX, panelHeight);


        Font originalFont = g2d.getFont();
        Font horseFont = originalFont;
        double laneHeight = (double) panelHeight / laneCount;

        Horse[] horses = race.getHorseLanes();
        List<Integer> activeLanes = race.getLaneIndexList();
        double scaleX = (double) trackWidth / raceLength;

        double centerX = trackStartX + trackWidth / 2.0;
        double centerY = trackVisualY + trackVisualHeight / 2.0;
        double radiusX = trackWidth / 2.0;
        double radiusY = trackVisualHeight / 2.0;
        double laneSpacingFactor = 0.8 / laneCount;

        if(horses == null || activeLanes == null) return;

        for (int laneNumber : activeLanes) {
            if (laneNumber >= 1 && laneNumber < horses.length) {
                Horse horse = horses[laneNumber];
                if (horse != null) {
                    int distance = horse.getDistanceTravelled();
                    distance = Math.min(distance, raceLength);
                    distance = Math.max(0, distance);

                    int x = 0;
                    int y = 0;
                    int laneIndex = laneNumber - 1;

                    if ("Oval".equals(shape)) {
                        double laneRadiusFactor = 1.0 - (laneIndex + 0.5) * laneSpacingFactor;
                        double laneRadiusX = Math.max(1.0, radiusX * laneRadiusFactor);
                        double laneRadiusY = Math.max(1.0, radiusY * laneRadiusFactor);
                        double angle = ((double) distance / raceLength) * 2.0 * Math.PI - (Math.PI / 2.0);
                        x = (int) (centerX + laneRadiusX * Math.cos(angle));
                        y = (int) (centerY + laneRadiusY * Math.sin(angle));

                    } else if ("Figure-eight".equals(shape)) {
                        double laneRadiusFactor = 1.0 - (laneIndex + 0.5) * laneSpacingFactor;
                        double laneAX = Math.max(1.0, (radiusX / 2.0) * laneRadiusFactor);
                        double laneAY = Math.max(1.0, radiusY * laneRadiusFactor);
                        double t = ((double) distance / raceLength) * 2.0 * Math.PI;
                        double figureEightCenterX = trackStartX + radiusX;
                        x = (int) (figureEightCenterX + laneAX * Math.cos(t));
                        y = (int) (centerY + laneAY * Math.sin(2 * t) / 2.0);

                    } else {
                        x = trackStartX + (int) (distance * scaleX);
                        y = (int) ((laneIndex + 0.5) * laneHeight);
                    }

                    int fontSize = Math.max(10, (int) (laneHeight * 0.6));
                    horseFont = new Font(originalFont.getName(), Font.BOLD, fontSize);
                    g2d.setFont(horseFont);

                    if (y > 0 && y < panelHeight + fontSize) {
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
        g2d.setFont(originalFont);
    }
}