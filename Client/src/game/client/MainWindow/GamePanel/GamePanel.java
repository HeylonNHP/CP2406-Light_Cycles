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
    private EventListenerList listeners = new EventListenerList();
    private LightCyclesGame lightCyclesGame;
    private boolean gameStarted = false;

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
            return;
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

        Font titleFont = new Font("Arial", Font.BOLD, 36);
        Font largeTextFont = new Font(titleFont.getFamily(),titleFont.getStyle(),20);

        GameGrid gameGrid = lightCyclesGame.getGameGrid();

        if(gameGrid != null){
            //System.out.println("Grid NOT null");
            Dimension gridDimensions = gameGrid.getGridDimensions();
            setPreferredSize(gridDimensions);
            setSize(gridDimensions);

            gameGrid.draw(graphics2D);

            if(!gameStarted) {

                graphics2D.setColor(new Color(200, 0, 0));
                drawCenteredString(graphics2D, "...Waiting for players...",
                        new Rectangle(0, 0, gridDimensions.width, 350),
                        titleFont);

                drawHorizontalCentredString(graphics2D,"Objective:",titleFont,0,50,gridDimensions.width);

                drawHorizontalCentredString(graphics2D,"To be the last player remaining on the grid.",
                        largeTextFont,0,86,gridDimensions.width);

                int keyGraphicsYpos = 300;
                int keyGraphicsXpos = gridDimensions.width/2;

                drawKeyboardKey(graphics2D,keyGraphicsXpos-65,keyGraphicsYpos,'◄');
                drawKeyboardKey(graphics2D,keyGraphicsXpos+65,keyGraphicsYpos,'►');
                drawKeyboardKey(graphics2D,keyGraphicsXpos,keyGraphicsYpos-50,'▲');
                drawKeyboardKey(graphics2D,keyGraphicsXpos,keyGraphicsYpos,'▼');
                drawKeyboardKey(graphics2D,keyGraphicsXpos,keyGraphicsYpos+100,' ');

                graphics2D.setColor(Color.orange);
                graphics2D.setFont(largeTextFont);

                graphics2D.drawString("Turn left", keyGraphicsXpos-180,keyGraphicsYpos);
                graphics2D.drawString("Turn right", keyGraphicsXpos+100,keyGraphicsYpos);
                drawHorizontalCentredString(graphics2D,"Move forward quickly", largeTextFont,
                        0,keyGraphicsYpos-80,gridDimensions.width);
                drawHorizontalCentredString(graphics2D,"Move forward slowly", largeTextFont,
                        0,keyGraphicsYpos+50,gridDimensions.width);
                drawHorizontalCentredString(graphics2D,"Toggle jet wall", largeTextFont,
                        0,keyGraphicsYpos+150,gridDimensions.width);
            }
        }

        graphics2D.setRenderingHints(defaultRH);
    }
    private static void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
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

    private static void drawHorizontalCentredString(Graphics g, String text, Font font, int x, int y, int width){
        Graphics graphics = g.create();
        graphics.setFont(font);
        FontMetrics fm = graphics.getFontMetrics(font);

        int centreX = x+(width-fm.stringWidth(text))/2;
        graphics.drawString(text,centreX,y);
        graphics.dispose();
    }

    enum KeySize {Normal, Space}

    private static void drawKeyboardKey(Graphics g, int x, int y, char keyValue){
        Graphics graphics = g.create();

        KeySize keySize;
        String keyString;

        switch (keyValue){
            case ' ':
                keySize = KeySize.Space;
                keyString = "Space";
                break;
            default:
                keySize = KeySize.Normal;
                keyString = String.valueOf(keyValue);
        }

        int[] xpoints = {20,-20,-30,30};
        int[] ypoints = {-20,-20,20,20};

        switch (keySize){
            case Normal:
                xpoints = new int[]{20,-20,-30,30};
                break;
            case Space:
                xpoints = new int[]{110,-110,-120,120};
                break;
        }

        for(int i = 0; i<xpoints.length;++i){
            xpoints[i] += x;
        }
        for (int i=0;i<ypoints.length;++i){
            ypoints[i] += y;
        }
        Polygon key = new Polygon(xpoints,ypoints,xpoints.length);
        graphics.fillPolygon(key);

        Font keyFont = new Font("Arial",Font.BOLD,30);

        graphics.setColor(Color.white);

        drawHorizontalCentredString(graphics,keyString,keyFont,
                x-30,y+10,60);

        graphics.dispose();
    }
}
