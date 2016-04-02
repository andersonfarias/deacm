package br.edu.ifpi.ads.deacm.domain.command;

import java.math.BigDecimal;

import org.apache.commons.math3.stat.descriptive.rank.Max;

import br.edu.ifpi.ads.deacm.domain.DMU;
import br.edu.ifpi.ads.deacm.domain.lp.LinearProgrammingModel;
import br.edu.ifpi.ads.deacm.domain.lp.Solution;
import br.edu.ifpi.ads.deacm.domain.lp.solutions.KAOSolution;

public class RelativeSizeCommand implements Command {

	@Override
	public void execute( Solution s ) {
		KAOSolution kao;
		try {
			kao = (KAOSolution) s;
		} catch ( ClassCastException e ) {
			throw new RuntimeException( "A solução informada não é uma solução KAO.\n Solução passada: " + s.toString() );
		}

		double[] relativeAreas = new double[kao.getDmus().size()];
		for ( int i = 0; i < relativeAreas.length; i++ )
			relativeAreas[i] = kao.getDmus().get( i ).getRelativeArea();

		BigDecimal maxRelativeArea = new BigDecimal( new Max().evaluate( relativeAreas ), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

		for ( DMU dmu : kao.getDmus() ) {
			BigDecimal relativeArea = new BigDecimal( dmu.getRelativeArea(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
			dmu.setRelativeSize( relativeArea.divide( maxRelativeArea, LinearProgrammingModel.DEFAULT_MATH_CONTEXT ).doubleValue() );
		}
	}

}