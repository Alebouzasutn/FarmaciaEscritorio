
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


public class SalesDao {
    
    
    ConnectionMysql cn = new ConnectionMysql();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    
    
     public boolean registerSaleQuery(int customer_id, int employee_id, double total) {
        String query = "INSERT INTO sales (customer_id, employee_id, total, sale_date)" +"VALUES( ?,?,?,?)";
        Timestamp datetime = new Timestamp(new Date().getTime());

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, customer_id);
            pst.setInt(2, employee_id);
            pst.setDouble(3,total);
            pst.setTimestamp(4, datetime);
            pst.execute();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "error al registrar la venta" + e);
            return false;
        }
    }
    
    public boolean registerSaleDetailQuery(int product_id, double sale_id, int sale_quantity, double sale_price, double sale_subtotal){
    
        String query = "INSERT INTO sale_details (product_id, sale_id, sale_quantity, sale_price, sale_subtotal) VALUES(?,?,?,?,?)";
    
    Timestamp datetime = new Timestamp(new Date().getTime());

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, product_id);
            pst.setDouble(2, sale_id);
            pst.setInt(3,sale_quantity);
            pst.setDouble(4,sale_price);
            pst.setDouble(5, sale_subtotal);
            pst.execute();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "error al registrar los detalles de la venta" + e);
            return false;
        }
    }
    
    
    public int saleId() {
        int id = 0;
        String query = "SELECT MAX(id) AS id FROM SALES";
        
        try {
            
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
            
            if (rs.next()){
                id = rs.getInt("id");
                
            }
        }  catch(SQLException e){
                    System.out.println(e.getMessage());
                    
                    }
            return id;
        
    
    }
    

       
    
    public List listAllSalesQuery(){
       List<Sales> list_sales = new ArrayList();
       String query = "SELECT pu.*, su.name AS supplier_name FROM purchases pu, suppliers su" +
               "WHERE pu.supplier_id = su.id ORDER BY pu.id ASC";
       
       try {
           conn = cn.getConnection();
           pst = conn.prepareStatement(query);
           rs =pst.executeQuery();
           while(rs.next()){
               Sales sale = new Sales();
               sale.setId(rs.getInt("id"));
               sale.setCustomer_name(rs.getString("customer"));
               sale.setEmployee_name(rs.getString("employee"));
               sale.setTotal_to_pay(rs.getDouble("total"));
               sale.setSale_date(rs.getString("sale_date"));
               list_sales.add(sale);
           }
       }catch(SQLException e){
           JOptionPane.showMessageDialog(null, e.getMessage());
           }
       return list_sales;
       }
    
    
    
}
    
    
    
    
    
    
    
    
    
    

