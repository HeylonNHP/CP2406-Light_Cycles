package com.company.MainWindow.JoinGamePanel;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;

public class JoinGamePanel extends JPanel {
    EventListenerList listenerList = new EventListenerList();
    JTextField nameInputBox = new JTextField();
    JButton joinGameButton = new JButton("Join game!");
    public JoinGamePanel(){
        super(new GridLayout(0,1), false);
        JLabel mainGameTitleLabel = new JLabel("<html><h1>Tron Light Cycles</h1></html>");
        add(mainGameTitleLabel);
        JLabel nameInputLabel = new JLabel("Input your desired name:");
        add(nameInputLabel);
        add(nameInputBox);

        joinGameButton.addActionListener((e) -> {
            String playerName = nameInputBox.getText();
            JoinGameEvent eventObject = new JoinGameEvent(this,playerName);
            raiseJoinGameEvent(eventObject);
        });

        add(joinGameButton);
    }

    public void addJoinGameListener(JoinGameEventListener e){
        listenerList.add(JoinGameEventListener.class,e);
    }
    private void raiseJoinGameEvent(JoinGameEvent eventObject){
        for(JoinGameEventListener listener: listenerList.getListeners(JoinGameEventListener.class)){
            listener.joinGameRequested(eventObject);
            System.out.println("JoinTest");
        }
    }
}
