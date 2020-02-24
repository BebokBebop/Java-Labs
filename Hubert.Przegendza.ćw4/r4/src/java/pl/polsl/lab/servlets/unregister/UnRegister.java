package pl.polsl.lab.servlets.unregister;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pl.polsl.lab.exceptions.MyException;
import pl.polsl.lab.saved.users.UsersDB;

/**
 * Removes an user from the database
 *
 * @author Hubert Przegendza
 * @version 1.0
 */
public class UnRegister extends HttpServlet {

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
        String name = null;
        response.setContentType("text/html");
        Cookie loginCookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("user")) {
                    name = cookie.getValue();
                    loginCookie = cookie;
                    break;
                }
            }
        }
        if (loginCookie != null) {
            loginCookie.setMaxAge(0);
            response.addCookie(loginCookie);

            UsersDB usersDB = (UsersDB) request.getSession().getAttribute("usersDB");
            if (usersDB == null) {
                usersDB = new UsersDB();
                request.getSession().setAttribute("usersDB", usersDB);
            }
            try {
                usersDB.removeUser(name);
            } catch (MyException ex) {
                response.setContentType("text/html;charset=UTF-8");
                try (PrintWriter out = response.getWriter()) {
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>Error!</title>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println(""
                            + "<h1>" + "Name doesn't exists in database!" + "</h1>"
                            + "<form action = \"MainPage\" method=\"GET\">\n"
                            + "    <input type=\"submit\" value=\"Main Page\" />\n"
                            + "</form>");
                    out.println("</body>");
                    out.println("</html>");
                }
            }
            response.sendRedirect("MainPage");
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
        return "Removes an user from the database or prints an error i case the user wasnt in it.";
    }

}
