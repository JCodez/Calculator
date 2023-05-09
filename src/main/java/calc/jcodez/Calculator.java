package calc.jcodez;

import com.formdev.flatlaf.FlatDarculaLaf;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.ValidationResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Calculator {
    private final JTextField textField;
    private final JFrame frame;

    public Calculator() {
       FlatDarculaLaf.setup();

        frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textField = new JTextField();
        textField.setEditable(false);
        textField.setFont(new Font("Arial", Font.BOLD, 24));
        textField.setHorizontalAlignment(JTextField.RIGHT);
        frame.add(textField, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 5, 5, 5));
        frame.add(buttonPanel, BorderLayout.CENTER);

        addButton(buttonPanel, "7");
        addButton(buttonPanel, "8");
        addButton(buttonPanel, "9");
        addButton(buttonPanel, "*");

        addButton(buttonPanel, "4");
        addButton(buttonPanel, "5");
        addButton(buttonPanel, "6");
        addButton(buttonPanel, "-");

        addButton(buttonPanel, "1");
        addButton(buttonPanel, "2");
        addButton(buttonPanel, "3");
        addButton(buttonPanel, "+");

        addButton(buttonPanel, "0");
        addButton(buttonPanel, "/");
        addButton(buttonPanel, ".");
        addButton(buttonPanel, "=");

        JButton clearButton = new JButton("C");
        clearButton.setFont(new Font("Arial", Font.BOLD, 24));
        clearButton.setFocusable(false);
        clearButton.addActionListener(e -> textField.setText(""));
        buttonPanel.add(clearButton);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustButtonSizes();
            }
        });

        frame.setSize(400, 600);
        frame.setVisible(true);
    }

    private void addButton(Container container, String label) {
        JButton button = new JButton(label);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setFocusable(false);

        if (label.equals("=")) {
            button.addActionListener(e -> calculateResult());
        } else {
            button.addActionListener(e -> addToTextField(label));
        }

        container.add(button);
    }

    private void addToTextField(String str) {
        textField.setText(textField.getText() + str);
    }

    private void calculateResult() {
        String expression = textField.getText();
        try {
            double result = evaluateExpression(expression);
            textField.setText(Double.toString(result));
        } catch (IllegalArgumentException e) {
            textField.setText("Error: " + e.getMessage());
        }
    }

    private double evaluateExpression(String expression) {
        try {
            Expression exp = new ExpressionBuilder(expression)
                    .build();

            ValidationResult validationResult = exp.validate();
            if (!validationResult.isValid()) {
                throw new IllegalArgumentException(validationResult.getErrors().get(0));
            }

            return exp.evaluate();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error evaluating expression: " + e.getMessage());
        }
    }

    private void adjustButtonSizes() {
        Component[] components = frame.getContentPane().getComponents();
        int buttonWidth = frame.getWidth() / 4;
        int buttonHeight = (frame.getHeight() - textField.getHeight()) / 6;

        for (Component component : components) {
            if (component instanceof JButton button) {
                button.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
                button.setFont(button.getFont().deriveFont(Font.BOLD, 24));
            }
        }
    }
}