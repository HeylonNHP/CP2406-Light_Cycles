package game.client.MainWindow.LeaderBoardViewer;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.HashMap;

public class LeaderBoardViewer extends JPanel {
    JTable leaderBoardTable;
    public LeaderBoardViewer(HashMap<String,Integer> leaderBoard){
        super();
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        add(new JLabel("Leader board scores:"));

        if(leaderBoard.size() > 0){
            leaderBoardTable = generateTable(leaderBoard);
            add(new JScrollPane(leaderBoardTable));
        }else{
            JLabel noScoresLabel = new JLabel(
                    "<html><h1>There are no scores <br>on the leader board yet!</h1></html>");
            noScoresLabel.setForeground(Color.red);
            add(noScoresLabel);
        }
    }

    private JTable generateTable(HashMap<String,Integer> leaderBoard){
        Object[] columnNames = {"Player name", "Player score"};
        Object[][] rowData = new Object[leaderBoard.size()][columnNames.length];

        int i = 0;
        for(String playerName:leaderBoard.keySet()){
            Object[] row = {playerName, leaderBoard.get(playerName)};
            rowData[i] = row;
            i++;
        }
        return new JTable(rowData,columnNames);
    }

    public static void main(String[] args){
        JFrame test = new JFrame("Test");
        test.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        test.setLayout(new FlowLayout());

        HashMap<String,Integer> leaderTest = new HashMap<>();
        leaderTest.put("Heylon", 2696);
        leaderTest.put("John", 440);
        leaderTest.put("Bob the builder", 710);

        HashMap<String,Integer> emptyLeaderTest = new HashMap<>();

        test.add(new LeaderBoardViewer(leaderTest));
        test.pack();
        test.setVisible(true);
    }
}
