package mx.unison.pong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame f = new JFrame("Pong");
        f.setResizable(false);

        JPanel p = new JPanel(new BorderLayout());
        Board board = new Board();
        p.add(board, BorderLayout.CENTER);

        f.addKeyListener(board);
        f.setContentPane(p);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                board.stopGame();
            }
        });

        f.pack();
        f.setVisible(true);
    }
}