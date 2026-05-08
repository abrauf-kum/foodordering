package foodordering.dao;

import foodordering.model.CartItem;
import foodordering.model.Order;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO extends BaseDAO {

    @Override
    protected String getTableName() { return "orders"; }

    public int placeOrder(int userId, List<CartItem> items, double total) {
        try {
            getConnection().setAutoCommit(false);

            PreparedStatement orderStmt = getConnection().prepareStatement(
                "INSERT INTO orders (user_id, total_amount) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            orderStmt.setInt(1, userId);
            orderStmt.setDouble(2, total);
            orderStmt.executeUpdate();

            ResultSet keys = orderStmt.getGeneratedKeys();
            if (!keys.next()) { getConnection().rollback(); return -1; }

            int orderId = keys.getInt(1);

            PreparedStatement detailStmt = getConnection().prepareStatement(
                "INSERT INTO order_details (order_id, item_id, quantity) VALUES (?, ?, ?)"
            );

            for (CartItem ci : items) {
                detailStmt.setInt(1, orderId);
                detailStmt.setInt(2, ci.getItem().getId());
                detailStmt.setInt(3, ci.getQuantity());
                detailStmt.addBatch();
            }

            detailStmt.executeBatch();
            getConnection().commit();

            return orderId;

        } catch (SQLException e) {
            try { getConnection().rollback(); }
            catch (SQLException ex) { ex.printStackTrace(); }

            e.printStackTrace();

        } finally {
            try { getConnection().setAutoCommit(true); }
            catch (SQLException e) { e.printStackTrace(); }
        }

        return -1;
    }

    public List<Order> getByUser(int userId) {
        return query(
            "SELECT o.*, u.name AS uname FROM orders o " +
            "JOIN users u ON o.user_id = u.user_id " +
            "WHERE o.user_id = ? ORDER BY o.order_date DESC",
            userId
        );
    }

    public List<Order> getAll() {
        return query(
            "SELECT o.*, u.name AS uname FROM orders o " +
            "JOIN users u ON o.user_id = u.user_id ORDER BY o.order_date DESC",
            -1
        );
    }

    public boolean updateStatus(int orderId, String status) {
        try (PreparedStatement ps = getConnection().prepareStatement(
            "UPDATE orders SET status = ? WHERE order_id = ?")) {

            ps.setString(1, status);
            ps.setInt(2, orderId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<String> getOrderLines(int orderId) {
        List<String> rows = new ArrayList<>();

        String sql = "SELECT m.item_name, od.quantity, m.price " +
                     "FROM order_details od JOIN item m ON od.item_id = m.item_id " +
                     "WHERE od.order_id = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();

            while (rs.next())
                rows.add(String.format("%-26s x%-3d  Rs.%.2f",
                    rs.getString("item_name"),
                    rs.getInt("quantity"),
                    rs.getDouble("price") * rs.getInt("quantity")
                ));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rows;
    }

    private List<Order> query(String sql, int param) {
        List<Order> list = new ArrayList<>();

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            if (param >= 0) ps.setInt(1, param);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    private Order mapRow(ResultSet rs) throws SQLException {
        return new Order(
            rs.getInt("order_id"),
            rs.getInt("user_id"),
            rs.getString("uname"),
            rs.getTimestamp("order_date"),
            rs.getDouble("total_amount"),
            rs.getString("status")
        );
    }
}