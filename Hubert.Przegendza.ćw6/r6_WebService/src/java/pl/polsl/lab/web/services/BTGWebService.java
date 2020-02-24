package pl.polsl.lab.web.services;

import pl.polsl.lab.exceptions.WSException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceUnit;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import pl.polsl.lab.entities.calculation.Calculation;
import pl.polsl.lab.entities.result.Result;
import pl.polsl.lab.entities.user.Users;
import pl.polsl.lab.exceptions.MyException;
import pl.polsl.lab.model.BergerTableGenerator;
import pl.polsl.lab.send.types.BTResultRow;
import pl.polsl.lab.send.types.CalculationList;
import pl.polsl.lab.send.types.History;
import pl.polsl.lab.send.types.ResultList;
import pl.polsl.lab.send.types.UserList;

/**
 * WebServices managing database and BTG model
 *
 * @author Hubert Przegendza
 * @version 1.0
 */
@WebService(serviceName = "BTGWebService")
public class BTGWebService {

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

    BergerTableGenerator btg;

    /**
     * Get a list of all the calculations done so far by all users.
     *
     * @return List of all calculations - id, argument, userID
     * @throws pl.polsl.lab.exceptions.WSException when a database error occurs
     */
    @WebMethod(operationName = "getCalculationList")
    public List<CalculationList> getCalculationList() throws WSException {
        assert emf != null;  //Make sure injection went through correctly.
        EntityManager em = null;
        List<Calculation> calcs = null;
        try {
            em = emf.createEntityManager();

            calcs = em.createQuery("SELECT c FROM Calculation c").getResultList();
        } catch (Exception ex) {
            throw new WSException(ex);
        } finally {
            if (em != null) {
                em.close();
            }
        }

        List<CalculationList> retList = new ArrayList<>();
        for (Calculation c : calcs) {
            retList.add(new CalculationList(c.getId(), c.getArgument(), c.getUser().getLogin()));
        }

        return retList;
    }

    /**
     * Calculate a berger table for a given amount of teams.
     *
     * @param argument argument to calculate BT for
     * @param userName name of the user asking for BT
     * @return berger table in form of a list of rows
     * @throws pl.polsl.lab.exceptions.WSException when database error occurs
     * @throws pl.polsl.lab.exceptions.MyException when a wrong argument is
     * passed
     */
    @WebMethod(operationName = "CalculateBergerTable")
    public List<BTResultRow> CalculateBergerTable(@WebParam(name = "teamCount") final Integer argument,
            @WebParam(name = "userName") final String userName) throws WSException, MyException {
        assert emf != null;
        EntityManager em = null;

        String result = "";
        List<List<Integer>> outp;
        Result dbResult = null;
        try {
            em = emf.createEntityManager();
            dbResult = (Result) em.createQuery("SELECT r FROM Result r WHERE r.argument LIKE :arg").setParameter("arg", argument.toString()).getSingleResult();
            result = dbResult.getResult();
        } catch (NoResultException ex) { //ignore on purpose
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
            if (btg == null) {
                btg = new BergerTableGenerator();
            }
            outp = btg.generateBergerTableNew(argument);
        }

        try {
            utx.begin();
            em = emf.createEntityManager();
            Users user = null;
            if (userName != null) {
                user = (Users) em.createQuery("SELECT u FROM Users u WHERE u.login LIKE :login")
                        .setParameter("login", userName).getSingleResult();
            } else {
                try {
                    user = (Users) em.createQuery("SELECT u FROM Users u WHERE u.id LIKE :lid")
                            .setParameter("lid", "0").getSingleResult();
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
            throw new WSException(ex); //database exception
        } finally {
            if (em != null) {
                em.close();
            }
        }
        List<BTResultRow> returnList = new ArrayList<>();
        for (List<Integer> r : outp) {
            returnList.add(new BTResultRow(r));
        }
        return returnList;
    }

    /**
     * Get history of all used argments by a given user.
     *
     * @param userName of user to get history for
     * @throws pl.polsl.lab.exceptions.MyException when username is null
     * @throws pl.polsl.lab.exceptions.WSException when database error occurs
     *
     * @return list of all the arguments used by a given user
     */
    @WebMethod(operationName = "getHistoryList")
    public List<History> getHistoryList(@WebParam(name = "userName") final String userName) throws MyException, WSException {
        assert emf != null;
        EntityManager em = null;

        if (userName == null) {
            throw new MyException();
        }
        Users user = null;
        try {
            em = emf.createEntityManager();
            user = (Users) em.createQuery("SELECT u FROM Users u where u.login LIKE :login").setParameter("login", userName).getSingleResult();
        } catch (Exception ex) {
            throw new WSException(ex);
        }
        List<Calculation> calcs = user.getCalculations();
        List<History> retHist = new ArrayList<>();
        if (calcs != null) {
            for (Calculation c : calcs) {
                retHist.add(new History(c.getArgument().toString()));
            }
        } else {
            return null;
        }

        return retHist;
    }

    /**
     * Get list of all already calculated results.
     *
     * @return List of Results - argument, #ofCalculatons, result in string form
     * @throws pl.polsl.lab.exceptions.WSException when database error occurs
     *
     */
    @WebMethod(operationName = "getResultList")
    public List<ResultList> getResultList() throws WSException {
        assert emf != null;
        EntityManager em = null;

        List<Result> results = null;
        try {
            em = emf.createEntityManager();
            results = em.createQuery("SELECT r FROM Result r").getResultList();
        } catch (Exception ex) {
            throw new WSException(ex); //db Error
        } finally {
            if (em != null) {
                em.close();
            }
        }
        List<ResultList> retResultList = null;
        if (results != null) {
            retResultList = new ArrayList<>();
            for (Result r : results) {
                retResultList.add(new ResultList(r.getArgument(), r.getCalculations().size(), r.getResult()));
            }
        }

        return retResultList;
    }

    /**
     * Get a list of all the users.
     *
     * @param userName has to be admin, otherwise no permission
     * @return list of all the users - ID, login, password, #ofCalculations
     * @throws pl.polsl.lab.exceptions.WSException when database error occurs
     * @throws pl.polsl.lab.exceptions.MyException when username is not "admin"
     *
     */
    @WebMethod(operationName = "getUserList")
    public List<UserList> getUserList(@WebParam(name = "userName")
            final String userName) throws WSException, MyException {
        assert emf != null;
        EntityManager em = null;

        if (userName == null) {
            throw new MyException(); //must be admin
        }
        if (!userName.equals("admin")) {
            throw new MyException(); //must be admin
        }

        List<Users> users = null;
        try {
            em = emf.createEntityManager();

            users = em.createQuery("SELECT u FROM Users u").getResultList();
        } catch (Exception ex) {
            throw new WSException(ex);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        List<UserList> retUserList = null;
        if (users != null) {
            retUserList = new ArrayList<>();
            for (Users u : users) {
                retUserList.add(new UserList(u.getID(), u.getLogin(), u.getPassword(), u.getHistory().size()));
            }
        }
        return retUserList;
    }

    /**
     * Check if login and password are correct.
     *
     * @param userName userName to be checked
     * @param password password to be checked
     * @return error message
     * @throws pl.polsl.lab.exceptions.WSException when database error occurs
     *
     */
    @WebMethod(operationName = "checkLogin")
    public String checkLogin(@WebParam(name = "login")
            final String userName,
            @WebParam(name = "password")
            final String password)
            throws WSException {
        assert emf != null;  //Make sure injection went through correctly.
        EntityManager em = null;

        String errorMessage = null;
        List users = null;
        try {
            em = emf.createEntityManager();

            users = em.createQuery("SELECT u FROM Users u WHERE u.login LIKE :login AND u.password LIKE :passwd")
                    .setParameter("login", userName).setParameter("passwd", password).getResultList();
        } catch (Exception ex) {
            throw new WSException(ex);
        }

        if (users.size() != 1) {
            if (userName.equals("admin")) {
                errorMessage = "admin account must be registered first. Ask server provider for the password.";
            } else {
                errorMessage = "Wrong name or password! Try again.";
            }
        }
        return errorMessage;
    }

    /**
     * Register user in the DB.
     *
     * @param login login to be registered
     * @param password to the account
     * @throws pl.polsl.lab.exceptions.WSException when database error occurs
     * @throws pl.polsl.lab.exceptions.MyException when name is already in use
     *
     */
    @WebMethod(operationName = "register")
    public void register(@WebParam(name = "login") String login,
            @WebParam(name = "password") String password) throws WSException, MyException {
        assert emf != null;  //Make sure injection went through correctly.
        EntityManager em = null;
        try {
            Users user = new Users(login, password);

            utx.begin();
            em = emf.createEntityManager();

            List users = em.createQuery("SELECT u FROM Users u where u.login LIKE :login").setParameter("login", login).getResultList();
            if (!users.isEmpty()) {
                throw new MyException(); //Name already in use
            } else {
                em.persist(user);
            }
            utx.commit();
        } catch (IllegalStateException | SecurityException | HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException ex) {
            throw new WSException(ex); //db error
        } finally {
            if (em != null) {
                em.close();
            }
        }

    }

    /**
     * Remove a registered user from the database.
     *
     * @param login name of the user to be removed
     * @throws pl.polsl.lab.exceptions.WSException when database error occurs
     *
     */
    @WebMethod(operationName = "unRegister")
    public void unRegister(@WebParam(name = "login") String login) throws WSException {
        assert emf != null;  //Make sure injection went through correctly.
        EntityManager em = null;
        try {
            utx.begin();
            em = emf.createEntityManager();
            Users user = (Users) em.createQuery("select u from Users u where u.login like :name")
                    .setParameter("name", login)
                    .getSingleResult();
            for (Calculation c : user.getCalculations()) {
                c.setUser(null);
                em.merge(c);
            }
            em.remove(user);
            utx.commit();
        } catch (Exception ex) { //db error
            throw new WSException(ex);
        } finally {
            if (em != null) {
                em.close();
            }
        }

    }

}
