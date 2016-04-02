package br.edu.ifpi.ads.deacm.domain.lp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.NonNegativeConstraint;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

import br.edu.ifpi.ads.deacm.domain.DMU;
import br.edu.ifpi.ads.deacm.domain.Input;
import br.edu.ifpi.ads.deacm.domain.Output;
import br.edu.ifpi.ads.deacm.domain.lp.solutions.DEASolution;

public class DEA implements LinearProgrammingModel {

	private DMU dmu0;

	private List<DMU> dmus;

	@Override
	public Solution solve() {

		double[] coefficients = new double[dmu0.getOutputs().size() + dmu0.getInputs().size()];
		for ( int i = 0; i < dmu0.getOutputs().size(); i++ ) {
			coefficients[i] = dmu0.getOutputs().get( i ).getValue();
		}
		for ( int i = 0; i < dmu0.getInputs().size(); i++ ) {
			coefficients[i + dmu0.getOutputs().size()] = 0;
		}

		LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction( coefficients, 0 );

		List<LinearConstraint> constraints = new ArrayList<>();

		for ( DMU dmu : dmus ) {

			List<Input> inputs = dmu.getInputs();
			List<Output> outputs = dmu.getOutputs();

			coefficients = new double[inputs.size() + outputs.size()];
			int i = 0;
			for ( ; i < outputs.size(); i++ ) {
				coefficients[i] = outputs.get( i ).getValue();
			}

			for ( ; i < inputs.size() + outputs.size(); i++ ) {
				Input input = inputs.get( i - outputs.size() );
				coefficients[i] = ( input.getValue() * input.getCost() ) * -1;
			}

			constraints.add( new LinearConstraint( coefficients, Relationship.LEQ, 0 ) );
		}

		coefficients = new double[dmu0.getInputs().size() + dmu0.getOutputs().size()];
		for ( int i = 0; i < dmu0.getOutputs().size(); i++ )
			coefficients[i] = 0;
		for ( int i = 0; i < dmu0.getInputs().size(); i++ ) {
			Input input = dmu0.getInputs().get( i );
			coefficients[i + dmu0.getOutputs().size()] = input.getValue() * input.getCost();
		}

		constraints.add( new LinearConstraint( coefficients, Relationship.EQ, 1 ) );

		SimplexSolver solver = new SimplexSolver();
		PointValuePair solution = solver.optimize( new MaxIter( 10000 ), objectiveFunction, new LinearConstraintSet( constraints ), GoalType.MAXIMIZE, new NonNegativeConstraint( true ) );

		return new DEASolution( dmu0, dmus, solution );
	}

	public DEA( DMU dmu0, List<DMU> dmus ) {
		super();
		this.dmu0 = dmu0;
		this.dmus = dmus;
	}

}