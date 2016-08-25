package org.circ34.demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Board extends JPanel implements ActionListener{

    private final int INTERVAL=40;
    private int[] LOC={
        0,40,160,280,400,510,630
    };

    private Image bg;
    private int bgx;
    private int bgy;
    private boolean conMove=false;
    private int bgJump=0;
    private int bgJumpBond=500;

    private Image p1;
    private Image p1f;
    private int p1Col=1;
    private int p1y;
    private boolean p1L=false;
    private boolean p1R=false;

    private class Drop{
        public int col;
        public int y;
        public Drop(int column){
            col=column;
            y=0;
        }
    }
    private ArrayList<Drop> dropArrayList;

    private Timer timer=new Timer(INTERVAL,this);
    private int timerJump=0;
    private int timerJumpBond=30;
    private int p1Jump=0;
    private int p1JumpBond=10;
    private int pscore=0;

    private JButton startButton=new JButton("Start");
    private JButton scoreboardButton=new JButton("Scoreboard");
    private boolean gameEntered=false;
    private JLabel scoreLabel= new JLabel("Score: ");

    private class ScoreRecord{
        public String name;
        public int score;
        public ScoreRecord(String iname,int iscore){
            name=iname;
            score=iscore;
        }
    }
    private ArrayList<ScoreRecord> scoreRecordArrayList=new ArrayList<>();

    private KeyAdapter keyAction=new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key=e.getKeyCode();
            if(key==KeyEvent.VK_LEFT){
                if (p1Col!=1&&!p1R) p1L=true;
            }else if (key==KeyEvent.VK_RIGHT){
                if (p1Col!=8&&!p1L) p1R=true;
            }
        }
    };

    public Board(){
        initBoard();
    }

    private void initBoard(){
        addButton();
        loadImage();
        scoreLabel.setFocusable(false);
        bgx=0;
        bgy=-2048+512+150;
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
                String scoreString=String.format("%-32s %s\n","Name","Score");
                for (int a=0;a<scoreRecordArrayList.size();a++){
                    scoreString+=String.format("%-32s %d\n",scoreRecordArrayList.get(a).name,scoreRecordArrayList.get(a).score);
                }
                JOptionPane.showMessageDialog(null,scoreString,"Scoreboard",JOptionPane.PLAIN_MESSAGE);
            }
        });
    }

    private void addButton() {
        add(startButton);
        add(scoreboardButton);
    }

    private void removeButton(){
        remove(startButton);
        remove(scoreboardButton);
    }

    private void startGame(){
        add(scoreLabel);
        pscore=0;
        gameEntered=true;
        dropArrayList=new ArrayList<>();
        p1y=512-150;
        bgx=0;
        bgy=-2048+512+200;
        conMove=false;
        addKeyListener(keyAction);
        setFocusable(true);
        requestFocus();
        timer.start();
    }

    private void endGame(){
        remove(scoreLabel);
        gameEntered=false;
        addButton();
        timer.stop();
        bgy=-2048+512+150;
        conMove=true;
        removeKeyListener(keyAction);
        scoreRecordArrayList.add(new ScoreRecord(JOptionPane.showInputDialog("You got "+pscore+" points!\nEnter your name:"),pscore));
        scoreRecordArrayList.sort(new Comparator<ScoreRecord>() {
            @Override
            public int compare(ScoreRecord o1, ScoreRecord o2) {
                return o2.score-o1.score;
            }
        });
    }

    private void loadImage(){
        ImageIcon tmp = new ImageIcon("p1.png");
        p1=tmp.getImage().getScaledInstance(100,100,Image.SCALE_SMOOTH);
        tmp=new ImageIcon("p1f.png");
        p1f=tmp.getImage().getScaledInstance(100,100,Image.SCALE_SMOOTH);
        tmp = new ImageIcon("back.png");
        bg=tmp.getImage();
    }

    private void spawnBall(){
        dropArrayList.add(new Drop(new Random(System.currentTimeMillis()+timerJump).nextInt(6)+1));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bg, bgx, bgy, null);
        if (gameEntered) {
            g.drawImage((p1Col % 2 == 1) ? p1 : p1f, LOC[p1Col], p1y, null);
            for (int a = 0; a < dropArrayList.size(); a++) {
                Drop curr = dropArrayList.get(a);
                g.drawImage(p1, LOC[curr.col], curr.y, null);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        scoreLabel.setText("Score: "+(++pscore));
        timerJump++;
        if (timerJump==timerJumpBond){
            spawnBall();
            timerJump=0;
        }
        if(p1L){
            p1Jump++;
            if (p1Jump==p1JumpBond){
                p1Jump=0;
                p1Col--;
                p1L=false;
            }
        }else if(p1R){
            p1Jump++;
            if (p1Jump==p1JumpBond){
                p1Jump=0;
                p1Col++;
                p1R=false;
            }
        }
        for (int a=0;a<dropArrayList.size();a++){
            Drop curr=dropArrayList.get(a);
            curr.y+=3;
            if (curr.y>500){
                dropArrayList.remove(a);
                a--;
            }else if (curr.y>p1y-100&&curr.y<p1y+200&&p1Col==curr.col){
                dropArrayList.remove(a);
                a--;
                endGame();
            }
        }
        if ((bgy<-2048+512+200+500)||(bgy<0&&conMove)){
            bgy++;
        }else if(bgy==-2048+512+200+500) {
            if (bgJump++==bgJumpBond) conMove=true;
        }else if(bgy==0) endGame();
        repaint();
    }

}
