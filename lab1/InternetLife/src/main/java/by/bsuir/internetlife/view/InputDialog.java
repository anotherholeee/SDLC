package by.bsuir.internetlife.view;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Дополнительное окно ввода: сколько часов в день пользователь проводит онлайн.
 */
public class InputDialog extends JDialog {
    private final JTextField hoursField = new JTextField(12);
    private boolean confirmed;

    public InputDialog(JFrame owner, String lastEnteredHours) {
        super(owner, "Ввод данных", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        ((AbstractDocument) hoursField.getDocument()).setDocumentFilter(new NumberOnlyFilter());
        hoursField.setText(lastEnteredHours == null ? "" : lastEnteredHours);
        hoursField.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel prompt = new JLabel("Сколько часов в день вы проводите онлайн?");
        prompt.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel hint = new JLabel("Можно ввести только число от 0 до 24, дробная часть через точку или запятую.");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        hint.setForeground(new Color(90, 90, 90));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(16, 20, 8, 20));
        form.setBackground(Color.WHITE);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.insets = new Insets(0, 0, 10, 0);
        form.add(prompt, constraints);

        constraints.insets = new Insets(0, 0, 6, 0);
        form.add(hoursField, constraints);

        constraints.insets = new Insets(0, 0, 0, 0);
        form.add(hint, constraints);

        JButton okButton = new JButton("Рассчитать");
        JButton cancelButton = new JButton("Отмена");
        okButton.addActionListener(event -> confirm());
        cancelButton.addActionListener(event -> dispose());
        getRootPane().setDefaultButton(okButton);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setBorder(BorderFactory.createEmptyBorder(8, 20, 16, 20));
        buttons.setBackground(Color.WHITE);
        buttons.add(cancelButton);
        buttons.add(okButton);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        root.add(form, BorderLayout.CENTER);
        root.add(buttons, BorderLayout.SOUTH);
        setContentPane(root);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent event) {
                hoursField.requestFocusInWindow();
                hoursField.selectAll();
            }
        });

        pack();
        setMinimumSize(new Dimension(420, getHeight()));
        setLocationRelativeTo(owner);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getHoursText() {
        return hoursField.getText();
    }

    private void confirm() {
        confirmed = true;
        dispose();
    }

    /**
     * Пропускает только цифры и один десятичный разделитель. Буквы и прочие символы
     * не попадают в поле даже при вставке из буфера.
     */
    private static final class NumberOnlyFilter extends DocumentFilter {
        private static boolean isNumber(String text) {
            return text.matches("\\d*([.,]\\d*)?");
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            replace(fb, offset, 0, string, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            String current = fb.getDocument().getText(0, fb.getDocument().getLength());
            String next = current.substring(0, offset)
                    + (text == null ? "" : text)
                    + current.substring(offset + length);
            if (isNumber(next)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }
}
