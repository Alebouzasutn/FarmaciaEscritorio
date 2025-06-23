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
import models.Categories;
import models.CategoriesDao;
import models.DynamicCombobox;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import views.SystemView;

public class CategoriesController implements ActionListener, MouseListener, KeyListener {

    // Paso 1
    private Categories category;
    private CategoriesDao categoryDao;
    private SystemView views;
    DefaultTableModel model = new DefaultTableModel();

    //Paso 2
    public CategoriesController(Categories category, CategoriesDao categoryDao, SystemView views) {
        this.category = category;
        this.categoryDao = categoryDao;
        this.views = views;
        this.views.btn_register_category.addActionListener(this);
        this.views.btn_update_category.addActionListener(this);
        this.views.btn_delete_category.addActionListener(this);
        this.views.jLabelCategories.addMouseListener(this);
        
        this.views.category_table.addMouseListener(this);
        this.views.txt_search_category.addKeyListener(this);
        getCategoryName();
        AutoCompleteDecorator.decorate(views.cmb_product_category);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == views.btn_register_category) {

            if (views.txt_category_name.getText().equals("")) {

                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                return;
            } else {

                category.setName(views.txt_category_name.getText().trim());

                if (categoryDao.registerCategoryQuery(category)) {
                    JOptionPane.showMessageDialog(null, "Categoria registrada con exito");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar la categoria");

                }

            }
        }else if (e.getSource() == views.btn_update_category) {
            if (views.txt_category_id.getText().equals("")) {
                
                JOptionPane.showMessageDialog(null, "Selecciona una fila para continuar");
            } else { 
                if (views.txt_category_id.getText().equals("")
                        || views.txt_category_name.getText().equals("")){
              JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");

                }else {
                category.setId(Integer.parseInt(views.txt_category_id.getText().trim()));
                category.setName(views.txt_category_name.getText().trim());
                
                if(categoryDao.updateCategotyQuery(category)){
                    cleanTable();
                   cleanFields();
                   views.btn_register_category.setEnabled(true);
                   listAllCategories();
                }
                
                
                }
    }
        
        }else if(e.getSource ()== views.btn_delete_category){
             int row = views.category_table.getSelectedRow(); //devuelve en que fila fue hecho el click//

            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Debes seleccionar una categoria para eliminar");

            
            } else {
                int id = Integer.parseInt(views.category_table.getValueAt(row, 0).toString());
                /*Esto convierte el valor en la primera columna de la fila seleccionada en un número entero, 
        ya que el ID debe ser un número para la eliminación.*/
                int question = JOptionPane.showConfirmDialog(null, "¿Quieres eliminar la categoria?");

                if (question == 0 && categoryDao.deleteCategoryQuery(id) != false) { //elimina de la base de datos //
                    cleanFields();
                    views.btn_register_category.setEnabled(true);
                   
                    JOptionPane.showMessageDialog(null, "Categoria eliminada con exito");
    }
            }
            }
        
        
    
    }
     public void listAllCategories() {
        List<Categories> list = categoryDao.listCategoryQuery(views.txt_search_category.getText());
        model = (DefaultTableModel) views.category_table.getModel();

        Object[] row = new Object[2];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getId();
            row[1] = list.get(i).getName();
            model.addRow(row);
        }
        views.category_table.setModel(model);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.category_table){

            //Devuelve la fila donde se hizo click//
            int row = views.category_table.rowAtPoint(e.getPoint());

            //Los datos de la tabla se extraen y se muestran en los txt
            views.txt_category_id.setText(views.category_table.getValueAt(row,0).toString());
            views.txt_category_name.setText(views.category_table.getValueAt(row,1).toString());

            //Deshabilitar
            views.txt_customer_id.setEditable(false);
            views.btn_register_customer.setEnabled(false);

        } else if (e.getSource()== views.jLabelCategories){

            views.PanelProduct.setSelectedIndex(4);
            cleanTable();
            cleanFields();
            listAllCategories();

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

        views.txt_category_id.setText("");
        views.txt_category_id.setEditable(true);
        views.txt_category_name.setText("");

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
 //Si se tecleo dentro de la caja de buscar, lista todas las categorias
        if (e.getSource() == views.txt_search_category) {
            cleanTable();
            listAllCategories();
        
    }
}
    
    public void getCategoryName(){
         List<Categories> list = categoryDao.listCategoryQuery(views.txt_search_category.getText());
         
         for(int i =0; i<list.size(); i++){
             int id = list.get(i).getId();
             String name = list.get(i).getName();
             views.cmb_product_category.addItem(new DynamicCombobox(id, name));
         }
    }
}