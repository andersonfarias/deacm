package br.edu.ifpi.ads.deacm.domain.command;

import java.math.BigDecimal;

import org.apache.commons.math3.optim.PointValuePair;

import br.edu.ifpi.ads.deacm.domain.DMU;
import br.edu.ifpi.ads.deacm.domain.Input;
import br.edu.ifpi.ads.deacm.domain.Output;
import br.edu.ifpi.ads.deacm.domain.lp.LinearProgrammingModel;
import br.edu.ifpi.ads.deacm.domain.lp.Solution;
import br.edu.ifpi.ads.deacm.domain.lp.solutions.DEASolution;

public class DEACommand implements Command {

	@Override
	public void execute( Solution s ) {
		DEASolution dea;
		try {
			dea = (DEASolution) s;
		} catch ( ClassCastException e ) {
			throw new RuntimeException( "A solução informada não é uma solução DEA.\n Solução passada: " + s.toString() );
		}

		PointValuePair lpSolution = dea.getSolution();

		DMU dmu0 = dea.getDmu0();
		dmu0.setDeaEfficiency( new BigDecimal( lpSolution.getValue(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT ).doubleValue() );

		double[] coefficients = lpSolution.getKey();

		BigDecimal inputSum = new BigDecimal( 0, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
		BigDecimal outputSum = new BigDecimal( 0, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

		for ( int i = 0; i < dmu0.getInputs().size(); i++ ) {
			double deaWeight = coefficients[i + dmu0.getOutputs().size()];
			Input input = dmu0.getInputs().get( i );

			input.setDeaWeight( deaWeight );

			inputSum = inputSum.add( new BigDecimal( input.getValue() * deaWeight, LinearProgrammingModel.DEFAULT_MATH_CONTEXT ) );
		}

		for ( int i = 0; i < dmu0.getOutputs().size(); i++ ) {
			double deaWeight = coefficients[i];
			Output output = dmu0.getOutputs().get( i );

			output.setDeaWeight( deaWeight );

			outputSum = outputSum.add( new BigDecimal( output.getValue() * deaWeight, LinearProgrammingModel.DEFAULT_MATH_CONTEXT ) );
		}

		dmu0.setDeaVirtualInput( inputSum.doubleValue() );
		dmu0.setDeaVirtualOutput( outputSum.doubleValue() );
	}

}