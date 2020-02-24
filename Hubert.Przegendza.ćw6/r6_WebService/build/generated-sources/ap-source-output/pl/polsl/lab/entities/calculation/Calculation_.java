package pl.polsl.lab.entities.calculation;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import pl.polsl.lab.entities.result.Result;
import pl.polsl.lab.entities.user.Users;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-01-21T00:48:27")
@StaticMetamodel(Calculation.class)
public class Calculation_ { 

    public static volatile SingularAttribute<Calculation, Result> result;
    public static volatile SingularAttribute<Calculation, String> id;
    public static volatile SingularAttribute<Calculation, Users> user;

}