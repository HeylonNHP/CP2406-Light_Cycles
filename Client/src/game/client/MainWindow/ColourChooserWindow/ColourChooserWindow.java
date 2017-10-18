package game.client.MainWindow.ColourChooserWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class ColourChooserWindow extends JFrame {
    public ColourChooserWindow(JColorChooser colorChooser){
        super();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        add(colorChooser);

        JButton closeButton = new JButton("Close");
        add(closeButton);

        closeButton.addActionListener((e) ->{
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });

        setVisible(true);
        pack();
    }
}
