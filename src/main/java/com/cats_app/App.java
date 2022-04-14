package com.cats_app;

import javax.swing.JOptionPane;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        int optionMenu = -1;
        String[] buttons = { " 1. Show cats", " 2. Exit" };

        do {
            String option = (String) JOptionPane.showInputDialog(null, "Cats JAVA", "Main menu",
                    JOptionPane.INFORMATION_MESSAGE,
                    null, buttons, buttons[0]);

            for (int i = 0; i < buttons.length; i++) {
                if (option.equals(buttons[i])) {
                    optionMenu = i;
                }
            }
            switch (optionMenu) {
                case 0:
                    break;

                case 1:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        } while (optionMenu != 1);
    }
}
