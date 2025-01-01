import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(urlPatterns = "/order")
public class OrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter printWriter = resp.getWriter();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company2", "root", "Ijse@1234");
            ResultSet resultSet = connection.prepareStatement("select max(order_id) as max_order_id from orders").executeQuery();

            if (resultSet.next()) {
                String max_order_id = resultSet.getString("max_order_id");
                System.out.println("max_order_id : " + max_order_id);
                printWriter.write(max_order_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String orderId = req.getParameter("orderid");
        String customerId = req.getParameter("customerid");
        String itemCode = req.getParameter("itemcode");
        String total = req.getParameter("total");
        System.out.println("orderId : " + orderId + ", customerId : " + customerId + ", itemCode : " + itemCode + ", total : " + total);

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company2", "root", "Ijse@1234");
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO orders (order_id, customer_id, total) VALUES (?, ?, ?)");

            preparedStatement.setString(1, orderId);
            preparedStatement.setString(2, customerId);
            preparedStatement.setString(3, total);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
