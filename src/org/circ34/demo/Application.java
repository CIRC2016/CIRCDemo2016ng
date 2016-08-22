package org.circ34.demo;

import java.awt.EventQueue;
import javax.swing.*;


public class Application extends JFrame {

    private Board board;

    public Application() {

        initUI();
    }

    private void initUI() {

        add(new Board());
        //add(new Welcome().p1);
        setSize(768, 768);

        setTitle("Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Application ex = new Application();
                ex.setVisible(true);
            }
        });
    }
}