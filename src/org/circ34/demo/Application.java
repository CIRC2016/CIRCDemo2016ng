package org.circ34.demo;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import java.io.FileWriter;


public class Application extends JFrame {
    private Board board=new Board();

    public Application() {

        initUI();
    }

    private void initUI() {

        add(board);

        setSize(768, 512);

        setTitle("CIRCDemo2016");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try{
                    FileWriter fw=new FileWriter("score.txt",false);
                    for (int a=0;a<board.scoreRecordArrayList.size();a++){
                        fw.write(board.scoreRecordArrayList.get(a).name+" "+board.scoreRecordArrayList.get(a).score+"\n");
                    }
                    fw.close();
                }catch (Exception ex){
                    ex.printStackTrace();
                }

                super.windowClosing(e);
            }
        });
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