package game.client.MainWindow.GamePanel;

import game.client.LightCyclesGame;
import game.client.VisibleGameObjects.GameGrid;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.EventObject;

public class GamePanel extends JPanel{
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

        //TODO: Use event handler on lightcyclesgame to update this
        javax.swing.Timer drawTimer = new javax.swing.Timer(50,(actionEvent) ->{
            raiseRePaintRequestListener(new EventObject(this));
        });
        drawTimer.start();

        //Begin listening for user input
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                //Left arrow pressed
                if(e.getKeyCode() == 37){
                    lightCyclesGame.turnLeft();
                }

                //Right arrow pressed
                if(e.getKeyCode() == 39){
                    lightCyclesGame.turnRight();
                }

                //Up arrow pressed
                if(e.getKeyCode() == 38){
                    lightCyclesGame.beginMovingQuickly();
                }

                //Down arrow pressed
                if(e.getKeyCode() == 40){
                    lightCyclesGame.beginMovingSlowly();
                }

                //Spacebar pressed
                if(e.getKeyCode() == 32){
                    lightCyclesGame.toggleJetwall();
                }
            }
        });
        //If you don't set this, the JPanel won't be focused so the KeyListener won't work
        setFocusable(true);
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
        //System.out.println("GamePanel paint");

        GameGrid gameGrid = lightCyclesGame.getGameGrid();

        //System.out.println((gameGrid == null)? "It's null":"It's not null");

        if(gameGrid != null){
            Dimension gridDimensions = gameGrid.getGridDimensions();
            setPreferredSize(gridDimensions);

            gameGrid.draw(graphics2D);
        }

    }
}
