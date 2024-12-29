import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/hello")
public class HelloServelet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();
        String contextPath = req.getContextPath();
        String requestURI = req.getRequestURI();
        String method = req.getMethod();
        String pathInfo = req.getPathInfo();
        String remoteUser = req.getRemoteUser();

        System.out.println("ServletPath: " + servletPath);
        System.out.println("ContextPath: " + contextPath);
        System.out.println("RequestURI: " + requestURI);
        System.out.println("Method: " + method);
        System.out.println("PathInfo: " + pathInfo);
        System.out.println("RemoteUser: " + remoteUser);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.println("hello world");
        System.out.println("1");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.println("1");
        System.out.println("1");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.println("2");
        System.out.println("1");    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.println("3");
        System.out.println("1");    }
}