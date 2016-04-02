package br.edu.ifpi.ads.deacm.domain.command;

import java.math.BigDecimal;

import br.edu.ifpi.ads.deacm.domain.DMU;
import br.edu.ifpi.ads.deacm.domain.lp.LinearProgrammingModel;
import br.edu.ifpi.ads.deacm.domain.lp.Solution;
import br.edu.ifpi.ads.deacm.domain.lp.solutions.KAOSolution;

public class RelativeAreaCommand implements Command {

	@Override
	public void execute( Solution s ) {
		KAOSolution kao;
		try {
			kao = (KAOSolution) s;
		} catch ( ClassCastException e ) {
			throw new RuntimeException( "A solução informada não é uma solução KAO.\n Solução passada: " + s.toString() );
		}

		BigDecimal two = new BigDecimal( 2, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

		for ( DMU dmu : kao.getDmus() ) {
			BigDecimal kaoVirtualInput = new BigDecimal( dmu.getKaoVirtualInput(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
			BigDecimal kaoVirtualOutput = new BigDecimal( dmu.getKaoVirtualOutput(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
			dmu.setRelativeArea( kaoVirtualInput.multiply( kaoVirtualOutput ).divide( two, LinearProgrammingModel.DEFAULT_MATH_CONTEXT ).doubleValue() );
		}
	}

}