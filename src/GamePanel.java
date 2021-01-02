import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{

    // Variables
    static final int screenWidth = 600;
    static final int screenHeight = 600;
    static final int unitSize = 25;
    static final int gameUnits = (screenWidth * screenHeight)/unitSize;
    static final int delay = 100;
    final int x[] = new int[gameUnits];
    final int y[] = new int[gameUnits];
    int bodyParts = 2;
    int applesEaten;
    int appleX;
    int appleY;
    char directions = 'D';
    boolean running = false;
    Timer timer;
    Random random;

    //Window Configuration
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
    }

    //Starts the game
    public void startGame (){
        newApple();
        running = true;
        timer = new Timer(delay,this);
        timer.start();
    }

    //Paints components
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    //Draws the components
    public void draw(Graphics g){

        if(running) {

            for (int i = 0; i < screenHeight / unitSize; i++) {
                g.drawLine(i * unitSize, 0, i * unitSize, screenHeight);
                g.drawLine(0, i * unitSize, screenWidth, i * unitSize);
                g.setColor(Color.BLUE);
            }

            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, unitSize, unitSize);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], unitSize, unitSize);
                } else {
                    g.setColor(new Color(38, 111, 32, 255));
                    g.fillRect(x[i], y[i], unitSize, unitSize);
                }
            }
            g.setColor(Color.red);
            g.setFont( new Font("Ink Free", Font.BOLD, 45));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (screenWidth - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    //Generates a new apple
    public void newApple(){
        appleX = random.nextInt((int)(screenWidth/unitSize))*unitSize;
        appleY = random.nextInt((int)(screenHeight/unitSize))*unitSize;
    }

    //Move units
    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (directions){
            case 'U':
                y[0] = y[0] - unitSize;
                break;

            case 'D':
                y[0] = y[0] + unitSize;
                break;

            case 'L':
                x[0] = x[0] - unitSize;
                break;

            case 'R':
                x[0] = x[0] + unitSize;
                break;
        }
    }

    //Checks the amount of apples the snake has eaten
    public void checkApple(){
        if((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    //Checks if the snake hits something
    public void checkCollisions(){
        //Collisions with the body
        for(int i = bodyParts; i > 0; i--){
            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }
        //Collisions with the left border
        if(x[0] < -1) { running = false;}

        //Collision with right border
        if(x[0] > screenWidth-1) { running = false;}

        //Collision with top border
        if(y[0] < -1) { running = false;}

        //Collision with bottom border
        if(y[0] > screenHeight-1) { running = false;}

        if(!running) { timer.stop(); }
    }

    public void gameOver(Graphics g){
        //Score
        g.setColor(Color.red);
        g.setFont( new Font("Ink Free", Font.BOLD, 45));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (screenWidth - metrics1.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());


        //Game Over Text
        g.setColor(Color.red);
        g.setFont( new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over!", (screenWidth - metrics2.stringWidth("Game Over!"))/2, screenHeight/2);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if(running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        //D-PAD Control
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(directions != 'R') {
                        directions = 'L';
                    }
                    break;

                case KeyEvent.VK_RIGHT:
                    if(directions != 'L') {
                        directions = 'R';
                    }
                    break;

                case KeyEvent.VK_UP:
                    if(directions != 'D') {
                        directions = 'U';
                    }
                    break;

                case KeyEvent.VK_DOWN:
                    if(directions != 'U') {
                        directions = 'D';
                    }
                    break;

                case KeyEvent.VK_ENTER:{
                    if(running == false){
                        startGame();
                        applesEaten = 0;
                        bodyParts = 2;
                    }
                    break;


                }

            }
        }
    }
}