import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    public MainPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        
        JLabel title = new JLabel("AI Concepts Visualizer - k-NN Demo", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(new Color(50, 100, 200));
        
        
        JButton startBtn = new JButton("Start k-NN Demo");
        startBtn.setFont(new Font("Arial", Font.BOLD, 18));
        startBtn.setBackground(new Color(70, 150, 255));
        startBtn.setForeground(Color.WHITE);
        startBtn.addActionListener(e -> showDemo());
        
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(title);
        centerPanel.add(startBtn);
        
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private void showDemo() {
    JFrame canvasFrame = new JFrame("k-NN Demo - Right-click: Blue, Left-click: Red");
    canvasFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    KNNCanvas canvas = new KNNCanvas();
    canvasFrame.add(canvas);
    canvasFrame.pack();
    canvasFrame.setLocationRelativeTo(null);
    canvasFrame.setVisible(true);
}

}
