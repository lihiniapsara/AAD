import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/orderDetails")
public class OrderDetailServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("order details ekata data send weno");
        String orderId = req.getParameter("orderid");
        String itemCode = req.getParameter("itemcode");
        System.out.println("orderId : " + orderId + ", itemCode : " + itemCode);

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company2", "root", "Ijse@1234");
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO order_details (order_id, item_code) VALUES (?, ?)");

            preparedStatement.setString(1, orderId);
            preparedStatement.setString(2, itemCode);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("do put");
        JsonObject jsonObject = Json.createReader(req.getReader()).readObject();
        System.out.println("update wenna oni data set eka");
        String itemCode = jsonObject.getString("itemCode");
        System.out.println(itemCode);
        String qty = jsonObject.getString("qty");
        System.out.println(qty);

        try {
            System.out.println("1");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company2", "root", "Ijse@1234");
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE item SET quantity  = quantity  - ? WHERE item_code = ?");
            preparedStatement.setString(1, qty);
            preparedStatement.setString(2, itemCode);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
