package view;

import model.Animal;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class PistaPanel extends JPanel {

    private static final Color BG_DARK   = new Color(10, 10, 20);
    private static final Color ACCENT    = new Color(255, 210, 50);
    private static final Color TEXT_LIGHT = new Color(230, 230, 255);
    private static final Color TEXT_DIM   = new Color(120, 120, 160);

    private static final int PADDING_LEFT = 110;
    private static final int PADDING_RIGHT = 70;
    private static final int PADDING_V    = 24;
    private static final int LANE_H       = 72;

    private final List<Animal> animales;

    public PistaPanel(List<Animal> animales) {
        this.animales = animales;
        setBackground(BG_DARK);
        setBorder(new EmptyBorder(8, 8, 8, 4));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int trackW = getWidth() - PADDING_LEFT - PADDING_RIGHT;

        dibujarLineasPista(g2, trackW);

        for (int i = 0; i < animales.size(); i++) {
            dibujarCarril(g2, animales.get(i), i, trackW);
        }
        g2.dispose();
    }

    private void dibujarLineasPista(Graphics2D g2, int trackW) {
        int totalH = PADDING_V + animales.size() * LANE_H;

        int metaX = PADDING_LEFT + trackW;
        g2.setColor(new Color(255, 255, 100, 80));
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                1, new float[]{6, 4}, 0));
        g2.drawLine(metaX, PADDING_V, metaX, totalH);
        g2.setFont(new Font("Monospaced", Font.BOLD, 10));
        g2.setColor(ACCENT);
        g2.drawString("META", metaX - 18, PADDING_V - 6);

        g2.setColor(new Color(255, 255, 255, 30));
        g2.drawLine(PADDING_LEFT, PADDING_V, PADDING_LEFT, totalH);
        g2.drawString("INICIO", PADDING_LEFT - 44, PADDING_V - 6);
    }

    private void dibujarCarril(Graphics2D g2, Animal animal, int idx, int trackW) {
        int y      = PADDING_V + idx * LANE_H;
        int prog   = animal.getProgreso();
        int barY   = y + LANE_H / 2 - 6;
        int barH   = 12;
        int barW   = (int) (trackW * prog / 100.0);
        Color c    = animal.getColor();

        g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 15));
        g2.fillRoundRect(PADDING_LEFT - 6, y + 4, trackW + 12, LANE_H - 8, 10, 10);
        g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 40));
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(PADDING_LEFT - 6, y + 4, trackW + 12, LANE_H - 8, 10, 10);

        g2.setFont(new Font("Monospaced", Font.BOLD, 13));
        g2.setColor(TEXT_LIGHT);
        g2.drawString(animal.getEmoji() + " " + animal.getNombre(), 4, y + 28);

        g2.setFont(new Font("Monospaced", Font.PLAIN, 10));
        g2.setColor(TEXT_DIM);
        g2.drawString(animal.getVelocidadBase() + "ms/paso", 4, y + 44);

        g2.setColor(new Color(40, 42, 60));
        g2.fillRoundRect(PADDING_LEFT, barY, trackW, barH, 6, 6);

        if (barW > 0) {
            GradientPaint gp = new GradientPaint(
                    PADDING_LEFT, barY, c,
                    PADDING_LEFT + barW, barY, c.darker());
            g2.setPaint(gp);
            g2.fillRoundRect(PADDING_LEFT, barY, barW, barH, 6, 6);

            g2.setColor(new Color(255, 255, 255, 40));
            g2.fillRoundRect(PADDING_LEFT, barY, barW, barH / 2, 6, 6);
        }

        g2.setFont(new Font("Monospaced", Font.BOLD, 11));
        g2.setColor(prog == 100 ? ACCENT : TEXT_LIGHT);
        g2.drawString(prog + "%", PADDING_LEFT + trackW + 8, barY + 10);

        if (prog > 0 && prog < 100) {
            g2.setFont(new Font("Serif", Font.PLAIN, 18));
            g2.drawString(animal.getEmoji(), PADDING_LEFT + barW - 8, barY + 14);
        }

        if (!animal.getEvento().isEmpty()) {
            g2.setFont(new Font("Monospaced", Font.ITALIC, 10));
            g2.setColor(new Color(255, 200, 80));
            g2.drawString(animal.getEvento(), PADDING_LEFT + 4, barY - 3);
        }

        if (animal.getLugar() > 0) {
            String medal = switch (animal.getLugar()) {
                case 1 -> "🥇";
                case 2 -> "🥈";
                case 3 -> "🥉";
                default -> "#" + animal.getLugar();
            };
            g2.setFont(new Font("Monospaced", Font.BOLD, 13));
            g2.setColor(ACCENT);
            g2.drawString(medal, PADDING_LEFT + trackW + 44, barY + 10);
        }
    }
}