package pl.polsl.lab.entities.user;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import pl.polsl.lab.entities.calculation.Calculation;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-01-21T00:48:27")
@StaticMetamodel(Users.class)
public class Users_ { 

    public static volatile SingularAttribute<Users, String> password;
    public static volatile SingularAttribute<Users, String> id;
    public static volatile ListAttribute<Users, Calculation> calculations;
    public static volatile SingularAttribute<Users, String> login;

}