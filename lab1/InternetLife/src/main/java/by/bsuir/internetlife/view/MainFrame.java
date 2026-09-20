package by.bsuir.internetlife.view;

import by.bsuir.internetlife.controller.InternetLifeController;
import by.bsuir.internetlife.model.CalculationResult;
import by.bsuir.internetlife.model.InternetLifeModel;
import by.bsuir.internetlife.model.ModelObserver;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Главное окно: кнопка ввода, метки с последними данными и результатами.
 * Подписано на активную модель и обновляется по её уведомлению.
 */
public class MainFrame extends JFrame implements ModelObserver {
    private static final Color BACKGROUND = new Color(246, 247, 251);
    private static final Color CARD = Color.WHITE;
    private static final Color ACCENT = new Color(37, 99, 235);
    private static final Color TEXT = new Color(17, 24, 39);
    private static final Color MUTED = new Color(107, 114, 128);

    private final InternetLifeModel model;
    private final JLabel hoursValue = valueLabel("—");
    private final JLabel yearsValue = valueLabel("—");
    private final JLabel memesValue = valueLabel("—");
    private final JLabel adsValue = valueLabel("—");
    private final DecimalFormat yearsFormat;
    private final DecimalFormat hoursFormat;
    private final DecimalFormat countFormat;

    private InternetLifeController controller;
    private String lastEnteredHours = "";

    public MainFrame(InternetLifeModel model) {
        super("Сколько ты проживёшь в интернете");
        this.model = model;

        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(new Locale("ru", "RU"));
        yearsFormat = new DecimalFormat("#0.00", symbols);
        hoursFormat = new DecimalFormat("#0.##", symbols);
        countFormat = new DecimalFormat("#,##0", symbols);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(640, 520));

        JPanel root = new JPanel(new BorderLayout(0, 16));
        root.setBackground(BACKGROUND);
        root.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        root.add(createHeader(), BorderLayout.NORTH);
        root.add(createResultsCard(), BorderLayout.CENTER);
        root.add(createFooter(), BorderLayout.SOUTH);
        setContentPane(root);
        pack();
        setLocationRelativeTo(null);
    }

    public void setController(InternetLifeController controller) {
        this.controller = controller;
    }

    public String getLastEnteredHours() {
        return lastEnteredHours;
    }

    public void rememberEnteredHours(String hoursText) {
        this.lastEnteredHours = hoursText == null ? "" : hoursText;
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void modelChanged() {
        if (!model.hasResult()) {
            return;
        }
        CalculationResult result = model.getResult();
        hoursValue.setText(hoursFormat.format(result.getHoursPerDay()) + " ч");
        yearsValue.setText(yearsFormat.format(result.getYearsOfLife()) + " лет");
        memesValue.setText(countFormat.format(result.getMemesSeen()));
        adsValue.setText(countFormat.format(result.getAdsScrolled()));
    }

    private JPanel createHeader() {
        JLabel title = new JLabel("Сколько ты проживёшь в интернете");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel("<html>Введите, сколько часов в день вы проводите онлайн."
                + "<br>Программа оценит, сколько лет жизни уйдёт на интернет, сколько мемов"
                + " вы увидите и сколько рекламы пролистаете за 50 лет такой привычки.</html>");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(MUTED);

        JButton enterButton = new JButton("Ввести данные");
        enterButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        enterButton.setBackground(ACCENT);
        enterButton.setForeground(Color.WHITE);
        enterButton.setFocusPainted(false);
        enterButton.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        enterButton.addActionListener(event -> {
            if (controller != null) {
                controller.onEnterData();
            }
        });

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        title.setAlignmentX(LEFT_ALIGNMENT);
        subtitle.setAlignmentX(LEFT_ALIGNMENT);
        enterButton.setAlignmentX(LEFT_ALIGNMENT);
        header.add(title);
        header.add(Box.createVerticalStrut(8));
        header.add(subtitle);
        header.add(Box.createVerticalStrut(16));
        header.add(enterButton);
        return header;
    }

    private JPanel createResultsCard() {
        JPanel grid = new JPanel(new GridLayout(2, 2, 12, 12));
        grid.setOpaque(false);
        grid.add(metric("Часов онлайн в день", hoursValue));
        grid.add(metric("Лет жизни уйдёт на интернет", yearsValue));
        grid.add(metric("Мемов увидите", memesValue));
        grid.add(metric("Рекламы пролистаете", adsValue));

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235)),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        card.add(grid, BorderLayout.CENTER);
        return card;
    }

    private JPanel createFooter() {
        JLabel note = new JLabel("<html>Оценка: 120 мемов и 72 рекламных объявления в час онлайн."
                + " Горизонт расчёта — 50 лет.</html>", SwingConstants.LEFT);
        note.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        note.setForeground(MUTED);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.add(note, BorderLayout.CENTER);
        return footer;
    }

    private JPanel metric(String caption, JLabel value) {
        JLabel captionLabel = new JLabel(caption);
        captionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        captionLabel.setForeground(MUTED);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(249, 250, 251));
        panel.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        captionLabel.setAlignmentX(LEFT_ALIGNMENT);
        value.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(captionLabel);
        panel.add(Box.createVerticalStrut(6));
        panel.add(value);
        return panel;
    }

    private static JLabel valueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 22));
        label.setForeground(TEXT);
        return label;
    }
}
