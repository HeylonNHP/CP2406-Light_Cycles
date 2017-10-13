package com.company.MainWindow.GamePanel;

import com.company.LightCyclesGame;

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
        //Testing
        add(new JButton("Test"));
        raiseRePaintRequestListener(new EventObject(this));

        try{
            lightCyclesGame.joinServer();
        }catch (Exception e){
            /*Tried to join the server and something went wrong.*/
            raiseJoinServerFailed(new JoinServerFailedEvent(this,e.getMessage()));
        }
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
        System.out.println("GamePanel paint");
        g.setColor(Color.cyan);
        g.draw3DRect(0,0,50,50,true);
    }
}
