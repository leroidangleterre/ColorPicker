package colorpicker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;

/**
 * This utility provides a window where the colors are represented.
 *
 * @author arthu
 */
class ColorDisplay extends JPanel {

    private static final int PREFERRED_WIDTH = 800;
    private static final int PREFERRED_HEIGHT = 800;

    private JPanel panel;

    private double SQRT_3 = Math.sqrt(3);

    private boolean wasPortait;
    private boolean isPortrait;

    private int oldWidth = 0;

    private ColorPicker model;

    // Coordinates of the pixels associated with the primary colors.
    private int xRed, yRed, xGreen, yGreen, xBlue, yBlue;

    // Period in milliseconds for the equilibrium task
    private int equiPeriod;
    private Timer timer;

    /**
     * The colors are displayed as a triangle where each corner is a primary
     * color. Any other color will be placed at the position given by the
     * weighted coordinates ot those three points.
     */
    public ColorDisplay() {
        super();

        this.setVisible(true);
        setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
        wasPortait = false;
        isPortrait = false;
        model = null;
        equiPeriod = 1000;
        timer = null;
    }

    public void setModel(ColorPicker newModel) {
        if (model != newModel) {
            model = newModel;
            repaint();
        }
    }

    /**
     * Paint the color panel. Bugfix todo: find why sometimes when increasing
     * window width, the Graphics' method getBounds().width returns a value of 2
     * instead of current width.
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {

        int width = g.getClipBounds().width;
        int height = g.getClipBounds().height;

        if (width < 50) {
            // Do not paint, return.
            return;
        }

        // Actual color triangle
        int triangleSize;

        int xTab[];
        int yTab[];

        int margin = 10;

        checkPortrait(width, height);

        int offsetHoriz = 0, offsetVertic = 0;

        setCoordinatesForPortraitOrLandscape(height, width);

        yTab = new int[]{yRed, yGreen, yBlue};
        xTab = new int[]{xRed, xGreen, xBlue};

        g.setColor(Color.gray);
        g.fillRect(0, 0, width, height);

        g.setColor(Color.white);
        g.fillPolygon(xTab, yTab, 3);

//        paintColors(g);
        System.out.println("Painting " + model.getColors().size() + " colors.");
        for (Color c : model.getColors()) {

            Point coords = getPixelCoordinates(c);

//            System.out.println("(" + xPixel + ", " + yPixel + ");");
            // Paint a dot
            g.setColor(c);
            int size = 10;
            g.fillOval((int) (coords.x - size),
                    (int) (coords.y - size),
                    (int) (2 * size),
                    (int) (2 * size));
            g.setColor(Color.black);
            g.drawOval((int) (coords.x - size),
                    (int) (coords.y - size),
                    (int) (2 * size),
                    (int) (2 * size));
        }

        // Paint the links between each color and its closest neighbor
        for (Color c0 : model.getColors()) {
            Point coords0 = getPixelCoordinates(c0);
            Point closestNeighbor = getClosestNeighbor(c0);

            // Paint a line between coords0 and closestNeighbor
            g.setColor(c0);
            g.drawLine(coords0.x, coords0.y, closestNeighbor.x, closestNeighbor.y);

            int distance = getDistance(coords0, closestNeighbor);
            g.drawString(distance + "", (coords0.x + closestNeighbor.x) / 2, (coords0.y + closestNeighbor.y) / 2);
        }
    }

    private void setCoordinatesForPortraitOrLandscape(int height, int width) {
        int offsetVertic;
        int triangleSize;
        int offsetHoriz;
        if (isPortrait) {
            // Portrait
            offsetVertic = (height - width) / 2;
            triangleSize = width;
            int triangleHeight = (int) (width * SQRT_3 / 2);

            xRed = 0;
            xGreen = width / 2;
            xBlue = width;
            yRed = height / 2 + triangleHeight / 2;
            yGreen = height / 2 - triangleHeight / 2;
            yBlue = height / 2 + triangleHeight / 2;

        } else {
            // Landscape:
            offsetHoriz = (width - height) / 2;
            triangleSize = (int) (height * 2 / SQRT_3);
            yRed = height;
            yGreen = 0;
            yBlue = height;
            xRed = width / 2 - triangleSize / 2;
            xGreen = width / 2;
            xBlue = width / 2 + triangleSize / 2;
        }
    }

    public Point getPixelCoordinates(Color c) {
        // Get the RGB values
        double red = c.getRed();
        double green = c.getGreen();
        double blue = c.getBlue();

        double sum = red + green + blue;

        // Barycentric coordinates
        double lambdaRed = red / sum;
        double lambdaGreen = green / sum;
        double lambdaBlue = blue / sum;

        double xPixel = lambdaRed * xRed + lambdaGreen * xGreen + lambdaBlue * xBlue;
        double yPixel = lambdaRed * yRed + lambdaGreen * yGreen + lambdaBlue * yBlue;

        return new Point((int) xPixel, (int) yPixel);
    }

    /**
     * Check if the frame can display an equilateral triangle with margin on the
     * sides (landscape) or on the top and bottom (portrait).
     *
     * @param width
     * @param height
     */
    private void checkPortrait(int width, int height) {

        oldWidth = width;

        wasPortait = isPortrait;
        if ((double) height >= (double) width * SQRT_3 / 2) {
            isPortrait = true;
        } else {
            isPortrait = false;
        }
    }

    /**
     * Get the greatest value.
     *
     * @param a
     * @param b
     * @return
     */
    private double max(double a, double b) {
        return (a > b ? a : b);
    }

    /**
     * Get the distance beetween two points that represent colors on the
     * triangle
     *
     * @param coords0
     * @param coords1
     * @return
     */
    private int getDistance(Point coords0, Point coords1) {
        int dx = coords0.x - coords1.x;
        int dy = coords0.y - coords1.y;

        return (int) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Get the distance beetween two points that represent colors on the
     * triangle
     *
     */
    private int getDistance(Color c0, Color c1) {
        return getDistance(getPixelCoordinates(c0), getPixelCoordinates(c1));
    }

    public Point getClosestNeighbor(Color c0) {

        int distanceMax = Integer.MAX_VALUE;

        Point coords0 = getPixelCoordinates(c0);
        Point closestNeighbor = coords0;

        for (Color c1 : model.getColors()) {
            if (c1 != c0) {
                Point coords1 = getPixelCoordinates(c1);
                int distance = getDistance(coords0, coords1);

                if (distance < distanceMax) {
                    // Found new closer neighbor
                    distanceMax = distance;
                    closestNeighbor = coords1;
                }
            }
        }
        return closestNeighbor;
    }

    public void equilibrateColors() {
        System.out.println("Equilibrate");

        for (int i = 0; i < model.getColors().size(); i++) {
            Color c0 = model.getColors().get(i);

            if (c0.equals(Color.red) || c0.equals(Color.green) || c0.equals(Color.blue)) {
//                System.out.println("Do nothing for " + c0);
            } else {

                // Find closest neighbor
                int distance = Integer.MAX_VALUE;
                Color neighbor = c0;
                for (Color c1 : model.getColors()) {
                    int currentDistance = getDistance(getPixelCoordinates(c0), getPixelCoordinates(c1));
                    if (c1 != c0 && currentDistance < distance) {
                        // Found a new candidate for closest neighbor
                        distance = currentDistance;
                        neighbor = c1;
                    }
                }

                // Modify c0 so that it gets a little bit further away from c1.
                model.setColors(takeColorAway(c0, neighbor), i);
            }
        }
        repaint();
    }

    /**
     *
     * Modify
     *
     * @param c0 so as to increase its distance to
     * @param neighbor
     */
    private Color takeColorAway(Color c0, Color neighbor) {

        int currentDistance = getDistance(c0, neighbor);

        // All the different ways to slightly change c0
        ArrayList<Color> neighbors = new ArrayList<>();

        int red = c0.getRed();
        int green = c0.getGreen();
        int blue = c0.getBlue();

        if (red < 255) {
            neighbors.add(new Color(red + 1, green, blue));
        }
        if (green < 255) {
            neighbors.add(new Color(red, green + 1, blue));
        }
        if (blue < 255) {
            neighbors.add(new Color(red, green, blue + 1));
        }
        if (red > 0) {
            neighbors.add(new Color(red - 1, green, blue));
        }
        if (green > 0) {
            neighbors.add(new Color(red, green - 1, blue));
        }
        if (blue > 0) {
            neighbors.add(new Color(red, green, blue - 1));
        }

        // Find, among these new colors, the one that is farthest from c0;
        Color farthest = null;
        int newDistance = currentDistance;
        System.out.println("Taking " + c0 + " away from " + neighbor + "; distance is " + currentDistance);

        for (Color newCandidate : neighbors) {
            int newPotentialDistance = getDistance(newCandidate, neighbor);
            System.out.println("new potential distance: " + newPotentialDistance);
            if (newPotentialDistance > newDistance) {
                newDistance = getDistance(newCandidate, neighbor);
                farthest = newCandidate;
                System.out.println("    new Distance is " + newDistance);
            }
        }

        if (farthest != null) {
            // We found a way to take c0 away.
            return farthest;
        } else {
            // No change here.
            return c0;
        }

    }

    public void increasePeriod(boolean mustIncrease) {
        System.out.println("increasePeriod(" + mustIncrease + ");");
        if (mustIncrease) {
            equiPeriod = equiPeriod * 2;
        } else {
            if (equiPeriod >= 2) {
                equiPeriod = equiPeriod / 2;
            }
        }
        restartTimer();
    }

    private void restartTimer() {

        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                equilibrateColors();
            }
        };

        int delay = 0;
        timer.schedule(task, delay, equiPeriod);
    }
}
