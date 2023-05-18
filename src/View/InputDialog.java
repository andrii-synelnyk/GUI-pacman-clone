package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

public class InputDialog extends JDialog {
    private JTextField inputField;
    private boolean isCanceled;

    private String dialogTitle;

    private Font customFont;

    public InputDialog(JFrame parent, String title) {
        super(parent, title, true);
        this.dialogTitle = title;

        addWindowListener(new WindowAdapter() { // for handling corner exit window button the same way as cancel button
            @Override
            public void windowClosing(WindowEvent e) {
                handleCancelButtonClick();
            }
        });

        initComponents();

        // Import custom font
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/Font/emulogic.ttf")).deriveFont(9f);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        setFontForAllComponents(this, customFont);
        pack();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Input");
        setLayout(new BorderLayout());

        JPanel messagePanel = new JPanel();
        messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        JLabel messageLabel = new JLabel(dialogTitle);
        messageLabel.setForeground(Color.WHITE);
        messagePanel.add(messageLabel);
        messagePanel.setBackground(Color.BLACK);
        add(messagePanel, BorderLayout.NORTH);

        inputField = new JTextField(5);
        inputField.setBackground(Color.BLACK);
        inputField.setForeground(Color.WHITE);
        inputField.setCaretColor(Color.WHITE);
        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        inputPanel.add(inputField);
        inputPanel.setBackground(Color.BLACK);
        add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> handleOkButtonClick());
        buttonPanel.add(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> handleCancelButtonClick());
        buttonPanel.add(cancelButton);

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPanel.setBackground(Color.BLACK);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleOkButtonClick() {
        isCanceled = false;
        dispose();
    }

    private void handleCancelButtonClick() {
        isCanceled = true;
        dispose();
    }

    public int getInputInt() {
        return isCanceled ? -1 : Integer.parseInt(inputField.getText());
    }

    public String getInputString(){
        return isCanceled ? "" : inputField.getText();
    }

    public void setFontForAllComponents(Container container, Font font) {
        for (Component c : container.getComponents()) {
            c.setFont(font);
            if (c instanceof Container) {
                setFontForAllComponents((Container) c, font);
            }
        }
    }
}