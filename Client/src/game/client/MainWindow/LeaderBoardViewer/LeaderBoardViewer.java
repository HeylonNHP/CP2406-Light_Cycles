package game.client.MainWindow.LeaderBoardViewer;

import game.client.HighScore;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;

public class LeaderBoardViewer extends JPanel {
    EventListenerList listeners = new EventListenerList();
    JTable leaderBoardTable;
    JButton gotoStartScreenButton = new JButton("Play again");
    public LeaderBoardViewer(ArrayList<HighScore> leaderBoard){
        super();
        //setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        gotoStartScreenButton.addActionListener((e) -> raiseReturnToStartScreenRequest());

        JPanel topPanel = new JPanel(new GridLayout(0,2));

        topPanel.add(new JLabel("Leader board scores:"));
        topPanel.add(gotoStartScreenButton);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(topPanel,gbc);

        gbc.gridy = 1;
        if(leaderBoard.size() > 0){
            leaderBoardTable = generateTable(leaderBoard);
            add(new JScrollPane(leaderBoardTable),gbc);
        }else{
            JLabel noScoresLabel = new JLabel(
                    "<html><h1>There are no scores <br>on the leader board yet!</h1></html>");
            noScoresLabel.setForeground(Color.red);
            add(noScoresLabel, gbc);
        }
    }

    private JTable generateTable(ArrayList<HighScore> leaderBoard){
        Object[] columnNames = {"Player name", "Player score"};
        Object[][] rowData = new Object[leaderBoard.size()][columnNames.length];

        int i = 0;
        for(HighScore highScore: leaderBoard){
            Object[] row = {highScore.getPlayerName(), highScore.getPlayerScore()};
            rowData[i] = row;
            i++;
        }
        return new JTable(rowData,columnNames);
    }

    public void addReturnToStartScreenRequestListener(ReturnToStartScreenListener e){
        listeners.add(ReturnToStartScreenListener.class,e);
    }
    public void raiseReturnToStartScreenRequest(){
        EventObject e = new EventObject(this);
        for(ReturnToStartScreenListener listener: listeners.getListeners(ReturnToStartScreenListener.class)){
            listener.returnToStartScreenRequested(e);
        }
    }
}
