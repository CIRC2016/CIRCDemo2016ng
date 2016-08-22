package org.circ34.demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel implements ActionListener{

    private final int INTERVAL=40;

    private Image bg;
    private int bgx;
    private int bgy;

    private Image p1;
    private int p1x;
    private int p1y;

    private Timer timer;

    public Board(){
        initBoard();
    }

    private void initBoard(){
        loadImage();
        p1x=0;
        p1y=768-200;
        bgx=0;
        bgy=-2048+768;
        timer = new Timer(INTERVAL,this);
        timer.start();
    }

    private void loadImage(){
        ImageIcon tmp = new ImageIcon("p1.png");
        p1=tmp.getImage().getScaledInstance(100,100,Image.SCALE_SMOOTH);
        tmp = new ImageIcon("back.png");
        bg=tmp.getImage();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bg, bgx, bgy, null);
        g.drawImage(p1, p1x, p1y,null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (bgy<0){
        bgy++;
        }
        repaint();
    }

}
