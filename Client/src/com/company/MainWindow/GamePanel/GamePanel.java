package com.company.MainWindow.GamePanel;

import com.company.LightCyclesGame;
import com.company.VisibleGameObjects.GameGrid;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.util.EventObject;

public class GamePanel extends JPanel {
    EventListenerList listeners = new EventListenerList();
    LightCyclesGame lightCyclesGame;

    public GamePanel(LightCyclesGame gameObject){
        super(true);
        lightCyclesGame = gameObject;
    }

    public void joinServer(){

        try{
            lightCyclesGame.joinServer();
        }catch (Exception e){
            /*Tried to join the server and something went wrong.*/
            raiseJoinServerFailed(new JoinServerFailedEvent(this,e.getMessage()));
        }

        raiseRePaintRequestListener(new EventObject(this));
    }

    public void addJoinServerFailedListener(JoinServerFailedListener e){
        listeners.add(JoinServerFailedListener.class,e);
    }
    private void raiseJoinServerFailed(JoinServerFailedEvent e){
        JOptionPane.showMessageDialog(this, e.getConnectionFailureReason());
        for(JoinServerFailedListener listener: listeners.getListeners(JoinServerFailedListener.class)){
            listener.joinServerFailed(e);
        }
    }

    public void addRePaintRequestListener(RePaintMeEventListener e){
        listeners.add(RePaintMeEventListener.class,e);
    }
    private void raiseRePaintRequestListener(EventObject e){
        for(RePaintMeEventListener listener: listeners.getListeners(RePaintMeEventListener.class)){
            listener.rePaintRequest(e);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D)g;
        System.out.println("GamePanel paint");

        GameGrid gameGrid = lightCyclesGame.getGameGrid();

        System.out.println((gameGrid == null)? "It's null":"It's not null");

        if(gameGrid != null){
            Dimension gridDimensions = gameGrid.getGridDimensions();
            setPreferredSize(gridDimensions);
            System.out.println(String.format(
                    "Width: %s Height: %s", gridDimensions.width, gridDimensions.height
            ));
            graphics2D.setColor(Color.blue);
            graphics2D.fillRect(0,0,gridDimensions.width,gridDimensions.height);

        }

    }
}
