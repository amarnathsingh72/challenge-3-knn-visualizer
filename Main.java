import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("AI Concepts Visualizer - k-NN Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            
          
            MainPanel panel = new MainPanel();
            frame.add(panel);
            
            frame.setVisible(true);
        });
    }
}
