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
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {
    private final List<CustomerDTO> customerDTOList = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        StringBuilder stringBuilder = new StringBuilder();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company2", "root", "Ijse@1234");
            ResultSet resultSet = connection.prepareStatement("select * from customer").executeQuery();
            JsonArrayBuilder allcustomers = Json.createArrayBuilder();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                JsonObjectBuilder customer = Json.createObjectBuilder();
                System.out.println("id : " + id + ", name : " + name + ", address : " + address);
                customer.add("id", id);
                customer.add("name", name);
                customer.add("address", address);
                allcustomers.add(customer);

            }
           // resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(allcustomers.build().toString());
            resp.addHeader("IP ADDRESS","244.178.44.111");
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);


        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String address = req.getParameter("address");

        System.out.println(id);
        System.out.println(name);
        System.out.println(address);

       // resp.setContentType("application/json");

        if (id == null || id.isEmpty() || name == null || name.isEmpty() || address == null || address.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\":\"ID, Name, and Address are required\"}");
            return;
        }

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company2", "root", "Ijse@1234");
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO customer (id, name, address) VALUES (?, ?, ?)");

            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, address);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("{\"message\":\"Customer added successfully\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"message\":\"Failed to add customer\"}");
            }

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"message\":\"Database error occurred: " + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject jsonObject = Json.createReader(req.getReader()).readObject();

        String id = jsonObject.getString("id") ;
        String name = jsonObject.getString("name") ;
        String address = jsonObject.getString("address") ;

        try {
            if (id == null || id.trim().isEmpty() || name == null || name.trim().isEmpty() || address == null || address.trim().isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"message\":\"ID, name, and address are required\"}");
                return;
            }

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company2", "root", "Ijse@1234");

            String query = "UPDATE customer SET name = ?, address = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name.trim());
            preparedStatement.setString(2, address.trim());
            preparedStatement.setString(3, id.trim());

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("{\"message\":\"Customer updated successfully\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"message\":\"Customer not found\"}");
            }

            preparedStatement.close();
            connection.close();

        } catch (ClassNotFoundException | SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"message\":\"An error occurred: " + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        System.out.println("id:"+id);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company2", "root", "Ijse@1234");

            String query = "DELETE from customer WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id);

            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}