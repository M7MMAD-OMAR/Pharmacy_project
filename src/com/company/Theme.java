package com.company;

import sun.util.resources.cldr.se.CurrencyNames_se;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

public class Theme {
    private Font font_button = new Font("font_button", Font.BOLD, 25);
    private Font font_label = new Font("font_label", Font.PLAIN, 23);
    private Font font_tf = new Font("font", Font.PLAIN, 20);
    private Color font_color_button = new Color(0, 0, 0);
    private Color font_color = new Color(0, 0, 0);
    private Color background_color_button = new Color(102, 233, 124);
    private Color background = new Color(236, 236, 236, 143);
    private Color border_color = new Color(0, 0, 0);
    private final int border = 1;
    private Color tf_ColorBorderNew = new Color(135, 183, 255);

    public void getImageLogo(JFrame frame) {
        try {
            File fileImage = new File("src/images/Logo.png");
            Image imageLogo = ImageIO.read(fileImage);
            frame.setIconImage(imageLogo);
        } catch (IOException ioException) {
            ioException.getStackTrace();
        }
    }


    public void btn_MouseListener(JButton button) {
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
            }
            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }
            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
            }
            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                button.setBackground(new Color(246, 244, 244));
                button.setBorder(BorderFactory.createLineBorder(tf_ColorBorderNew, 2, true));
                Cursor btn_cursor = new Cursor(Cursor.HAND_CURSOR);//HAND_CURSOR,,TEXT_CURSOR
                button.setCursor(btn_cursor);
            }
            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                button.setBackground(background_color_button);
                button.setBorder(BorderFactory.createLineBorder(border_color, border, true));
            }
        });
    }
    public void tf_focusEvent(JTextField textField) {
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                textField.setBorder(BorderFactory.createLineBorder(tf_ColorBorderNew, border, true));
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                textField.setBorder(BorderFactory.createLineBorder(border_color, border, true));
            }
        });

    }

    public Color getFont_color_button() {
        return font_color_button;
    }

    public Color getBackground_color_button() {
        return background_color_button;
    }

    public Color getBackground() {
        return background;
    }

    public Color getFont_color() {
        return font_color;
    }

    public Color getBorder_color() {
        return border_color;
    }

    public Font getFont_button() {
        return font_button;
    }

    public Font getFont_label() {
        return font_label;
    }

    public Font getFont_tf() {
        return font_tf;
    }

    public int getBorder() {
        return border;
    }

    public Color getTf_ColorBorderNew() {
        return tf_ColorBorderNew;
    }
}
