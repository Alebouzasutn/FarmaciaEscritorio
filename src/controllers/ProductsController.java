package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.DynamicCombobox;
import static models.EmployeesDao.rol_user;
import models.Products;
import models.ProductsDao;
import views.SystemView;

public class ProductsController implements ActionListener, MouseListener, KeyListener {

    private Products product;
    private ProductsDao productDao;
    private SystemView views;
    String rol = rol_user;
    DefaultTableModel model = new DefaultTableModel();

    public ProductsController(Products product, ProductsDao productDao, SystemView views) {
        this.product = product;
        this.productDao = productDao;
        this.views = views;
        this.views.btn_register_product.addActionListener(this);
        this.views.btn_update_product.addActionListener(this);
        this.views.Product_table.addMouseListener(this);
        this.views.txt_search_product.addKeyListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_register_product) {
            if (views.txt_product_code.getText().equals("")
                    || views.txt_product_name.getText().equals("")
                    || views.txt_product_description.getText().equals("")
                    || views.txt_product_unit_price.getText().equals("")
                    || views.cmb_product_category.getSelectedItem().equals("")) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            
            
        } else {
            product.setCode(Integer.parseInt(views.txt_product_code.getText().trim()));
            product.setName(views.txt_product_name.getText().trim());
            product.setDescription(views.txt_product_description.getText().trim());
            product.setUnit_price(Double.parseDouble(views.txt_product_unit_price.getText().trim()));
            DynamicCombobox category_id = (DynamicCombobox) views.cmb_product_category.getSelectedItem();
            product.setCategory_id(category_id.getId());

            if (productDao.registerProductsQuery(product)) {
                
                cleanTable();
                listAllProducts();
                JOptionPane.showMessageDialog(null, "Producto registrado con exito");

            } else {
                JOptionPane.showMessageDialog(null, "Error al registrar el producto");

            }

        }
    }else if(e.getSource() == views.btn_update_product){
         if (views.txt_product_code.getText().equals("")
                    || views.txt_product_name.getText().equals("")
                    || views.txt_product_description.getText().equals("")
                    || views.txt_product_unit_price.getText().equals("")
                    || views.cmb_product_category.getSelectedItem().equals("")) {
              
             JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            
            
        } else {
            product.setCode(Integer.parseInt(views.txt_product_code.getText().trim()));
            product.setName(views.txt_product_name.getText().trim());
            product.setDescription(views.txt_product_description.getText().trim());
            product.setUnit_price(Double.parseDouble(views.txt_product_unit_price.getText().trim()));
            DynamicCombobox category_id = (DynamicCombobox) views.cmb_product_category.getSelectedItem();
            product.setCategory_id(category_id.getId());
        
            //paso id al metodo
            product.setId(Integer.parseInt(views.txt_product_id.getText()));
        
        if(productDao.updateProductQuery(product)){
            cleanTable();
            cleanFields();
            listAllProducts();
            
           JOptionPane.showMessageDialog(null, "Datos del producto modificados con exito");

        }else{
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar los datos del producto");

        }
          
    }
    }else if(e.getSource() == views.btn_delete_product){
        
         int row = views.Product_table.getSelectedRow(); //devuelve en que fila fue hecho el click//

            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Debes seleccionar un producto para eliminar");

            
            } else {
                int id = Integer.parseInt(views.Product_table.getValueAt(row, 0).toString());
                /*Esto convierte el valor en la primera columna de la fila seleccionada en un número entero, 
        ya que el ID debe ser un número para la eliminación.*/
                int question = JOptionPane.showConfirmDialog(null, "¿Quieres eliminar el producto?");

                if (question == 0 && productDao.deleteProductQuery(id) != false) { //elimina de la base de datos //
                    cleanFields();
                    views.btn_register_product.setEnabled(true);
                     listAllProducts();
                    JOptionPane.showMessageDialog(null, "Producto eliminado con exito");
    }
        
          
    }
    }else if(e.getSource() == views.btn_cancel_product){
        
        views.btn_register_product.setEnabled(true);
        cleanFields();
        
        
    }
    }
        
        
        
        
    public void listAllProducts() {
        if (rol.equals("Administrador") || rol.equals("Auxiliar")) {

            List<Products> list = productDao.listProductQuery(views.txt_search_product.getText());
            model = (DefaultTableModel) views.Product_table.getModel();
            Object row[] = new Object[7];
            for (int i = 0; i < list.size(); i++) {
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getCode();
                row[2] = list.get(i).getName();
                row[3] = list.get(i).getDescription();
                row[4] = list.get(i).getUnit_price();
                row[5] = list.get(i).getProduct_quantity();
                row[6]= list.get(i).getCategory_name();
                model.addRow(row);
            }
            views.Product_table.setModel(model);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getSource() == views.Product_table) {

            //Devuelve la fila donde se hizo click//
            int row = views.Product_table.rowAtPoint(e.getPoint());

            //Los datos de la tabla se extraen y se muestran en los txt
            views.txt_product_id.setText(views.Product_table.getValueAt(row, 0).toString());
            product = productDao.searchProduct(Integer.parseInt(views.txt_product_id.getText()));
            views.txt_product_code.setText(views.Product_table.getValueAt(row,1).toString());
            views.txt_product_name.setText(views.Product_table.getValueAt(row,2).toString());
            views.txt_product_description.setText(views.Product_table.getValueAt(row,3).toString());
            views.txt_product_unit_price.setText(views.Product_table.getValueAt(row,4).toString());

            //Deshabilitar
            views.txt_product_id.setEditable(false);
            views.btn_register_product.setEnabled(false);

        } else if (e.getSource() == views.jLabelProducts) {

            views.PanelProduct.setSelectedIndex(0);
            cleanTable();
            cleanFields();
            listAllProducts();

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

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getSource() == views.txt_search_product) {
            cleanTable();
            listAllProducts();

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void cleanTable() {
        //Recorre la tabla por filas para limpiar cada una
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

    private void cleanFields() {

        views.txt_product_id.setText("");
        views.txt_product_id.setEditable(true);
        views.txt_product_name.setText("");

    }
}
