package visual;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import test.stateRecord;

public class AESVisualizerGUI extends JFrame {
    private JLabel roundTitleLabel;
    private JTextArea subBytesArea, shiftRowsArea, mixColumnsArea, addRoundKeyArea;
    private JButton nextRoundButton, autoPlayButton, resetButton;
    private LinkedList<stateRecord> steps;
    private int currentRound = 0;
    private Timer autoPlayTimer;
    private JProgressBar progressBar;
    private JLabel statusLabel;
    private String processName;
    private Color[] matrixColors = {
            new Color(33, 150, 243),
            new Color(76, 175, 80),
            new Color(255, 152, 0),
            new Color(233, 30, 99)
    };

    public AESVisualizerGUI(LinkedList<stateRecord> steps, String processName) {
        this.processName = processName;
        this.steps = steps;
        initializeUI();
        showNextRound();
    }

    public void updateVisualization(LinkedList<stateRecord> newSteps) {
        this.steps = newSteps;
        this.progressBar.setMaximum(newSteps.size());
        resetVisualFrame();
        showNextRound();
    }

    private void resetVisualFrame() {
        stopAutoPlay();
        currentRound = 0;
        progressBar.setValue(0);
        roundTitleLabel.setText(this.processName + " Initial Round");
        statusLabel.setText("Ready");
        nextRoundButton.setEnabled(true);
        autoPlayButton.setEnabled(true);

        // Clear matrices
        subBytesArea.setText("");
        shiftRowsArea.setText("");
        mixColumnsArea.setText("");
        addRoundKeyArea.setText("");

        // Optionally, reset any colors or highlights if necessary
        subBytesArea.setBackground(Color.WHITE);
        shiftRowsArea.setBackground(Color.WHITE);
        mixColumnsArea.setBackground(Color.WHITE);
        addRoundKeyArea.setBackground(Color.WHITE);
    }

    private void initializeUI() {
        setTitle("Visualizer");
        setSize(700, 600); // Smaller frame size
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
        getContentPane().setBackground(new Color(245, 245, 245));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMatrixPanel(), BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        headerPanel.setBackground(new Color(33, 33, 33));

        roundTitleLabel = new JLabel(this.processName + " Initial Round", SwingConstants.CENTER);
        roundTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        roundTitleLabel.setForeground(Color.WHITE);

        statusLabel = new JLabel("Ready", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusLabel.setForeground(new Color(200, 200, 200));

        progressBar = new JProgressBar(0, steps.size());
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(0, 188, 212));
        progressBar.setBackground(new Color(55, 55, 55));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(roundTitleLabel, BorderLayout.CENTER);
        titlePanel.add(statusLabel, BorderLayout.SOUTH);
        titlePanel.setOpaque(false);

        headerPanel.add(titlePanel, BorderLayout.CENTER);
        headerPanel.add(progressBar, BorderLayout.SOUTH);

        return headerPanel;
    }

    private JPanel createMatrixPanel() {
        JPanel matrixPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        matrixPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        matrixPanel.setBackground(new Color(245, 245, 245));

        subBytesArea = createMatrixTextArea(matrixColors[0]);
        shiftRowsArea = createMatrixTextArea(matrixColors[1]);
        mixColumnsArea = createMatrixTextArea(matrixColors[2]);
        addRoundKeyArea = createMatrixTextArea(matrixColors[3]);

        matrixPanel.add(createStyledMatrixPanel("SubBytes", subBytesArea, matrixColors[0]));
        matrixPanel.add(createStyledMatrixPanel("ShiftRows", shiftRowsArea, matrixColors[1]));
        matrixPanel.add(createStyledMatrixPanel("MixColumns", mixColumnsArea, matrixColors[2]));
        matrixPanel.add(createStyledMatrixPanel("AddRoundKey", addRoundKeyArea, matrixColors[3]));

        return matrixPanel;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        controlPanel.setBackground(new Color(245, 245, 245));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        nextRoundButton = createControlButton("Next Round", matrixColors[0]);
        nextRoundButton.addActionListener(e -> showNextRound());

        autoPlayButton = createControlButton("Auto Play", matrixColors[2]);
        autoPlayButton.addActionListener(e -> toggleAutoPlay());

        resetButton = createControlButton("Reset", matrixColors[3]);
        resetButton.addActionListener(e -> resetVisualization());

        controlPanel.add(nextRoundButton);
        controlPanel.add(autoPlayButton);
        controlPanel.add(resetButton);

        return controlPanel;
    }

    private JTextArea createMatrixTextArea(Color baseColor) {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(Color.WHITE);
        textArea.setFont(new Font("Consolas", Font.BOLD, 20));
        textArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(baseColor.darker(), 1),
                BorderFactory.createEmptyBorder(6, 6, 6, 6)));
        return textArea;
    }

    private JPanel createStyledMatrixPanel(String title, JTextArea textArea, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(color);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JButton createControlButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(6, 16, 6, 16)));
        return button;
    }

    private void showNextRound() {
        if (currentRound < steps.size()) {
            stateRecord step = steps.get(currentRound);
            updateUIForStep(step);
            currentRound++;

            if (currentRound == steps.size()) {
                endVisualization();
            }
        }
    }

    private void updateUIForStep(stateRecord step) {
        roundTitleLabel.setText(processName + " " + step.getRoundName());
        statusLabel.setText("Processing round " + (currentRound + 1) + " of " + steps.size());
        progressBar.setValue(currentRound + 1);

        updateMatrix(subBytesArea, step.getSubBytesMatrix(), "SubBytes");
        updateMatrix(shiftRowsArea, step.getShiftRowsMatrix(), "ShiftRows");
        updateMatrix(mixColumnsArea, step.getMixColumnsMatrix(), "MixColumns");
        updateMatrix(addRoundKeyArea, step.getAddRoundKeyMatrix(), "AddRoundKey");
    }

    private void updateMatrix(JTextArea area, int[][] matrix, String operation) {
        if (matrix != null) {
            area.setText(formatMatrix(matrix));
            animateMatrixUpdate(area, operation);
        } else {
            // area.setText("Not performed in this round");
            area.setBackground(new Color(250, 250, 250));
        }
    }

    private void animateMatrixUpdate(JTextArea area, String operation) {
        Color originalColor = Color.WHITE;
        Color highlightColor = new Color(255, 255, 200);

        Timer flashTimer = new Timer(150, e -> {
            area.setBackground(highlightColor);
            ((Timer) e.getSource()).stop();

            Timer resetTimer = new Timer(150, ev -> {
                area.setBackground(originalColor);
                ((Timer) ev.getSource()).stop();
            });
            resetTimer.setRepeats(false);
            resetTimer.start();
        });
        flashTimer.setRepeats(false);
        flashTimer.start();
    }

    private void toggleAutoPlay() {
        if (autoPlayTimer == null || !autoPlayTimer.isRunning()) {
            startAutoPlay();
        } else {
            stopAutoPlay();
        }
    }

    private void startAutoPlay() {
        autoPlayTimer = new Timer(2000, e -> {
            if (currentRound < steps.size()) {
                showNextRound();
            } else {
                stopAutoPlay();
            }
        });
        autoPlayTimer.start();
        autoPlayButton.setText("Pause");
        autoPlayButton.setBackground(matrixColors[2].darker());
        statusLabel.setText("Auto-play enabled");
    }

    private void stopAutoPlay() {
        if (autoPlayTimer != null) {
            autoPlayTimer.stop();
        }
        autoPlayButton.setText("Auto Play");
        autoPlayButton.setBackground(matrixColors[2]);
        statusLabel.setText("Auto-play paused");
    }

    private void resetVisualization() {
        stopAutoPlay();
        currentRound = 0;
        progressBar.setValue(0);
        nextRoundButton.setEnabled(true);
        autoPlayButton.setEnabled(true);
        statusLabel.setText("Visualization reset");
        showNextRound();
    }

    private void endVisualization() {
        nextRoundButton.setEnabled(false);
        autoPlayButton.setEnabled(false);
        statusLabel.setText("Visualization complete!");
        progressBar.setForeground(new Color(76, 175, 80));

        // Get final encrypted text
        stateRecord finalStep = steps.getLast(); // Or steps.get(steps.size() - 1)
        String encryptedText = finalStep.getEncryptedText();

        // Show it in a dialog box
        JOptionPane.showMessageDialog(
                this,
                "AES Encryption Complete!\nEncrypted Text:\n" + encryptedText,
                "Encryption Finished",
                JOptionPane.INFORMATION_MESSAGE);

    }

    private String formatMatrix(int[][] matrix) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : matrix) {
            for (int value : row) {
                sb.append(String.format("%02X ", value));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
