package br.edu.ifpi.ads.deacm.domain.command;

import java.math.BigDecimal;

import br.edu.ifpi.ads.deacm.domain.DMU;
import br.edu.ifpi.ads.deacm.domain.Input;
import br.edu.ifpi.ads.deacm.domain.lp.LinearProgrammingModel;
import br.edu.ifpi.ads.deacm.domain.lp.Solution;
import br.edu.ifpi.ads.deacm.domain.lp.solutions.SetDMUEfficiencyMaximizationSolution;

public class CapitalConsumptionDMU implements Command {

	@Override
	public void execute( Solution sol ) {

		if ( sol instanceof SetDMUEfficiencyMaximizationSolution ) {
			SetDMUEfficiencyMaximizationSolution solution;
			try {
				solution = (SetDMUEfficiencyMaximizationSolution) sol;
			} catch ( ClassCastException e ) {
				throw new RuntimeException( "A solução informada não é uma solução SetDMUEfficiencyMaximizationSolution.\n Solução passada: " + sol.toString() );
			}

			for ( DMU target : solution.getTargets() ) {

				BigDecimal capitalBD = new BigDecimal( 0, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
				for ( Input input : target.getInputs() ) {
					BigDecimal offsetDB = new BigDecimal( input.getOffset(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
					capitalBD = capitalBD.add( offsetDB.multiply( new BigDecimal( input.getCost(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT ) ) );
				}

				target.setCapital( capitalBD.doubleValue() );

				for ( DMU dmu : solution.getDmus() ) {
					if ( dmu.getName().equals( target.getName() ) ) {
						dmu.setCapital( target.getCapital() );
						break;
					}
				}
			}
		}

	}

}