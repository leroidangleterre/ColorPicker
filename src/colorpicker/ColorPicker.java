package colorpicker;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class represents a list of colors as close or as different as wished.
 *
 * @author arthu
 */
class ColorPicker {

    private ArrayList<Color> colorList;

    private ColorDisplay display;

    public ColorPicker(ColorDisplay newDisplay) {
        colorList = new ArrayList<>();
        display = newDisplay;
        display.setModel(this);
    }

    public void add(Color newColor) {
        if (!colorList.contains(newColor)) {
            colorList.add(newColor);
        }
        display.repaint();
    }

    public ArrayList<Color> getColors() {
        return (ArrayList<Color>) colorList.clone();
    }

    public void setColors(Color newColor, int i) {
        colorList.set(i, newColor);
    }

    public int getNbColors() {
        if (colorList == null) {
            return 0;
        } else {
            return colorList.size();
        }
    }

    public void createNewColor() {

        Random r = new Random();
        int red = r.nextInt(255);
        int green = r.nextInt(255);
        int blue = r.nextInt(255);

        while (red + green + blue < 252) {
            red++;
            green++;
            blue++;
        }
        add(new Color(red, green, blue));
    }

}
