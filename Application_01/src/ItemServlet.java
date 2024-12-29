import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.IOException;
import java.sql.*;

@WebServlet(urlPatterns = "/item")
public class ItemServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("do get");
        resp.setContentType("application/json");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company2", "root", "Ijse@1234");
            ResultSet resultSet = connection.prepareStatement("select * from item").executeQuery();
            JsonArrayBuilder allitems = Json.createArrayBuilder();
            while (resultSet.next()) {
                String item_code = resultSet.getString("item_code");
                String description = resultSet.getString("description");
                String unit_price = resultSet.getString("unit_price");
                int qty = Integer.parseInt(resultSet.getString("quantity"));

                JsonObjectBuilder item = Json.createObjectBuilder();
                System.out.println("item_code : " + item_code + ", description : " + description + ", unit_price : " + unit_price + ", qty : " + qty);
                item.add("item_code", item_code);
                item.add("description", description);
                item.add("unit_price", unit_price);
                item.add("qty", qty);
                allitems.add(item);
            }
            resp.getWriter().write(allitems.build().toString());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("do post");

        String item_code = req.getParameter("itemCode");
       String description = req.getParameter("itemDescription");
       String unit_price = req.getParameter("itemPrice");
       String qty = req.getParameter("itemQty");
       System.out.println("item_code : " + item_code + ", description : " + description + ", unit_price : " + unit_price + ", qty : " + qty);

      /* if (item_code == null || item_code.isEmpty() || description == null || description.isEmpty() || unit_price == null || unit_price.isEmpty() || qty == null || qty.isEmpty()) {
           resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
           resp.getWriter().write("{\"message\":\"Item code, description, unit price, and quantity are required\"}");
           return;
       }*/
       try {
           Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company2", "root", "Ijse@1234");
           PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO item (item_code, description, unit_price, quantity) VALUES (?, ?, ?, ?)");

           preparedStatement.setString(1, item_code);
           preparedStatement.setString(2, description);
           preparedStatement.setString(3, unit_price);
           preparedStatement.setString(4, qty);

           preparedStatement.executeUpdate();
           resp.setStatus(HttpServletResponse.SC_CREATED);
           resp.getWriter().write("{\"message\":\"Item added successfully\"}");
       } catch (SQLException e) {
           resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
           resp.getWriter().write("{\"message\":\"Failed to add item\"}");
           e.printStackTrace();
       }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("do put");
        JsonObject jsonObject = Json.createReader(req.getReader()).readObject();

        String item_code = jsonObject.getString("itemCode");
        String description = jsonObject.getString("itemDescription");
        String unit_price = jsonObject.getString("itemPrice");
        String qty = jsonObject.getString("itemQty");

        if (item_code == null || item_code.isEmpty() || description == null || description.isEmpty() || unit_price == null || unit_price.isEmpty() || qty == null || qty.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\":\"Item code, description, unit price, and quantity are required\"}");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company2", "root", "Ijse@1234");

            String query = "UPDATE item SET description = ?, unit_price = ?, quantity = ? WHERE item_code = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, description);
            preparedStatement.setString(2, unit_price);
            preparedStatement.setString(3, qty);
            preparedStatement.setString(4, item_code);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("{\"message\":\"Item updated successfully\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"message\":\"Item not found\"}");
            }

            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException|SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"message\":\"Failed to update item\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("do delete");
        String item_code = req.getParameter("item_code");
        System.out.println("item_code : " + item_code);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company2", "root", "Ijse@1234");

            String query = "DELETE FROM item WHERE item_code = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, item_code);

            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
