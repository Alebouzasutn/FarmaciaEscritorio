package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

public class ProductsDao {

    ConnectionMysql cn = new ConnectionMysql();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    public boolean registerProductsQuery(Products product) {
        String query = "INSERT INTO products (code, name, description, unit_price, created, updated, category_id)" + "VALUES( ?,?,?,?,?,?,?)";
        Timestamp datetime = new Timestamp(new Date().getTime());

        try {
            conn = cn.getConnection();
            
            //Prepara la query para enviar a la bd//
            pst = conn.prepareStatement(query);
            
            pst.setInt(1, product.getCode()); //Llena los ? con cada atributo correspondiente del objeto//
            pst.setString(2, product.getName());
            pst.setString(3, product.getDescription());
            pst.setDouble(4, product.getUnit_price());
            pst.setTimestamp(5, datetime);
            pst.setTimestamp(6, datetime);
            pst.setInt(7, product.getCategory_id());
            pst.execute();
            return true;
            /*cada setInt, String, etc() asigna un valor real al correspondiente ? en la consulta SQL.
            setInt(1, product.getCode()); → Reemplaza el primer ? con el código del producto.
            setString(2, product.getName()); → Reemplaza el segundo ? con el nombre del producto*/

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar el producto" + e);
            return false;
        }
    }

    public List listProductQuery(String value) {
        List<Products> list_products = new ArrayList();
        String query = "SELECT pro.*, ca.name AS category_name FROM products pro, categories ca WHERE pro.category_id = ca.id";
        String query_search_product = "SELECT pro.*, ca.name AS category_name FROM products pro INNER JOIN categories ca ON pro.category_id = ca.id WHERE pro.name LIKE '%" + value + "%'";

        try {

            conn = cn.getConnection();
            if (value.equalsIgnoreCase("")) {
                pst = conn.prepareStatement(query);
                rs = pst.executeQuery();

            } else {
                pst = conn.prepareStatement(query_search_product);
                rs = pst.executeQuery();
            }
            while (rs.next()) {
                Products product = new Products();
                product.setId(rs.getInt("id"));
                product.setCode(rs.getInt("code"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setUnit_price(rs.getDouble("unit_price"));
                product.setProduct_quantity(rs.getInt("product_quantity"));
                product.setCategory_id(rs.getInt("category_id"));

                list_products.add(product);

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return list_products;
    }

    public boolean updateProductQuery(Products product) {
        String query = "UPDATE products SET code = ?, name =?, description =?, unit_price =?, product_quantity =?, category_id =?, created =?,updated=?, category_name=? WHERE id = ?";
        Timestamp datetime = new Timestamp(new Date().getTime());

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, product.getCode());
            pst.setString(2, product.getName());
            pst.setString(3, product.getDescription());
            pst.setDouble(4, product.getUnit_price());
            pst.setInt(5, product.getCategory_id());
            pst.setInt(6, product.getProduct_quantity());
            pst.setTimestamp(7, datetime);
            pst.execute();
            return true;
             /*Actualiza la base de datos enviando la query UPDATE y setea cada atributo del objeto */
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "error al modificar producto" + e);
            return false;
        }
    }

    public boolean deleteProductQuery(int id) {
        String query = "DELETE FROM products WHERE id = " + id;

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.execute();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No puede eliminar un producto que tenga relacion con otra tabla");
            return false;

        }

    }

    public Products searchProduct (int id){
        String query = "SELECT pro.*, ca.name AS category_name FROM products pro INNER JOIN categories ca ON pro.category_id = ca.id WHERE pro.id = ?";
        
        Products product = new Products ();
        try{
            conn = cn.getConnection();
            pst= conn.prepareStatement(query);
            pst.setInt(1,id);
            rs=pst.executeQuery();
            
            if (rs.next()){
                product.setId(rs.getInt("id"));
                product.setCode(rs.getInt("code"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setUnit_price(rs.getDouble("unit_price"));
                product.setCategory_id(rs.getInt("category_id"));
                product.setCategory_name(rs.getString("category_name"));
            }
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return product;
    }

     public Products searchCode (int code){
        String query = "SELECT pro.id, pro.name FROM products pro WHERE code = ?";
        Products product = new Products ();
        try{
            conn = cn.getConnection();
            pst= conn.prepareStatement(query);
            pst.setInt(1,code);
            rs=pst.executeQuery();
            
            if (rs.next()){
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                
            }
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return product;
    }



    
    
public Products searchId (int id){
    String query = "SELECT pro.product_quantity  FROM products pro WHERE pro.code = ?";
    Products product = new Products();
    
    try{
        conn = cn.getConnection();
            pst= conn.prepareStatement(query);
            pst.setInt(1, id);
            rs= pst.executeQuery();
            if(rs.next()){
                product.setProduct_quantity(rs.getInt("product_quantity"));
            
       }
       }catch(SQLException e){
    JOptionPane.showMessageDialog(null, e.getMessage());
}
return product;
}
public boolean updateStockQuery(int amount, int product_id){
    String query = "UPDATE products SET product_quantity = ? WHERE id = ?";
    try{
        conn = cn.getConnection();
        pst = conn.prepareStatement(query);
        pst.setInt(1, amount);
        pst.setInt(2, product_id);
        pst.execute();
        return true;
    }catch(SQLException e){
        JOptionPane.showMessageDialog(null, e.getMessage());
        return false;
    }
}
}