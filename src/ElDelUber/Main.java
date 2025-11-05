package ElDelUber;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ProductDAO dao = new ProductDAO();

        try {
            System.out.println("=== AGREGANDO PRODUCTO ===");
            Product p1 = new Product("Silla de oficina", 49.99, 5, "Muebles", "Silla ergonómica usada");
            dao.addProduct(p1);
            System.out.println("Producto agregado: " + p1);

            System.out.println("\n=== BUSCANDO PRODUCTO POR ID ===");
            Product found = dao.getProductById(p1.getId());
            System.out.println("Producto recuperado: " + found);

            System.out.println("\n=== LISTA COMPLETA ===");
            List<Product> all = dao.getAllProducts();
            all.forEach(System.out::println);

            System.out.println("\n=== ACTUALIZANDO PRODUCTO ===");
            found.setPrice(39.99);
            dao.updateProduct(found);
            System.out.println("Producto actualizado: " + dao.getProductById(found.getId()));

            System.out.println("\n=== ELIMINANDO PRODUCTO ===");
            dao.deleteProduct(found.getId());
            System.out.println("Producto eliminado con éxito.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}