package spacegame;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class SpaceGame extends JPanel {
    
    //<editor-fold defaultstate="collapsed" desc="Variables">
    static int pos = 450;
    static int ran = 100;
    static int xp = -1;
    static int yp = -1;
    //Ship and asteroid position variables.
    
    static JPanel pan;
    static JPanel menu;
    
    static JFrame fr;
    static Timer time;
    static Cursor Blank;
    
    static int[] starx = new int[100];
    static int[] stary = new int[100];
    static int stco;
    static int sp = 3;
    //Creation variables (stars, frame, ect.).
    
    static boolean hit;
    static int hsec;
    
    static boolean shoot;
    static int fsec;
    
    static boolean crash;
    static int csec;
    static Color scol = Color.WHITE;
    static Color acol = Color.LIGHT_GRAY;
    //Temporary variables (color, laser, ect.).
    
    static boolean slomo;
    static int ssec;
    
    static boolean aimb;
    static int ashot; 
    
    static int lives = 3;
    static boolean lost;
    static int hstr;
    static int score;
    //Statistic variables.
    
    static String text = "";
    static Color tcol;
    static boolean display;
    static int dsec;
    //Message variables.
    
    //</editor-fold>
    
    public static void main(String[] args) {
        fr = new JFrame("Asteroids");
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setSize(500, 500);
        fr.setLocationRelativeTo(null);
        fr.setResizable(false);
        //Basic frame code.
        
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Blank = Toolkit.getDefaultToolkit().createCustomCursor(
        cursorImg, new Point(0, 0), "Blank");
        //Setup for blank cursor.
        
        //<editor-fold defaultstate="collapsed" desc="Main Menu">
        
        menu = new JPanel();
        menu.setBackground(Color.BLACK);
        menu.setLayout(null);
        
        JLabel toptext = new JLabel("ASTEROIDS");
        toptext.setBounds(30, 0, 500, 250);
        toptext.setForeground(Color.MAGENTA);
        toptext.setFont(new Font("Bold", Font.BOLD, 75));
        menu.add(toptext);
        JLabel toptext2 = new JLabel("ASTEROIDS");
        toptext2.setBounds(26, 0, 500, 250);
        toptext2.setForeground(Color.BLUE);
        toptext2.setFont(new Font("Bold", Font.BOLD, 75));
        menu.add(toptext2);
        //Main title text and color.
        
        JButton start = new JButton("START");
        start.setBackground(Color.CYAN);
        start.setBounds(200, 250, 100, 50);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                menu.setVisible(false);
                time = new Timer(sp, act);
                time.start();
                
                pan.setVisible(true);
                fr.getContentPane().setCursor(Blank);
                fr.add(pan);
                //Timer starts. Game begins.
            }
        });
        menu.add(start);
        //Start button starts game at default speed.
        
        JButton setup = new JButton("SETUP");
        setup.setBackground(Color.CYAN);
        setup.setBounds(200, 325, 100, 50);
        setup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                menu.setVisible(false);
                final JPanel set = new JPanel(null);
                set.setBackground(Color.BLACK);
                //Settings contained in a JPanel. Buttons below.
                
                //<editor-fold defaultstate="collapsed" desc="Easy Button">
                JButton easy = new JButton("EASY");
                easy.setBackground(Color.WHITE);
                easy.setBounds(200, 50, 100, 50);
                easy.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sp = 5;
                    }
                });
                set.add(easy);
                //</editor-fold>
                
                //<editor-fold defaultstate="collapsed" desc="Medium Button">
                JButton med = new JButton("MEDIUM");
                med.setBackground(Color.WHITE);
                med.setBounds(200, 125, 100, 50);
                med.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sp = 3;
                    }
                });
                set.add(med);
                //</editor-fold>
                
                //<editor-fold defaultstate="collapsed" desc="Hard Button">
                JButton hard = new JButton("HARD");
                hard.setBackground(Color.WHITE);
                hard.setBounds(200, 200, 100, 50);
                hard.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sp = 2;
                    }
                });
                set.add(hard);
                //</editor-fold>
                
                //<editor-fold defaultstate="collapsed" desc="Insane Button">
                JButton ins = new JButton("INSANE");
                ins.setBackground(Color.WHITE);
                ins.setBounds(200, 275, 100, 50);
                ins.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sp = 1;
                    }
                });
                set.add(ins);
                //</editor-fold>
                
                //<editor-fold defaultstate="collapsed" desc="Play Button">
                JButton play = new JButton("PLAY");
                play.setBackground(Color.CYAN);
                play.setBounds(200, 350, 100, 50);
                play.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        set.setVisible(false);
                        time = new Timer(sp, act);
                        time.start();
                        
                        pan.setVisible(true);
                        fr.getContentPane().setCursor(Blank);
                        fr.add(pan);
                        //Timer starts. Game begins.
                    }
                });
                set.add(play);
                //</editor-fold>
                
                fr.add(set);
            }
        });
        menu.add(setup);
        //Menu button for speed options.
        
        fr.add(menu);
        
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Game Setup">
        pan = new SpaceGame();
        for (int i = 0; i < 100; i++) {
            starx[i] = (int) (Math.random() * 500);
            stary[i] = (int) (Math.random() * 500);
        }
        //Creates graphics panel and first stars.
        
        pan.addMouseMotionListener(motion);
        pan.addMouseListener(click);
        //Adds mouselisteners to panel.
        
        //</editor-fold>
        
        fr.setVisible(true);
        //Sets everything visible. 
    }
    
    @Override
    public void paint(Graphics g) {
        Graphics2D gr = (Graphics2D) g;
        gr.clearRect(0, 0, 500, 500);
        //Creates graphics and clears panel.
        
        gr.setColor(Color.BLACK);
        gr.fillRect(0, 0, 500, 500);
        gr.setColor(Color.WHITE);
        for (int i = 0; i < 100; i++) {
            gr.drawLine(starx[i], stary[i], starx[i], stary[i]);
        }
        //Black background with stars.
        
        gr.setColor(Color.RED);
        gr.drawString("Programmed by Daniel Oliynyk", 310, 460);
        //Credits text on bottom.
        
        gr.setColor(Color.YELLOW);
        gr.drawString(score + "", 450, 15);
        
        if (lost == false) {
            //What to do if player hasn't lost.
        
            gr.setColor(Color.CYAN);
            gr.drawString(hstr + "", 238, 15);
            //Draws the current hit streak.
            
            if (display) {
                gr.setColor(tcol);
                gr.drawString(text, 210, 240);
            }
            //If message exists, display it with chosen color.
            
            for (int i = 0; i < lives; i++) {
                gr.setColor(Color.YELLOW);
                gr.drawOval((i * 15) + 10, 10, 10, 10);
                gr.setColor(Color.RED);
                gr.fillOval((i * 15) + 10, 10, 10, 10);
            }
            //Displays remaining lives.
            
            gr.setColor(acol);
            gr.fillOval(pos, ran, 50, 50);
            //Creates asteriod. If it is hit, it will turn green.
            
            gr.setColor(Color.BLACK);
            gr.drawOval(pos + 10, ran + 10, 10, 10);
            gr.drawOval(pos + 25, ran + 20, 20, 20);
            gr.drawOval(pos + 7, ran + 30, 15, 15);
            //Asteriod decorations.
            
            if (xp > 0) {
                //If mouse entered screen.
                gr.setColor(scol);
                gr.fillRect(xp - 10, yp - 10, 10, 20);
                gr.fillRect(xp - 5, yp - 5, 40, 10);
                gr.setColor(Color.ORANGE);
                gr.fillRect(xp - 10, yp - 12, 20, 2);
                gr.fillRect(xp - 10, yp + 10, 20, 2);
                gr.setColor(Color.BLUE);
                gr.fillRect(xp - 2, yp - 2, 16, 4);
                gr.setColor(Color.RED);
                gr.fillRect(xp - 16, yp - 3, 6, 6);
                //Ship and decorations.
                
            }
            
            if (shoot && hit == false && aimb == false) {
                gr.setColor(Color.MAGENTA);
                gr.drawLine(xp + 35, yp, 500, yp);
                //Draws laser until edge if player missed. Resets hit streak.
            }
            else if (shoot && aimb && fsec < 30) {
                
                if (ashot > 1) {
                    gr.setColor(Color.YELLOW);
                }
                else {
                    gr.setColor(Color.RED);
                }
                //Sets color to red on last shot.
                
                gr.drawLine(xp + 35, yp, pos, ran + 25);
                //Draws laser to middle of asteroid if aimbot is on.
            }
            else if (shoot && hit && aimb == false) {
                gr.setColor(Color.MAGENTA);
                gr.drawLine(xp + 35, yp, pos, yp);
                //Draws laser until asteriod if hit scored.
            }
            
        }
        else {
            gr.setColor(Color.YELLOW);
            gr.drawString("YOU LOST", 220, 240);
            //If lost, all graphics except background and message disappear.
        }
    }
    
    public static MouseMotionListener motion = new MouseMotionListener() {

        @Override
        public void mouseDragged(MouseEvent me) {
            xp = me.getX();
            yp = me.getY();
            //Mouse location update.
        }

        @Override
        public void mouseMoved(MouseEvent me) {
            xp = me.getX();
            yp = me.getY();
            //Mouse location update.
        }
    };
    
    public static MouseListener click = new MouseListener() {

        @Override
        public void mousePressed(MouseEvent me) {
            
            boolean missed = true;
            //Checks if player missed.
            
            if (lost) {
                
                //<editor-fold defaultstate="collapsed" desc="Variables Reset">
                pos = 450;
                ran = 100;
                xp = -1;
                yp = -1;
                hit = false;
                hsec = 0;
                shoot = false;
                fsec = 0;
                scol = Color.WHITE;
                acol = Color.LIGHT_GRAY;
                lives = 3;
                hstr = 0;
                slomo = false;
                ssec = 0;
                score = 0;
                text = "";
                display = false;
                dsec = 0;
                //All relevant varialbles reset to original value.
                
                //</editor-fold>
                
                pan.setVisible(false);
                menu.setVisible(true);
                lost = false;
                //Clicking restarts game if player has lost.
            }
            
            else if (me.getY() > ran && ran + 50 > me.getY()
                    && me.getX() < pos - 35 && aimb == false) {
                hit = true;
                score = score + 50;
                missed = false;
                hstr++;
                //If laser hits, turns on hit animation.
            }
            
            else if (aimb) {
                ashot--;
                if (ashot <= 0) {
                    aimb = false;
                    if (me.getY() > ran && ran + 50 > me.getY()
                            && me.getX() < pos - 35) {
                        hit = true;
                        score = score + 50;
                        missed = false;
                        hstr++;
                    }
                }
                else {
                    hit = true;
                    score = score + 50;
                    missed = false;
                    hstr++;
                }
                //If aimbot is on, laser tracks asteroid.
            }
            
            if (hstr > 0 && hstr % 5 == 0) {
                display = true;
                dsec = 0;
                tcol = Color.YELLOW;
                text = hstr + " HIT STREAK";
                score = score + 200;
                
                if (hstr > 0 && hstr % 15 == 0) {
                    tcol = Color.CYAN;
                    text = "AIMBOT ACTIVE";
                    ashot = 5;
                    aimb = true;
                    //Aimbot setup for three shots.
                }
                
                if (hstr > 0 && hstr % 25 == 0 && slomo == false) {
                    tcol = Color.CYAN;
                    text = "SLOW MO BONUS";
                    scol = Color.CYAN;
                    sp = sp * 2;
                    time.stop();
                    time = new Timer(sp, act);
                    time.start();
                    ssec = 0;
                    slomo = true;
                    //Activates slow motion bonus.
                }
                
                if (hstr > 0 && hstr % 40 == 0) {
                    tcol = Color.CYAN;
                    text = "EXTRA LIFE";
                    lives++;
                    //Activates extra life bonus.
                }
            }
            //Updates hit streak and makes message.
            
            if (missed) {
                if (hstr > 4) {
                    display = true;
                    dsec = 0;
                    tcol = Color.RED;
                    text = "HIT STREAK RESET";
                    //If hitstreak over four, display message.
                }
                hstr = 0;
            }
            //Reset hitstreak if player missed. 
            
            shoot = true;
            //Turns on shooting animation.
        }

        @Override
        public void mouseExited(MouseEvent me) {
            if (lost == false) {
                xp = -1;
                yp = -1;
                display = true;
                tcol = Color.YELLOW;
                text = "GAME PAUSED";
                fr.repaint();
                time.stop();
                //Hides ship if mouse exits screen and pauses game.
            }
        }
        
        @Override
        public void mouseEntered(MouseEvent me) {
            if (lost == false) {
                dsec = 600;
                text = "";
                time.restart();
                //Starts the game after pause.
            }
        }
        
        //<editor-fold defaultstate="collapsed" desc="Unused Commands">
        @Override
        public void mouseClicked(MouseEvent me) {
            
        }

        @Override
        public void mouseReleased(MouseEvent me) {
            
        }
        //</editor-fold>
        
    };
    
    public static ActionListener act = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent ae) {
            
            if (pos - 1 >= xp - 10 && pos - 1 <= xp + 35
                    && ran + 50 > yp - 7 && ran < yp + 7) {
                acol = Color.RED;
                scol = Color.RED;
                crash = true;
            }
            
            if (crash) {
                csec++;
                if (csec > 100) {
                    csec = 0;
                    pos = -1;
                    acol = Color.LIGHT_GRAY;
                    scol = Color.WHITE;
                    crash = false;
                }
            }
            
            if (pos > 0) {
                pos = pos - 1;
                //Moves asteroid horizontally if not at edge.
            }
            
            else if (crash == false) {
                pos = 450;
                ran = (int) (Math.random() * 420);
                hstr = 0;
                scol = Color.WHITE;
                ssec = 0;
                slomo = false;
                //If asteriod reaches edge, resets position (random).
                
                lives--;
                
                if (lives <= -1) {
                    lives = 3;
                    fr.getContentPane().setCursor(Cursor.
                            getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    lost = true;
                }
                
                else {
                    display = true;
                    dsec = 0;
                    tcol = Color.RED;
                    if (lives == 1) {
                        text = "1 LIFE LEFT";
                    }
                    else {
                        text = lives + " LIVES LEFT";
                    }
                    //Sets up message and color;
                }
                //Player loses life. If no lives are left, he dies.
            }
            
            if (shoot) {
                fsec++;
                if (fsec > 50) {
                    fsec = 0;
                    shoot = false;
                }
                //Waits 50 miliseconds before hiding laser.
            }
            
            if (hit) {
                acol = Color.GREEN;
                hsec++;
                if (hsec > 30) {
                    hsec = 0;
                    acol = Color.LIGHT_GRAY;
                    pos = 450;
                    ran = (int) (Math.random() * 420);
                    hit = false;
                }
            }
            //Changes asteriod color to green temporarily, then resets it.
            
            if (display) {
                dsec++;
                if (dsec > 500) {
                    dsec = 0;
                    text = "";
                    display = false;
                }
            }
            //Display timer set to half a second.
            
            if (slomo) {
                ssec++;
                if (ssec > 2000) {
                    ssec = 0;
                    scol = Color.WHITE;
                    sp = sp / 2;
                    time.stop();
                    time = new Timer(sp, act);
                    time.start();
                    slomo = false;
                }
            }
            //Timer for slow motion.
            
            stco++;
            
            if (stco > 99) {
                stco = 0;
            }
            starx[stco] = (int) (Math.random() * 500);
            stary[stco] = (int) (Math.random() * 500);
            //Every milisecond, a star changes position.
            
            if (lost == false && (stco == 0 || stco % 10 == 0)) {
                score++;
            }
            //Updates score every 10 miliseconds.
            
            fr.repaint();
            //Updates graphics at constant rate.
        }
    };
    
}