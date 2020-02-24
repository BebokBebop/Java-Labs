package pl.polsl.lab.servlets.calculate;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;
import pl.polsl.lab.entities.calculation.Calculation;
import pl.polsl.lab.entities.result.Result;
import pl.polsl.lab.entities.user.Users;
import pl.polsl.lab.exceptions.MyException;
import pl.polsl.lab.model.BergerTableGenerator;

/**
 * Communicates with the model to get and print the output
 *
 * @author Hubert Przegendza
 * @version 1.1
 */
public class Calculate extends HttpServlet {

    /**
     * Used to create EntityManager.
     */
    @PersistenceUnit
    private EntityManagerFactory emf;

    /**
     * Used to create transactions.
     */
    @Resource
    private UserTransaction utx;

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
        response.setContentType("text/html; charset=ISO-8859-2");
        assert emf != null;
        EntityManager em = null;
        Integer argument;

        try {
            argument = Integer.parseInt(request.getParameter("teamCount"));
        } catch (NumberFormatException ex) {
            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Error!</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Must send a Number!</h1>");
                out.println("</body>");
                out.println("</html>");
                return;
            }
        }

        String result = "";
        List<List<Integer>> outp;
        Result dbResult = null;
        try {
            em = emf.createEntityManager();
            dbResult = (Result) em.createQuery("SELECT r FROM Result r WHERE r.argument LIKE :arg").setParameter("arg", argument.toString()).getSingleResult();
            result = dbResult.getResult();

            /**
             * @TODO: Increase max BTG size
             */
//            if(result.equals("TOO BIG TO STORE (for now)")){ 
//                result = "";
//            }
        } catch (NoResultException ex) {
        } catch (Exception ex) {
            throw new ServletException(ex);
        } finally {
            if (em != null) {
                em.close();
            }
        }

        if (!"".equals(result)) { //Result has been calculated already
            outp = new ArrayList<>();
            String[] rows = result.split("/");
            for (String row : rows) {
                List<Integer> l = new ArrayList<>();
                String test = row.substring(1, row.length());
                String[] pairs = test.split("I");
                for (String pair : pairs) {
                    String[] teams = pair.split("-");
                    l.add(Integer.parseInt(teams[0]));
                    l.add(Integer.parseInt(teams[1]));
                }
                outp.add(l);
            }
        } else {//Result hasn't been calculated yet
            //guarantees one model per session
            BergerTableGenerator btg = (BergerTableGenerator) request.getSession().getAttribute("model");
            if (btg == null) {
                btg = new BergerTableGenerator();
                request.getSession().setAttribute("model", btg);
            }
            try {
                outp = btg.generateBergerTableNew(argument);
            } catch (MyException ex) {
                try (PrintWriter out = response.getWriter()) {
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>Error!</title>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<h1>Wrong number sent! Must be [2-100]!</h1>");
                    out.println("</body>");
                    out.println("</html>");
                    return;
                }
            }
        }

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Calculate</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Generated Berger Table: </h1>");

            boolean dispParity = true;
            for (List<Integer> i : outp) {
                out.println("<h1>|");
                for (int j : i) {
                    out.println(j);
                    if (dispParity) {
                        out.println("-");
                    } else {
                        out.println(" | ");
                    }
                    dispParity = !dispParity;
                }
                out.println("</h1>");
            }

            out.println("</h1>");
            out.println("</body>");
            out.println("</html>");
        }
        //write to database
        try {
            utx.begin();
            em = emf.createEntityManager();
            String userName = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if (c.getName().equals("user")) {
                        userName = c.getValue();
                    }
                }
            }
            Users user;
            if (userName != null) {
                user = (Users) em.createQuery("SELECT u FROM Users u WHERE u.login LIKE :login").setParameter("login", userName).getSingleResult();
            } else {
                try {
                    user = (Users) em.createQuery("SELECT u FROM Users u WHERE u.id LIKE :lid").setParameter("lid", "0").getSingleResult();
                } catch (Exception ex) {
                    user = new Users("0", "Guest", "123");
                    em.persist(user);
                }
            }

            if ("".equals(result)) {
                boolean dispParity = true;
                for (List<Integer> i : outp) {
                    result += "I";
                    for (int j : i) {
                        result += j;
                        if (dispParity) {
                            result += "-";
                        } else {
                            result += "I";
                        }
                        dispParity = !dispParity;
                    }
                    result += "/";
                }
                dbResult = new Result(argument.toString(), result);
                em.persist(dbResult);
            }
            Calculation dbCalc = new Calculation(dbResult, user);
            user.getCalculations().add(dbCalc);
            dbResult.getCalculations().add(dbCalc);

            em.merge(dbResult);
            em.merge(user);
            em.persist(dbCalc);

            utx.commit();
        } catch (Exception ex) {
            throw new ServletException(ex);
        } finally {
            if (em != null) {
                em.close();
            }
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
        return "Generates and displays a berger table.";
    }

}
