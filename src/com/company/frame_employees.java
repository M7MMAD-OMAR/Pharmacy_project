package com.company;

import javax.swing.*;

public class frame_employees {
    private JPanel panel_main;
    private JPanel panel_addEmployee;
    private JPanel panel_editImage;
    private JPanel panel_table;
    private JScrollPane sp_employeeTable;
    private JTable employeeTable;
    private JButton btn_addEmployee;
    private JButton btn_back;
    private JButton btn_delete;
    private JButton btn_editSave;
    private JButton btn_allDelete;
    private JLabel label_firstName;
    private JTextField tf_firstName;
    private JLabel label_lastName;
    private JTextField tf_lastName;
    private JLabel label_address;
    private JTextField tf_address;
    private JLabel label_birthDate;
    private JPanel panel_birthDate;
    private JLabel label_hireDate;
    private JPanel panel_hireDate;
    private JLabel label_city;
    private JTextField tf_city;
    private JLabel label_phone;
    private JTextField tf_phone;
    private JLabel label_notes;
    private JScrollPane sp_notes;
    private JLabel label_image;
    private JButton btn_addImage;
    private JPanel panel_editEmployee;
    private JLabel label_addressEdit;
    private JTextField tf_addressEdit;
    private JLabel label_cityEdit;
    private JTextField tf_cityEdit;
    private JLabel label_phoneEdit;
    private JTextField tf_phoneEdit;
    private JLabel label_notesEdit;
    private JEditorPane ep_notes;
    private JScrollPane sp_notesEdit;
    private JEditorPane ep_notesEdit;


    public static void main(String[] args) {
        JFrame frame = new JFrame("frame_employees");
        frame.setContentPane(new frame_employees().panel_main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
