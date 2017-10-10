package com.company.MainWindow;

import com.company.LightCyclesGame;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    LightCyclesGame lightCyclesGame;
    public GamePanel(LightCyclesGame gameObject){
        super(true);
        lightCyclesGame = gameObject;
        try{
            lightCyclesGame.joinServer();
        }catch (Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
            return;
        }

    }
}
