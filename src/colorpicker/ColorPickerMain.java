/* Generate any number of colors such that the distance between any two colors
*  is as large as possible.
 */
package colorpicker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author arthurmanoha
 */
public class ColorPickerMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setSize(new Dimension(1000, 800));

        ColorDisplay display = new ColorDisplay();
        frame.setLayout(new BorderLayout());

        frame.setContentPane(display);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ColorPicker model = new ColorPicker(display);

        JButton buttonEquilibrate = new JButton("Equilibrate");
        buttonEquilibrate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                display.equilibrateColors();
            }
        });
        frame.add(buttonEquilibrate, BorderLayout.SOUTH);

        KeyListener keyListener = new MyKeyListener(display);
        frame.addKeyListener(keyListener);
        display.addKeyListener(keyListener);

        int nbColors = 100;

        // Add the first three colors;
        model.add(Color.blue);
        model.add(Color.red);
        model.add(Color.green);

        while (model.getNbColors() < nbColors) {
            Random r = new Random();
            model.add(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
        }

    }

}
