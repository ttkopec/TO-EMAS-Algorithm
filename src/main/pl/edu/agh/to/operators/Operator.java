package pl.edu.agh.to.operators;


/**
 * Created by krzys on 04.12.2016.
 */
public interface Operator {

    boolean checkTypes(Object... args);

    Object execute(Object... args);
}