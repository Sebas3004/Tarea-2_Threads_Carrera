package view;

import model.Animal;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RankingPanel extends JPanel {

    private static final Color BG_TRACK   = new Color(20, 22, 40);
    private static final Color ACCENT     = new Color(255, 210, 50);
    private static final Color TEXT_LIGHT = new Color(230, 230, 255);
    private static final Color TEXT_DIM   = new Color(120, 120, 160);

    private final List<Animal> animales;

    public RankingPanel(List<Animal> animales) {
        this.animales = animales;
        setBackground(BG_TRACK);
        setBorder(new CompoundBorder(
                new LineBorder(new Color(50, 50, 80), 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        List<Animal> ordenados = new ArrayList<>(animales);
        ordenados.sort(Comparator
                .comparingInt((Animal a) -> a.getLugar() == 0 ? Integer.MAX_VALUE : a.getLugar())
                .thenComparingInt(a -> -a.getProgreso()));

        int rowH = (getHeight() - 16) / animales.size();

        for (int pos = 0; pos < ordenados.size(); pos++) {
            Animal a = ordenados.get(pos);
            int y    = 8 + pos * rowH;
            Color c  = a.getColor();

            g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 20));
            g2.fillRoundRect(0, y, getWidth(), rowH - 2, 6, 6);

            g2.setFont(new Font("Monospaced", Font.BOLD, 13));
            g2.setColor(pos == 0 ? ACCENT : TEXT_DIM);
            g2.drawString((pos + 1) + ".", 6, y + rowH / 2 + 5);

            g2.setColor(TEXT_LIGHT);
            g2.drawString(a.getEmoji() + " " + a.getNombre(), 28, y + rowH / 2 + 5);
            
            g2.setColor(c);
            g2.drawString(a.getProgreso() + "%", getWidth() - 42, y + rowH / 2 + 5);
        }
        g2.dispose();
    }
}