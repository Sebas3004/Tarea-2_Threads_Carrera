package view;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LogPanel extends JPanel {

    private static final Color BG_TRACK   = new Color(20, 22, 40);
    private static final Color TEXT_LIGHT = new Color(230, 230, 255);

    private final List<String> entries = new ArrayList<>();
    private static final int MAX = 50;

    public LogPanel() {
        setBackground(BG_TRACK);
        setBorder(new CompoundBorder(
                new LineBorder(new Color(50, 50, 80), 1, true),
                new EmptyBorder(4, 8, 4, 8)
        ));
    }

    public void addEntry(String msg) {
        entries.add(0, msg);
        if (entries.size() > MAX) entries.remove(entries.size() - 1);
        repaint();
    }

    public void clear() {
        entries.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(new Font("Monospaced", Font.PLAIN, 11));

        int y = 16;
        for (int i = 0; i < entries.size() && y < getHeight(); i++) {
            float alpha = Math.max(0.2f, 1f - i * 0.08f);
            g2.setColor(new Color(
                    TEXT_LIGHT.getRed(), TEXT_LIGHT.getGreen(), TEXT_LIGHT.getBlue(),
                    (int)(alpha * 255)));
            g2.drawString(entries.get(i), 0, y);
            y += 16;
        }
        g2.dispose();
    }
}