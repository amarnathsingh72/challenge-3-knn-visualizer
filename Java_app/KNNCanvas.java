import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.net.*;
import java.io.*;

public class KNNCanvas extends JPanel {
    public List<PointData> points = new ArrayList<>();
    private PointData queryPoint = null;
    private int kValue = 3;
    
    static class PointData {
        Point p;
        boolean isBlue;
        PointData(Point p, boolean isBlue) { this.p = p; this.isBlue = isBlue; }
    }
    
    public KNNCanvas() {
        setPreferredSize(new Dimension(900, 600));
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        
        CanvasPanel canvas = new CanvasPanel();
        add(canvas, BorderLayout.CENTER);
        
        JPanel controls = new JPanel();
        controls.setPreferredSize(new Dimension(250, 600));
        controls.setBackground(Color.decode("#F0F8FF"));
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        controls.setBorder(BorderFactory.createTitledBorder("Controls"));
        
        JLabel kLabel = new JLabel("k = " + kValue);
        JSlider kSlider = new JSlider(1, 20, kValue);
        kSlider.addChangeListener(e -> {
            kValue = kSlider.getValue();
            kLabel.setText("k = " + kValue);
            canvas.repaint();
        });
        
        JButton predictBtn = new JButton("Add Query Point & Predict");
        predictBtn.addActionListener(e -> {
            queryPoint = new PointData(new Point(400, 300), false);
            canvas.repaint();
        });
        
        JButton clearBtn = new JButton("Clear All");
        clearBtn.addActionListener(e -> {
            points.clear();
            queryPoint = null;
            canvas.repaint();
        });
        
        
        JButton geminiBtn = new JButton("🤖 Explain with Gemini");
        geminiBtn.addActionListener(e -> explainWithGemini());
        
        controls.add(Box.createVerticalStrut(20));
        controls.add(kLabel);
        controls.add(kSlider);
        controls.add(Box.createVerticalStrut(20));
        controls.add(predictBtn);
        controls.add(Box.createVerticalStrut(10));
        controls.add(clearBtn);
        controls.add(Box.createVerticalStrut(10));
        controls.add(geminiBtn);  
        
        add(controls, BorderLayout.EAST);
    }
    
    
private String getPredictionText(int blueCount, int total) {
    if (blueCount > total/2) return "Class A (Blue)";
    if (blueCount < total/2) return "Class B (Red)";
    return "TIE - Equal Votes";
}

private void explainWithGemini() {
    if (queryPoint == null) {
        JOptionPane.showMessageDialog(this, "Add Query Point first!");
        return;
    }
    
    List<PointData> neighbors = getKNearest(queryPoint.p, kValue);
    int blueCount = (int) neighbors.stream().filter(n -> n.isBlue).count();
    int redCount = neighbors.size() - blueCount;
    String prediction = getPredictionText(blueCount, neighbors.size());
    
    // Local AI (handles ties perfectly)
    String localExplanation = String.format(
        "🤖 k-NN Analysis (k=%d):\n\n" +
        "Nearest neighbors: %d total\n" +
        "├─ Blue (Class A): %d\n" +
        "└─ Red (Class B): %d\n\n" +
        "%s\n" +
        "   (%s)",
        kValue, neighbors.size(), blueCount, redCount, 
        prediction,
        blueCount > redCount ? " The query point is assigned to   BLUE  beacuse it has the highest number of neighbors among the k nearest neighbors.!" : 
        blueCount < redCount ? "The query point is classified as RED , It has more nearest neighbors compared to other classes.!" : " Tie has  occurred , To resolve this Change the k value!"
    );
    
    JOptionPane.showMessageDialog(this, localExplanation, "🤖 AI Analysis", 
        JOptionPane.INFORMATION_MESSAGE);
}

private String extractExplanation(String json) {
    try {
        
        String[] keys = {"explanation", "message"};
        for (String key : keys) {
            int start = json.indexOf("\"" + key + "\":\"") + key.length() + 4;
            if (start > key.length() + 3) {
                int end = json.indexOf("\"", start);
                if (end > start) {
                    return json.substring(start, end).replace("\\n", "\n").replace("\\\"", "\"");
                }
            }
        }
    } catch (Exception ignored) {}
    return "PARSE_ERROR";
}
 

    

    
    public List<PointData> getKNearest(Point query, int k) {
        return points.stream()
            .sorted((a, b) -> Double.compare(dist(query, a.p), dist(query, b.p)))
            .limit(k)
            .collect(Collectors.toList());
    }
    
    public double dist(Point a, Point b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }
    
    public PointData getQueryPoint() { return queryPoint; }
    public int getKValue() { return kValue; }
    public List<PointData> getPoints() { return points; }
    
    private class CanvasPanel extends JPanel {
        public CanvasPanel() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    PointData newPoint = new PointData(e.getPoint(), e.getButton() == MouseEvent.BUTTON3);
                    points.add(newPoint);
                    repaint();
                }
            });
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            
            g2d.setColor(Color.LIGHT_GRAY);
            for (int i = 0; i < getWidth(); i += 40) g2d.drawLine(i, 0, i, getHeight());
            for (int i = 0; i < getHeight(); i += 40) g2d.drawLine(0, i, getWidth(), i);
            
            
            g2d.setColor(Color.BLUE);
            for (PointData pd : points) if (pd.isBlue) g2d.fillOval(pd.p.x-8, pd.p.y-8, 16, 16);
            g2d.setColor(Color.RED);
            for (PointData pd : points) if (!pd.isBlue) g2d.fillOval(pd.p.x-8, pd.p.y-8, 16, 16);
            
           
            if (queryPoint != null) {
                g2d.setColor(Color.GREEN);
                g2d.fillOval(queryPoint.p.x-10, queryPoint.p.y-10, 20, 20);
                
                List<PointData> neighbors = getKNearest(queryPoint.p, kValue);
                g2d.setColor(Color.YELLOW);
                g2d.setStroke(new BasicStroke(3));
                for (PointData n : neighbors) {
                    g2d.drawLine(queryPoint.p.x, queryPoint.p.y, n.p.x, n.p.y);
                }
                
                int blueCount = (int) neighbors.stream().filter(n -> n.isBlue).count();
                String prediction = blueCount > neighbors.size()/2 ? "Class A (Blue)" : "Class B (Red)";
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Arial", Font.BOLD, 20));
                g2d.drawString("Prediction: " + prediction, 20, 50);
                g2d.drawString("k = " + kValue + " neighbors highlighted", 20, 80);
            }
            
            
            g2d.setColor(Color.BLUE); g2d.fillOval(20, 110, 12, 12); g2d.setColor(Color.BLACK); g2d.drawString("Class A", 40, 122);
            g2d.setColor(Color.RED); g2d.fillOval(20, 135, 12, 12); g2d.setColor(Color.BLACK); g2d.drawString("Class B", 40, 147);
            g2d.setColor(Color.GREEN); g2d.fillOval(20, 160, 12, 12); g2d.setColor(Color.BLACK); g2d.drawString("Query", 40, 172);
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            g2d.drawString("Right-click: Blue, Left-click: Red", 20, getHeight()-40);
        }
    }
}
