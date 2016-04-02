package br.edu.ifpi.ads.deacm.domain.command;

import java.math.BigDecimal;

import org.apache.commons.math3.optim.PointValuePair;

import br.edu.ifpi.ads.deacm.domain.DMU;
import br.edu.ifpi.ads.deacm.domain.lp.LinearProgrammingModel;
import br.edu.ifpi.ads.deacm.domain.lp.Solution;
import br.edu.ifpi.ads.deacm.domain.lp.solutions.DEASolution;

public class SuperEfficiencyDEACommand implements Command {

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
		dmu0.setDeaSuperEfficiency( new BigDecimal( lpSolution.getValue(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT ).doubleValue() );

		for ( DMU dmu : dea.getDmus() ) {
			if ( dmu.getName().equals( dmu0.getName() ) ) {
				dmu.setDeaSuperEfficiency( new BigDecimal( lpSolution.getValue(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT ).doubleValue() );
				break;
			}
		}
	}

}