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
import models.Customers;
import models.CustomersDao;
import views.SystemView;



public class CustomersController implements ActionListener, MouseListener, KeyListener {
// Creo variables para almancenar los datos para su manipulacion en setters y getters en los eventos debajo
    private Customers customer;
    private CustomersDao customerDao;
    private SystemView views;
    DefaultTableModel model = new DefaultTableModel();
    private Object rol;

    
    public CustomersController(Customers customer, CustomersDao customerDao, SystemView views) {
        this.customer = customer;
        this.customerDao = customerDao;
        this.views = views;
        this.views.btn_register_customer.addActionListener(this);
        this.views.btn_modify_customer.addActionListener(this);
        this.views.btn_delete_customer.addActionListener(this);
        this.views.btn_cancel_customer.addActionListener(this);
        this.views.jLabelCustomers.addMouseListener(this);
        //Tabla y buscador
        this.views.customers_table.addMouseListener(this);
        this.views.txt_search_customer.addKeyListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == views.btn_register_customer) {
            if (views.txt_customer_id.getText().equals("")
                    || views.txt_customer_full_name.getText().equals("")
                    || views.txt_customer_address.getText().equals("")
                    || views.txt_customer_telephone.getText().equals("")
                    || views.txt_customer_email.getText().equals("")) {

                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                return;

            } else {

                customer.setId(Integer.parseInt(views.txt_customer_id.getText().trim()));
                customer.setFull_name(views.txt_customer_full_name.getText().trim());
                customer.setAddress(views.txt_customer_address.getText().trim());
                customer.setTelephone(views.txt_customer_telephone.getText().trim());
                customer.setEmail(views.txt_customer_email.getText().trim());

                if (customerDao.registerCustomerQuery(customer)) {
                    cleanTable();
                    listAllCustomers();

                    JOptionPane.showMessageDialog(null, "Cliente registrado con exito");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar el cliente");

                }

            }

        }else if (e.getSource() == views.btn_modify_customer) {
            if (views.txt_customer_id.equals("")) {
                
                JOptionPane.showMessageDialog(null, "Selecciona una fila para continuar");
            } else { //verificar campos vacios
                if (views.txt_customer_id.getText().equals("")
                        || views.txt_customer_full_name.getText().equals("")
                        || views.txt_customer_address.getText().equals("")
                        || views.txt_customer_telephone.getText().equals("")
                        || views.txt_customer_email.getText().equals("")){
                        
                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                
                }else{
                customer.setId(Integer.parseInt(views.txt_customer_id.getText().trim()));
                customer.setFull_name(views.txt_customer_full_name.getText().trim());
                customer.setAddress(views.txt_customer_address.getText().trim());
                customer.setTelephone(views.txt_customer_telephone.getText().trim());
                customer.setEmail(views.txt_customer_email.getText().trim());
                    
                
                    if (customerDao.updateCustomerQuery(customer)) {
                        cleanTable();
                        cleanFields();
                        listAllCustomers();
                        views.btn_register_customer.setEnabled(true);
                        JOptionPane.showMessageDialog(null, "Datos del cliente modificados con exito");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al modificar datos del cliente");
                    }

                }
            }
    } else if (e.getSource() == views.btn_delete_customer) { // Si el click fue en boton Eliminar//
            int row = views.customers_table.getSelectedRow(); //devuelve en que fila fue hecho el click//

            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Debes seleccionar un cliente para eliminar");

            
            } else {
                int id = Integer.parseInt(views.customers_table.getValueAt(row, 0).toString());
                /*Esto convierte el valor en la primera columna de la fila seleccionada en un número entero, 
        ya que el ID debe ser un número para la eliminación.*/
                int question = JOptionPane.showConfirmDialog(null, "¿Quieres eliminar el cliente?");

                if (question == 0 && customerDao.deleteCustomerQuery(id) != false) { //elimina de la base de datos //
                    cleanFields();
                    views.btn_register_customer.setEnabled(true);
                     listAllCustomers();
                    JOptionPane.showMessageDialog(null, "Cliente eliminado con exito");
    }
            }
    }else if(e.getSource() == views.btn_cancel_customer){
        
        views.btn_register_customer.setEnabled(true);
        cleanFields();
        
        
    }
    }
   
    public void listAllCustomers() {

        List<Customers> list = customerDao.listCustomerQuery(views.txt_search_customer.getText());
        model = (DefaultTableModel) views.customers_table.getModel();
        Object[] row = new Object[5];

        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getId();
            row[1] = list.get(i).getFull_name();
            row[2] = list.get(i).getAddress();
            row[3] = list.get(i).getTelephone();
            row[4] = list.get(i).getEmail();
            model.addRow(row);

        }
        views.customers_table.setModel(model);
}

    @Override
    public void mouseClicked(MouseEvent e) {

        //Si el evento click fue hecho sobre la tabla//
        if (e.getSource() == views.customers_table) {

            //Devuelve la fila donde se hizo click//
            int row = views.customers_table.rowAtPoint(e.getPoint());

            //Los datos de la tabla se extraen y se muestran en los txt
            views.txt_customer_id.setText(views.customers_table.getValueAt(row, 0).toString());
            views.txt_customer_full_name.setText(views.customers_table.getValueAt(row, 1).toString());
            views.txt_customer_address.setText(views.customers_table.getValueAt(row, 2).toString());
            views.txt_customer_telephone.setText(views.customers_table.getValueAt(row, 3).toString());
            views.txt_customer_email.setText(views.customers_table.getValueAt(row, 4).toString());

            //Deshabilitar
            views.txt_customer_id.setEditable(false);
            views.btn_register_customer.setEnabled(false);

        } else if (e.getSource() == views.jLabelCustomers) {
           
                views.PanelProduct.setSelectedIndex(2);
                cleanTable();
                cleanFields();
                listAllCustomers();
           
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
        //Si se tecleo dentro de la caja de buscar, lista todos los empleados
        if (e.getSource() == views.txt_search_customer) {
            cleanTable();
            listAllCustomers();
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
        
            views.txt_customer_id.setText("");
            views.txt_customer_full_name.setText("");
            views.txt_customer_address.setText("");
            views.txt_customer_telephone.setText("");
            views.txt_customer_email.setText("");
    }    
       

}

    
    
