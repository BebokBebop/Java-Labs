package pl.polsl.lab.servlets.login;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Allows the user to log in on site
 *
 * @author Hubert Przegendza
 * @version 1.0
 */
public class Login extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Register</title>");
            out.println("</head>");
            out.println("<body>");
            out.println(""
                    + "<form action = \"LoginAnswer\" method=\"POST\">\n"
                    + "    <p>Nickname:<input type=text size=20 name=nickname></p>\n"
                    + "    <p>Password:<input type=password size=20 name=password></p>\n"
                    + "    <input type=\"submit\" value=\"Login\" />\n"
                    + "</form>");
            out.println(""
                    + "<form action = \"Register\" method=\"GET\">\n"
                    + "<h2> Don't have an account yet?</h2>"
                    + "    <input type=\"submit\" value=\"Register\" />\n"
                    + "</form>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Allows login of an user";
    }

}
