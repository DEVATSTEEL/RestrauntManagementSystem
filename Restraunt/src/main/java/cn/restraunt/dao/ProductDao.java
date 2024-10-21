package cn.restraunt.dao;

import java.sql.*;
import java.util.*;

import cn.restraunt.model.Cart;
import cn.restraunt.model.Product;

public class ProductDao {
    private Connection con;

    public ProductDao(Connection con) {
        this.con = con;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products";

        try (PreparedStatement pst = this.con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setCategory(rs.getString("category"));
                product.setPrice(rs.getDouble("price"));
                product.setImage(rs.getString("image"));

                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public Product getSingleProduct(int id) {
        Product product = null;
        String query = "SELECT * FROM products WHERE id=?";

        try (PreparedStatement pst = this.con.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setCategory(rs.getString("category"));
                product.setPrice(rs.getDouble("price"));
                product.setImage(rs.getString("image"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    public double getTotalCartPrice(List<Cart> cartList) {
        double total = 0;

        for (Cart item : cartList) {
            String query = "SELECT price FROM products WHERE id=?";
            try (PreparedStatement pst = this.con.prepareStatement(query)) {
                pst.setInt(1, item.getId());
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    total += rs.getDouble("price") * item.getQuantity();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return total;
    }

    public List<Cart> getCartProducts(List<Cart> cartList) {
        List<Cart> cartProducts = new ArrayList<>();

        for (Cart item : cartList) {
            String query = "SELECT * FROM products WHERE id=?";
            try (PreparedStatement pst = this.con.prepareStatement(query)) {
                pst.setInt(1, item.getId());
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    Cart cartItem = new Cart();
                    cartItem.setId(rs.getInt("id"));
                    cartItem.setName(rs.getString("name"));
                    cartItem.setCategory(rs.getString("category"));
                    cartItem.setPrice(rs.getDouble("price") * item.getQuantity());
                    cartItem.setQuantity(item.getQuantity());
                    cartProducts.add(cartItem);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return cartProducts;
    }

    public Product getProductById(int productId) {
        Product product = null;
        String query = "SELECT * FROM products WHERE id=?";

        try (PreparedStatement pst = this.con.prepareStatement(query)) {
            pst.setInt(1, productId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setCategory(rs.getString("category"));
                product.setPrice(rs.getDouble("price"));
                product.setImage(rs.getString("image"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product; // Returns null if the product is not found
    }
}
