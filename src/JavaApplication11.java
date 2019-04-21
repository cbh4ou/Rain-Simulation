
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class JavaApplication11 extends JFrame {

    public JavaApplication11() {
        
        initUI();
    }
    
    private void initUI() {
        
        add(new RPanel());
        
        setResizable(true);
        pack();
        
        setTitle("TicTacToe");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void main(String[] args) {
        
        EventQueue.invokeLater(() -> {
            JFrame ex = new JavaApplication11();
            ex.setVisible(true);
        });
    }
    
}

