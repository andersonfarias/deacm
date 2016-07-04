package br.edu.ifpi.ads.deacm.domain.command;

import java.math.BigDecimal;

import org.apache.commons.math3.stat.descriptive.rank.Max;

import br.edu.ifpi.ads.deacm.domain.DMU;
import br.edu.ifpi.ads.deacm.domain.Input;
import br.edu.ifpi.ads.deacm.domain.Output;
import br.edu.ifpi.ads.deacm.domain.lp.LinearProgrammingModel;
import br.edu.ifpi.ads.deacm.domain.lp.Solution;
import br.edu.ifpi.ads.deacm.domain.lp.solutions.KAOSolution;

public class KAONormalizedWeightsCommand implements Command {

	@Override
	public void execute( Solution s ) {
		KAOSolution kao;
		try {
			kao = (KAOSolution) s;
		} catch ( ClassCastException e ) {
			throw new RuntimeException( "A solução informada não é uma solução KAO.\n Solução passada: " + s.toString() );
		}

		double[] relativeAreas = new double[kao.getDmus().size()];
		double[] deaSuperEfficiencies = new double[kao.getDmus().size()];
		for ( int i = 0; i < deaSuperEfficiencies.length; i++ ) {
			DMU dmu = kao.getDmus().get( i );

			relativeAreas[i] = dmu.getRelativeArea();
			//deaSuperEfficiencies[i] = dmu.getDeaSuperEfficiency();
			//deaSuperEfficiencies[i] = dmu.getnEfficiency();
			deaSuperEfficiencies[i] = dmu.getL2Efficiency();
		}

		BigDecimal maxRelativeArea = new BigDecimal( new Max().evaluate( relativeAreas ), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
		BigDecimal maxDEASuperEfficiency = new BigDecimal( new Max().evaluate( deaSuperEfficiencies ), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

		BigDecimal areaTimesEfficiency = maxRelativeArea.multiply( maxDEASuperEfficiency );
		BigDecimal areaDividedByEfficiency = maxDEASuperEfficiency.divide( maxRelativeArea, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

		BigDecimal sqrtOfProduct = new BigDecimal( Math.sqrt( areaTimesEfficiency.doubleValue() ), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
		BigDecimal sqrtOfDivision = new BigDecimal( Math.sqrt( areaDividedByEfficiency.doubleValue() ), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

		for ( DMU dmu : kao.getDmus() ) {
			BigDecimal inputSum = new BigDecimal( 0, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
			BigDecimal outputSum = new BigDecimal( 0, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

			for ( Input input : dmu.getInputs() ) {
				BigDecimal value = new BigDecimal( input.getValue(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
				BigDecimal kaoWeight = new BigDecimal( input.getKaoWeight(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
				BigDecimal cost = new BigDecimal( input.getCost(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

				BigDecimal kaoNormalizedWeight = kaoWeight.multiply( sqrtOfDivision, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
				input.setKaoNormalizedWeight( kaoNormalizedWeight.doubleValue() );

				inputSum = inputSum.add( value.multiply( cost ).multiply( kaoNormalizedWeight, LinearProgrammingModel.DEFAULT_MATH_CONTEXT ) );
			}

			for ( Output output : dmu.getOutputs() ) {

				BigDecimal kaoWeight = new BigDecimal( output.getKaoWeight(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
				BigDecimal kaoNormalizedWeight = kaoWeight.divide( sqrtOfProduct, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

				output.setKaoNormalizedWeight( kaoNormalizedWeight.doubleValue() );

				outputSum = outputSum.add( new BigDecimal( output.getValue(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT ).multiply( kaoNormalizedWeight ) );
			}

			dmu.setKaoNormalizedVirtualInput( inputSum.doubleValue() );
			dmu.setKaoNormalizedVirtualOutput( outputSum.doubleValue() );
		}
	}

}