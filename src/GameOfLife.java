import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameOfLife extends JFrame {
    Field field;

    final int borderWidth = 10;
    final int gridLineWidth = 1;
    final int cellDim = 20;

    final Color dark = Color.getHSBColor(0.35f,0.1f,0.3f);
    final Color cell = Color.getHSBColor(0.09f,0.6f,0.7f);
    final Color light = Color.getHSBColor(0.20f,0.1f,0.8f);

    final JLabel generationLabel = new JLabel() {
        @Override
        public void paint(Graphics g) {
            super.setText("Generation: %d".formatted(field.generation));
            super.paint(g);
        }
    };
    final JLabel aliveLabel = new JLabel(){
        @Override
        public void paint(Graphics g) {
            super.setText("Alive: %d".formatted(field.getAliveCount()));
            super.paint(g);
        }
    };
    final JPanel labelPanel = new JPanel();
    final JPanel fieldPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            for (int i = 0; i < field.dimension; i++) {
                for (int j = 0; j < field.dimension; j++) {
                    int x = i * (cellDim + gridLineWidth) + borderWidth;
                    int y = j * (cellDim + gridLineWidth) + 45 + borderWidth;
                    if (field.isAlive(new Point(i, j))) {
                        g.setColor(cell);
                    } else {
                        g.setColor(light);
                    }
                    g.fillRect(x, y, cellDim, cellDim);
                }
            }
        }
    };

    boolean paused = false;

    final Thread render = new Thread(new Runnable() {
        @Override
        public void run() {
            while(!Thread.interrupted() && field.getAliveCount() > 0 && !field.equals(field.nextGeneration())) {
                try {
                    Thread.sleep(400L);
                    if (!paused) {
                        field = field.nextGeneration();
                        repaint();
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    });

    final JToggleButton playToggleButton = new JToggleButton(new ImageIcon("img/pause.png"), true);
    final JButton resetButton = new JButton(new ImageIcon("img/redo.png")) ;

    public GameOfLife() {
        super("Game of Life");
        initField();
        setBackground(dark);

        setVisible(true);
        initLabelPanel();
        add(fieldPanel);

        setPreferredSize(new Dimension(labelPanel.getWidth(), labelPanel.getWidth() + labelPanel.getHeight()));
        pack();
        render.start();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private void initField() {
        this.field = new Field(30);
    }

    private void initLabelPanel() {
        generationLabel.setName("GenerationLabel");
        generationLabel.setText("Initializing...");
        aliveLabel.setName("AliveLabel");
        aliveLabel.setText("Initializing...");

        playToggleButton.setName("PlayToggleButton");
        playToggleButton.setBackground(dark);
        playToggleButton.setForeground(light);
        playToggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                paused = !paused;
                if (paused) {
                    playToggleButton.setIcon(new ImageIcon("img/play.png"));
                } else {
                    playToggleButton.setIcon(new ImageIcon("img/pause.png"));
                }
            }
        });

        resetButton.setName("ResetButton");
        resetButton.setBackground(dark);
        resetButton.setForeground(light);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                initField();
                repaint();
            }
        });

        labelPanel.add(generationLabel);
        labelPanel.add(aliveLabel);
        labelPanel.add(playToggleButton);
        labelPanel.add(resetButton);

        labelPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        labelPanel.setBackground(light);
        labelPanel.setForeground(dark);

        labelPanel.setSize(borderWidth * 2 + cellDim * field.dimension + gridLineWidth * (field.dimension - 1), 45);
        add(labelPanel);
        labelPanel.setBounds(0, 0, labelPanel.getWidth(), labelPanel.getHeight());

    }

}
