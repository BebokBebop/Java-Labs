package pl.polsl.lab.entities.result;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import pl.polsl.lab.entities.calculation.Calculation;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-01-21T00:48:27")
@StaticMetamodel(Result.class)
public class Result_ { 

    public static volatile SingularAttribute<Result, String> result;
    public static volatile SingularAttribute<Result, String> argument;
    public static volatile ListAttribute<Result, Calculation> calculations;

}