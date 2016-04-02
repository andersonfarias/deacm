package br.edu.ifpi.ads.deacm.domain.command;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.edu.ifpi.ads.deacm.domain.DMU;
import br.edu.ifpi.ads.deacm.domain.lp.LinearProgrammingModel;
import br.edu.ifpi.ads.deacm.domain.lp.Solution;
import br.edu.ifpi.ads.deacm.domain.lp.solutions.SetDMUCapitalMinimizationSolution;
import br.edu.ifpi.ads.deacm.domain.lp.solutions.SetDMUEfficiencyMaximizationSolution;

public class OversizeAndSuperEfficiencyCommandForSetOfDMU implements Command {

	@Override
	public void execute( Solution sol ) {

		if ( sol instanceof SetDMUEfficiencyMaximizationSolution ) {
			SetDMUEfficiencyMaximizationSolution solution;
			try {
				solution = (SetDMUEfficiencyMaximizationSolution) sol;
			} catch ( ClassCastException e ) {
				throw new RuntimeException( "A solução informada não é uma solução SetDMUEfficiencyMaximizationSolution.\n Solução passada: " + sol.toString() );
			}

			algorithm2( solution.getTargets(), solution.getDmus() );
		} else if ( sol instanceof SetDMUCapitalMinimizationSolution ) {
			SetDMUCapitalMinimizationSolution solution;
			try {
				solution = (SetDMUCapitalMinimizationSolution) sol;
			} catch ( ClassCastException e ) {
				throw new RuntimeException( "A solução informada não é uma solução SetDMUCapitalMinimizationSolution.\n Solução passada: " + sol.toString() );
			}

			algorithm2( solution.getTargets(), solution.getDmus() );
		}

	}

	private void algorithm2( List<DMU> targets, List<DMU> dmus ) {
		boolean someOversized = false;
		boolean someSuperEfficienced = false;

		List<Double> efficiencies = new ArrayList<>();
		List<Double> relativeSizes = new ArrayList<>();

		System.out.println( "******* Targets" );
		for ( DMU dmu0 : targets ) {

			BigDecimal a = new BigDecimal( dmu0.getNewVirtualInput(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
			BigDecimal b = new BigDecimal( dmu0.getNewVirtualOutput(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

			BigDecimal size = a.multiply( b ).divide( new BigDecimal( 2 ), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

			dmu0.setNewRelativeSize( size.doubleValue() );
			dmu0.setOversized( dmu0.getNewRelativeSize() > 1 );

			dmu0.setNewL2Efficiency( b.divide( a, LinearProgrammingModel.DEFAULT_MATH_CONTEXT ).doubleValue() );
			dmu0.setSuperEfficient( dmu0.getNewL2Efficiency() > 1 );

			someOversized |= dmu0.isOversized();
			someSuperEfficienced |= dmu0.isSuperEfficient();

			relativeSizes.add( dmu0.getNewRelativeSize() );
			efficiencies.add( dmu0.getNewL2Efficiency() );
		}

		relativeSizes.add( 1d );
		efficiencies.add( 1d );

		BigDecimal a0 = BigDecimal.ZERO;
		BigDecimal b0 = BigDecimal.ZERO;

		for ( int i = 0; i < targets.size(); i++ ) {
			if ( efficiencies.get( i ) > a0.doubleValue() )
				a0 = new BigDecimal( efficiencies.get( i ), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
			if ( relativeSizes.get( i ) > b0.doubleValue() )
				b0 = new BigDecimal( relativeSizes.get( i ), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
		}

		System.out.println( "\n\n******* DEBUG *******" );
		System.out.println( "Efficiencies: " + Arrays.deepToString( efficiencies.toArray() ) );
		System.out.println( "Relative Sizes: " + Arrays.deepToString( relativeSizes.toArray() ) );
		System.out.println( "Oversized = " + someOversized + "; Super Efficiency: " + someSuperEfficienced );
		System.out.println( "a0: " + a0.doubleValue() + "; b0: " + b0.doubleValue() );

		if ( someSuperEfficienced || someOversized ) {

			if ( a0.doubleValue() < 1 )
				a0 = new BigDecimal( 1, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

			if ( b0.doubleValue() < 1 )
				b0 = new BigDecimal( 1, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

			double ke = 1 / a0.doubleValue();
			double ks = 1 / b0.doubleValue();

			BigDecimal kS = new BigDecimal( ks, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
			BigDecimal kE = new BigDecimal( ke, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
			BigDecimal two = new BigDecimal( 2, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

			System.out.println( "ke = " + ke + ";\tks = " + ks );

			for ( DMU dmu : dmus ) {

				boolean isTargetDMU = false;
				DMU targetDMU = null;

				for ( DMU target : targets ) {
					if ( target.getName().equals( dmu.getName() ) ) {
						targetDMU = target;
						isTargetDMU = true;
						break;
					}
				}

				if ( isTargetDMU ) {
					dmu.setKaoVirtualInput( targetDMU.getKaoVirtualInput() );
					dmu.setKaoVirtualOutput( targetDMU.getKaoVirtualOutput() );
					dmu.setNewVirtualInput( targetDMU.getNewVirtualInput() );
					dmu.setNewVirtualOutput( targetDMU.getNewVirtualOutput() );
					dmu.setInputs( targetDMU.getInputs() );
					dmu.setOutputs( targetDMU.getOutputs() );
				}

				BigDecimal kaoVI = new BigDecimal( isTargetDMU ? dmu.getNewVirtualInput() : dmu.getKaoNormalizedVirtualInput(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
				BigDecimal kaoVO = new BigDecimal( isTargetDMU ? dmu.getNewVirtualOutput() : dmu.getKaoNormalizedVirtualOutput(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

				BigDecimal a = kaoVI.multiply( new BigDecimal( Math.sqrt( kS.divide( kE, LinearProgrammingModel.DEFAULT_MATH_CONTEXT ).doubleValue() ), LinearProgrammingModel.DEFAULT_MATH_CONTEXT ) );
				BigDecimal b = kaoVO.multiply( new BigDecimal( Math.sqrt( kS.multiply( kE ).doubleValue() ), LinearProgrammingModel.DEFAULT_MATH_CONTEXT ) );

				BigDecimal e = b.divide( a, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

				double size = ( a.multiply( b ) ).divide( two, LinearProgrammingModel.DEFAULT_MATH_CONTEXT ).doubleValue();

				if ( !isTargetDMU ) {
					dmu.setKaoVirtualInput( a.doubleValue() );
					dmu.setKaoVirtualOutput( b.doubleValue() );
				}

				dmu.setNewL2Efficiency( e.doubleValue() );
				if ( isTargetDMU )
					targetDMU.setNewL2Efficiency( e.doubleValue() );

				dmu.setNewRelativeSize( size );
				if ( isTargetDMU )
					targetDMU.setNewRelativeSize( size );

				System.out.println( "\nDMU = " + dmu.getName() + "\nisTarget = " + isTargetDMU + ";\tkaoVI = " + kaoVI.doubleValue() + ";\tkaoVO = " + kaoVO.doubleValue() + ";\ta = " + a.doubleValue() + ";\tb = " + b.doubleValue() + ";\te = " + e.doubleValue() + ";" );
			}

		} else {
			for ( DMU dmu : dmus ) {

				boolean isTargetDMU = false;
				for ( DMU dmu0 : targets ) {
					if ( dmu.getName().equals( dmu0.getName() ) ) {
						dmu.setNewL2Efficiency( dmu0.getNewL2Efficiency() );
						dmu.setNewRelativeSize( dmu0.getNewRelativeSize() );
						dmu.setKaoVirtualInput( dmu0.getKaoVirtualInput() );
						dmu.setKaoVirtualOutput( dmu0.getKaoVirtualOutput() );
						dmu.setNewVirtualInput( dmu0.getNewVirtualInput() );
						dmu.setNewVirtualOutput( dmu0.getNewVirtualOutput() );
						dmu.setInputs( dmu0.getInputs() );
						dmu.setOutputs( dmu0.getOutputs() );

						isTargetDMU = true;
						break;
					}
				}

				if ( !isTargetDMU ) {
					dmu.setNewL2Efficiency( dmu.getL2Efficiency() );
					dmu.setNewRelativeSize( dmu.getRelativeSize() );
				}

			}
		}

		for ( DMU dmu0 : targets ) {

			dmu0.setNewL2Efficiency( round( 2, dmu0.getNewL2Efficiency() ) );
			dmu0.setNewRelativeSize( round( 7, dmu0.getNewRelativeSize() ) );

			if ( dmu0.getNewL2Efficiency() > 1 )
				dmu0.setNewL2Efficiency( 1d );
			if ( dmu0.getNewRelativeSize() > 1 )
				dmu0.setNewRelativeSize( 1d );
		}

		for ( DMU dmu : dmus ) {

			dmu.setNewL2Efficiency( round( 2, dmu.getNewL2Efficiency() ) );
			dmu.setNewRelativeSize( round( 7, dmu.getNewRelativeSize() ) );

			if ( dmu.getNewL2Efficiency() > 1 )
				dmu.setNewL2Efficiency( 1d );
			if ( dmu.getNewRelativeSize() > 1 )
				dmu.setNewRelativeSize( 1d );
		}
	}

	private Double round( int precision, Double value ) {
		return new BigDecimal( value ).setScale( precision, RoundingMode.HALF_UP ).doubleValue();
	}

}