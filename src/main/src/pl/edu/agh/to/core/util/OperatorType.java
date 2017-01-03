package pl.edu.agh.to.core.util;

import pl.edu.agh.to.operators.CrossOverOperator;
import pl.edu.agh.to.operators.EvaluationOperator;
import pl.edu.agh.to.operators.MutationOperator;
import pl.edu.agh.to.operators.Operator;

public enum OperatorType {
    CrossOver(CrossOverOperator.class),
    Evaluation(EvaluationOperator.class),
    Mutation(MutationOperator.class),
    Selection(MutationOperator.class);


    private final Class<? extends Operator> operatorClass;

    OperatorType(Class<? extends Operator> operatorClass) {
        this.operatorClass = operatorClass;
    }

    public Operator getInstance() throws IllegalAccessException, InstantiationException {
        return operatorClass.newInstance();
    }
}
