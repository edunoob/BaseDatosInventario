package ElDelUber;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ProductDAO {
   public Product addProduct(Product product) throws SQLException {
       String sql = "INSERT INTO products (name, price, stock, category, description) VALUES (?, ?, ?, ?, ?)";
       try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
           stmt.setString(1, product.getName());
           stmt.setDouble(2, product.getPrice());
           stmt.setInt(3, product.getStock());
           stmt.setString(4, product.getCategory());
           stmt.setString(5, product.getDescription());
           stmt.executeUpdate();
           try (ResultSet rs = stmt.getGeneratedKeys()) {
               if (rs.next()) product.setId(rs.getInt(1));
           }
       }
       return product;
   }
   public Product getProductById(int id) throws SQLException {
       String sql = "SELECT * FROM products WHERE id = ?";
       Product product = null;
       try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
           stmt.setInt(1, id);
           try (ResultSet rs = stmt.executeQuery()) {
               if (rs.next()) {
                   product = new Product(
                           rs.getInt("id"),
                           rs.getString("name"),
                           rs.getDouble("price"),
                           rs.getInt("stock"),
                           rs.getString("category"),
                           rs.getString("description")
                   );
               }
           }
       }
       return product;
   }
   public List<Product> getAllProducts() throws SQLException {
       List<Product> products = new ArrayList<>();
       String sql = "SELECT * FROM products";
       try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
           while (rs.next()) {
               products.add(new Product(
                       rs.getInt("id"),
                       rs.getString("name"),
                       rs.getDouble("price"),
                       rs.getInt("stock"),
                       rs.getString("category"),
                       rs.getString("description")
               ));
           }
       }
       return products;
   }
   public boolean updateProduct(Product product) throws SQLException {
       String sql = "UPDATE products SET name=?, price=?, stock=?, category=?, description=? WHERE id=?";
       try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
           stmt.setString(1, product.getName());
           stmt.setDouble(2, product.getPrice());
           stmt.setInt(3, product.getStock());
           stmt.setString(4, product.getCategory());
           stmt.setString(5, product.getDescription());
           stmt.setInt(6, product.getId());
           return stmt.executeUpdate() > 0;
       }
   }
   public boolean deleteProduct(int id) throws SQLException {
       String sql = "DELETE FROM products WHERE id=?";
       try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
           stmt.setInt(1, id);
           return stmt.executeUpdate() > 0;
       }
   }
}

