package com.company.MainWindow.GamePanel;

import com.company.LightCyclesGame;

import javax.swing.*;
import javax.swing.event.EventListenerList;

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
}
