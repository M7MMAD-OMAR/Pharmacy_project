package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class frame_products implements interfaceImages {
    private Theme theme = new Theme();
    private JFrame frame = new JFrame(" Products");
    private JPanel panel_showProducts = new JPanel(null);
    private JPanel panel_tabbedPane = new JPanel(new GridLayout());
    private DefaultTableModel model_productsDetails = new DefaultTableModel();
    private JTable table_productsDetails = new JTable(model_productsDetails);
    private JScrollPane sp_tableProductsDetails = new JScrollPane(table_productsDetails);
    private DefaultTableModel model_productsTable = new DefaultTableModel();
    private JTable table_products = new JTable(model_productsTable);
    private JScrollPane sp_tableProducts = new JScrollPane(table_products);
    private JButton btn_backProducts = new JButton("رجـوع");
    private JTabbedPane tabbedPaneTable = new JTabbedPane();
    private Connection connection;
    private PreparedStatement ps;
    private ResultSet rs;
    private ArrayList<Products_arrayList> products_array = new ArrayList<>();
    private ArrayList<Products_arrayList> newProductArray = new ArrayList<>();
    private ArrayList<ProductsDetails_arrayList> productsDetails_array = new ArrayList<>();
    private JTextField tf_search = new JTextField();
    private JLabel label_search = new JLabel("بحث");
    private int selectedRow;



    public frame_products() throws SQLException {
        create_frame_product();
    }

    private void create_frame_product()  {

        deleteProductsIsNull();


        // خصائص العناصر مع مواقعها

        label_search.setFont(theme.getFont_tf());
        tf_search.setFont(theme.getFont_tf());
        theme.tf_focusEvent(tf_search);
        label_search.setIcon(icon_search);
        panel_tabbedPane.setBounds(0, 60, 643, 630);
        panel_showProducts.setBounds(0,0,640, 630);
        btn_backProducts.setBounds(140, 705, 350, 35);
        label_search.setBounds(400, 20, 100, 28);
        tf_search.setBounds(150, 20, 250, 30);
        btn_backProducts.setFont(theme.getFont_button());
        btn_backProducts.setBackground(theme.getBackground_color_button());
        panel_showProducts.setBackground(theme.getBackground());

        //  خصائص الجداول
        sp_tableProducts.setViewportView(table_products);
        table_products.setFont(new Font("font", Font.PLAIN, 16));
        table_products.setBackground(new Color(250, 249, 249));
        table_products.setForeground(Color.black);
        table_products.setSelectionBackground(new Color(190, 224, 255));
        table_products.setGridColor(Color.black);
        table_products.setSelectionForeground(Color.black);
        table_products.setRowHeight(25);
        table_products.setAutoCreateRowSorter(true);
        table_products.setRowSelectionAllowed(true);
        table_products.setEnabled(true);
        sp_tableProductsDetails.setViewportView(table_productsDetails);
        table_productsDetails.setFont(new Font("font", Font.PLAIN, 16));
        table_productsDetails.setBackground(new Color(250, 249, 249));
        table_productsDetails.setForeground(Color.black);
//        table_productsDetails.setSelectionBackground(new Color(190, 224, 255));
        table_productsDetails.setGridColor(Color.black);
//        table_productsDetails.setSelectionForeground(Color.black);
        table_productsDetails.setRowHeight(25);
        table_productsDetails.setAutoCreateRowSorter(true);
//        table_productsDetails.setRowSelectionAllowed(true);
        table_productsDetails.setEnabled(false);

        //        //   اضافة ال tabbedPane وإضافة الأعمدة للجدول
        tabbedPaneTable.addTab(" Products ", sp_tableProducts);
        tabbedPaneTable.addTab(" Products Details ", sp_tableProductsDetails);
        model_productsDetails.addColumn("OrderID");
        model_productsDetails.addColumn("ProductID");
        model_productsDetails.addColumn("Quantity");
        model_productsDetails.addColumn("Unit Price");
        model_productsDetails.addColumn("Date");
        model_productsTable.addColumn("Product ID");
        model_productsTable.addColumn("Product Name");
        model_productsTable.addColumn("Made By");
        model_productsTable.addColumn("Quantity");
        btn_backProducts.setToolTipText("رجوع للصفحة الرئيسية");
        tf_search.setToolTipText("بـحث");
        tabbedPaneTable.setToolTipTextAt(0, "جـدول المنتجات");
        tabbedPaneTable.setToolTipTextAt(1, "جـدول تفاصيل المنتجات");


        // اضافة خصائص للجدوال
        TableColumnModel columnModelProductsDetails = table_productsDetails.getColumnModel();
        TableColumnModel columnModelProducts = table_products.getColumnModel();
        columnModelProducts.getColumn(0).setMinWidth(90);
        columnModelProducts.getColumn(0).setMaxWidth(110);
        columnModelProducts.getColumn(3).setMinWidth(90);
        columnModelProducts.getColumn(3).setMaxWidth(110);
        columnModelProducts.setColumnMargin(10);
        columnModelProductsDetails.setColumnMargin(10);
        columnModelProductsDetails.getColumn(4).setMinWidth(140);
        columnModelProductsDetails.getColumn(4).setMaxWidth(160);
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        columnModelProducts.getColumn(0).setCellRenderer(cellRenderer);
        columnModelProducts.getColumn(3).setCellRenderer(cellRenderer);
        columnModelProductsDetails.getColumn(0).setCellRenderer(cellRenderer);
        columnModelProductsDetails.getColumn(1).setCellRenderer(cellRenderer);
        columnModelProductsDetails.getColumn(2).setCellRenderer(cellRenderer);
        columnModelProductsDetails.getColumn(3).setCellRenderer(cellRenderer);
        columnModelProductsDetails.getColumn(4).setCellRenderer(cellRenderer);

        tabbedPaneTable.setMinimumSize(new Dimension(20,30));

        theme.getImageLogo(frame);

        panel_tabbedPane.add(tabbedPaneTable);
        panel_showProducts.add(tf_search);
        panel_showProducts.add(label_search);
        btn_backProducts.setIcon(icon_back);
        panel_showProducts.add(btn_backProducts);
        panel_showProducts.add(panel_tabbedPane);





        frame.add(panel_showProducts);
        frame.setSize(650,800);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        frame.setLocation(620,70);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        theme.btn_MouseListener(btn_backProducts);

        try{
            // عرض البيانات في جدول المنتجات
            showProductsInTable();
            //   عند تشغيل البرنامج سوف تظهر تفاصيل اول عنصر في جدول التفاصيل
            int productID = products_array.get(0).getProductID();
            showProductsDetails(productID);
        }catch(IndexOutOfBoundsException index) {
            new DialogPublicError("لدينا مشكلة : لم يتم العثور على منتجات سابقة", icon_error_50);
        }
        catch (SQLException | NullPointerException error) {
            error.getStackTrace();
        }
        btn_backProducts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                deleteProductsIsNull();
                model_productsTable.setRowCount(0);
                model_productsTable.setRowCount(0);
                tf_search.setText("");
                new main_project();
                frame.dispose();
            }
        });

        tf_search.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent keyEvent) {
                String search = tf_search.getText().toLowerCase();
                Products_arrayList products;
                Object[] row = new Object[4];
                newProductArray.clear();
                if (!(search.length() == 0)) {
                    for (int i = 0; i < products_array.size(); i++) {
                        while (products_array.get(i).getProductName().toLowerCase().contains(search) ||
                                products_array.get(i).getMadeBy().toLowerCase().contains(search) ||
                                search.contains(products_array.get(i).getProductID() + "")) {
                            products = new Products_arrayList(products_array.get(i).getProductID(),
                                    products_array.get(i).getProductName(),
                                    products_array.get(i).getMadeBy(),
                                    products_array.get(i).getQuantity()
                            );
                            newProductArray.add(products);
                            break;
                        }
                    }
                    model_productsTable.setRowCount(0);
                    model_productsTable.setNumRows(0);
                    for (int i = 0; i < newProductArray.size(); i++) {
                        row[0] = newProductArray.get(i).getProductID();
                        row[1] = newProductArray.get(i).getProductName();
                        row[2] = newProductArray.get(i).getMadeBy();
                        row[3] = newProductArray.get(i).getQuantity();
                        model_productsTable.addRow(row);
                    }

                }else {
                    try {
                        model_productsTable.setRowCount(0);
                        model_productsTable.setNumRows(0);
                        showProductsInTable();
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                }
            }
        });

        table_products.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                    // اذا كان حقل البحث فارغ اجلب البيانات من المصفوفة القديمة
                    if (tf_search.getText().length() == 0) {
                        selectedRow = table_products.getSelectedRow();
                        int productID = products_array.get(selectedRow).getProductID();
                        showProductsDetails(productID);
                        tabbedPaneTable.setSelectedIndex(1);
                    }else {
                        // اذا كان هناك قيمة في حقل البحث اجلب القيم من المصفوفة الجديدة
                        selectedRow = table_products.getSelectedRow();
                       int productID = newProductArray.get(selectedRow).getProductID();
                       showProductsDetails(productID);
                       tabbedPaneTable.setSelectedIndex(1);
                    }
            }
        });
    }
    //  حذف المنتجت التي لا فاتورة لها
    private void deleteProductsIsNull() {
        String sql = "DELETE  FROM products \n" +
                "WHERE ProductID IN\n" +
                "(SELECT p.ProductID FROM products p LEFT JOIN orderdetails od on p.ProductID=od.ProductID\n" +
                "WHERE od.OrderID is  null )";
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    //  اظهار البيانات في جدول تفاصيل الفاتورة عبر productID
    private void showProductsDetails(int productID) {
        String sql = "SELECT od.OrderID, od.ProductID, od.Quantity, od.Unit_Price, o.Date\n" +
                "                FROM `orderdetails` od INNER JOIN orders o \n" +
                "                on o.OrderID=od.OrderID\n" +
                "                HAVING ProductID= ? ";
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sql);
            ProductsDetails_arrayList productsDetails;
            Object[] row = new Object[5];
            ps.setInt(1, productID);
            System.out.println(productID);
            model_productsDetails.setRowCount(0);
            model_productsDetails.setNumRows(0);
            productsDetails_array.clear();
            rs = ps.executeQuery();
            while(rs.next()) {
                productsDetails = new ProductsDetails_arrayList(
                        rs.getInt("OrderID"),
                        rs.getInt("ProductID"),
                        rs.getInt("Quantity"),
                        rs.getFloat("Unit_Price"),
                        rs.getDate("Date"));
                productsDetails_array.add(productsDetails);
            }
            for (int i = 0; i < productsDetails_array.size(); i++) {
                row[0] = productsDetails_array.get(i).getOrderId();
                row[1] = productsDetails_array.get(i).getProductId();
                row[2] = productsDetails_array.get(i).getQuantity();
                row[3] = productsDetails_array.get(i).getUnitPrice();
                row[4] = productsDetails_array.get(i).getDate();
                model_productsDetails.addRow(row);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }


    }
    ////      اظهار البيانات في جدول المنتجات
    private void showProductsInTable() throws SQLException
    {
        products_array.clear();
        String sql = "SELECT * FROM  products";
        String sql1 = "SELECT SUM(Quantity) AS 'sumQuantity'\n" +
                "FROM orderdetails \n" +
                "GROUP BY ProductID\n";
        try{
            connection = getConnection();
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            PreparedStatement ps1 = connection.prepareStatement(sql1);
            ResultSet rs1 = ps1.executeQuery();
            Products_arrayList products;
            Object[] arrObject = new Object[4];
            while(rs.next()) {
                rs1.next();
                products = new Products_arrayList(rs.getInt("ProductID"),
                        rs.getString("Product_name"), rs.getString("Made_By"), rs1.getInt(1));
                products_array.add(products);
            }
            for (int i = 0; i < products_array.size(); i++) {
                arrObject[0] = products_array.get(i).getProductID();
                arrObject[1] = products_array.get(i).getProductName();
                arrObject[2] = products_array.get(i).getMadeBy();
                arrObject[3] = products_array.get(i).getQuantity();
                model_productsTable.addRow(arrObject);
            }
        } catch (SQLException sqlException) {
        sqlException.getStackTrace();
        }finally {
            connection.close();
        }
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


class ProductsDetails_arrayList {
    private int orderId, productId, quantity;
    private Float unitPrice;
    private Date date;
    public ProductsDetails_arrayList(int orderId, int productId, int quantity, Float unitPrice, Date date) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.date = date;
    }

    public int getQuantity() {
        return quantity;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public int getProductId() {
        return productId;
    }

    public Date getDate() {
        return date;
    }

    public int getOrderId() {
        return orderId;
    }
}