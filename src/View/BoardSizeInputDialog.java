package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class BoardSizeInputDialog extends JDialog {
    private JTextField inputField;
    private boolean isCanceled;

    private String dialogTitle;

    public BoardSizeInputDialog(JFrame parent, String title) {
        super(parent, title, true);
        this.dialogTitle = title;

        addWindowListener(new WindowAdapter() { // for handling corner exit window button the same way as cancel button
            @Override
            public void windowClosing(WindowEvent e) {
                handleCancelButtonClick();
            }
        });

        initComponents();
    }

    private void initComponents() {
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
//        okButton.setBackground(Color.YELLOW);
//        okButton.setForeground(Color.BLACK);
//        okButton.setOpaque(true);
//        okButton.setBorderPainted(false);
        okButton.addActionListener(e -> handleOkButtonClick());
        buttonPanel.add(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(0, 0, 139)); // Dark Blue
//        cancelButton.setForeground(Color.WHITE);
//        cancelButton.setOpaque(true);
//        cancelButton.setBorderPainted(false);
        cancelButton.addActionListener(e -> handleCancelButtonClick());
        buttonPanel.add(cancelButton);

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPanel.setBackground(Color.BLACK);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getParent());
    }

    private void handleOkButtonClick() {
        isCanceled = false;
        setVisible(false);
    }

    private void handleCancelButtonClick() {
        isCanceled = true;
        setVisible(false);
    }

    public int getInputInt() {
        return isCanceled ? -1 : Integer.parseInt(inputField.getText());
    }

    public String getInputString(){
        return isCanceled ? "" : inputField.getText();
    }
}