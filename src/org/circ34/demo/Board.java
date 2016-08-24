package org.circ34.demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class Board extends JPanel implements ActionListener{

    private final int INTERVAL=40;
    private int[] LOC={
        0,0,100,200,300,400,500,600,700
    };

    private Image bg;
    private int bgx;
    private int bgy;

    private Image p1;
    private int p1Col=1;
    private int p1y;

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

    public Board(){
        initBoard();
    }

    private void initBoard(){
        loadImage();
        p1y=512-150;
        bgx=0;
        bgy=-2048+512;
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //super.keyPressed(e);
                int key=e.getKeyCode();
                System.out.println(e.getKeyCode()+" pressed");
                if(key==KeyEvent.VK_LEFT){
                    if (p1Col!=1) p1Col--;
                }else if (key==KeyEvent.VK_RIGHT){
                    if (p1Col!=8) p1Col++;
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
        tmp = new ImageIcon("back.png");
        bg=tmp.getImage();
    }

    private void spawnBall(){
        dropArrayList.add(new Drop(new Random().nextInt(8)+1));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bg, bgx, bgy, null);
        g.drawImage(p1, LOC[p1Col], p1y,null);
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
