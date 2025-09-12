package game.ui;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class MessagePanel extends JPanel {
    private final LinkedList<String> messages = new LinkedList<>();
    private final int maxMessages = 6;

    public MessagePanel() {
        setPreferredSize(new Dimension(800, maxMessages * 20 + 60));
        setBackground(new Color(30, 30, 30));
    }

    public void logMessage(String msg) {
        if (messages.size() >= maxMessages) {
            messages.removeFirst();
        }
        messages.addLast(msg);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        int y = 20;
        for (String msg : messages) {
            g.drawString(msg, 10, y);
            y += 20;
        }
    }
}
