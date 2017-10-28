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
    boolean gameStarted = false;

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

        lightCyclesGame.getGameGrid().addGridUpdatedListener((e) -> {
            raiseRePaintRequestListener(new EventObject(this));
            setFocusable(true);
            requestFocusInWindow();
            gameStarted = true;
        });

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
        requestFocusInWindow();
        //Initially paint the grid while waiting for players
        Dimension size = lightCyclesGame.getGameGrid().getGridDimensions();
        setPreferredSize(size);
        setSize(size);
        raiseRePaintRequestListener(new EventObject(this));
    }

    public void addJoinServerFailedListener(JoinServerFailedListener e){
        listeners.add(JoinServerFailedListener.class,e);
    }
    private void raiseJoinServerFailed(JoinServerFailedEvent e){
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
        RenderingHints defaultRH = graphics2D.getRenderingHints();
        RenderingHints enhancedRH = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHints(enhancedRH);
        //System.out.println("GamePanel paint");

        GameGrid gameGrid = lightCyclesGame.getGameGrid();

        if(gameGrid != null){
            System.out.println("Grid NOT null");
            Dimension gridDimensions = gameGrid.getGridDimensions();
            setPreferredSize(gridDimensions);
            setSize(gridDimensions);

            gameGrid.draw(graphics2D);

            if(!gameStarted) {
                graphics2D.setColor(new Color(200, 0, 0));
                drawCenteredString(graphics2D, "...Waiting for players...",
                        new Rectangle(0, 0, gridDimensions.width, gridDimensions.height),
                        new Font("Arial", Font.BOLD, 36));
            }
        }

        graphics2D.setRenderingHints(defaultRH);
    }
    public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        // Set the font
        g.setFont(font);
        // Draw the String
        g.drawString(text, x, y);
    }
}
