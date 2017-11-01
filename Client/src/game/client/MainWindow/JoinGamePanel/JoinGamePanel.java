package game.client.MainWindow.JoinGamePanel;

import game.client.MainWindow.ColourChooserWindow.ColourChooserWindow;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;

public class JoinGamePanel extends JPanel {
    private EventListenerList listenerList = new EventListenerList();
    private JTextField nameInputBox = new JTextField();
    private JColorChooser colorChooser = new JColorChooser();
    private JButton joinGameButton = new JButton("Join game!");
    private JTextField ipText = new JTextField("127.0.0.1");
    private JTextField portText = new JTextField("56970");
    public JoinGamePanel(){
        //super(new GridLayout(0,1), false);
        super(new GridBagLayout());
        GridBagConstraints frameGBC = new GridBagConstraints();
        frameGBC.fill = GridBagConstraints.BOTH;
        JLabel mainGameTitleLabel = new JLabel("<html><h1>Tron Light Cycles</h1></html>");
        frameGBC.gridx=0;
        frameGBC.gridy=0;
        add(mainGameTitleLabel,frameGBC);
        JLabel nameInputLabel = new JLabel("Input your desired name:");
        frameGBC.gridy=1;
        add(nameInputLabel,frameGBC);
        frameGBC.gridx=1;
        frameGBC.ipadx = 150;
        add(nameInputBox,frameGBC);
        frameGBC.ipadx = 0;

        //Colour input
        JButton colourChooserButton = new JButton("Choose your colour");
        frameGBC.gridy=2;
        frameGBC.gridx = 0;
        frameGBC.gridwidth = 2;
        add(colourChooserButton,frameGBC);

        colourChooserButton.addActionListener((e) -> new ColourChooserWindow(colorChooser));

        //IP address
        JPanel serverIPPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        JLabel ipTitleLabel = new JLabel("Server address");
        JLabel ipLabel = new JLabel("IP address");
        JLabel portLabel = new JLabel("Port");

        gbc.gridx = 0;
        gbc.gridy = 0;
        serverIPPanel.add(ipTitleLabel,gbc);
        gbc.gridy = 1;
        serverIPPanel.add(ipLabel,gbc);
        gbc.gridx = 1;
        gbc.ipadx = 150;
        serverIPPanel.add(ipText,gbc);
        gbc.gridy = 2;
        serverIPPanel.add(portText,gbc);
        gbc.ipadx = 0;
        gbc.gridx = 0;
        serverIPPanel.add(portLabel,gbc);

        serverIPPanel.setPreferredSize(new Dimension(400,120));
        serverIPPanel.setBackground(Color.lightGray);

        frameGBC.gridy=3;
        add(serverIPPanel,frameGBC);

        joinGameButton.addActionListener((e) -> {
            String playerName = nameInputBox.getText();
            JoinGameEvent eventObject = new JoinGameEvent(this,playerName,colorChooser.getColor(),
                    ipText.getText(),Integer.parseInt(portText.getText()));
            raiseJoinGameEvent(eventObject);
        });

        frameGBC.gridy = 4;
        add(joinGameButton,frameGBC);
        setPreferredSize(new Dimension(400,350));
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
