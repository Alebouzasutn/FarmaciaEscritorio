
package controllers;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.DynamicCombobox;
import static models.EmployeesDao.id_user;
import static models.EmployeesDao.rol_user;
import models.Products;
import models.ProductsDao;
import models.Purchases;
import models.PurchasesDao;
import views.SystemView;

public class PurchasesController implements KeyListener, ActionListener, MouseListener {

    private Purchases purchase;
    private PurchasesDao purchaseDao;
    private SystemView views;
    private int getIdSupplier = 0;
    private int item = 0;
    DefaultTableModel model = new DefaultTableModel();
    DefaultTableModel temp;

    Products product = new Products();
    ProductsDao productDao = new ProductsDao();
    String rol = rol_user;

    public PurchasesController(Purchases purchase, PurchasesDao purchaseDao, SystemView views) {
        this.purchase = purchase;
        this.purchaseDao = purchaseDao;
        this.views = views;

        this.views.btn_add_product_to_buy.addActionListener(this);
        this.views.btn_confirm_purchase.addActionListener(this);
        this.views.btn_delete_purchase.addActionListener(this);
        this.views.txt_purchase_product_code.addKeyListener(this);
        this.views.txt_purchase_price.addKeyListener(this);
        this.views.btn_new_purchase.addActionListener(this);
        this.views.jLabelPurchases.addMouseListener(this);
        this.views.jLabelReports.addMouseListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_add_product_to_buy) {
            DynamicCombobox supplier_cmb = (DynamicCombobox) views.cmb_purchase_supplier.getSelectedItem();
            int supplier_id = supplier_cmb.getId();

            if (getIdSupplier == 0) {
                getIdSupplier = supplier_id;
            } else if (getIdSupplier != supplier_id) {
                JOptionPane.showMessageDialog(null, "No puede realizar una misma compra a dos proveedores");
                return;
            }

            int amount = Integer.parseInt(views.txt_purchase_amount.getText());
            if (amount <= 0) {
                JOptionPane.showMessageDialog(null, "Ingrese una cantidad válida");
                return;
            }

            String product_name = views.txt_purchase_product_name.getText();
            double price = Double.parseDouble(views.txt_purchase_price.getText());
            int purchase_id = Integer.parseInt(views.txt_purchase_id.getText());
            String supplier_name = views.cmb_purchase_supplier.getSelectedItem().toString();

            temp = (DefaultTableModel) views.purchases_table.getModel();
            for (int i = 0; i < views.purchases_table.getRowCount(); i++) {
                if (views.purchases_table.getValueAt(i, 1).equals(product_name)) {
                    JOptionPane.showMessageDialog(null, "El producto ya está registrado en la tabla de Compras");
                    return;
                }
            }

            ArrayList<Object> list = new ArrayList<>();
            item++;
            list.add(item);
            list.add(purchase_id);
            list.add(product_name);
            list.add(amount);
            list.add(price);
            list.add(amount * price);
            list.add(supplier_name);

            Object[] obj = { list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6) };
            temp.addRow(obj);
            views.purchases_table.setModel(temp);
            cleanFieldsPurchases();
            views.cmb_purchase_supplier.setEditable(false);
            views.txt_purchase_product_code.requestFocus();
            calculatePurchase();
        } else if (e.getSource() == views.btn_confirm_purchase) {
            insertPurchase();
        } else if (e.getSource() == views.btn_delete_purchase) {
            model = (DefaultTableModel) views.purchases_table.getModel();
            int selectedRow = views.purchases_table.getSelectedRow();
            if (selectedRow != -1) {
                model.removeRow(selectedRow);
                calculatePurchase();
                views.txt_purchase_product_code.requestFocus();
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un producto para eliminar");
            }
        } else if (e.getSource() == views.btn_new_purchase) {
            cleanTableTemp();
            cleanFieldsPurchases();
            insertPurchase();
        }
    }

    private void insertPurchase() {
        double total = Double.parseDouble(views.txt_purchase_total_to_pay.getText());
        int employee_id = id_user;
        if (purchaseDao.registerPurchaseQuery(getIdSupplier, employee_id, total)) {
            int purchase_id = purchaseDao.purchaseId();

            for (int i = 0; i < views.purchases_table.getRowCount(); i++) {
                int product_id = Integer.parseInt(views.purchases_table.getValueAt(i, 0).toString());
                int purchase_amount = Integer.parseInt(views.purchases_table.getValueAt(i, 2).toString());
                double purchase_price = Double.parseDouble(views.purchases_table.getValueAt(i, 3).toString());
                double purchase_subtotal = purchase_price * purchase_amount;

                purchaseDao.registerPurchaseDetailQuery(purchase_id, purchase_price, purchase_amount, purchase_subtotal, product_id);

                product = productDao.searchId(product_id);
                int amount = product.getProduct_quantity() + purchase_amount;

                productDao.updateStockQuery(amount, product_id);
            }
            cleanTableTemp();
            JOptionPane.showMessageDialog(null, "Compra registrada");
                  
        }
    }
    
    private void cleanFieldsPurchases() {

        views.txt_purchase_product_name.setText("");
        views.txt_purchase_price.setEditable(true);
        views.txt_purchase_amount.setText("");
        views.txt_purchase_product_code.setText("");
        views.txt_purchase_subtotal.setText("");
        views.txt_purchase_id.setText("");
        views.txt_purchase_total_to_pay.setText("");
    }
    //Calcular total a pagar

    public void calculatePurchase() {
        double total = 0.00;
        int numRow = views.purchases_table.getRowCount();

        for (int i = 0; i < numRow; i++) {
            total = total + Double.parseDouble(String.valueOf(views.purchases_table.getValueAt(i, 4)));

        }
        views.txt_purchase_total_to_pay.setText("" + total);
    }

    public void cleanTabletemp() {
        
        for(int i=0; i<temp.getRowCount(); i++){
            temp.removeRow(i);
            i = i-1;
        }
        
    }

    

   public void cleanTable() {
        //Recorre la tabla por filas para limpiar cada una
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

@Override
    public void mouseClicked(MouseEvent e) {
        
        if(e.getSource()== views.jLabelPurchases){
            if(rol.equals("Administrador")){
                
                views.PanelProduct.setSelectedIndex(1);
            cleanTable();
            
        }else{
                views.PanelProduct.setEnabledAt(1,false);
                views.jLabelPurchases.setEnabled(false);
                JOptionPane.showMessageDialog(null, "No tiene privilegios de Administrador");
            }
        }else if(e.getSource() == views.jLabelReports){
            views.PanelProduct.setSelectedIndex(6);
            cleanTable();
            listAllpurchases();
        }
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    

}




/*package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.DynamicCombobox;
import static models.EmployeesDao.id_user;
import static models.EmployeesDao.rol_user;
import models.Products;
import models.ProductsDao;
import models.Purchases;
import models.PurchasesDao;
import views.SystemView;

public class PurchasesController implements KeyListener, ActionListener, MouseListener {

    private Purchases purchase;
    private PurchasesDao purchaseDao;
    private SystemView views;
    private int getIdSupplier = 0;
    private int item = 0;
    DefaultTableModel model = new DefaultTableModel();
    DefaultTableModel temp;

    Products product = new Products();
    ProductsDao productDao = new ProductsDao();
    String rol = rol_user;

    public PurchasesController(Purchases purchase, PurchasesDao purchaseDao, SystemView views) {
        this.purchase = purchase;
        this.purchaseDao = purchaseDao;
        this.views = views;
        //Boton de agregar
        this.views.btn_add_product_to_buy.addActionListener(this);
        //Boton confirmar
        this.views.btn_confirm_purchase.addActionListener(this);
        //Boton Eliminar
        this.views.btn_delete_purchase.addActionListener(this);
        this.views.txt_purchase_product_code.addKeyListener(this);
        this.views.txt_purchase_price.addKeyListener(this);
        this.views.btn_new_purchase.addActionListener(this);
        this.views.jLabelPurchases.addMouseListener(this);
        this.views.jLabelReports.addMouseListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_add_product_to_buy) {
            DynamicCombobox supplier_cmb = (DynamicCombobox) views.cmb_purchase_supplier.getSelectedItem();
            int supplier_id = supplier_cmb.getId();

            if (getIdSupplier == 0) {
                getIdSupplier = supplier_id;
            } else {
                if (getIdSupplier != supplier_id) {
                    JOptionPane.showMessageDialog(null, "No puede realizar una misma compra a dos proveedores");
                } else {
                    int amount = Integer.parseInt(views.txt_purchase_amount.getText());
                    String product_name = views.txt_purchase_product_name.getText();
                    double price = Double.parseDouble(views.txt_purchase_price.getText());
                    int purchase_id = Integer.parseInt(views.txt_purchase_id.getText());
                    String supplier_name = views.cmb_purchase_supplier.getSelectedItem().toString();

                    if (amount > 0)
                    temp = (DefaultTableModel) views.purchases_table.getModel();

                    for (int i = 0; i < views.purchases_table.getRowCount(); i++) {

                        if (views.purchases_table.getValueAt(i, 1).equals(views.txt_purchase_product_name.getText()));
                        JOptionPane.showMessageDialog(null, "El producto ya esta registrado en la tabla de Compras");

                        return;
                    }

                    ArrayList list = new ArrayList();
                    item = 1;
                    list.add(item);
                    list.add(purchase_id);
                    list.add(product_name);
                    list.add(amount);
                    list.add(price);
                    list.add(amount * price);
                    list.add(supplier_name);

                    Object[] obj = new Object[6];
                    obj[0] = list.get(1);
                    obj[1] = list.get(2);
                    obj[2] = list.get(3);
                    obj[3] = list.get(4);
                    obj[4] = list.get(5);
                    obj[5] = list.get(6);
                    temp.addRow(obj);
                    views.purchases_table.setModel(model);
                    cleanFieldsPurchases();
                    views.cmb_purchase_supplier.setEditable(false);
                    views.txt_purchase_product_code.requestFocus();
                    calculatePurchase();
                }
            }

        }else if(e.getSource()== views.btn_confirm_purchase){
            insertPurchase();
    }else if(e.getSource()== views.btn_delete_purchase){
        model = (DefaultTableModel) views.purchases_table.getModel();
        model.removeRow(views.purchases_table.getSelectedRow());
        calculatePurchase();
        views.txt_purchase_product_code.requestFocus();
        
    }else if(e.getSource() == views.btn_new_purchase){
       cleanTabletemp();
       cleanFieldsPurchases();
       insertPurchase();
    }
        
    }

    
    private void insertPurchase(){
        double total = Double.parseDouble(views.txt_purchase_total_to_pay.getText());
        int employee_id = id_user;
        if(purchaseDao.registerPurchaseQuery(getIdSupplier, employee_id, total)){
            int purchase_id = purchaseDao.purchaseId();
            
            for(int i=0; i<views.purchases_table.getRowCount();i++){
                int product_id = Integer.parseInt(views.purchases_table.getValueAt(i,0).toString());
                int purchase_amount = Integer.parseInt(views.purchases_table.getValueAt(i,2).toString());
                double purchase_price = Double.parseDouble(views.purchases_table.getValueAt(i,3).toString());
               double purchase_subtotal = purchase_price * purchase_amount;

                //Registro detalle comora
                 purchaseDao.registerPurchaseDetailQuery(purchase_id, purchase_price, purchase_amount, purchase_subtotal, product_id);
                
                 //Trae la cantidad de productos
                 
                 product = productDao.searchId(product_id);
                 int amount = product.getProduct_quantity()+ purchase_amount;
                 
                 productDao.updateStockQuery(amount, product_id);
            }
            cleanTabletemp();
            JOptionPane.showMessageDialog(null, "Compra registrada con exito");
            cleanFieldsPurchases();
            
        }
        
        
        
        
    }
    
    
    //Metodo para listar compras realizadas
    
    public void listAllpurchases(){
        
        if(rol.equals("Administrador")|| rol.equals("Auxiliar")){
            
            List<Purchases>list = purchaseDao.listAllPurchasesQuery();
            model = (DefaultTableModel) views.table_all_purchases.getModel();
            Object [] row = new Object[4];
            
            for(int i =0; i<list.size(); i++){
           
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getSupplier_name_product();
                row[2] = list.get(i).getTotal();
                row[3] = list.get(i).getCreated();
                
                model.addRow(row);
        }
            views.table_all_purchases.setModel(model);
    }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getSource() == views.txt_purchase_product_code) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (views.txt_purchase_product_code.getText().equals("")) {

                    JOptionPane.showMessageDialog(null, "Ingrese su producto a comprar");

                } else {
                    int id = Integer.parseInt(views.txt_purchase_product_code.getText());
                    product = productDao.searchCode(id);
                    views.txt_purchase_product_name.setText(product.getName());
                    views.txt_purchase_id.setText("" + product.getId());
                    views.txt_purchase_amount.requestFocus();
                }
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == views.txt_purchase_amount) {
            int quantity;
            double price = 0.0;

            if (views.txt_purchase_amount.getText().equals("")) {
                quantity = 1;
                views.txt_purchase_price.setText("" + price);
            } else {
                quantity = Integer.parseInt(views.txt_purchase_amount.getText());
                price = Double.parseDouble(views.txt_purchase_price.getText());
                views.txt_purchase_subtotal.setText("" + quantity * price);

            }
        }
    }

    private void cleanFieldsPurchases() {

        views.txt_purchase_product_name.setText("");
        views.txt_purchase_price.setEditable(true);
        views.txt_purchase_amount.setText("");
        views.txt_purchase_product_code.setText("");
        views.txt_purchase_subtotal.setText("");
        views.txt_purchase_id.setText("");
        views.txt_purchase_total_to_pay.setText("");
    }
    //Calcular total a pagar

    public void calculatePurchase() {
        double total = 0.00;
        int numRow = views.purchases_table.getRowCount();

        for (int i = 0; i < numRow; i++) {
            total = total + Double.parseDouble(String.valueOf(views.purchases_table.getValueAt(i, 4)));

        }
        views.txt_purchase_total_to_pay.setText("" + total);
    }

    public void cleanTabletemp() {
        
        for(int i=0; i<temp.getRowCount(); i++){
            temp.removeRow(i);
            i = i-1;
        }
        
    }

    

   public void cleanTable() {
        //Recorre la tabla por filas para limpiar cada una
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

@Override
    public void mouseClicked(MouseEvent e) {
        
        if(e.getSource()== views.jLabelPurchases){
            if(rol.equals("Administrador")){
                
                views.PanelProduct.setSelectedIndex(1);
            cleanTable();
            
        }else{
                views.PanelProduct.setEnabledAt(1,false);
                views.jLabelPurchases.setEnabled(false);
                JOptionPane.showMessageDialog(null, "No tiene privilegios de Administrador");
            }
        }else if(e.getSource() == views.jLabelReports){
            views.PanelProduct.setSelectedIndex(6);
            cleanTable();
            listAllpurchases();
        }
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    

}*/

