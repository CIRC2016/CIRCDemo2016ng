package org.circ34.demo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/*
 * Created by ethan on 2016/8/22.
 * Merged by xdavidwu
 */
public class Welcome {
    public JPanel p1;
    private JPanel p2;
    private JButton playRetryButton;
    private JButton scoreboardButton;

    public Welcome() {
        playRetryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("btn1");
            }
        });
        scoreboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("btn2");
            }
        });
        p1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                int keyvalue=e.getKeyCode();
                    switch (keyvalue){
                        case KeyEvent.VK_UP:
                        case KeyEvent.VK_LEFT:
                        case KeyEvent.VK_RIGHT:
                    }
            }
        });
    }

    /*public static void main(String[] args) {
        JFrame frame = new JFrame("test");
        frame.setContentPane(new test().p1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
	*/
}
