import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Panel extends JPanel {

    private Timer timer;
    private ArrayList<PurpleMan> aliens;
    private SpaceShip player;
    private ArrayList<PewPew> playerLasers, alienLasers;
    private ArrayList<Shield> shields;

    private int laserDelay, laserCounter, alienVx, lives, playerLoss; //delay is # of frames between shots, counter counts frames
    private boolean win, lose;

    public Panel(int width, int height) {
        setBounds(0, 0, width, height);
        lives = 3;
        aliens = new ArrayList<>();
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 10; c++) {
//                Cars.add(new Car(Resources.Car3, new Point(70, 360), 4));
                aliens.add(new PurpleMan(40+(40*c), 40+(40*r), 20));
            }
        }

        alienVx = 1;

        player = new SpaceShip(getWidth() / 2 - 15, getHeight() - 100);

        shields = new ArrayList<>();
        shields.add(new Shield(Resources.sheild, new Point(250, 500)));
        shields.add(new Shield(Resources.sheild, new Point(250, 500)));
        shields.add(new Shield(Resources.sheild, new Point(500, 500)));

        playerLasers = new ArrayList<>();
        alienLasers = new ArrayList<>();
        laserDelay = 30;
        laserCounter = laserDelay;
        playerLoss = 0;

        setupKeyListener();
        timer = new Timer(1000 / 60, e -> update());
        timer.start();

        win = false;
        lose = false;

    }

    public void update() {

//        int count = 0;
//        for (int i = 0; i < alienLasers.size(); i++) {
//            PewPew pewPew =  alienLasers.get(i);
//            for (int j = 0; j < shields.size(); j++) {
//                Shield shield = shields.get(j);
//                if(pewPew.getHitBox().intersects(shield.getHitBox())){
//                    count ++;
//                }
//            }
//            if(count <=5){
//                alienLasers.remove(i);
//            }
//
//        }


        laserCounter++;
        updatePurpleMan();
        player.move(getWidth());
        updateAlienLasers();
        if (playerLoss > 0) {
            playerLoss--;
        }


        updatePlayerLasers();
        repaint();
    }

    public void updatePurpleMan() {
        boolean hitEdge = false;

        if (aliens.size() <= 0) {
            win();
        }

        for (PurpleMan alien : aliens) {
            alien.move(alienVx);
            if (alien.getX() <= 0) {
                hitEdge = true;
            } else if (alien.getX() + alien.getSize() >= getWidth()) {
                hitEdge = true;
            }
        }
        if (hitEdge) {
            alienVx *= -1;
            for (PurpleMan alien : aliens) {
                alien.shiftDown();
            }
        }


        //aliens shooting laser beams:
        for (PurpleMan alien : aliens) {
            if (aliens.size() * Math.random() < 0.05) {
                //pick alien at random and fire.
                //make laser at that alien's location, and add it to alienLasers
                PewPew laser = new PewPew((alien.getX() + (alien.getSize() / 2)), (alien.getY() + alien.getSize()), 5);
                alienLasers.add(laser);
            }
        }

        for (PurpleMan alien : aliens) {
            if (alien.getY() + alien.getSize() >= getHeight() - 60) {
                lose();
            }
        }
    }

    public void updatePlayerLasers() {
        for (int i = 0; i < playerLasers.size(); i++) {
            PewPew laser = playerLasers.get(i);
            laser.move();
            if (laser.getY() < -10) {
                playerLasers.remove(i);
                i--;
            }
        }

        for (int i = 0; i < playerLasers.size(); i++) {
            PewPew laser = playerLasers.get(i);
            for (int a = 0; a < aliens.size(); a++) {
                PurpleMan alien = aliens.get(a);
                if (laser.getHitBox().intersects(alien.getHitBox())) {
                    collision(a, i);
                    a = aliens.size();
                    i--;
                }
            }
        }

    }

    public void updateAlienLasers() {
        for (int i = 0; i < alienLasers.size(); i++) {
            PewPew laser = alienLasers.get(i);
            laser.move();
            if (laser.getY() > getHeight() + 10) {
                alienLasers.remove(i);
                i--;
            }
        }

        for (int i = 0; i < alienLasers.size(); i++) {
            PewPew laser = alienLasers.get(i);
            if (laser.getHitBox().intersects(player.getHitBox())) {
                alienLasers.remove(i);
                i--;
                if (playerLoss <= 0)
                    loseLife();
            }
        }

    }

    public void setupKeyListener() {
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (laserCounter >= laserDelay) {
                        PewPew laser = new PewPew(player.getX() + player.getWidth() / 2, player.getY(), -5);
                        playerLasers.add(laser);
                        laserCounter = 0;
                    }
                } else {
                    player.pressed(e.getKeyCode()); //notify play obj that key is down
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                player.released(e.getKeyCode()); //notify play obj that key is released
            }

        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, 700, 778);


//        for (Shield a : shields){
//            a.draw(g2);}


        g2.setColor(Color.BLUE);
        g2.fillRect(50, 500, 100, 50);
        g2.fillRect(250, 500, 100, 50);
        g2.fillRect(500, 500, 100, 50);

        Font stringFont = new Font("SansSerif", Font.PLAIN, 18);
        if (win) {
            g2.setColor(Color.MAGENTA);
            g2.setFont(stringFont);
            g2.drawString("YOU WIN!!!! YAYYYY!!!!", 308, 290);
        } else if (lose) {
            g2.setColor(Color.MAGENTA);
            g2.setFont(stringFont);
            g2.drawString("YOU LOST!!!!!!! LOLZ", 305, 290);
        } else {
            for (PurpleMan alien : aliens) {
                g2.setColor(Color.MAGENTA);
                alien.draw(g2);
            }
            for (PewPew laser : playerLasers) {
                g2.setColor(Color.MAGENTA);
                laser.draw(g2);
            }
            for (PewPew laser : alienLasers) {
                g2.setColor(Color.MAGENTA);
                laser.draw(g2);
            }
            g2.setColor(Color.MAGENTA);
            if (playerLoss > 0) {
                Color color = Color.RED;
                g2.setColor(color);
            }
            player.draw(g2);
        }}


        public void collision(int alien, int laser){
            aliens.remove(alien);
            playerLasers.remove(laser);
        }

        public void loseLife(){
            lives --;
            playerLoss = 85;
            if (lives <= 0){
                lose();
            }
        }

        public void win(){
            win = true;
            timer.stop();
        }

        public void lose(){
            lose = true;
            timer.stop();
}}