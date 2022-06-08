package com.company;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;

public class main_project implements  interfaceImages {
    private Theme theme = new Theme();

    private JFrame frame_main = new JFrame(" Main Window ");
    private JPanel panel_main = new JPanel(null);
    private JPanel panel_title = new JPanel(new GridBagLayout());
    private JPanel panel_line = new JPanel();
    private JButton btn_addOrder = new JButton("إدخال فاتورة");
    private JButton btn_showOrder = new JButton("الفواتير");
    private JButton btn_showProducts = new JButton("عرض المنتجات");
    private JButton btn_outputOrders = new JButton("إخراج فاتورة");
    private JLabel label_titleFrameMain = new JLabel("مشروع إدارة صيدلية");
    private JMenuBar menuBar = new JMenuBar();
    private JMenu menuHome = new JMenu("   Home ");
    private JMenuItem menuEmployee = new JMenuItem("  Employee ");

    public main_project() {
        create_main_frame();
    }


    private void create_main_frame() {

        btn_showOrder.setToolTipText("عرض الفواتير");
        btn_showProducts.setToolTipText("عرض جميع المنتجات");
        btn_addOrder.setToolTipText("إضافة فاتورة جديدة");
        btn_showProducts.setIcon(icon_showTable_30);
        btn_showOrder.setIcon(iconOrder);
        btn_addOrder.setIcon(icon_addOrders);
        btn_showProducts.setFont(theme.getFont_button());
        btn_showProducts.setForeground(theme.getFont_color_button());
        btn_showProducts.setBackground(theme.getBackground_color_button());
        btn_showProducts.setBorder(BorderFactory.createLineBorder(theme.getBorder_color(), theme.getBorder(), true));
        btn_showOrder.setBorder(BorderFactory.createLineBorder(theme.getBorder_color(), theme.getBorder(), true));
        btn_outputOrders.setBorder(BorderFactory.createLineBorder(theme.getBorder_color(), theme.getBorder(), true));
        btn_addOrder.setBorder(BorderFactory.createLineBorder(theme.getBorder_color(), theme.getBorder(), true));
        btn_showOrder.setFont(theme.getFont_button());
        btn_showOrder.setForeground(theme.getFont_color_button());
        btn_showOrder.setBackground(theme.getBackground_color_button());
        btn_addOrder.setFont(theme.getFont_button());
        btn_addOrder.setForeground(theme.getFont_color_button());
        btn_addOrder.setBackground(theme.getBackground_color_button());
        btn_outputOrders.setFont(theme.getFont_button());
        btn_outputOrders.setForeground(theme.getFont_color_button());
        btn_outputOrders.setBackground(theme.getBackground_color_button());


        panel_title.setBackground(new Color(210, 210, 210));
        label_titleFrameMain.setFont(new Font("fontTitle", Font.BOLD, 35));
        label_titleFrameMain.setForeground(Color.black);

        panel_line.setBackground(Color.black);
        panel_main.setBackground(theme.getBackground());
        btn_addOrder.setBounds(100, 200, 250, 50);
        btn_outputOrders.setBounds(100, 320, 250, 50);
        btn_showOrder.setBounds(450, 320, 250, 50);
        panel_title.setBounds(0, 0, 800, 125);
        panel_line.setBounds(0, 125, 800, 1);
        panel_main.setBounds(0, 0, 800, 500);
        btn_showProducts.setBounds(450, 200, 250, 50);

        panel_title.add(label_titleFrameMain);
        panel_main.add(panel_line);
        panel_main.add(btn_addOrder);
        panel_main.add(btn_outputOrders);
        panel_main.add(btn_showOrder);
        panel_main.add(panel_title);
        panel_main.add(btn_showProducts);



        menuBar.setBackground(new Color(210, 210, 210));
        menuHome.setFont(theme.getFont_label());
        menuHome.setForeground(Color.black);
        menuBar.setBorderPainted(false);
        menuEmployee.setFont(new Font("font", Font.PLAIN, 19));
        menuEmployee.setBackground(Color.white);
        menuEmployee.setForeground(Color.black);
        menuEmployee.setIcon(icon_employeeImage);
        menuBar.add(menuHome);
        menuHome.add(menuEmployee);


        frame_main.setJMenuBar(menuBar);
        frame_main.add(panel_main);
        frame_main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame_main.setSize(800, 700);
//        frame_main.setLocation(550, 175);
        frame_main.setResizable(false);
        frame_main.setLocationRelativeTo(null);
        frame_main.setVisible(true);
        theme.getImageLogo(frame_main);

        // اضافة لون وثيم للعناصر من كلاس theme
        theme.btn_MouseListener(btn_outputOrders);
        theme.btn_MouseListener(btn_addOrder);
        theme.btn_MouseListener(btn_showOrder);
        theme.btn_MouseListener(btn_showProducts);

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (actionEvent.getSource() == menuEmployee) {
                    int result = JOptionPane.showConfirmDialog(null, "هل تريد اضافة موظف أو التعديل عليه ");
                    if (result == JOptionPane.YES_OPTION) {
                        System.out.println("ok JOptionPane");
                    }

                }
                else if (actionEvent.getSource() == btn_showProducts) {
                    try {
                        new frame_products();
                        frame_main.dispose();
                    } catch (SQLException  sqlException) {
                        sqlException.getStackTrace();
                    }
                }
                else if (actionEvent.getSource() == btn_addOrder) {
                    try {
                        new frame_orders();
                        frame_main.dispose();
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                }
                else if (actionEvent.getSource() == btn_showOrder) {
                    new frame_showData();
                    frame_main.dispose();
                }
            }
        };

        menuHome.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {}
            @Override
            public void mousePressed(MouseEvent mouseEvent) {}
            @Override
            public void mouseReleased(MouseEvent mouseEvent) {}
            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                menuHome.setForeground(Color.blue);
                menuHome.setFont(new Font("font",Font.BOLD, 23));
            }
            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                menuHome.setForeground(Color.black);
                menuHome.setFont(theme.getFont_label());
            }
        });

        btn_showProducts.addActionListener(actionListener);
        btn_showOrder.addActionListener(actionListener);
        btn_addOrder.addActionListener(actionListener);
        menuEmployee.addActionListener(actionListener);

    }


}
