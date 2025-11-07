//package ElDelUber;
//
//import java.sql.SQLException;
//import java.util.List;
//
//public class Main {
//    public static void main(String[] args) {
//        ProductDAO dao = new ProductDAO();
//
//        try {
//            System.out.println("=== AGREGANDO PRODUCTO ===");
//            Product p1 = new Product("Silla de oficina", 49.99, 5, "Muebles", "Silla ergonómica usada");
//            dao.addProduct(p1);
//            System.out.println("Producto agregado: " + p1);
//
//            System.out.println("\n=== BUSCANDO PRODUCTO POR ID ===");
//            Product found = dao.getProductById(p1.getId());
//            System.out.println("Producto recuperado: " + found);
//
//            System.out.println("\n=== LISTA COMPLETA ===");
//            List<Product> all = dao.getAllProducts();
//            all.forEach(System.out::println);
//
//            System.out.println("\n=== ACTUALIZANDO PRODUCTO ===");
//            found.setPrice(39.99);
//            dao.updateProduct(found);
//            System.out.println("Producto actualizado: " + dao.getProductById(found.getId()));
//
//            System.out.println("\n=== ELIMINANDO PRODUCTO ===");
//            dao.deleteProduct(found.getId());
//            System.out.println("Producto eliminado con éxito.");
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}
package ElDelUber;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// 🔹 Importamos las excepciones personalizadas
import ElDelUber.CustomException;
import ElDelUber.NegativeNumberException;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int totalTests = 8;
        int passedTests = 0;
        List<String> failedTests = new ArrayList<>();

        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();
            ProductDAO dao = new ProductDAO();

            System.out.println("========== 🧪 TEST AUTOMÁTICOS CRUD PRODUCT ==========");

            // --------------------------
            // 1️⃣ Crear producto válido
            // --------------------------
            System.out.println("\n🟢 Test 1: Crear producto válido");
            if (!askContinue()) return;
            try {
                Product p1 = new Product("Guitarra Fender", 699.99, 5, "Instrumentos", "Guitarra eléctrica Fender Stratocaster");
                dao.addProduct(p1);
                System.out.println("✅ Producto añadido correctamente con ID: " + p1.getId());
                passedTests++;
            } catch (Exception e) {
                System.out.println("❌ Error en Test 1: " + e.getMessage());
                failedTests.add("1 / Crear producto válido");
            }

            // --------------------------
            // 2️⃣ Obtener producto por ID
            // --------------------------
            System.out.println("\n🟢 Test 2: Obtener producto por ID (1)");
            if (!askContinue()) return;
            try {
                Product fetched = dao.getProductById(1);
                if (fetched != null) {
                    System.out.println("✅ Producto obtenido: " + fetched);
                    passedTests++;
                } else {
                    System.out.println("❌ No se encontró el producto con ID 1");
                    failedTests.add("2 / Obtener producto por ID");
                }
            } catch (Exception e) {
                System.out.println("❌ Error al obtener producto: " + e.getMessage());
                failedTests.add("2 / Obtener producto por ID");
            }

            // --------------------------
            // 3️⃣ Listar todos los productos
            // --------------------------
            System.out.println("\n🟢 Test 3: Listar todos los productos");
            if (!askContinue()) return;
            try {
                List<Product> all = dao.getAllProducts();
                if (all.isEmpty()) {
                    System.out.println("⚠️ No hay productos en la base de datos.");
                    failedTests.add("3 / Listar todos los productos");
                } else {
                    System.out.println("✅ Productos encontrados: ");
                    all.forEach(System.out::println);
                    passedTests++;
                }
            } catch (Exception e) {
                System.out.println("❌ Error al listar productos: " + e.getMessage());
                failedTests.add("3 / Listar todos los productos");
            }

            // --------------------------
            // 4️⃣ Actualizar producto
            // --------------------------
            System.out.println("\n🟢 Test 4: Actualizar producto con ID 1");
            if (!askContinue()) return;
            try {
                Product fetched = dao.getProductById(1);
                if (fetched != null) {
                    fetched.setPrice(749.99);
                    fetched.setStock(7);
                    boolean updated = dao.updateProduct(fetched);
                    System.out.println(updated ? "✅ Producto actualizado correctamente" : "❌ No se actualizó el producto");
                    if (updated) passedTests++;
                    else failedTests.add("4 / Actualizar producto");
                } else {
                    System.out.println("⚠️ No existe el producto con ID 1 para actualizar.");
                    failedTests.add("4 / Actualizar producto");
                }
            } catch (Exception e) {
                System.out.println("❌ Error al actualizar: " + e.getMessage());
                failedTests.add("4 / Actualizar producto");
            }

            // --------------------------
            // 5️⃣ Borrar producto
            // --------------------------
            System.out.println("\n🟢 Test 5: Borrar producto con ID 1");
            if (!askContinue()) return;
            try {
                boolean deleted = dao.deleteProduct(1);
                System.out.println(deleted ? "✅ Producto borrado correctamente" : "❌ No se borró el producto");
                if (deleted) passedTests++;
                else failedTests.add("5 / Borrar producto");
            } catch (Exception e) {
                System.out.println("❌ Error al borrar: " + e.getMessage());
                failedTests.add("5 / Borrar producto");
            }

            // --------------------------
            // 6️⃣ Campos demasiado largos
            // --------------------------
            System.out.println("\n🔴 Test 6: Crear producto con texto demasiado largo");
            if (!askContinue()) return;
            try {
                String longText = "X".repeat(400);
                Product pError6 = new Product(longText, 999.99, 10, "Instrumentos", longText);
                dao.addProduct(pError6);
                System.out.println("❌ ERROR: Se permitió producto con texto demasiado largo");
                failedTests.add("6 / Campos demasiado largos");
            } catch (SQLException e) {
                System.out.println("✅ Excepción SQL capturada (texto demasiado largo): " + e.getMessage());
                passedTests++;
            } catch (Exception e) {
                System.out.println("✅ Excepción capturada correctamente: " + e.getMessage());
                passedTests++;
            }

            // --------------------------
            // 7️⃣ Comprobar conexión cerrada
            // --------------------------
            System.out.println("\n🟢 Test 7: Comprobar si la conexión está cerrada después de los tests");
            if (!askContinue()) return;
            try {
                conn.close();
                if (conn.isClosed()) {
                    System.out.println("✅ La conexión está cerrada correctamente.");
                    passedTests++;
                } else {
                    System.out.println("❌ La conexión sigue abierta, debería estar cerrada.");
                    failedTests.add("7 / Conexión cerrada después de tests");
                }
            } catch (SQLException e) {
                System.out.println("❌ Error comprobando la conexión: " + e.getMessage());
                failedTests.add("7 / Conexión cerrada después de tests");
            }

            // --------------------------
            // 8️⃣ Producto con número negativo
            // --------------------------
            System.out.println("\n🔴 Test 8: Crear producto con precio negativo");
            if (!askContinue()) return;
            try {
                double precio = -10.0;
                if (precio < 0)
                    throw new NegativeNumberException("precio");
                Product pError8 = new Product("Producto Erróneo", precio, 5, "Error", "Debe fallar");
                dao.addProduct(pError8);
                System.out.println("❌ ERROR: Se permitió producto con precio negativo");
                failedTests.add("8 / Producto con número negativo");
            } catch (NegativeNumberException e) {
                System.out.println("✅ Excepción personalizada capturada: " + e.getMessage());
                passedTests++;
            }

            // --------------------------
            // RESUMEN FINAL
            // --------------------------
            System.out.println("\n========== ✅ FIN DE TEST AUTOMÁTICOS ==========");
            double passedPercent = (passedTests * 100.0) / totalTests;
            double failedPercent = 100 - passedPercent;

            System.out.printf("\n📊 Resumen de Tests:\n");
            System.out.printf("✔️ Pasados: %.2f%% (%d/%d)\n", passedPercent, passedTests, totalTests);
            System.out.printf("❌ Fallidos: %.2f%% (%d/%d)\n", failedPercent, failedTests.size(), totalTests);

            if (!failedTests.isEmpty()) {
                System.out.println("\nTests fallidos:");
                failedTests.forEach(f -> System.out.println(" - " + f));
            }

            // --------------------------
            // 🔧 MODO MANUAL
            // --------------------------
            System.out.println("\n========== 🧍 MODO MANUAL (INTRODUCIR DATOS) ==========");
            manualMode(dao);

        } catch (SQLException e) {
            System.err.println("❌ Error SQL general: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean askContinue() {
        System.out.print("\n¿Deseas continuar con el siguiente test? (s/n): ");
        String input = scanner.nextLine().trim().toLowerCase();
        return input.equals("s") || input.equals("si");
    }

    /**
     * 🧍 Permite al usuario introducir productos y realizar operaciones CRUD manualmente.
     */
    private static void manualMode(ProductDAO dao) {
        while (true) {
            System.out.println("\nSeleccione una opción:");
            System.out.println("1. Añadir producto");
            System.out.println("2. Listar productos");
            System.out.println("3. Buscar producto por ID");
            System.out.println("4. Actualizar producto");
            System.out.println("5. Borrar producto");
            System.out.println("6. Salir");
            System.out.print("Opción: ");
            String option = scanner.nextLine();

            try {
                switch (option) {
                    case "1": {
                        System.out.print("Nombre: ");
                        String name = scanner.nextLine();
                        System.out.print("Precio: ");
                        double price = Double.parseDouble(scanner.nextLine());
                        System.out.print("Stock: ");
                        int stock = Integer.parseInt(scanner.nextLine());
                        System.out.print("Categoría: ");
                        String category = scanner.nextLine();
                        System.out.print("Descripción: ");
                        String description = scanner.nextLine();

                        // ✅ Validaciones con excepciones personalizadas
                        if (price < 0)
                            throw new NegativeNumberException("precio");
                        if (stock < 0)
                            throw new NegativeNumberException("stock");
                        if (name.isEmpty() || category.isEmpty() || description.isEmpty())
                            throw new CustomException("El nombre, categoría y descripción no pueden estar vacíos");

                        Product p = new Product(name, price, stock, category, description);
                        dao.addProduct(p);
                        System.out.println("✅ Producto añadido con ID: " + p.getId());
                        break;
                    }
                    case "2": {
                        List<Product> all = dao.getAllProducts();
                        if (all.isEmpty()) System.out.println("No hay productos.");
                        else all.forEach(System.out::println);
                        break;
                    }
                    case "3": {
                        System.out.print("Introduce el ID: ");
                        int id = Integer.parseInt(scanner.nextLine());
                        Product p = dao.getProductById(id);
                        System.out.println(p != null ? p : "Producto no encontrado.");
                        break;
                    }
                    case "4": {
                        System.out.print("ID del producto a actualizar: ");
                        int id = Integer.parseInt(scanner.nextLine());
                        Product p = dao.getProductById(id);
                        if (p == null) {
                            System.out.println("No existe ese producto.");
                            break;
                        }
                        System.out.print("Nuevo precio: ");
                        double newPrice = Double.parseDouble(scanner.nextLine());
                        if (newPrice < 0)
                            throw new NegativeNumberException("precio");
                        System.out.print("Nuevo stock: ");
                        int newStock = Integer.parseInt(scanner.nextLine());
                        if (newStock < 0)
                            throw new NegativeNumberException("stock");
                        p.setPrice(newPrice);
                        p.setStock(newStock);
                        boolean ok = dao.updateProduct(p);
                        System.out.println(ok ? "✅ Actualizado correctamente" : "❌ No se actualizó");
                        break;
                    }
                    case "5": {
                        System.out.print("ID del producto a borrar: ");
                        int id = Integer.parseInt(scanner.nextLine());
                        boolean ok = dao.deleteProduct(id);
                        System.out.println(ok ? "✅ Producto eliminado" : "❌ No se encontró el producto");
                        break;
                    }
                    case "6": {
                        System.out.println("👋 Saliendo del modo manual...");
                        return;
                    }
                    default:
                        System.out.println("❌ Opción no válida.");
                }
            } catch (Exception e) {
                System.out.println("⚠️ Error: " + e.getMessage());
            }
        }
    }
}