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
import models.Employees;
import models.EmployeesDao;
import static models.EmployeesDao.id_user;
import static models.EmployeesDao.rol_user;
import views.SystemView;

public class EmployeesController implements ActionListener, MouseListener, KeyListener {

    private Employees employee;
    private EmployeesDao employeeDao;
    private SystemView views;

    String rol = rol_user;
    DefaultTableModel model = new DefaultTableModel();

    public EmployeesController(Employees employee, EmployeesDao employeeDao, SystemView views) {
        this.employee = employee;
        this.employeeDao = employeeDao;
        this.views = views;

        //Boton de registrar empleados
        this.views.btn_register_employee.addActionListener(this);
        //Boton de modificar empleado
        this.views.btn_update_employee.addActionListener(this);
        //Boton de eliminar
        this.views.btn_delete_employee.addActionListener(this);
        this.views.btn_cancel_employee.addActionListener(this);
        //Boton cambiar contraseña
        this.views.btn_modify_data.addActionListener(this);
        //Pestaña empleados en escucha
        this.views.jLabelEmployees.addMouseListener(this);


        //Tabla y buscador
        this.views.employees_table.addMouseListener(this);
        this.views.txt_search_employee.addKeyListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_register_employee) {

            // Verifico campos vacíos
            if (views.txt_employee_id.getText().equals("")
                    || views.txt_employee_full_name.getText().equals("")
                    || views.txt_employee_user_name.getText().equals("")
                    || views.txt_employee_address.getText().equals("")
                    || views.txt_employee_telephone.getText().equals("")
                    || views.txt_employee_email.getText().equals("")
                    || views.cmb_employee_rol.getSelectedItem().toString().equals("")
                    || String.valueOf(views.txt_employee_password.getPassword()).equals("")) {

                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                return; // Detener ejecución si hay campos vacíos
            }else{

            // Realiza la inserción
            employee.setId(Integer.parseInt(views.txt_employee_id.getText().trim()));
            employee.setFull_name(views.txt_employee_full_name.getText().trim());
            employee.setUsername(views.txt_employee_user_name.getText().trim());
            employee.setAddress(views.txt_employee_address.getText().trim());
            employee.setTelephone(views.txt_employee_telephone.getText().trim());
            employee.setEmail(views.txt_employee_email.getText().trim());
            employee.setPassword(String.valueOf(views.txt_employee_password.getPassword()));
            employee.setRol(views.cmb_employee_rol.getSelectedItem().toString());

            
                if (employeeDao.registerEmployeeQuery(employee)) {
                   
                    listAllEmployees();

                    JOptionPane.showMessageDialog(null, "Empleado registrado con éxito");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar al empleado");
                }
            }  
        } else if (e.getSource() == views.btn_update_employee) {
            if (views.txt_employee_id.equals("")) {
                JOptionPane.showMessageDialog(null, "Selecciona una fila para continuar");
            } else { //verificar campos vaicos
                if (views.txt_employee_id.getText().equals("")
                        || views.txt_employee_full_name.getText().equals("")
                        || views.cmb_employee_rol.getSelectedItem().toString().equals("")) {

                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                } else {

                    employee.setId(Integer.parseInt(views.txt_employee_id.getText().trim()));
                    employee.setFull_name(views.txt_employee_full_name.getText().trim());
                    employee.setUsername(views.txt_employee_user_name.getText().trim());
                    employee.setAddress(views.txt_employee_address.getText().trim());
                    employee.setTelephone(views.txt_employee_telephone.getText().trim());
                    employee.setEmail(views.txt_employee_email.getText().trim());
                    employee.setPassword(String.valueOf(views.txt_employee_password.getPassword()));
                    employee.setRol(views.cmb_employee_rol.getSelectedItem().toString());

                    if (employeeDao.updateEmployeeQuery(employee)) {
                        cleanTable();
                        cleanFields();
                        listAllEmployees();
                        views.btn_register_employee.setEnabled(true);
                        JOptionPane.showMessageDialog(null, "Datos del empleado modificados con exito");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al modificar datos del empleado");
                    }

                }
            }
        } else if (e.getSource() == views.btn_delete_employee) { // Si el click fue en boton Eliminar//
            int row = views.employees_table.getSelectedRow(); //devuelve en que fila fue hecho el click//

            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Debes seleccionar un empleado para eliminar");

            } else if (views.employees_table.getValueAt(row, 0).equals(id_user)) {
                /*Evita que el usuario autenticado 
        se elimine a sí mismo*/

                JOptionPane.showMessageDialog(null, "No puede eliminar al usuario autenticado");

            } else {
                int id = Integer.parseInt(views.employees_table.getValueAt(row, 0).toString());
                /*Esto convierte el valor en la primera columna de la fila seleccionada en un número entero, 
        ya que el ID debe ser un número para la eliminación.*/
                int question = JOptionPane.showConfirmDialog(null, "¿Quieres eliminar el usuario?");

                if (question == 0 && employeeDao.deleteEmployeeQuery(id) != false) { //elimina de la base de datos //
                    cleanFields();
                    views.btn_register_employee.setEnabled(true);
                    views.txt_employee_password.setEnabled(true);
                    listAllEmployees();
                    JOptionPane.showMessageDialog(null, "Empleado eliminado con exito");

                    /*Se obtiene el ID del empleado seleccionado con: 
int id = Integer.parseInt(views.employees_table.getValueAt(row,0).toString());
Esto convierte el valor en la primera columna de la fila seleccionada en un número entero, 
ya que el ID debe ser un número para la eliminación*/
 /* La respuesta del usuario se almacena en question.
                Si el usuario elige "Sí", question tendrá el valor 0*/
                }
            }
        } else if (e.getSource() == views.btn_cancel_employee) {
            cleanFields();
            views.btn_register_employee.setEnabled(true);
            views.txt_employee_password.setEnabled(true);
            views.txt_employee_id.setEnabled(true);
        } else if (e.getSource() == views.btn_modify_data) {

            //Recolecto info de los box password
            String password = String.valueOf(views.txt_password_modify.getPassword());
            String confirm_password = String.valueOf(views.txt_password_modify_confirm.getPassword());

            //Verificar que las cajas de texto no estén vacías
            if (!password.equals("") && !confirm_password.equals("")) {  // 
                //Verificar que sean iguales
                if (password.equals(confirm_password)) {
                    employee.setPassword(password);  // 

                    if (employeeDao.updateEmployeePassword(employee)) {
                        JOptionPane.showMessageDialog(null, "Contraseña modificada con éxito");
                    } else {
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un error");
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Las Contraseñas no coinciden");
                }

            } else {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            }
        }
    }

    // Listar todos los empleados en la tabla
    public void listAllEmployees() {

           List<Employees> list = employeeDao.listEmployeesQuery(views.txt_search_employee.getText());
            model = (DefaultTableModel) views.employees_table.getModel();
            Object[] row = new Object[7];
            for (int i = 0; i < list.size(); i++) {

                row[0] = list.get(i).getId();
                row[1] = list.get(i).getFull_name();
                row[2] = list.get(i).getUsername();
                row[3] = list.get(i).getAddress();
                row[4] = list.get(i).getTelephone();
                row[5] = list.get(i).getEmail();
                row[6] = list.get(i).getRol();
                
                model.addRow(row); //agrega filas a la tabla
               
            } 
             views.employees_table.setModel(model);

        }

    

    @Override
    public void mouseClicked(MouseEvent e) {

        //Si el evento click fue hecho sobre la tabla//
        if (e.getSource() == views.employees_table) {

            //Devuelve la fila donde se hizo click//
            int row = views.employees_table.rowAtPoint(e.getPoint());

            //Los datos de la tabla se extraen y se muestran en los txt
            views.txt_employee_id.setText(views.employees_table.getValueAt(row, 0).toString());
            views.txt_employee_full_name.setText(views.employees_table.getValueAt(row, 1).toString());
            views.txt_employee_user_name.setText(views.employees_table.getValueAt(row, 2).toString());
            views.txt_employee_address.setText(views.employees_table.getValueAt(row, 3).toString());
            views.txt_employee_telephone.setText(views.employees_table.getValueAt(row, 4).toString());
            views.txt_employee_email.setText(views.employees_table.getValueAt(row, 5).toString());
            views.cmb_employee_rol.setSelectedItem(views.employees_table.getValueAt(row, 6).toString());

            //Deshabilitar
            views.txt_employee_id.setEditable(false);
            views.txt_employee_password.setEnabled(false);
            views.btn_register_employee.setEnabled(false);
        
        }else if(e.getSource() == views.jLabelEmployees){
        if(rol.equals("Admin")){
            views.PanelProduct.setSelectedIndex(3);
            cleanTable();
            cleanFields();
            listAllEmployees();
        }else{
            views.PanelProduct.setEnabledAt(3,false);
            views.jLabelEmployees.setEnabled(false);
            JOptionPane.showMessageDialog(null, "No tienes privilegios de Administrador para acceder");
        }
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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //Si se tecleo dentro de la caja de buscar, lista todos los empleados
        if (e.getSource() == views.txt_search_employee) {

            listAllEmployees();
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
        
         views.txt_employee_id.setText("");
            views.txt_employee_id.setEditable(true);
            views.txt_employee_full_name.setText("");
            views.txt_employee_address.setText("");
            views.txt_employee_telephone.setText("");
            views.txt_employee_email.setText("");
       
    }

}
