package org.circ34.demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Board extends JPanel implements ActionListener {

    private final int INTERVAL = 40;
    private int[] LOC = {
            0, 40, 160, 280, 400, 510, 630
    };

    private Image bg;
    private int bgy;
    private boolean conMove = false;
    private int bgJump = 0;
    private int bgJumpBond = 500;

    private Image[][] pimage = new Image[4][4];
    private int pimageSelect = 0;
    private int p1Col = 1;
    private int p1y = 512 - 150;
    private boolean p1L = false;
    private boolean p1R = false;
    private boolean boost = false;
    private int ani = 0;

    private class Drop {
        public int col;
        public int y;

        public Drop(int column) {
            col = column;
            y = 0;
        }
    }

    private ArrayList<Drop> dropArrayList = new ArrayList<>();
    private Image dropImage;

    private Timer timer = new Timer(INTERVAL, this);
    private int timerJump = 0;
    private int timerJumpBond = 30;
    private int p1Jump = 0;
    private int p1JumpBond = 10;
    private int pscore = 0;

    private JButton leftButton = new JButton("⇚");
    private JButton startButton = new JButton("Start");
    private JButton scoreboardButton = new JButton("Scoreboard");
    private JButton rightButton = new JButton("⇛");
    private boolean gameEntered = false;
    private JLabel scoreLabel = new JLabel("Score: ");

    public class ScoreRecord implements Comparable<ScoreRecord> {
        public String name;
        public int score;

        public ScoreRecord(String iname, int iscore) {
            name = iname;
            score = iscore;
        }

        public int compareTo(ScoreRecord a) {
            return a.score - score;
        }
    }

    public ArrayList<ScoreRecord> scoreRecordArrayList = new ArrayList<>();

    private KeyAdapter keyAction = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT) {
                if (p1Col != 1 && !p1R) p1L = true;
            } else if (key == KeyEvent.VK_RIGHT) {
                if (p1Col != 6 && !p1L) p1R = true;
            } else if (key == KeyEvent.VK_UP) {
                boost = true;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_UP) boost = false;
        }
    };

    public Board() {
        p1y = 512 - 150;
        readFile();
        addButton();
        loadImage();
        scoreLabel.setFocusable(false);
        bgy = -2048 + 512 + 150;
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
                removeButton();
            }
        });
        scoreboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String scoreString = String.format("%-32s %s\n", "Name", "Score");
                for (int a = 0; a < scoreRecordArrayList.size(); a++) {
                    scoreString += String.format("%-32s %d\n", scoreRecordArrayList.get(a).name, scoreRecordArrayList.get(a).score);
                }
                JOptionPane.showMessageDialog(null, scoreString, "Scoreboard", JOptionPane.PLAIN_MESSAGE);
            }
        });
        leftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (++pimageSelect < 0) pimageSelect = 3;
                repaint();
            }
        });
        rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (++pimageSelect > 3) pimageSelect = 0;
                repaint();
            }
        });
    }

    private void readFile() {
        File save = new File("score.txt");
        try {
            if (save.exists()) {
                BufferedReader br = new BufferedReader(new FileReader("score.txt"));
                String tmp = new String();
                while (br.ready()) {
                    Scanner scanner = new Scanner(br.readLine());
                    scoreRecordArrayList.add(new ScoreRecord(scanner.next(), scanner.nextInt()));
                }
                br.close();
            } else save.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addButton() {
        add(leftButton);
        add(startButton);
        add(scoreboardButton);
        add(rightButton);
    }

    private void removeButton() {
        remove(leftButton);
        remove(startButton);
        remove(scoreboardButton);
        remove(rightButton);
    }

    private void startGame() {
        add(scoreLabel);
        pscore = 0;
        ani = 0;
        gameEntered = true;
        dropArrayList.clear();
        p1y = 512 - 150;
        bgy = -2048 + 512 + 200;
        conMove = false;
        addKeyListener(keyAction);
        setFocusable(true);
        requestFocus();
        timer.start();
    }

    private void endGame() {
        p1y = 512 - 150;
        remove(scoreLabel);
        gameEntered = false;
        addButton();
        timer.stop();
        bgy = -2048 + 512 + 150;
        conMove = true;
        removeKeyListener(keyAction);
        scoreRecordArrayList.add(new ScoreRecord(JOptionPane.showInputDialog("You got " + pscore + " points!\nEnter your name:"), pscore));
        Collections.sort(scoreRecordArrayList);
    }

    private void loadImage() {
        ImageIcon tmp = new ImageIcon("p1f.png");
        pimage[0][0] = tmp.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        tmp = new ImageIcon("p1f2.png");
        pimage[0][1] = tmp.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        tmp = new ImageIcon("p1.png");
        pimage[0][2] = tmp.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        tmp = new ImageIcon("p12.png");
        pimage[0][3] = tmp.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        tmp = new ImageIcon("p2f.png");
        pimage[1][0] = tmp.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        tmp = new ImageIcon("p2f2.png");
        pimage[1][1] = tmp.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        tmp = new ImageIcon("p2.png");
        pimage[1][2] = tmp.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        tmp = new ImageIcon("p22.png");
        pimage[1][3] = tmp.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        tmp = new ImageIcon("p3f.png");
        pimage[2][0] = tmp.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        tmp = new ImageIcon("p3f2.png");
        pimage[2][1] = tmp.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        tmp = new ImageIcon("p3.png");
        pimage[2][2] = tmp.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        tmp = new ImageIcon("p32.png");
        pimage[2][3] = tmp.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        tmp = new ImageIcon("p4f.png");
        pimage[3][0] = tmp.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        tmp = new ImageIcon("p4f2.png");
        pimage[3][1] = tmp.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        tmp = new ImageIcon("p4.png");
        pimage[3][2] = tmp.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        tmp = new ImageIcon("p42.png");
        pimage[3][3] = tmp.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        tmp = new ImageIcon("drop.png");
        dropImage = tmp.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        tmp = new ImageIcon("back.png");
        bg = tmp.getImage();
    }

    private void spawnBall() {
        dropArrayList.add(new Drop(new Random().nextInt(6) + 1));
    }

    private Image getAniImage() {
        int index = (p1Col % 2) * 2;
        if (boost) index += (ani % 4 >= 2) ? 0 : 1;
        else index += ((ani % 6 >= 2) ? 0 : 1);
        return pimage[pimageSelect][index];
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bg, 0, bgy, this);
        if (gameEntered) {
            g.drawImage(getAniImage(), LOC[p1Col], p1y, thisl);
            for (int a = 0; a < dropArrayList.size(); a++) {
                Drop curr = dropArrayList.get(a);
                g.drawImage(dropImage, LOC[curr.col], curr.y, this);
            }
        } else {
            g.drawImage(pimage[pimageSelect][3], LOC[3], p1y - 100, this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ani++;
        if (boost) pscore++;
        scoreLabel.setText("Score: " + (++pscore));
        timerJump++;
        if (timerJump == timerJumpBond) {
            spawnBall();
            timerJump = 0;
        }
        if (p1L) {
            p1Jump++;
            if (p1Jump == p1JumpBond) {
                p1Jump = 0;
                p1Col--;
                p1L = false;
            }
        } else if (p1R) {
            p1Jump++;
            if (p1Jump == p1JumpBond) {
                p1Jump = 0;
                p1Col++;
                p1R = false;
            }
        }
        for (int a = 0; a < dropArrayList.size(); a++) {
            Drop curr = dropArrayList.get(a);
            curr.y += 3;
            if (boost) curr.y += 1;
            if (curr.y > 500) {
                dropArrayList.remove(a);
                a--;
            } else if (curr.y > p1y - 100 && curr.y < p1y + 200 && p1Col == curr.col) {
                dropArrayList.remove(a);
                a--;
                repaint();
                endGame();
            }
        }
        if ((bgy < -2048 + 512 + 200 + 500) || (bgy < 0 && conMove)) {
            bgy++;
            if (boost) bgy++;
        } else if (bgy == -2048 + 512 + 200 + 500) {
            if (boost) bgJump++;
            if (bgJump++ >= bgJumpBond) conMove = true;
        } else if (bgy == 0) endGame();
        repaint();
    }

}
