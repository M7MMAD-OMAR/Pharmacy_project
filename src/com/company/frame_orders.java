package com.company;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class frame_orders extends Errors implements interfaceImages{
    private Theme theme = new Theme();
    private JFrame frame;
    private JPanel panel_order, panel_order_details, panel_date, panel_newOrDe, panel_newOrDeLine;
    private JLayeredPane layeredPane_orders;
    private JButton btn_backOrder, btn_add_details, btn_orDe_added, btn_mainHome, btn_addNewOrders;
    private JLabel label_madeBy,  label_supplierName, label_orderDate,
            label_orDe_product, label_quantity, label_unitPrice, label_discount, label_newOrderID,
            label_newSupplierName, label_employeeID;
    private JTextField tf_madeBy, tf_orDe_productName, tf_unitPrice, tf_employeeID;//  tf_supplierName
    private JComboBox comboBox_supplierName;
    private JSpinner spinner_quantity, spinner_discount;
    private int index_productID;
    private Calendar calender = Calendar.getInstance();
    private JDateChooser dateChooser = new JDateChooser(calender.getTime());
    private SpinnerModel model_discount = new SpinnerNumberModel(0.0, 0.0, 1.0, 0.01);
    private SpinnerModel model_quantity = new SpinnerNumberModel(1, 1, 100000, 1);
    private ArrayList<JButton> list_btn = new ArrayList<>();
    private ArrayList<JLabel> list_label = new ArrayList<>();
    private ArrayList<JTextField> list_tf = new ArrayList<>();
    private ArrayList<String> list_supplierName = new ArrayList<>();
    private Connection connection;
    private PreparedStatement ps;
    private ResultSet rs;
    private int orderID;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");



    public frame_orders() throws SQLException {
        create_fromOrders();
    }


    //  انشاء الواجهة الرئيسية لاضافة فاتورة
    private void create_fromOrders() throws SQLException {

        element_identifications();
        addTheme_element();
        getLayeredPane();
        add_itemSupplierName();

        show(panel_order);
        hide(panel_order_details);

        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.setFont(new Font("date", Font.PLAIN, 18));
        spinner_discount.setModel(model_discount);
        spinner_discount.setFont(theme.getFont_tf());
        spinner_discount.setBorder(BorderFactory.createLineBorder(theme.getBorder_color(), theme.getBorder(), true));
        spinner_discount.setBounds(235, 370, 150, 35);
        label_discount.setBounds(50, 370, 150, 35);

        comboBox_supplierName.setBackground(Color.white);
        comboBox_supplierName.setFont(theme.getFont_tf());
        comboBox_supplierName.setEditable(true);

        label_supplierName.setBounds(50, 190, 200, 35);
        tf_employeeID.setBounds(235, 130, 310, 35);
        label_employeeID.setBounds(50, 130, 200, 35);
        comboBox_supplierName.setBounds(235, 190, 310, 35);
        panel_date.setBounds(235, 280, 310, 35);
        label_orderDate.setBounds(50, 280, 200, 35);
        btn_backOrder.setBounds(50, 500, 180, 40);
        btn_add_details.setBounds(280, 500, 250, 40);
        panel_order.setBounds(0, 0, 600, 750);
        panel_order_details.setBounds(0, 0, 600, 750);
        panel_order.setBackground(theme.getBackground());


        theme.getImageLogo(frame);
        panel_order.add(tf_employeeID);
        panel_order.add(label_employeeID);
        panel_date.add(dateChooser);
        panel_order.add(btn_add_details);
        panel_order.add(btn_backOrder);
        panel_order.add(panel_date);
        panel_order.add(label_orderDate);
        panel_order.add(label_supplierName);
        panel_order.add(comboBox_supplierName);
        panel_order.add(spinner_discount);
        panel_order.add(label_discount);

        frame.setTitle("Orders");
        frame.add(layeredPane_orders);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(600, 750);
//        frame.setLocation(630, 140);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        events();


    }

    // انشاء واجهة لإضافة تفاصسيل الفاتورة
    private void create_panel_orderDetails() {
        show(panel_order_details);
        frame.setTitle(" Order Details Window ");
        hide(panel_order);

        panel_order_details.setBackground(theme.getBackground());
        spinner_quantity.setModel(model_quantity);
        spinner_quantity.setFont(theme.getFont_tf());
        spinner_quantity.setBorder(BorderFactory.createLineBorder(theme.getBorder_color(), theme.getBorder(), true));

        btn_orDe_added.setFont(theme.getFont_label());
        btn_orDe_added.setBackground(theme.getBackground_color_button());
        label_newOrderID.setFont(new Font("font", Font.ITALIC, 20));
        label_newSupplierName.setFont(new Font("font", Font.ITALIC, 20));
        panel_newOrDeLine.setBackground(Color.black);

        btn_mainHome.setIcon(icon_home_2);
        btn_mainHome.setBackground(theme.getBackground_color_button());
        btn_mainHome.setFont(theme.getFont_tf());
//        btn_addNewOrders.setIcon(icon_addOrders);
        btn_addNewOrders.setBackground(theme.getBackground_color_button());
        btn_addNewOrders.setFont(theme.getFont_tf());


        panel_newOrDe.setBackground(new Color(217, 217, 217));
        label_madeBy.setBounds(50, 170, 150, 35);
        tf_madeBy.setBounds(220, 170, 320, 35);
        label_orDe_product.setBounds(50, 100, 150, 35);
        label_quantity.setBounds(50, 240, 150, 35);
        label_unitPrice.setBounds(50, 310, 150, 35);
        btn_orDe_added.setBounds(175, 390, 230, 30);
        tf_orDe_productName.setBounds(220, 100, 320, 35);
        spinner_quantity.setBounds(220, 240, 150, 35);
        tf_unitPrice.setBounds(220, 310, 150, 35);
        btn_mainHome.setBounds(320, 470, 200, 35);
        btn_addNewOrders.setBounds(75, 470, 200, 35);
        label_newOrderID.setBounds(120, 20, 350, 50);
        label_newSupplierName.setBounds(115, 80, 500, 50);
        panel_newOrDe.setBounds(0, 540, 600, 750);
        panel_newOrDeLine.setBounds(0, 0, 600, 1);




        panel_newOrDe.add(label_newOrderID);
        panel_newOrDe.add(label_newSupplierName);
        panel_newOrDe.add(panel_newOrDeLine);

        panel_order_details.add(label_madeBy);
        panel_order_details.add(tf_madeBy);
        panel_order_details.add(label_orDe_product);
        panel_order_details.add(label_quantity);
        panel_order_details.add(label_unitPrice);
        panel_order_details.add(btn_orDe_added);
        panel_order_details.add(tf_orDe_productName);
        panel_order_details.add(spinner_quantity);
        panel_order_details.add(tf_unitPrice);
        panel_order_details.add(btn_mainHome);
        panel_order_details.add(btn_addNewOrders);
        panel_order_details.add(panel_newOrDe);
    }
    //   تعريف العناصر
    private void element_identifications() {
        //  frame order .........
        frame = new JFrame(" Orders Window");
        panel_order = new JPanel(null);
        panel_date = new JPanel(new GridLayout());
        layeredPane_orders = new JLayeredPane();
        label_supplierName = new JLabel("Supplier Name");
        label_orderDate = new JLabel("Order Date");
        comboBox_supplierName = new JComboBox();
        btn_backOrder = new JButton("رجوع");
        btn_add_details = new JButton("إضافة تفاصيل للفاتورة");
        tf_employeeID = new JTextField();
        label_employeeID = new JLabel("Employee Name");

//        BasicArrowButton(4, theme.getFont_color(),theme.getBorder_color(),Color.red,Color.blue);
        ///   panel order details ........
        panel_order_details = new JPanel(null);
        label_orDe_product = new JLabel("Product Name");
        label_quantity = new JLabel("Quantity");
        label_unitPrice = new JLabel("Unit Price");
        label_discount = new JLabel("Discount");
        label_newOrderID = new JLabel("");
        label_newSupplierName = new JLabel("");
        tf_orDe_productName = new JTextField();
        spinner_quantity = new JSpinner();
        tf_unitPrice = new  JTextField();
        spinner_discount = new JSpinner();
        label_madeBy = new JLabel("Made By");
        tf_madeBy = new  JTextField();
        btn_orDe_added = new JButton("إضافة المنتج");
        btn_mainHome = new JButton("الصفحة الرئيسية");
        btn_addNewOrders = new JButton("إضافة فاتورة جديدة");
        panel_newOrDe = new JPanel(null);
        panel_newOrDeLine = new JPanel();


    }
    //    اضافة للعناصر خصائص وايقونات
    private void addTheme_element() {
        list_btn.add(btn_backOrder);
        list_btn.add(btn_add_details);
        list_btn.add(btn_orDe_added);

        list_label.add(label_madeBy);
        list_label.add(label_supplierName);
        list_label.add(label_orderDate);
        list_label.add(label_orDe_product);
        list_label.add(label_quantity);
        list_label.add(label_unitPrice);
        list_label.add(label_discount);
        list_label.add(label_newSupplierName);
        list_label.add(label_newOrderID);
        list_label.add(label_employeeID);

        list_tf.add(tf_employeeID);
        list_tf.add(tf_madeBy);
        list_tf.add(tf_orDe_productName);
        list_tf.add(tf_unitPrice);

        for (JButton list : list_btn) {
            list.setFont(theme.getFont_button());
            list.setForeground(theme.getFont_color());
            list.setBackground(theme.getBackground_color_button());
        }
        for (JLabel list : list_label) {
            list.setFont(theme.getFont_label());
            list.setForeground(theme.getFont_color());
        }
        for (JTextField list : list_tf) {
            list.setFont(theme.getFont_tf());
            list.setBorder(BorderFactory.createLineBorder(theme.getBorder_color(), theme.getBorder(), true));
        }

        btn_backOrder.setIcon(icon_back);
    }

    private void getLayeredPane() {
        layeredPane_orders.add(panel_order, Integer.valueOf(2));
        layeredPane_orders.add(panel_order_details, Integer.valueOf(1));
    }

    private void events(){
        theme.tf_focusEvent(tf_madeBy);
        theme.tf_focusEvent(tf_unitPrice);
//        theme.comboBox_focusEvent(comboBox_supplierName);
        theme.tf_focusEvent(tf_orDe_productName);
        theme.btn_MouseListener(btn_add_details);
        theme.btn_MouseListener(btn_orDe_added);
        theme.btn_MouseListener(btn_backOrder);
        theme.btn_MouseListener(btn_mainHome);
        theme.btn_MouseListener(btn_addNewOrders);

        tf_unitPrice.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() >= '0' && e.getKeyChar() <= '9') {
                    tf_unitPrice.setEditable(true);
                }else if (e.getKeyChar() == '.'  || e.getKeyCode() == 8) {
                    tf_unitPrice.setEditable(true);
                }else {
                    tf_unitPrice.setEditable(false);
                    new DialogPublicError("خطأ : يرجى كتابة أعداد فقط", icon_error_50);
                }
            }
        });
        btn_backOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new main_project();
                hide(frame);
                spinner_discount.setValue(0.0);
            }
        });
        btn_add_details.addActionListener(new ActionListener() {////////////////////
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String sql2 = "SELECT MAX(OrderID) FROM `orders` ";
                String supplerName = comboBox_supplierName.getSelectedItem().toString();
                String date;
                String sql1 = "INSERT INTO `orders`(`EmployeeID`,`Supplier_name`, `Date`) VALUES (?, ?, ?)";
                        try {
                            if (checkOrders(supplerName, dateChooser)) {
                                connection = getConnection();
                                ps = connection.prepareStatement(sql1);
                                int empID = Integer.valueOf(tf_employeeID.getText());
                                ps.setInt(1, empID);
                                ps.setString(2, supplerName);
                                date = sdf.format(dateChooser.getDate());
                                ps.setString(3, date);
                                ps.executeUpdate();
                                thread_add("تم إضافة الفاتورة",370);
                                ps = connection.prepareStatement(sql2);
                                rs = ps.executeQuery();
                                rs.next();
                                orderID = rs.getInt(1);
                                label_newOrderID.setText("Order ID is : " + orderID);
                                label_newSupplierName.setText("Supplier Name is : " + supplerName);
                                hide(panel_order);
                                create_panel_orderDetails();
                            }else {}
                        }  catch ( error_length error_length) {
                            new DialogPublicError(error_length.getMessage(), icon_error_50);
                        }catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                connection.close();
                            }catch (NullPointerException | SQLException sqlException) {
                                }
                        }
            }
        });

        btn_orDe_added.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String productName = tf_orDe_productName.getText();
                String madeBy = tf_madeBy.getText();
                try {
                    checkInProductsAndMadeBy(productName, madeBy);
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            }
        });


        btn_addNewOrders.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (checkOrderAndOrderDetails(orderID)) {
                    show(panel_order);
                    tf_orDe_productName.setText("");
                    tf_madeBy.setText("");
                    tf_unitPrice.setText("");
                    spinner_discount.setValue(0);
                    spinner_quantity.setValue(1);
                    hide(panel_order_details);
                }
            }
        });

        btn_mainHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (checkOrderAndOrderDetails(orderID)) {
                    new main_project();
                    tf_orDe_productName.setText("");
                    tf_madeBy.setText("");
                    tf_unitPrice.setText("");
                    spinner_quantity.setValue(1);
                    hide(frame);
                }
            }
        });
    }

    //  اضافة عناصر ل comboBox_supplierName
    private void add_itemSupplierName() throws SQLException {
        list_supplierName.clear();
        String sql = "SELECT DISTINCT Supplier_name FROM orders";
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()) {
                list_supplierName.add(rs.getString("Supplier_name"));
            }
            for (int i = 0; i < list_supplierName.size(); i++) {
                comboBox_supplierName.addItem(list_supplierName.get(i));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }finally{
            connection.close();
        }
    }

    //       فحص اذا كانت الفاتورة تحتوي على منتجات
    private boolean checkOrderAndOrderDetails(int orderID) {
        String sql ="SELECT * FROM orders o INNER JOIN orderdetails od\n" +
                "on o.OrderID=od.OrderID \n" +
                "HAVING o.OrderID = ? ";
        try{
            connection = getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, orderID);
            rs = ps.executeQuery();
            if (rs.next()){
                return true;
            }else {
                new DialogPublicError("يوجد مشكلة : يرجى إضافة منتج واحد على الأقل", icon_error_50);
                return false;
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return false ;
        }
    }

    // فحص بيانات الفاتورة
    private boolean checkOrders(String sup_name, JDateChooser date) throws error_length {
        try{
            errorLength(sup_name, sup_name, "يوجد مشكلة : يرجى ملئ حقل اسم المورد لا يمكن تركه فارغ",
                    "يوجد مشكلة : لا يمكن لاسم المورد بأن يكون أقل من حرفان ",
                    "يوجد مشكلة : لا يمكن لاسم المورد بأن يكون أكثر من 40 حرف ",
                    40);

                String orderDate = sdf.format(date.getDate());
                return true;
            }  catch(error_length error) {
            new DialogPublicError(error.getMessage(), icon_error_50);
            return false;
        }catch(Exception error) {
                new DialogPublicError(" يوجد مشكلة : يرجى كتابة التاريخ بهذه الصيغة  yyyy-MM-dd ", icon_errorDate_50);
                return false;
            }
    }

    //  اضافة تفاصيل للفاتورة إلى قاعدة البيانات مع فحص القيم
    private void addOrdersInDB() throws SQLException, error_length {

        String sql = "INSERT INTO orderdetails VALUES (?,?,?,?,?)";
        Object quantityObject = spinner_quantity.getValue();
        int quantity = Integer.parseInt(quantityObject.toString());
        Object discountObject = spinner_discount.getValue();
        Float discount = Float.valueOf(discountObject.toString());
        try{
            Float unitPrice = Float.valueOf(tf_unitPrice.getText());
            connection = getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, orderID);
            ps.setInt(2, index_productID);
            ps.setInt(3, quantity);
            ps.setFloat(4, unitPrice);
            ps.setFloat(5, discount);
            ps.executeUpdate();
            thread_add("تم إضافة المنتج", 300);
            tf_unitPrice.setText("");
            tf_orDe_productName.setText("");
            tf_madeBy.setText("");
            spinner_quantity.setValue(1);
        } catch (NumberFormatException error) {
            new DialogPublicError("  يوجد مشكلة : رجاء قم بإضافة سعر للمنتج    ", icon_error_50);
        } catch(MysqlDataTruncation error) {
            new DialogPublicError("يوجد مشكلة : قيمة السعر كبيرة جدا يرجى إضافة قيمة أقل منها", icon_error_50);
        }catch (SQLException sqlException) {
            sqlException.getStackTrace();
        }

    }

    // فحص اسم المنتج اذا كان موجود في الفاتورة ,,,,  فحص الشركة المصنعة مع اضافة المنتج الجديد
    private void checkInProductsAndMadeBy(String productName, String madeBy) throws SQLException {
        String sql1 = "SELECT ProductID FROM products where Product_name=? AND Made_By = ?";
        String sql2 = "INSERT INTO products (Product_name,Made_By) VALUES (?,?)";
        try {
            errorLength(productName, productName, "يوجد مشكلة : لا يمكن أن يكون اسم المنتج فارغ "
                    , "يوجد مشكلة : لا يمكن أن يكون اسم المنتج أقل من حرفان",
                    "يوجد مشكلة : لا يمكن أن يكون اسم المنتج أكبر من 40 حرف", 40);
            //  فحص المنتج اذا كان موجود ضمن جدول المنتجات اذا كان موجود يرجع id المنتج
            connection = getConnection();
            ps = connection.prepareStatement(sql1);
            ps.setString(1, productName);
            ps.setString(2, madeBy);
            rs = ps.executeQuery();
            if (rs.next()) {
                index_productID = (rs.getInt("ProductID"));
                checkOrdersWithProduct(orderID, productName, madeBy);
            } else {
                //    اذا كان غير موجود المنتج سوف يتم فحص قيمة الشركة المصنعة ومن ثم اضافة المنتج الى جدول المنتجات
                    errorLength(madeBy, madeBy, "يوجد مشكلة : لا يمكن أن يكون اسم الشركة المصنعة فارغ "
                            , "يوجد مشكلة : لا يمكن أن يكون اسم الشركة المصنعة أقل من حرفان",
                            "يوجد مشكلة : لا يمكن أن يكون اسم الشركة المصنعة أكبر من 40 حرف", 40);
                    ps = connection.prepareStatement(sql2);
                    ps.setString(1, productName);
                    ps.setString(2, madeBy);
                    ps.executeUpdate();
                    //   سوف يتم جلب معرف المنتج من جدول المنتجات
                    ps = connection.prepareStatement(sql1);
                    ps.setString(1, productName);
                    ps.setString(2, madeBy);
                    rs = ps.executeQuery();
                    rs.next();
                    index_productID = (rs.getInt("ProductID"));
                    checkOrdersWithProduct(orderID, productName, madeBy);
            }
        }catch (error_length error) {
            new DialogPublicError(error.getMessage(), icon_error_50);
        } catch (SQLException error) {
            error.getStackTrace();
        }finally {
            connection.close();
        }
    }

    //  فحص اذا كانت الفاتورة تحتوي على منتج ما
    private void checkOrdersWithProduct(int orderID, String productName, String madeBy) throws SQLException {
        String sql = "SELECT * FROM orderdetails WHERE OrderID = ? \n" +
                "AND ProductID = (SELECT ProductID FROM products WHERE Product_name = ? AND Made_By = ? )\n";
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, orderID);
            ps.setString(2, productName);
            ps.setString(3, madeBy);
            rs = ps.executeQuery();
            if (rs.next()) {
                new DialogPublicError("يوجد مشكلة : هذا المنتج موجود مسبقا في هذه الفاتورة لا يمكن إضافته", icon_error_50);
            } else {
                //  اضافة المنتج للفاتورة
                addOrdersInDB();
            }
        } catch (error_length error_length) {
            new DialogPublicError(error_length.getMessage(), icon_error_50);
        }catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            connection.close();
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

    private void show(JFrame frame) {
        frame.setVisible(true);
    }
    private void hide(JFrame frame) {
        frame.dispose();
    }
    private void show(JPanel panel) {
        panel.setVisible(true);
    }
    private void hide(JPanel panel) {
        panel.setVisible(false);
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