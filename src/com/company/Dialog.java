package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class DialogPublicError implements interfaceImages {
    private JDialog dialog;
    private JLabel label, label_icon;
    private JButton btn_ok;

    public DialogPublicError(String str, Icon icon) {
        dialog = new JDialog();
        label = new JLabel(str + "  ");
        label_icon = new JLabel();
        btn_ok = new JButton("");
        Cursor btn_cursor = new Cursor(Cursor.HAND_CURSOR);
        btn_ok.setCursor(btn_cursor);

        label_icon.setOpaque(true);
        label_icon.setIcon(icon);
        if (str.length()>60) {
            label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 17));
            btn_ok.setBounds(250, 80, 75, 30);
            dialog.setSize(570, 150);
        } else if (str.length() < 30) {
            label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 21));
            btn_ok.setBounds(140, 80, 75, 30);
            dialog.setSize(350, 150);
        }else {
            label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
            btn_ok.setBounds(250, 80, 75, 30);
            dialog.setSize(525, 150);
            }

        btn_ok.setIcon(icon_ok_24_1);
        label.setForeground(Color.black);
        label_icon.setBounds(20, 20, 60, 60);
        label.setBounds(100, 25, 400, 30);
        dialog.setBackground(Color.gray);
        dialog.setVisible(true);
        dialog.setLayout(null);
        dialog.setLocation(680, 400);
        dialog.setTitle("  Error Message");
        dialog.setModal(false);
        dialog.setResizable(false);
        dialog.add(label_icon);
        dialog.add(label);
        dialog.add(btn_ok);

        btn_ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dialog.dispose();
            }
        });
    }
}


class DialogAdd {
    private JDialog dialog;
    private JLabel label;
    public DialogAdd(String msg) {
        dialog = new JDialog();
        label = new JLabel(msg);

        label.setFont(new Font("font", Font.ITALIC, 18));
        label.setForeground(Color.black);
        dialog.setBackground(Color.gray);

        dialog.setLocation(800,100);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(300, 125);
        dialog.setTitle("Dialog");
        dialog.setModal(false);
        dialog.add(label);

    }

    //    Dialog إظهار الـ
    public void show() {
        dialog.setVisible(true);
    }
    //    Dialog إخفاء الـ
    public void hide() {
        dialog.dispose();
    }
}


