package view;

import model.Animal;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.List;


public class MainView extends JFrame {

    private static final Color BG_DARK   = new Color(10, 10, 20);
    private static final Color BG_TRACK  = new Color(20, 22, 40);
    private static final Color ACCENT    = new Color(255, 210, 50);
    private static final Color TEXT_DIM  = new Color(120, 120, 160);
    private static final Color TEXT_LIGHT = new Color(230, 230, 255);

    
    private final PistaPanel   pistaPanel;
    private final RankingPanel rankingPanel;
    private final LogPanel     logPanel;


    private final JButton btnIniciar;
    private final JButton btnReset;
    private final JLabel  lblEstado;
    private final JLabel  lblGanador;

  
    private Runnable onIniciar;
    private Runnable onReset;

   
    public MainView(List<Animal> animales) {
        super("Threads_Carrera ");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 700);
        setMinimumSize(new Dimension(800, 580));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout());

  
        pistaPanel   = new PistaPanel(animales);
        rankingPanel = new RankingPanel(animales);
        logPanel     = new LogPanel();

  
        add(buildHeader(),   BorderLayout.NORTH);
        add(buildCenter(),   BorderLayout.CENTER);

    
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        footer.setBackground(BG_DARK);
        footer.setBorder(new MatteBorder(1, 0, 0, 0, new Color(50, 50, 80)));

        lblEstado  = styledLabel("Presiona  ▶  para iniciar la carrera", TEXT_DIM, 13f);
        lblGanador = styledLabel("", ACCENT, 15f);
        btnIniciar = buildButton("▶  INICIAR CARRERA", ACCENT, BG_DARK);
        btnReset   = buildButton("↺  REINICIAR", TEXT_DIM, BG_TRACK);
        btnReset.setEnabled(false);

        btnIniciar.addActionListener(e -> { if (onIniciar != null) onIniciar.run(); });
        btnReset.addActionListener(  e -> { if (onReset   != null) onReset.run();   });

        footer.add(btnIniciar);
        footer.add(btnReset);
        footer.add(lblEstado);
        footer.add(lblGanador);
        add(footer, BorderLayout.SOUTH);

        new Timer(40, e -> {
            pistaPanel.repaint();
            rankingPanel.repaint();
        }).start();
    }

    public void carreraIniciada() {
        btnIniciar.setEnabled(false);
        btnReset.setEnabled(false);
        lblGanador.setText("");
        logPanel.clear();
    }

    public void carreraFinalizada() {
        btnReset.setEnabled(true);
    }

    public void mostrarGanador(String msg) {
        lblGanador.setText(msg);
        lblEstado.setText("Carrera finalizada");
    }

    public void actualizarEstado(String msg) {
        lblEstado.setText(msg);
    }

    public void agregarLog(String msg) {
        logPanel.addEntry(msg);
    }

    public void resetVista() {
        lblEstado.setText("Listo para una nueva carrera");
        lblGanador.setText("");
        logPanel.clear();
        btnIniciar.setEnabled(true);
        btnReset.setEnabled(false);
        pistaPanel.repaint();
        rankingPanel.repaint();
    }

    public void setOnIniciar(Runnable r) { this.onIniciar = r; }
    public void setOnReset(Runnable r)   { this.onReset   = r; }

    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_TRACK);
        p.setBorder(new EmptyBorder(14, 24, 14, 24));

        JLabel title = new JLabel("CARRERA", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(ACCENT);

        

        JPanel texts = new JPanel(new GridLayout(2, 1, 0, 2));
        texts.setOpaque(false);
        texts.add(title);
        p.add(texts, BorderLayout.CENTER);
        return p;
    }

    private JSplitPane buildCenter() {
        JPanel sidePanel = new JPanel(new BorderLayout(0, 8));
        sidePanel.setBackground(BG_DARK);
        sidePanel.setBorder(new EmptyBorder(8, 4, 0, 8));

        JLabel rankTitle = styledLabel("  CLASIFICACIÓN", TEXT_DIM, 11f);
        rankTitle.setBorder(new EmptyBorder(0, 0, 4, 0));
        sidePanel.add(rankTitle, BorderLayout.NORTH);
        sidePanel.add(rankingPanel, BorderLayout.CENTER);

        JPanel logContainer = new JPanel(new BorderLayout());
        logContainer.setOpaque(false);
        logContainer.add(styledLabel("  EVENTOS EN PISTA", TEXT_DIM, 11f), BorderLayout.NORTH);
        logContainer.add(logPanel, BorderLayout.CENTER);
        logContainer.setPreferredSize(new Dimension(340, 220));
        sidePanel.add(logContainer, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pistaPanel, sidePanel);
        split.setDividerLocation(640);
        split.setDividerSize(4);
        split.setBorder(null);
        split.setBackground(BG_DARK);
        return split;
    }

    private JLabel styledLabel(String text, Color color, float size) {
        JLabel l = new JLabel(text);
        l.setForeground(color);
        l.setFont(new Font("Monospaced", Font.PLAIN, (int) size));
        return l;
    }

    private JButton buildButton(String text, Color fg, Color bg) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isEnabled() ? bg : new Color(30, 30, 50));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(isEnabled() ? fg : TEXT_DIM);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                        (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        b.setFont(new Font("Monospaced", Font.BOLD, 12));
        b.setForeground(fg);
        b.setBackground(bg);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setBorder(new EmptyBorder(8, 18, 8, 18));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}