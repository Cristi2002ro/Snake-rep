package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Random;

public class SnakePanel extends JPanel implements ActionListener {
    public static final int SCREEN_WIDTH = 1200;
    public static final int SCREEN_HEIGHT = 600;
    public static final int UNIT_SIZE = 25;
    public static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    public static final int DELAY = 80;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int score = 0;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Random random;
    Timer timer;
    int highscore;
    BufferedReader reader;
    BufferedWriter writer;

    SnakePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        try {
            reader=new BufferedReader(new FileReader("C:\\Users\\Alexandru Duna\\IdeaProjects\\Snake\\src\\text"));
            highscore=Integer.parseInt(reader.readLine());
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        score = 0;
        bodyParts = 6;
        for (int i = 0; i < bodyParts; i++) {
            x[i] = i;
            y[i] = i;
        }
        direction = 'R';
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {/*
            for (int i = 0; i < SCREEN_WIDTH / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            }
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }*/
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(new Color(100, 160, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + score, (SCREEN_WIDTH - metrics.stringWidth("Score: " + score)) / 2, g.getFont().getSize());
        } else {
            gameover(g);
        }
    }

    public void newApple() {
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U':
                y[0] -= UNIT_SIZE;
                break;
            case 'D':
                y[0] += UNIT_SIZE;
                break;
            case 'L':
                x[0] -= UNIT_SIZE;
                break;
            case 'R':
                x[0] += UNIT_SIZE;
                break;
        }

    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            score++;
            bodyParts++;
            newApple();
        }

    }

    public void checkColision() {
        // muscat coada
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) running = false;
        }

        //verifica daca atinge in dreapta
        if (x[0] == SCREEN_WIDTH) {
            x[0]=0;
        }
        //verifica in partea stanga
        if (x[0] <0) {
            x[0]=SCREEN_WIDTH;
        }
        //verifica in paretea de jos
        if (y[0] == SCREEN_HEIGHT) {
            y[0]=0;
        }
        //verifica in parea de sus
        if (y[0] <0) {
            y[0]=SCREEN_HEIGHT;
        }
    }

    public void gameover(Graphics g) {
        if(score>highscore){
            try {
                writer=new BufferedWriter(new FileWriter("C:\\Users\\Alexandru Duna\\IdeaProjects\\Snake\\src\\text"));
                writer.write(String.valueOf(score));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        g.setColor(Color.red);
        g.setFont(new Font("Plain", Font.PLAIN, 75));
        FontMetrics metrics = getFontMetrics(new Font("Plain", Font.PLAIN, 75));
        g.drawString("Game over :(", (SCREEN_WIDTH - metrics.stringWidth("Game over :(")) / 2, SCREEN_HEIGHT / 2);
        g.setFont(new Font("Ink FreE", Font.PLAIN, 75));
        g.setColor(Color.GREEN);
        g.drawString("Press Enter to play again", (SCREEN_WIDTH - metrics.stringWidth("Press Enter to play again")) / 2, SCREEN_HEIGHT / 2 + 80);
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        g.drawString("Score: " + score, (SCREEN_WIDTH - metrics.stringWidth(("Score: " + score))) / 2 + 70, g.getFont().getSize());
        g.drawString("Highscore: "+highscore,(SCREEN_WIDTH - metrics.stringWidth(("Highscore: "+highscore))) / 2 + 100,g.getFont().getSize()+50);
        running = false;
        repaint();
        timer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkColision();
            checkApple();
        }
        repaint();

    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_ENTER:
                    if (running == false) startGame();
            }
        }
    }


}
