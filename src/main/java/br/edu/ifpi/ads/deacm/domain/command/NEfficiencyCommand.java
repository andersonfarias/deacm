package br.edu.ifpi.ads.deacm.domain.command;

import java.math.BigDecimal;

import org.apache.commons.math3.stat.descriptive.rank.Max;

import br.edu.ifpi.ads.deacm.domain.DMU;
import br.edu.ifpi.ads.deacm.domain.lp.LinearProgrammingModel;
import br.edu.ifpi.ads.deacm.domain.lp.Solution;
import br.edu.ifpi.ads.deacm.domain.lp.solutions.KAOSolution;

public class NEfficiencyCommand implements Command {

	@Override
	public void execute( Solution s ) {
		KAOSolution kao;
		try {
			kao = (KAOSolution) s;
		} catch ( ClassCastException e ) {
			throw new RuntimeException( "A solução informada não é uma solução KAO.\n Solução passada: " + s.toString() );
		}

		double[] l2Efficiencies = new double[kao.getDmus().size()];
		for ( int i = 0; i < l2Efficiencies.length; i++ )
			l2Efficiencies[i] = kao.getDmus().get( i ).getL2Efficiency();

		BigDecimal maxL2Efficiency = new BigDecimal( new Max().evaluate( l2Efficiencies ), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

		for ( DMU dmu : kao.getDmus() ) {
			BigDecimal l2Efficiency = new BigDecimal( dmu.getL2Efficiency(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
			dmu.setnEfficiency( l2Efficiency.divide( maxL2Efficiency, LinearProgrammingModel.DEFAULT_MATH_CONTEXT ).doubleValue() );
		}
	}

}