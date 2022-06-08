package com.company;

import com.mysql.cj.exceptions.DataReadException;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class frame_showData extends Errors implements ActionListener, interfaceImages{
    private Theme theme  = new Theme();
    private JFrame frame;
    private JPanel panel_orderTable, panel_detailsTable, panel_dataChooser_update, panel_tabbedPane,
            panel_upOrders, panel_upOrderDetails, panel_upButtons;
    private JLayeredPane  layeredPane_upOrders;
    private JButton  btn_saveUpdate, btn_backShowOrders, btn_okDelete, btn_noDelete,  btn_deleteOrders,
            btn_deleteAll;
    private JLabel  label_upSupplierName, label_upOrderDate, label_upDiscount, label_upProductName,
            label_upMadeBy, label_upQuantity, label_upUnitPrice, labelDelete, label_iconDelete,
            label_iconSearchOrders, label_iconSearchOrderDetails;
    private JTextField tf_upProductName, tf_upUnitPrice,
            tf_searchOrders, tf_searchOrderDetails;//tf_upSupplierName  tf_upMadeBy,
    private JComboBox comboBoxSuppliers, comboBoxMadeBy;
    private JSpinner  spinner_upQuantity, spinner_upDiscount;
    private DefaultTableModel model_orderTable, model_detailsTable;
    private JTable table_order, table_details;
    private JScrollPane sp_orderTable, sp_detailsTable;
    private JTabbedPane tabbedPane_tables;
    private int index_order, index_details;
    private JDialog dialogDelete;
    private Connection connection;
    private PreparedStatement ps;
    private ResultSet rs;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Calendar calender = Calendar.getInstance();
    private JDateChooser dateChooser_update = new JDateChooser(calender.getTime());
    private SpinnerModel model_discount = new SpinnerNumberModel(0.0, 0.0, 1.0, 0.01);
    private SpinnerModel model_quantity = new SpinnerNumberModel(1, 1, 100000, 1);
    private ArrayList<JLabel> list_label = new ArrayList<>();
    private ArrayList<JTextField> list_tf = new ArrayList<>();
    private ArrayList<Orders_arrayList> arrayList_order = new ArrayList<>();
    private ArrayList<OrderDetails_arrayList> arrayList_orderDetails = new ArrayList<>();
    private ArrayList<OrderDetails_arrayList> newArrayListOrderDetails = new ArrayList<>();
    private ArrayList<Orders_arrayList> newArrayListOrders = new ArrayList<>();
    private int[] arrOrdersInSelected;
    private ArrayList<Integer> arrayListOrdersIDInSelected = new ArrayList<>();
    private ArrayList<String> arrayListComboBoxSuppliers = new ArrayList<>();
    private ArrayList<String> arrayListComboBoxMadeBy = new ArrayList<>();
    private Cursor hand_cursor = new Cursor(Cursor.HAND_CURSOR);

    public frame_showData() {
        create_frame_showOrder();
    }

    //   الواجهة الرئيسية
    private void create_frame_showOrder() {

        element_identifications();
        deleteOrdersIsNull();
        columnAdd_AND_tabbedPane_tables();
        addTheme_elements();

        try {
            // اضافة اسماء الموردين الى مصفوفة الموردين من اجل  comboBox
            add_itemSupplierName();
            // اضافة اسم الشركة المصنعة الى مصفوفة الشركة المصنعة من اجل  comboBox
            add_itemMadeBy();
            // اضافة العناصر لجدول الفواتير
            createShowDataInTableOrders();
            //   اضافة تفاصيل في جدول التفاصيل لأول فاتورة موجودة
            createShowOrderDetails(0);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (Exception exception) {
            exception.getStackTrace();
        }

        show(panel_upOrders);
        hide(panel_upOrderDetails);

        label_iconSearchOrderDetails.setIcon(icon_search);
        label_iconSearchOrderDetails.setFont(theme.getFont_tf());
        label_iconSearchOrderDetails.setBounds(285, 30, 80, 30);
        tf_searchOrderDetails.setBounds(40, 30, 240, 30);
        tf_searchOrderDetails.setFont(theme.getFont_tf());
        theme.tf_focusEvent(tf_searchOrderDetails);

        comboBoxSuppliers.setBackground(Color.white);
        comboBoxSuppliers.setFont(theme.getFont_tf());
        comboBoxSuppliers.setEditable(true);
        comboBoxMadeBy.setBackground(Color.white);
        comboBoxMadeBy.setFont(theme.getFont_tf());
        comboBoxMadeBy.setEditable(true);

        label_iconSearchOrders.setIcon(icon_search);
        label_iconSearchOrders.setFont(theme.getFont_tf());
        tf_searchOrders.setFont(theme.getFont_tf());
        theme.tf_focusEvent(tf_searchOrders);

        tabbedPane_tables.setMinimumSize(new Dimension(20,30));
        btn_saveUpdate.setCursor(hand_cursor);
        btn_deleteAll.setCursor(hand_cursor);
        btn_deleteOrders.setCursor(hand_cursor);
        btn_backShowOrders.setCursor(hand_cursor);
        btn_saveUpdate.setBackground(new Color(248, 248, 248));
        btn_saveUpdate.setFont(theme.getFont_button());
        btn_deleteOrders.setFont(theme.getFont_button());
        btn_deleteOrders.setBackground(new Color(248, 248, 248));
        btn_deleteAll.setFont(theme.getFont_button());
        btn_deleteAll.setBackground(new Color(248, 248, 248));
        btn_backShowOrders.setFont(theme.getFont_button());
        btn_backShowOrders.setBackground(new Color(248, 248, 248));
        layeredPane_upOrders.setLayout(new GridLayout());
        layeredPane_upOrders.setBounds(0, 0, 800, 500);
        panel_upButtons.setBackground(new Color(236, 236, 236));
        panel_upOrderDetails.setBackground(new Color(236, 236, 236));
        panel_upOrders.setBackground(new Color(236, 236, 236));

        //   خصائص للجداول
        table_order.setColumnSelectionAllowed(false);
        sp_orderTable.setViewportView(table_order);
        table_details.setColumnSelectionAllowed(false);
        sp_detailsTable.setViewportView(table_details);
        table_order.setFont(new Font("font", Font.PLAIN, 16));
        table_order.setBackground(new Color(250, 249, 249));
        table_order.setForeground(Color.black);
        table_order.setSelectionBackground(new Color(182, 205, 255));
        table_order.setGridColor(Color.black);
        table_order.setSelectionForeground(Color.black);
        table_order.setRowHeight(25);
        table_details.setFont(new Font("font", Font.PLAIN, 16));
        table_details.setBackground(new Color(250, 249, 249));
        table_details.setForeground(Color.black);
        table_details.setSelectionBackground(new Color(182, 205, 255));
        table_details.setGridColor(Color.black);
        table_details.setSelectionForeground(Color.black);
        table_details.setRowHeight(26);
        table_order.setAutoCreateRowSorter(true);
        table_details.setAutoCreateRowSorter(true);
        TableColumnModel columnModelOrders = table_order.getColumnModel();
        columnModelOrders.getColumn(0).setMinWidth(150);
        columnModelOrders.getColumn(0).setMaxWidth(170);
        columnModelOrders.getColumn(2).setMinWidth(190);
        columnModelOrders.getColumn(2).setMaxWidth(200);
        columnModelOrders.getColumn(3).setMinWidth(190);
        columnModelOrders.getColumn(3).setMaxWidth(200);
        columnModelOrders.getColumn(4).setMinWidth(190);
        columnModelOrders.getColumn(4).setMaxWidth(200);
        columnModelOrders.getColumn(5).setMinWidth(150);
        columnModelOrders.getColumn(5).setMaxWidth(170);
        columnModelOrders.setColumnMargin(10);
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        columnModelOrders.getColumn(0).setCellRenderer(cellRenderer);
        columnModelOrders.getColumn(2).setCellRenderer(cellRenderer);
        columnModelOrders.getColumn(5).setCellRenderer(cellRenderer);



        tabbedPane_tables.addTab("  Order Table", icon_table, panel_orderTable);
        tabbedPane_tables.addTab(" Details Table", icon_table, panel_detailsTable);

        panel_tabbedPane.setBounds(400, 0, 1205, 765);
        btn_saveUpdate.setBounds(190, 550, 200, 35);
        btn_backShowOrders.setBounds(30, 640, 130, 35);
        label_upSupplierName.setBounds(40, 150, 150, 30);
        comboBoxSuppliers.setBounds(40, 185, 300, 35);
        label_upOrderDate.setBounds(40, 250, 150, 30);
        panel_dataChooser_update.setBounds(40, 285, 300, 35);
        label_upDiscount.setBounds(40, 350, 150, 30);
        spinner_upDiscount.setBounds(40, 385, 150, 35);
        label_upProductName.setBounds(40, 100, 150, 30);
        tf_upProductName.setBounds(40, 135, 300, 35);
        label_upMadeBy.setBounds(40, 190, 150, 30);
        comboBoxMadeBy.setBounds(40, 225, 300, 35);
        label_upQuantity.setBounds(40, 280, 150, 30);
        spinner_upQuantity.setBounds(40, 315, 150, 35);
        label_upUnitPrice.setBounds(40, 370, 150, 30);
        tf_upUnitPrice.setBounds(40, 405, 150, 35);
        btn_deleteAll.setBounds(190, 640, 200, 35);
        btn_deleteOrders.setBounds(30, 550, 130, 35);
        tf_searchOrders.setBounds(40, 30, 240, 30);
        label_iconSearchOrders.setBounds(285, 30, 80, 30);

        dateChooser_update.setDateFormatString("yyyy-MM-dd");
        dateChooser_update.setFont(new Font("date", Font.PLAIN, 18));
        spinner_upDiscount.setModel(model_discount);
        spinner_upDiscount.setFont(theme.getFont_tf());
        spinner_upDiscount.setBorder(BorderFactory.createLineBorder(theme.getBorder_color(), theme.getBorder(), true));
        spinner_upQuantity.setModel(model_quantity);
        spinner_upQuantity.setFont(theme.getFont_tf());
        spinner_upQuantity.setBorder(BorderFactory.createLineBorder(theme.getBorder_color(), theme.getBorder(), true));

        theme.getImageLogo(frame);


        panel_upButtons.add(btn_deleteAll);
        panel_upButtons.add(btn_deleteOrders);
        panel_upButtons.add(btn_backShowOrders);
        panel_upButtons.add(btn_saveUpdate);
        panel_upOrders.add(panel_dataChooser_update);
        panel_upOrders.add(label_upSupplierName);
        panel_upOrders.add(comboBoxSuppliers);
        panel_upOrders.add(label_upOrderDate);
        panel_upOrders.add(label_upDiscount);
        panel_upOrders.add(spinner_upDiscount);
        panel_upOrders.add(tf_searchOrders);
        panel_upOrders.add(label_iconSearchOrders);

        panel_upOrderDetails.add(label_iconSearchOrderDetails);
        panel_upOrderDetails.add(tf_searchOrderDetails);
        panel_upOrderDetails.add(label_upProductName);
        panel_upOrderDetails.add(tf_upProductName);
        panel_upOrderDetails.add(label_upMadeBy);
        panel_upOrderDetails.add(comboBoxMadeBy);
        panel_upOrderDetails.add(label_upQuantity);
        panel_upOrderDetails.add(spinner_upQuantity);
        panel_upOrderDetails.add(label_upUnitPrice);
        panel_upOrderDetails.add(tf_upUnitPrice);
        panel_dataChooser_update.add(dateChooser_update);
        panel_orderTable.add(sp_orderTable);
        panel_detailsTable.add(sp_detailsTable);

        panel_tabbedPane.add(tabbedPane_tables);
        frame.add(panel_tabbedPane);


        frame.add(layeredPane_upOrders);
        frame.add(panel_upButtons);

        frame.setTitle("Show Orders Window");

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1610, 800);
//        frame.setLocation(200, 120);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        events();
    }

    //   تعريف العناصر
    private void element_identifications() {
        frame = new JFrame("Object");
        panel_orderTable = new JPanel(new GridLayout());
        panel_detailsTable = new JPanel(new GridLayout());
        layeredPane_upOrders = new JLayeredPane();
        panel_upOrders = new JPanel(null);
        panel_upOrderDetails = new JPanel(null);
        panel_upButtons = new JPanel(null);
        panel_dataChooser_update = new JPanel(new GridLayout());
        panel_tabbedPane = new JPanel(new GridLayout());
        model_orderTable = new DefaultTableModel();
        model_detailsTable = new DefaultTableModel();
        table_order = new JTable(model_orderTable);
        table_details = new JTable(model_detailsTable);
        sp_orderTable = new JScrollPane(table_order);
        sp_detailsTable = new JScrollPane(table_details);
        tabbedPane_tables = new JTabbedPane();
        spinner_upQuantity = new JSpinner();
        spinner_upDiscount = new JSpinner();
        btn_saveUpdate = new JButton("حفظ التعديل");
        btn_backShowOrders = new JButton("رجوع");
        label_upSupplierName = new JLabel("Supplier Name");
        label_upOrderDate = new JLabel("Order Date");
        label_upDiscount = new JLabel("Discount");
        comboBoxSuppliers = new JComboBox();
        label_upProductName = new JLabel("Product Name");
        label_upMadeBy = new JLabel("Made By");
        label_upQuantity = new JLabel("Quantity");
        label_upUnitPrice = new JLabel("Unit Price");
        tf_upProductName = new JTextField();
        comboBoxMadeBy = new JComboBox();
        tf_upUnitPrice = new JTextField();
        btn_deleteOrders = new JButton("حذف");
        tf_searchOrderDetails = new JTextField();
        label_iconSearchOrderDetails = new JLabel("بحث");
        tf_searchOrders = new JTextField();
        label_iconSearchOrders = new JLabel("بحث");
        btn_deleteAll = new JButton("حذف الكل");
    }

    //   اضافة الأعمدة للجداول
    private void columnAdd_AND_tabbedPane_tables() {
        layeredPane_upOrders.add(panel_upOrders, Integer.valueOf(2));
        layeredPane_upOrders.add(panel_upOrderDetails, Integer.valueOf(1));

        String[] column_orderTable = {"Order ID", "Supplier Name", "Order Date", "Total Price With Discount", "Total Price Without Discount", "Discount"};
        String[] column_detailsTable = {"Order ID", "Product ID", "Product Name", "Made By", "Quantity", "Unit Price"};
        for (String arr : column_orderTable) {
            model_orderTable.addColumn(arr);
        }
        for (String arr : column_detailsTable) {
            model_detailsTable.addColumn(arr);
        }

    }

    //  اضافة الثيم للعناصر واضافة الايقونات
    private void addTheme_elements() {
        btn_backShowOrders.setFont(theme.getFont_button());
        btn_backShowOrders.setForeground(theme.getFont_color());
        btn_backShowOrders.setBackground(theme.getBackground_color_button());

        tf_upUnitPrice.setFont(theme.getFont_tf());
        tf_upUnitPrice.setBorder(BorderFactory.createLineBorder(theme.getBorder_color(), theme.getBorder(), true));
        tf_upProductName.setFont(theme.getFont_tf());
        tf_upProductName.setBorder(BorderFactory.createLineBorder(theme.getBorder_color(), theme.getBorder(), true));

        list_label.add(label_upSupplierName);
        list_label.add(label_upOrderDate);
        list_label.add(label_upDiscount);
        list_label.add(label_upProductName);
        list_label.add(label_upMadeBy);
        list_label.add(label_upQuantity);
        list_label.add(label_upUnitPrice);

        for (JLabel list : list_label) {
            list.setFont(theme.getFont_label());
            list.setForeground(theme.getFont_color());
        }


        btn_backShowOrders.setIcon(icon_back);
        btn_saveUpdate.setIcon(icon_save_24);
        btn_deleteOrders.setIcon(icon_delete);
        btn_deleteAll.setIcon(icon_delete);
        btn_deleteAll.setIcon(icon_delete);

    }

    //   اضافة الحركات والأحداث
    private void events() {
        btn_saveUpdate.addActionListener(this);
        theme.tf_focusEvent(tf_upProductName);
        theme.tf_focusEvent(tf_upUnitPrice);

        tf_searchOrders.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent keyEvent) {
                String textOrder = tf_searchOrders.getText().toLowerCase();
                String textDetails = tf_searchOrderDetails.getText().toLowerCase();
                Orders_arrayList orders;
                Object[] row = new Object[6];
                newArrayListOrders.clear();
                if (!(textOrder.length() == 0)) {
                    for (int i = 0; i < arrayList_order.size(); i++) {
                        while (textOrder.contains(arrayList_order.get(i).getSupplierName().toLowerCase()) ||
                                textOrder.contains(arrayList_order.get(i).getOrderId()+""))
                        {
                            orders = new Orders_arrayList(arrayList_order.get(i).getOrderId(),
                                    arrayList_order.get(i).getSupplierName(),
                                    arrayList_order.get(i).getOrderDate(),
                                    arrayList_order.get(i).getTotal_pr_with_dis(),
                                    arrayList_order.get(i).getTotal_pr_without_dis(),
                                    arrayList_order.get(i).getDiscount());
                            newArrayListOrders.add(orders);
                            break;
                        }
                    }
                    model_orderTable.setNumRows(0);
                    model_orderTable.setRowCount(0);
                    for (int i = 0; i < newArrayListOrders.size(); i++) {
                        row[0] = newArrayListOrders.get(i).getOrderId();
                        row[1] = newArrayListOrders.get(i).getSupplierName();
                        row[2] = newArrayListOrders.get(i).getOrderDate();
                        row[3] = newArrayListOrders.get(i).getTotal_pr_with_dis();
                        row[4] = newArrayListOrders.get(i).getTotal_pr_without_dis();
                        row[5] = newArrayListOrders.get(i).getDiscount();
                        model_orderTable.addRow(row);
                    }
                    createSearchAfter(0);
                }else if(textDetails.length()==0) {
                    createShowOrderDetails(0);
                    model_detailsTable.setNumRows(0);
                    model_detailsTable.setRowCount(0);
                    createShowDataInTableOrders();
                }
            }
        });

        tf_searchOrderDetails.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent keyEvent) {
            String text = tf_searchOrderDetails.getText().toLowerCase();
            String textOrder = tf_searchOrders.getText().toLowerCase();
            OrderDetails_arrayList orderdetails;
            Object[] row = new Object[6];
            newArrayListOrderDetails.clear();
            if (!(text.length() == 0)) {
                for (int i = 0; i < arrayList_orderDetails.size(); i++) {
                    while (text.contains(arrayList_orderDetails.get(i).getMadeBy().toLowerCase()) ||
                            text.contains(arrayList_orderDetails.get(i).getProductName().toLowerCase()) ||
                            text.contains(arrayList_orderDetails.get(i).getProductId()+""))
                    {
                        orderdetails = new OrderDetails_arrayList(
                                arrayList_orderDetails.get(i).getOrderId(),
                                arrayList_orderDetails.get(i).getProductId(),
                                arrayList_orderDetails.get(i).getProductName(),
                                arrayList_orderDetails.get(i).getMadeBy(),
                                arrayList_orderDetails.get(i).getQuantity(),
                                arrayList_orderDetails.get(i).getUnitPrice());
                        newArrayListOrderDetails.add(orderdetails);
                        break;
                    }
                }
                model_detailsTable.setNumRows(0);
                model_detailsTable.setRowCount(0);
                for (int i = 0; i < newArrayListOrderDetails.size(); i++) {
                    row[0] = newArrayListOrderDetails.get(i).getOrderId();
                    row[1] = newArrayListOrderDetails.get(i).getProductId();
                    row[2] = newArrayListOrderDetails.get(i).getProductName();
                    row[3] = newArrayListOrderDetails.get(i).getMadeBy();
                    row[4] = newArrayListOrderDetails.get(i).getQuantity();
                    row[5] = newArrayListOrderDetails.get(i).getUnitPrice();
                    model_detailsTable.addRow(row);
                }
            }else if (textOrder.length() == 0){
                createShowOrderDetails(index_order);
            }else {
            }
            }
        });

        tf_upUnitPrice.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() >= '0' && e.getKeyChar() <= '9') {
                    tf_upUnitPrice.setEditable(true);
                }else if (e.getKeyChar() == '.' || e.getKeyCode() == 8) {
                    tf_upUnitPrice.setEditable(true);
                }else {
                    tf_upUnitPrice.setEditable(false);
                    new DialogPublicError("خطأ : يرجى كتابة أعداد فقط", icon_error_50);
                }
            }
        });
        table_details.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (tf_searchOrderDetails.getText().length() > 0) {
                    try{
                        index_details = table_details.getSelectedRow();
                        tf_upProductName.setText(newArrayListOrderDetails.get(index_details).getProductName());
                        comboBoxMadeBy.setSelectedItem(newArrayListOrderDetails.get(index_details).getMadeBy());
                        int quantity = newArrayListOrderDetails.get(index_details).getQuantity();
                        Float  fUnitPrice = newArrayListOrderDetails.get(index_details).getUnitPrice();
                        String unitPrice = fUnitPrice.toString();
                        spinner_upQuantity.setValue(quantity);
                        tf_upUnitPrice.setText(unitPrice);
                    }catch (Exception exception) {
                        exception.getStackTrace();
                    }
                }else {
                    try{
                        index_details = table_details.getSelectedRow();
                        tf_upProductName.setText(arrayList_orderDetails.get(index_details).getProductName());
                        comboBoxMadeBy.setSelectedItem(arrayList_orderDetails.get(index_details).getMadeBy());
                        int quantity = arrayList_orderDetails.get(index_details).getQuantity();
                        Float  fUnitPrice = arrayList_orderDetails.get(index_details).getUnitPrice();
                        String unitPrice = fUnitPrice.toString();
                        spinner_upQuantity.setValue(quantity);
                        tf_upUnitPrice.setText(unitPrice);
                    }catch (Exception exception) {
                        exception.getStackTrace();
                    }
                }
            }
        });


        table_details.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == e.VK_UP || e.getKeyCode() == e.VK_DOWN) {
                    if (tf_searchOrderDetails.getText().length() > 0) {
                        try{
                        index_details = table_details.getSelectedRow();
                        tf_upProductName.setText(newArrayListOrderDetails.get(index_details).getProductName());
                        comboBoxMadeBy.setSelectedItem(newArrayListOrderDetails.get(index_details).getMadeBy());
                        int quantity = newArrayListOrderDetails.get(index_details).getQuantity();
                        Float  fUnitPrice = newArrayListOrderDetails.get(index_details).getUnitPrice();
                        String unitPrice = fUnitPrice.toString();
                        spinner_upQuantity.setValue(quantity);
                        tf_upUnitPrice.setText(unitPrice);
                    }catch (Exception exception) {
                        exception.getStackTrace();
                    }
                    }else {
                        try{
                            index_details = table_details.getSelectedRow();
                            tf_upProductName.setText(arrayList_orderDetails.get(index_details).getProductName());
                            comboBoxMadeBy.setSelectedItem(arrayList_orderDetails.get(index_details).getMadeBy());
                            int quantity = arrayList_orderDetails.get(index_details).getQuantity();
                            Float  fUnitPrice = arrayList_orderDetails.get(index_details).getUnitPrice();
                            String unitPrice = fUnitPrice.toString();
                            spinner_upQuantity.setValue(quantity);
                            tf_upUnitPrice.setText(unitPrice);
                        }catch (Exception exception) {
                            exception.getStackTrace();
                        }
                    }
                }
            }
        });
        table_order.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == e.VK_UP || e.getKeyCode() == e.VK_DOWN) {
                    if (tf_searchOrders.getText().length() > 0) {
                        try{
                        index_order = table_order.getSelectedRow();
                        createSearchAfter(index_order);
                        comboBoxSuppliers.setSelectedItem(newArrayListOrders.get(index_order).getSupplierName());
                        spinner_upDiscount.setValue(newArrayListOrders.get(index_order).getDiscount());
                        int orderId = newArrayListOrders.get(index_order).getOrderId();
                        Date date = get_dateWithOrderId(orderId);
                        dateChooser_update.setDate(date);
                    }catch (Exception exception) {
                        exception.getStackTrace();
                    }
                    }else {
                        try{
                            index_order = table_order.getSelectedRow();
                            createShowOrderDetails(index_order);
                            comboBoxSuppliers.setSelectedItem(arrayList_order.get(index_order).getSupplierName());
                            spinner_upDiscount.setValue(arrayList_order.get(index_order).getDiscount());
                            int orderId = arrayList_order.get(index_order).getOrderId();
                            Date date = get_dateWithOrderId(orderId);
                            dateChooser_update.setDate(date);
                        }catch (Exception exception) {
                            exception.getStackTrace();
                        }
                    }
                }
            }
        });

        table_order.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (tf_searchOrders.getText().length() > 0) {
                    try{
                        index_order = table_order.getSelectedRow();
                        createSearchAfter(index_order);
                        comboBoxSuppliers.setSelectedItem(newArrayListOrders.get(index_order).getSupplierName());
                        spinner_upDiscount.setValue(newArrayListOrders.get(index_order).getDiscount());
                        int orderId = newArrayListOrders.get(index_order).getOrderId();
                        Date date = get_dateWithOrderId(orderId);
                        dateChooser_update.setDate(date);
                    }catch (Exception exception) {
                        exception.getStackTrace();
                    }
                }else {
                    try{
                        index_order = table_order.getSelectedRow();
                        createShowOrderDetails(index_order);
                        comboBoxSuppliers.setSelectedItem(arrayList_order.get(index_order).getSupplierName());
                        spinner_upDiscount.setValue(arrayList_order.get(index_order).getDiscount());
                        int orderId = arrayList_order.get(index_order).getOrderId();
                        Date date = get_dateWithOrderId(orderId);
                        dateChooser_update.setDate(date);
                    }catch (Exception exception) {
                        exception.getStackTrace();
                    }
                }
            }
        });

        tabbedPane_tables.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (tabbedPane_tables.getSelectedIndex() == 1) {
                    layeredPane_upOrders.setLayer(panel_upOrders, 1);
                    layeredPane_upOrders.setLayer(panel_upOrderDetails, 2);
                    tf_searchOrderDetails.setText("");
                    hide(panel_upOrders);
                    show(panel_upOrderDetails);
                }else if (tabbedPane_tables.getSelectedIndex() == 0) {
                    layeredPane_upOrders.setLayer(panel_upOrders, 2);
                    layeredPane_upOrders.setLayer(panel_upOrderDetails, 1);
                    hide(panel_upOrderDetails);
                    tf_upProductName.setText("");
                    spinner_upQuantity.setValue(1);
                    tf_upUnitPrice.setText("");
                    show(panel_upOrders);
                }
            }
        });
        btn_backShowOrders.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                deleteOrdersIsNull();
                spinner_upDiscount.setValue(0.0);
                dateChooser_update.setDate(calender.getTime());
                arrayList_order.clear();
                arrayList_orderDetails.clear();
                new main_project();
                hide(frame);
            }
        });

        btn_deleteOrders.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // فيها ترتيب العناصر المحددة في الجدول
                if (tabbedPane_tables.getSelectedIndex() == 0) {
                    arrOrdersInSelected = table_order.getSelectedRows();
                    try {
                        if (tf_searchOrders.getText().length() == 0) {
                            for (int i : arrOrdersInSelected) {
                                //  جلبنا orderId الفاتورة عن طريق ترتيبها بالجدول
                                arrayListOrdersIDInSelected.add(arrayList_order.get(i).getOrderId());
                            }
                            DialogDeleteOrders();
                        }else {
                            for (int i : arrOrdersInSelected) {
                                //  جلبنا orderId الفاتورة عن طريق ترتيبها بالجدول
                                arrayListOrdersIDInSelected.add(newArrayListOrders.get(i).getOrderId());
                            }
                            DialogDeleteOrders();
                        }
                    }catch (IndexOutOfBoundsException error) {
                        new DialogPublicError("يوجد مشكلة : لم يتم العثور على بيانات لإجراء التعديل", icon_error_50);
                    }

                }else if (tabbedPane_tables.getSelectedIndex() == 1) {
                    try{
                        if (tf_searchOrderDetails.getText().length() == 0) {
                            int orderId = arrayList_orderDetails.get(index_details).getOrderId();
                            int productId = arrayList_orderDetails.get(index_details).getProductId();
                                 //  دالة لحذف التفاصيل عن طريق معرف الفاتورة ومعرف المنتج
                            delete_orderDetails(orderId, productId);
                            thread_add("تم الحذف",400);
                            comboBoxMadeBy.setSelectedIndex(0);
                            tf_upProductName.setText("");
                            tf_searchOrderDetails.setText("");
                            tf_upUnitPrice.setText("");
                            spinner_upQuantity.setValue(0.0);
                        }else {
                            int orderId = newArrayListOrderDetails.get(index_details).getOrderId();
                            int productId = newArrayListOrderDetails.get(index_details).getProductId();
                            //  دالة لحذف التفاصيل عن طريق معرف الفاتورة ومعرف المنتج
                            delete_orderDetails(orderId, productId);
                            thread_add("تم الحذف",400);
                            comboBoxMadeBy.setSelectedIndex(0);
                            tf_upProductName.setText("");
                            tf_searchOrderDetails.setText("");
                            tf_upUnitPrice.setText("");
                            spinner_upQuantity.setValue(0.0);
                        }
                    } catch(IndexOutOfBoundsException error) {
                        error.getStackTrace();
                        new DialogPublicError("يوجد مشكلة : لم يتم العثور على بيانات ", icon_error_50);
                    }

                }
            }
        });


        btn_deleteAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (tabbedPane_tables.getSelectedIndex() == 0) {
                    DialogDeleteAllOrders();
                }else {
                    int orderId = arrayList_order.get(index_order).getOrderId();
                    try {
                        deleteAllDetails(orderId);
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                }
            }
        });


    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btn_saveUpdate) {
            if (tabbedPane_tables.getSelectedIndex() == 0) {
                try{
                    int orderID = arrayList_order.get(index_order).getOrderId();
                    if (UpdateDataInOrders(orderID)) {
                        createShowDataInTableOrders();
                        tf_searchOrders.setText("");
                        spinner_upDiscount.setValue(0.0);
                        dateChooser_update.setDate(calender.getTime());
                    }
                } catch (IndexOutOfBoundsException error) {
                    new DialogPublicError("يوجد مشكلة : لم يتم العثور على بيانات لإجراء التعديل", icon_error_50);
                }
            }else if (tabbedPane_tables.getSelectedIndex() == 1) {
                try {
                    if (tf_searchOrderDetails.getText().length() > 0 && tf_searchOrders.getText().length() > 0) {
                        int orderID = newArrayListOrderDetails.get(index_details).getOrderId();
                        int productID = newArrayListOrderDetails.get(index_details).getProductId();
                        update_dateInorderDetails(orderID, productID);
                        createSearchAfter(index_order);
                        tf_searchOrderDetails.setText("");
                    }else if (tf_searchOrderDetails.getText().length() == 0 && tf_searchOrders.getText().length() > 0) {
                        int orderID = arrayList_orderDetails.get(index_details).getOrderId();
                        int productID = arrayList_orderDetails.get(index_details).getProductId();
                        update_dateInorderDetails(orderID, productID);
                        createSearchAfter(index_order);
                        tf_searchOrderDetails.setText("");
                    }else if(tf_searchOrderDetails.getText().length() == 0 && tf_searchOrders.getText().length() == 0) {
                        int orderID = arrayList_orderDetails.get(index_details).getOrderId();
                        int productID = arrayList_orderDetails.get(index_details).getProductId();
                        update_dateInorderDetails(orderID, productID);
                        createShowOrderDetails(index_order);
                        tf_searchOrderDetails.setText("");
                    }else if(tf_searchOrders.getText().length() ==0 && tf_searchOrderDetails.getText().length()>0){
                        int orderID = arrayList_orderDetails.get(index_details).getOrderId();
                        int productID = arrayList_orderDetails.get(index_details).getProductId();
                        update_dateInorderDetails(orderID, productID);
                        createShowOrderDetails(index_order);
                        tf_searchOrderDetails.setText("");
                    }

                } catch (IndexOutOfBoundsException error) {
                    new DialogPublicError("يوجد مشكلة : لم يتم العثور على بيانات لإجراء التعديل", icon_error_50);
                } catch (SQLException error) {
                    error.getStackTrace();
                }
            }
        }

    }

    //  اضافة عناصر ل comboBox_MadeBy
    private void add_itemMadeBy() throws SQLException {
        arrayListComboBoxMadeBy.clear();
        String sql = "SELECT DISTINCT Made_By FROM `products`";
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()) {
                arrayListComboBoxMadeBy.add(rs.getString("Made_By"));
            }
            for (int i = 0; i < arrayListComboBoxMadeBy.size(); i++) {
                comboBoxMadeBy.addItem(arrayListComboBoxMadeBy.get(i));
            }
          }catch (SQLException sqlException) {
            sqlException.getStackTrace();
        }finally {
            connection.close();
        }
    }

    //  اضافة عناصر ل comboBox_supplierName
    private void add_itemSupplierName() throws SQLException {
        arrayListComboBoxSuppliers.clear();
        String sql = "SELECT DISTINCT Supplier_name FROM orders";
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()) {
                arrayListComboBoxSuppliers.add(rs.getString("Supplier_name"));
            }
            for (int i = 0; i < arrayListComboBoxSuppliers.size(); i++) {
                comboBoxSuppliers.addItem(arrayListComboBoxSuppliers.get(i));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }finally{
            connection.close();
        }
    }


    //   حذف جميع الفواتير التي لا منتجات لها
    private void deleteOrdersIsNull() {
        String sql = "DELETE FROM orders WHERE OrderID in (SELECT OrderID FROM (SELECT o.OrderID, od.OrderID as 'new' FROM orders o LEFT JOIN orderdetails od \n" +
                "on o.OrderID=od.OrderID\n" +
                "HAVING od.OrderID is null) newtable)";
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    //   حذف جميع البيانات الفواتير مع تفاصيلها
    private void deleteAllData() throws SQLException {
        String sql1 = "DELETE FROM orderdetails";
        String sql2 = "DELETE FROM orders";

        try {
            connection = getConnection();
            ps = connection.prepareStatement(sql1);
            ps.executeUpdate();
            ps = connection.prepareStatement(sql2);
            ps.executeUpdate();
            thread_add("تم الحذف",400);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }finally {
            connection.close();
        }
    }

    // حذف جميع التفاصيل حسب الفاتورة
    private void deleteAllDetails(int orderID) throws SQLException {
        String sql = "DELETE FROM orderdetails WHERE OrderID = ?";
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, orderID);
            ps.executeUpdate();
            createShowOrderDetails(index_order);
            thread_add("تم الحذف", 400);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }finally{
            connection.close();
        }
    }

    //   تعديل بيانات تفاصيل الفاتورة مع فحص القيم
    private void update_dateInorderDetails(int orderID, int productID) throws SQLException {
        String sql1 = "UPDATE `products` SET `Product_name`= ? ,`Made_By`= ?  WHERE ProductID = ? ";
        String sql2 = "UPDATE `orderdetails` SET `Quantity`= ? ,`Unit_Price`= ? WHERE OrderID = ? AND ProductID = ? ";

        try {
            String productName = tf_upProductName.getText();
            String madeBy = comboBoxMadeBy.getSelectedItem().toString();
            errorLength(productName, productName, "يوجد مشكلة : يرجى إضافة اسم منتج, لا يمكن أن يكون الحقل فارغ",
                    "يوجد مشكلة : لا يمكن أن يكون اسم المنتج أقل من حرفان",
                    "يوجد مشكلة : لا يمكن أن يكون اسم المنتج أكثر من 40 حرف ", 40);
            errorLength(madeBy, madeBy, "يوجد مشكلة : يرجى إضافة اسم الشركة المصنعة لا يمكن أن يكون الحقل فارغ",
                    "يوجد مشكلة : لا يمكن أن يكون اسم الشركة المصنعة أقل من حرفان",
                    "يوجد مشكلة : لا يمكن أن يكون اسم الشركة المصنعة أكثر من 40 حرف ", 40);

            try {
                Object objectQuantity = spinner_upQuantity.getValue();
                Float quantity = Float.valueOf(objectQuantity.toString());
                Float unitPrice = Float.valueOf(tf_upUnitPrice.getText());
                connection = getConnection();
                ps = connection.prepareStatement(sql1);
                ps.setString(1, productName);
                ps.setString(2, madeBy);
                ps.setInt(3, productID);
                ps.executeUpdate();
                connection = getConnection();
                ps = connection.prepareStatement(sql2);
                ps.setFloat(1, quantity);
                ps.setFloat(2, unitPrice);
                ps.setInt(3, orderID);
                ps.setInt(4, productID);
                ps.executeUpdate();
                if (tf_searchOrderDetails.getText().length() > 0) {
                    createSearchAfter(index_order);
                }else {
                    createShowOrderDetails(index_order);
                }
                thread_add("تم تعديل البيانات",400);
                tf_upProductName.setText("");
                comboBoxMadeBy.setSelectedIndex(0);
                spinner_upQuantity.setValue(1);
                tf_upUnitPrice.setText("");
            }  catch(MysqlDataTruncation error) {
                new DialogPublicError("يوجد مشكلة : قيمة السعر كبيرة جدا يرجى إضافة قيمة أقل منها", icon_error_50);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            } finally {
                connection.close();
            }
        }catch (error_length error) {
            new DialogPublicError(error.getMessage(),icon_error_50);
        } catch (NumberFormatException nuForExc) {
            new DialogPublicError("يوجد مشكلة : يرجى إضافة سعر للمنتج", icon_error_50);
        }catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    //  ميثور تعديل بيانات جدول الفواتير update order
    private boolean UpdateDataInOrders(int  orderID) {
        String sql1 = "UPDATE `orders` SET `Supplier_name`= ? , `Date`= ?  WHERE OrderID = ? ";
        String sql2 = "UPDATE `orderdetails` SET `discount`= ? WHERE OrderID = ? ";
        try{
            String supplierName = comboBoxSuppliers.getSelectedItem().toString() , date;
            Object discountObject = spinner_upDiscount.getValue();
            Float discount = Float.valueOf(discountObject.toString());
            date = setOrderDate(dateChooser_update);
            errorLength(supplierName, supplierName, "خطأ : يرجى كتابة اسم المورد لا يمكن ترك الحقل فارغ",
                    "خطأ : لا يمكن أن يكون اسم المورد أقل من حرفان",
                    "خطأ : لا يمكن أن يكون اسم المورد أكثر من 40 حرف", 40);
            connection = getConnection();
            ps = connection.prepareStatement(sql1);
            ps.setString(1, supplierName);
            ps.setString(2, date);
            ps.setInt(3, orderID);
            ps.executeUpdate();
            ps = connection.prepareStatement(sql2);
            ps.setFloat(1, discount);
            ps.setInt(2, orderID);
            ps.executeUpdate();
            createShowDataInTableOrders();
            thread_add("تم تعديل البيانات", 400);
            return true;
        } catch (error_length error) {
            new DialogPublicError(error.getMessage(), icon_error_50);
            return false;
        }catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return false;
        }

    }

        //   فحص التاريخ اذا كان صحيح
    private  String  setOrderDate(JDateChooser dateChooser) {
        String  date = null;
        try{
            date = sdf.format(dateChooser.getDate());
            return date;
        } catch(Exception error) {
            new DialogPublicError("خطأ : يرجى كتابة التاريخ بهذه الصيغة   yyyy-MM-dd", icon_errorDate_50);
        }
        return date;
    }

    //   اظهار البيانات داخل جدول الفواتير
    private void createShowDataInTableOrders() {
        arrayList_order.clear();
        model_orderTable.setNumRows(0);
        model_orderTable.setRowCount(0);

        String sql ="SELECT o.OrderID,o.Supplier_name,o.Date,SUM(od.Quantity*od.Unit_Price-od.Quantity*od.Unit_Price*od.discount)\n" +
                "AS 'total price with discount', SUM(od.Quantity*od.Unit_Price) AS 'total price without discount', od.discount\n" +
                "FROM orders o LEFT JOIN orderdetails od \n" +
                "on o.OrderID=od.OrderID \n" +
                "    GROUP BY o.OrderID\n" +
                "    ORDER BY o.OrderID DESC\n";

        try{
            connection = getConnection();
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            Orders_arrayList orders;
            Object[] arrObject = new Object[6];
            while (rs.next()) {
                orders =new Orders_arrayList(rs.getInt("OrderID"),rs.getString("Supplier_name")
                        , rs.getString("Date"), rs.getFloat("total price with discount"),
                        rs.getFloat("total price without discount"),rs.getFloat("discount"));
                arrayList_order.add(orders);
            }
            for (int i = 0; i < arrayList_order.size(); i++) {
                arrObject[0] = arrayList_order.get(i).getOrderId();
                arrObject[1] = arrayList_order.get(i).getSupplierName();
                arrObject[2] = arrayList_order.get(i).getOrderDate();
                arrObject[3] = arrayList_order.get(i).getTotal_pr_with_dis();
                arrObject[4] = arrayList_order.get(i).getTotal_pr_without_dis();
                arrObject[5] = arrayList_order.get(i).getDiscount();
                model_orderTable.addRow(arrObject);

            }
        }catch(DataReadException error) {
            new DialogPublicError( "عذرا حدثت مشكلة لم يتم العثور على بيانات لعرضها", icon_errorDate_50);
        }catch(NullPointerException error) {

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }finally {
            try {
                connection.close();
            } catch (SQLException sqlException) {
            }
        }

    }

    //    اظهار تفاصيل الفاتورة في جدول التفاصيل
    private void createShowOrderDetails(int index) {
        arrayList_orderDetails.clear();

        String sql ="SELECT od.OrderID, p.ProductID, p.Product_name, p.Made_By, od.Quantity, od.Unit_Price\n" +
                "                FROM products p INNER JOIN orderdetails od\n" +
                "                on p.ProductID=od.ProductID \n" +
                "                HAVING od.OrderID = ? \n" +
                "                order BY p.ProductID  ASC";
        try{
            int order = arrayList_order.get(index).getOrderId();
            model_detailsTable.setRowCount(0);
            model_detailsTable.setNumRows(0);
            model_detailsTable.setColumnCount(6);
            connection = getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, order);
            rs = ps.executeQuery();
            OrderDetails_arrayList orderDetails_arrayList;
            Object[] arr = new Object[6];
                while (rs.next()) {
                    orderDetails_arrayList = new OrderDetails_arrayList(rs.getInt("OrderID"),
                            rs.getInt("ProductID"), rs.getString("Product_name"),
                            rs.getString("Made_By"),rs.getInt("Quantity"),
                            rs.getFloat("Unit_Price"));
                    arrayList_orderDetails.add(orderDetails_arrayList);
                }


            for (int i = 0; i < arrayList_orderDetails.size(); i++) {
                arr[0] = arrayList_orderDetails.get(i).getOrderId();
                arr[1] = arrayList_orderDetails.get(i).getProductId();
                arr[2] = arrayList_orderDetails.get(i).getProductName();
                arr[3] = arrayList_orderDetails.get(i).getMadeBy();
                arr[4] = arrayList_orderDetails.get(i).getQuantity();
                arr[5] = arrayList_orderDetails.get(i).getUnitPrice();
                model_detailsTable.addRow(arr);
            }

            TableColumnModel columnModelOrderDetails = table_details.getColumnModel();
            columnModelOrderDetails.getColumn(0).setMinWidth(170);
            columnModelOrderDetails.getColumn(0).setMaxWidth(190);
            columnModelOrderDetails.getColumn(1).setMinWidth(170);
            columnModelOrderDetails.getColumn(1).setMaxWidth(190);
            columnModelOrderDetails.getColumn(4).setMinWidth(170);
            columnModelOrderDetails.getColumn(4).setMaxWidth(190);
            columnModelOrderDetails.getColumn(5).setMinWidth(170);
            columnModelOrderDetails.getColumn(5).setMaxWidth(190);
            columnModelOrderDetails.setColumnMargin(10);
            DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
            cellRenderer.setHorizontalAlignment(JLabel.CENTER);
            columnModelOrderDetails.getColumn(0).setCellRenderer(cellRenderer);
            columnModelOrderDetails.getColumn(1).setCellRenderer(cellRenderer);
            columnModelOrderDetails.getColumn(4).setCellRenderer(cellRenderer);
            columnModelOrderDetails.getColumn(5).setCellRenderer(cellRenderer);


        } catch(IndexOutOfBoundsException error) {

        }catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    //     اظهار تفاصيل الفاتورة في جدول التفاصيل عن طريق مصفوفة جديدة
    private void createSearchAfter(int index) {
        arrayList_orderDetails.clear();
        String sql ="SELECT od.OrderID, p.ProductID, p.Product_name, p.Made_By, od.Quantity, od.Unit_Price\n" +
                "                FROM products p INNER JOIN orderdetails od\n" +
                "                on p.ProductID=od.ProductID \n" +
                "                HAVING od.OrderID = ? \n" +
                "                order BY p.ProductID  ASC";
        try{
            int order = newArrayListOrders.get(index).getOrderId();
            model_detailsTable.setRowCount(0);
            model_detailsTable.setNumRows(0);
            model_detailsTable.setColumnCount(6);
            connection = getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, order);
            rs = ps.executeQuery();
            OrderDetails_arrayList orderDetails_arrayList;
            Object[] arr = new Object[6];
            while (rs.next()) {
                orderDetails_arrayList = new OrderDetails_arrayList(rs.getInt("OrderID"),
                        rs.getInt("ProductID"), rs.getString("Product_name"),
                        rs.getString("Made_By"),rs.getInt("Quantity"),
                        rs.getFloat("Unit_Price"));
                arrayList_orderDetails.add(orderDetails_arrayList);
            }


            for (int i = 0; i < arrayList_orderDetails.size(); i++) {
                arr[0] = arrayList_orderDetails.get(i).getOrderId();
                arr[1] = arrayList_orderDetails.get(i).getProductId();
                arr[2] = arrayList_orderDetails.get(i).getProductName();
                arr[3] = arrayList_orderDetails.get(i).getMadeBy();
                arr[4] = arrayList_orderDetails.get(i).getQuantity();
                arr[5] = arrayList_orderDetails.get(i).getUnitPrice();
                model_detailsTable.addRow(arr);
            }
        } catch(IndexOutOfBoundsException error) {

        }catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    //         إظهار رسالة   الإضاافة
    private void thread_add(String msg,int sleep) {
        Runnable runnable = () -> {
            DialogAdd dialogName = new DialogAdd(msg);
            dialogName.show();
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dialogName.hide();
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    ///    ترجع التاريخ عن طريق معرف الفاتورة من قاعدة البيانات
    private Date get_dateWithOrderId(int orderID) {
        String sql = "SELECT Date FROM `orders` WHERE OrderID = ? ";
        Date date = null ;
        try{
            connection = getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, orderID);
            rs = ps.executeQuery();
            rs.next();
            date = rs.getDate("Date");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return date;
    }

        // حذف البيانات من جدول تفاصيل الفاتورة
    private void delete_orderDetails(int orderId, int productId) {
        String sql = "DELETE FROM orderdetails WHERE OrderID = ? AND ProductID = ? ";
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, orderId);
            ps.setInt(2, productId);
            ps.executeUpdate();
            createShowOrderDetails(index_order);

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    //  حذف البيانات من جدول الفواتير
    private void delete_orders(int  orderId) {
        String sql1 = "DELETE FROM orderdetails WHERE OrderID = ? ";
        String sql2 = "DELETE FROM orders WHERE OrderID = ? ";
        try {

            connection = getConnection();
            ps = connection.prepareStatement(sql1);
            ps.setInt(1, orderId);
            ps.executeUpdate();
            ps = connection.prepareStatement(sql2);
            ps.setInt(1, orderId);
            ps.executeUpdate();
            createShowDataInTableOrders();
            comboBoxSuppliers.setSelectedIndex(0);
            tf_upUnitPrice.setText("");
            dateChooser_update.setDate(calender.getTime());
            thread_add("تم الحذف",400);
        }catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

//    Dialog اظهار رسالة عند حذف الفاتورة
    public void DialogDeleteOrders() {
        dialogDelete = new JDialog();
        labelDelete = new JLabel("سوف يتم حذف الفاتورة مع جميع بياناتها هل تريد ذالك !");
        label_iconDelete = new JLabel();
        btn_okDelete = new JButton();
        btn_noDelete = new JButton(" NO ");
        label_iconDelete.setOpaque(true);
        label_iconDelete.setIcon(icon_how_50);
        labelDelete.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        labelDelete.setForeground(Color.black);
        btn_okDelete.setIcon(icon_ok_24_1);
        btn_noDelete.setForeground(Color.black);
        btn_noDelete.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 19));
        btn_noDelete.setCursor(hand_cursor);
        btn_okDelete.setCursor(hand_cursor);
        label_iconDelete.setBounds(20, 20, 60, 60);
        labelDelete.setBounds(100, 40, 350, 40);
        btn_okDelete.setBounds(280, 100, 100, 30);
        btn_noDelete.setBounds(100, 100, 100, 30);
        dialogDelete.setVisible(true);
        dialogDelete.setLayout(null);
        dialogDelete.setLocation(700, 400);
        dialogDelete.setSize(500, 180);
        dialogDelete.setTitle(" Message ");
        dialogDelete.setModal(false);
        dialogDelete.setResizable(false);
        dialogDelete.add(label_iconDelete);
        dialogDelete.add(labelDelete);
        dialogDelete.add(btn_okDelete);
        dialogDelete.add(btn_noDelete);
        btn_noDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dialogDelete.dispose();
            }
        });
        btn_okDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                for (Integer arr : arrayListOrdersIDInSelected) {
                    delete_orders(arr);
                }
                dialogDelete.dispose();
                createShowOrderDetails(index_order);
                Arrays.fill(arrOrdersInSelected, 0);
                arrayListOrdersIDInSelected.clear();
            }
        });
    }

    //    Dialog اظهار رسالة عند حذف  جميع الفواتير
    public void DialogDeleteAllOrders() {
        JDialog dialogDelete = new JDialog();
        JLabel labelDelete = new JLabel("سوف يتم حذف 'جميع' الفواتير مع جميع بياناتهم هل تريد ذلك !");
        JLabel label_iconDelete = new JLabel();
        JButton btn_okDelete = new JButton();
        JButton btn_noDelete = new JButton(" NO ");
        label_iconDelete.setOpaque(true);
        label_iconDelete.setIcon(icon_how_50);
        labelDelete.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        labelDelete.setForeground(Color.black);
        btn_okDelete.setIcon(icon_ok_24_1);
        btn_noDelete.setForeground(Color.black);
        btn_noDelete.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 19));
        label_iconDelete.setBounds(20, 20, 60, 60);
        labelDelete.setBounds(100, 40, 500, 40);
        btn_okDelete.setBounds(280, 100, 100, 30);
        btn_noDelete.setBounds(100, 100, 100, 30);
        dialogDelete.setVisible(true);
        dialogDelete.setLayout(null);
        dialogDelete.setLocation(700, 400);
        dialogDelete.setSize(520, 180);
        dialogDelete.setTitle(" Message ");
        dialogDelete.setModal(false);
        dialogDelete.setResizable(false);
        dialogDelete.add(label_iconDelete);
        dialogDelete.add(labelDelete);
        dialogDelete.add(btn_okDelete);
        dialogDelete.add(btn_noDelete);
        btn_noDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dialogDelete.dispose();
            }
        });
        btn_okDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    deleteAllData();
                    dialogDelete.dispose();
                    createShowDataInTableOrders();
                    createShowOrderDetails(0);
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            }
        });
    }

    //  اخفاء الواجهة
    private void hide(JFrame frame) {
        frame.dispose();
    }
    //  اظهار الواجهة
    private void show(JFrame frame) {
        frame.setVisible(true);
    }
    //  اخفاء الواجهة
    private void hide(JPanel panel) {
        panel.setVisible(false);
    }
    //  اظهار الواجهة
    private void show(JPanel panel) {
        panel.setVisible(true);
    }

    private Connection getConnection() {
        Connection connection;
        try {
            connection = DriverManager.getConnection(DB_info.url_width_encoding, DB_info.user, DB_info.password);
            return connection;
        } catch (SQLException error) {
            JOptionPane.showMessageDialog(null, error.getMessage(), " Connection Error ", JOptionPane.WARNING_MESSAGE, icon_databaseError_50);
            return null;
        }
    }



}

