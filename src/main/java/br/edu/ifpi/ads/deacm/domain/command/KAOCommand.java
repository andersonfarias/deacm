package br.edu.ifpi.ads.deacm.domain.command;

import java.math.BigDecimal;

import br.edu.ifpi.ads.deacm.domain.DMU;
import br.edu.ifpi.ads.deacm.domain.Input;
import br.edu.ifpi.ads.deacm.domain.Output;
import br.edu.ifpi.ads.deacm.domain.lp.LinearProgrammingModel;
import br.edu.ifpi.ads.deacm.domain.lp.Solution;
import br.edu.ifpi.ads.deacm.domain.lp.solutions.KAOSolution;

public class KAOCommand implements Command {

	@Override
	public void execute( Solution s ) {
		KAOSolution kao;
		try {
			kao = (KAOSolution) s;
		} catch ( ClassCastException e ) {
			throw new RuntimeException( "A solução informada não é uma solução KAO.\n Solução passada: " + s.toString() );
		}

		System.out.println( "\n\n" );
		for ( DMU dmu : kao.getDmus() ) {
			BigDecimal inputSum = new BigDecimal( 0, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
			BigDecimal outputSum = new BigDecimal( 0, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

			for ( int i = 0; i < dmu.getOutputs().size(); i++ ) {
				Output output = dmu.getOutputs().get( i );
				BigDecimal weight = new BigDecimal( kao.getU()[i], LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
				output.setKaoWeight( weight.doubleValue() );

				outputSum = outputSum.add( new BigDecimal( output.getValue(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT ).multiply( weight ) );
			}

			for ( int i = 0; i < dmu.getInputs().size(); i++ ) {
				Input input = dmu.getInputs().get( i );

				BigDecimal weight = new BigDecimal( kao.getV()[i], LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
				BigDecimal cost = new BigDecimal( input.getCost(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
				BigDecimal valueBD = new BigDecimal( input.getValue(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

				input.setKaoWeight( weight.doubleValue() );

				inputSum = inputSum.add( valueBD.multiply( cost ).multiply( weight ) );
			}

			dmu.setKaoVirtualInput( inputSum.doubleValue() );
			dmu.setKaoVirtualOutput( outputSum.doubleValue() );
			dmu.setL2Efficiency( outputSum.divide( inputSum, LinearProgrammingModel.DEFAULT_MATH_CONTEXT ).doubleValue() );

			System.out.println( String.format( "DMU: %s, Input Virtual: %f, Output Virtual: %f, L2: %f", dmu.getName(), dmu.getKaoVirtualInput(), dmu.getKaoVirtualOutput(), dmu.getL2Efficiency() ) );
		}

	}

}