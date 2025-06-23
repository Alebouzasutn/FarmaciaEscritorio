
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import models.Customers;
import models.Suppliers;
import models.SuppliersDao;
import views.SystemView;
import java.sql.Connection;
import javax.swing.table.DefaultTableModel;
import models.Categories;
import models.ConnectionMysql;
import models.DynamicCombobox;

public class SuppliersController implements ActionListener, MouseListener, KeyListener{
    
    private Suppliers supplier;
    private SuppliersDao supplierDao;
    private SystemView views;
    
    //String rol = rol_user;
    DefaultTableModel model = new DefaultTableModel(); 

    public SuppliersController(Suppliers supplier, SuppliersDao supplierDao, SystemView views) {
        this.supplier = supplier;
        this.supplierDao = supplierDao;
        this.views = views;
       
        this.views.btn_register_supplier.addActionListener(this);
        this.views.btn_update_supplier.addActionListener(this);
        this.views.btn_delete_supplier.addActionListener(this);
        this.views.suppliers_table.addMouseListener(this);
        this.views.txt_search_supplier.addKeyListener(this);
        this.views.jLabelSuppliers.addMouseListener(this);
       

        
        //Boton modificar prov
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == views.btn_register_supplier) {
    if (views.txt_supplier_name.getText().equals("")
                    || views.txt_supplier_description.getText().equals("")
                    || views.txt_supplier_address.getText().equals("")
                    || views.txt_supplier_telephone.getText().equals("")
                    || views.cmb_supplier_city.getSelectedItem().toString().equals("")
                    || views.txt_supplier_email.getText().equals("")){

                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                return;
              
             // Realiza la inserción
    }else{
            supplier.setName(views.txt_supplier_name.getText().trim());
            supplier.setDescription(views.txt_supplier_description.getText().trim());
            supplier.setAddress(views.txt_supplier_address.getText().trim());
            supplier.setTelephone(views.txt_supplier_telephone.getText().trim());
            supplier.setCity(views.cmb_supplier_city.getSelectedItem().toString());
            supplier.setEmail(views.txt_supplier_email.getText().trim());
}
    
    if (supplierDao.registerSupplierQuery(supplier)) {
                   
                    JOptionPane.showMessageDialog(null, "Proveedor registrado con éxito");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar este proveedor");
                }
            }  else if(e.getSource()== views.btn_update_supplier){
                if (views.txt_supplier_id.equals("")) {
                
                JOptionPane.showMessageDialog(null, "Selecciona una fila para continuar");
                
            } else { //verificar campos vacios
                
                if (views.txt_supplier_name.getText().equals("")
                    || views.txt_supplier_description.getText().equals("")
                    || views.txt_supplier_address.getText().equals("")
                    || views.txt_supplier_telephone.getText().equals("")
                    || views.cmb_supplier_city.getSelectedItem().toString().equals("")
                    || views.txt_supplier_email.getText().equals("")){

                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                return;
            
    }else{
            supplier.setId(Integer.parseInt(views.txt_supplier_id.getText().trim()));
            supplier.setName(views.txt_supplier_name.getText().trim());
            supplier.setDescription(views.txt_supplier_description.getText().trim());
            supplier.setAddress(views.txt_supplier_address.getText().trim());
            supplier.setTelephone(views.txt_supplier_telephone.getText().trim());
            supplier.setCity(views.cmb_supplier_city.getSelectedItem().toString());
            supplier.setEmail(views.txt_supplier_email.getText().trim());
   
            if(supplierDao.updateSupplierQuery(supplier)){
                cleanTable();
                cleanFields();
                listAllSuppliers();
                
                JOptionPane.showMessageDialog(null, "Datos del proveedor modificados con exito");
               
            }else{
               
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar este proveedor");
            }
            
            
            }

    
    }
            }else if(e.getSource ()== views.btn_delete_supplier){
             int row = views.suppliers_table.getSelectedRow(); //devuelve en que fila fue hecho el click//

            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Debes seleccionar un proveedor para eliminar");

            
            } else {
                int id = Integer.parseInt(views.suppliers_table.getValueAt(row, 0).toString());
                /*Esto convierte el valor en la primera columna de la fila seleccionada en un número entero, 
        ya que el ID debe ser un número para la eliminación.*/
                int question = JOptionPane.showConfirmDialog(null, "¿Quieres eliminar el proveedor?");

                if (question == 0 && supplierDao.deleteSupplierQuery(id) != false) { //elimina de la base de datos //
                    cleanFields();
                    views.btn_register_supplier.setEnabled(true);
                     listAllSuppliers();
                    JOptionPane.showMessageDialog(null, "Proveedor eliminado con exito");
    }
            }
            }
        
        else if(e.getSource() == views.btn_cancel_supplier){
        
        views.btn_register_supplier.setEnabled(true);
        cleanFields();
        
        
    }
    }
        
    
        
public void listAllSuppliers() {

        List<Suppliers> list = supplierDao.listSupplierQuery(views.txt_search_supplier.getText());
        model = (DefaultTableModel) views.suppliers_table.getModel();
        Object[] row = new Object[7];

        for (int i = 0; i < list.size(); i++) {
            
            row[0] = list.get(i).getId();
            row[1] = list.get(i).getName();
            row[2] = list.get(i).getDescription();
            row[3] = list.get(i).getAddress();
            row[4] = list.get(i).getTelephone();
            row[5] = list.get(i).getEmail();
            row[6] = list.get(i).getCity();
              model.addRow(row);

        }
        views.suppliers_table.setModel(model);
        
 }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == views.suppliers_table){
            int row = views.suppliers_table.rowAtPoint(e.getPoint());
            views.txt_supplier_id.setText(views.suppliers_table.getValueAt(row,0).toString());
            views.txt_supplier_name.setText(views.suppliers_table.getValueAt(row,1).toString());
            views.txt_supplier_description.setText(views.suppliers_table.getValueAt(row,2).toString());
            views.txt_supplier_address.setText(views.suppliers_table.getValueAt(row,3).toString());
            views.txt_supplier_telephone.setText(views.suppliers_table.getValueAt(row,4).toString());
            views.txt_supplier_email.setText(views.suppliers_table.getValueAt(row,5).toString());
            views.cmb_supplier_city.setSelectedItem(views.suppliers_table.getValueAt(row,6).toString());
            
            //Deshabilitar botones
            views.btn_register_supplier.setEnabled(false);
            views.txt_supplier_id.setEnabled(false);
            
            } else if (e.getSource() == views.jLabelSuppliers) {

            views.PanelProduct.setSelectedIndex(5);
           cleanTable();
           cleanFields();
           listAllSuppliers();


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
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getSource() == views.txt_search_supplier){
            cleanTable();
            listAllSuppliers();
        }
         
    }
       public void cleanTable() {
        //Recorre la tabla por filas para limpiar cada una
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
          
       }
         
       private void cleanFields() {
              
             views.txt_supplier_id.setText("");
            views.txt_supplier_id.setEditable(true);
            views.txt_supplier_name.setText("");
            views.txt_supplier_address.setText("");
            views.txt_supplier_telephone.setText("");
            views.txt_supplier_email.setText("");
            views.cmb_supplier_city.setSelectedIndex(0);
              
          }
       
}