package org.circ34.demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class Board extends JPanel implements ActionListener{

    private final int INTERVAL=40;
    private int[] LOC={
        0,40,160,280,400,510,630
    };

    private Image bg;
    private int bgx;
    private int bgy;

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
    private ArrayList<Drop> dropArrayList=new ArrayList<>();

    private Timer timer;
    private int timerJump=0;
    private int timerJumpBond=25;
    private int p1Jump=0;
    private int p1JumpBond=10;

    public Board(){
        initBoard();
    }

    private void initBoard(){
        loadImage();
        p1y=512-150;
        bgx=0;
        bgy=-2048+512+200;
        addKeyListener(new KeyAdapter() {
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
        });
        setFocusable(true);
        timer = new Timer(INTERVAL,this);
        timer.start();
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
        dropArrayList.add(new Drop(new Random().nextInt(6)+1));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bg, bgx, bgy, null);
        g.drawImage((p1Col%2==1)?p1:p1f, LOC[p1Col], p1y,null);
        for (int a=0;a<dropArrayList.size();a++){
            Drop curr=dropArrayList.get(a);
            g.drawImage(p1,LOC[curr.col],curr.y,null);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
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
            curr.y+=2;
            if (curr.y>512){
                dropArrayList.remove(a);
                a--;
            }else if (curr.y>p1y-100&&curr.y<p1y+200&&p1Col==curr.col){
                dropArrayList.remove(a);
                a--;
            }
        }
        if (bgy<0){
            bgy++;
        }
        repaint();
    }

}
