package controllers;

import java.awt.event.ActionListener;
import models.Employees;
import models.EmployeesDao;
import views.LoginView;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import views.SystemView;

public class Logincontroller implements ActionListener {

    private Employees employee;
    private EmployeesDao employees_dao;
    private LoginView login_view;

    public Logincontroller(Employees employee, EmployeesDao employees_dao, LoginView login_view) {
        this.employee = employee;
        this.employees_dao = employees_dao;
        this.login_view = login_view;
        this.login_view.btn_enter.addActionListener(this);
    }

    @Override
public void actionPerformed(ActionEvent e) {
    // Obtengo datos de la vista
    String user = login_view.txt_username.getText().trim();
    String pass = String.valueOf(login_view.txt_password.getPassword());

    // Evento botón enter en login view
    if (e.getSource() == login_view.btn_enter) {
        // Valido que los campos no estén vacíos
        if (!user.equals("") ||!pass.equals("")) {
            
            
            
            try {
                // Pasar los parámetros al método Login
                employee = employees_dao.loginQuery(user,pass);
            } catch (SQLException ex) {
                Logger.getLogger(Logincontroller.class.getName()).log(Level.SEVERE, null, ex);
            }
            
               
            }
            }

            // Verificar la existencia del usuario
            if (!user.equals("") && !pass.equals("")) { try {
                // Aseguramos que ambos campos estén llenos
                employee = employees_dao.loginQuery(user,pass);
        } catch (SQLException ex) {
            Logger.getLogger(Logincontroller.class.getName()).log(Level.SEVERE, null, ex);
        }

    // Verificamos si el usuario existe
    if (employee.getUsername() != null) {
        if (employee.getRol().equals("Administrador")) {
            SystemView admin = new SystemView();
            admin.setVisible(true);
        } else {
            SystemView aux = new SystemView();
            aux.setVisible(true);
        }
        this.login_view.dispose(); // Cerramos la ventana de login
    } else {
        JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
    }
} else {
    JOptionPane.showMessageDialog(null, "Los campos están vacíos");
}
}

}