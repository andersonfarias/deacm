package br.edu.ifpi.ads.deacm.domain.command;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import br.edu.ifpi.ads.deacm.domain.DMU;
import br.edu.ifpi.ads.deacm.domain.lp.LinearProgrammingModel;
import br.edu.ifpi.ads.deacm.domain.lp.Solution;
import br.edu.ifpi.ads.deacm.domain.lp.solutions.SingleDMUCapitalManagementSolution;

public class OversizeAndSuperEfficiencyCommandForSingleDMU implements Command {

	private final List<DMU> dmus;

	@Override
	public void execute( Solution sol ) {
		SingleDMUCapitalManagementSolution singleDMUSolution;
		try {
			singleDMUSolution = (SingleDMUCapitalManagementSolution) sol;
		} catch ( ClassCastException e ) {
			throw new RuntimeException( "A solução informada não é uma solução SingleDMUMinCapitalSolution.\n Solução passada: " + sol.toString() );
		}

		DMU dmu0 = singleDMUSolution.getDmu0();

		Double ks = singleDMUSolution.getKs();
		Double ke = singleDMUSolution.getKe();

		BigDecimal kS = null;
		BigDecimal kE = null;

		System.out.println( "DMU0 = '" + dmu0.getName() + "'\tks = '" + ks + "'" + "\tke = '" + ke +"'" );
		
		BigDecimal newRelativeSize = new BigDecimal( dmu0.getNewRelativeSize() != null ? dmu0.getNewRelativeSize() : dmu0.getRelativeSize(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
		System.out.println( "New Relative Size = '" + newRelativeSize.doubleValue() + "'");
		if ( ks != null && ks > 0 ) {
			kS = new BigDecimal( ks, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
			newRelativeSize = kS.multiply( newRelativeSize, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
		}
		dmu0.setOversized( newRelativeSize.doubleValue() > 1 );
		dmu0.setNewRelativeSize( newRelativeSize.doubleValue() );

		BigDecimal newL2Efficiency = new BigDecimal( dmu0.getNewL2Efficiency() != null ? dmu0.getNewL2Efficiency() : dmu0.getL2Efficiency(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
		System.out.println( "New L2 Efficiency = '" + newL2Efficiency.doubleValue() + "'");
		if ( ke != null && ke > 0 ) {
			kE = new BigDecimal( ke, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
			newL2Efficiency = kE.multiply( newL2Efficiency, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
		}
		dmu0.setSuperEfficient( newL2Efficiency.doubleValue() > 1 );
		dmu0.setNewL2Efficiency( newL2Efficiency.doubleValue() );

		System.out.println( "\n\n******* DEBUG *******" );

		System.out.println( "Oversized = " + dmu0.isOversized() + "; Super Efficiency: " + dmu0.isSuperEfficient() );
		if ( dmu0.isOversized() || dmu0.isSuperEfficient() ) {

			BigDecimal a0 = dmu0.isSuperEfficient() ? newL2Efficiency : new BigDecimal( 1, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
			BigDecimal b0 = dmu0.isOversized() ? newRelativeSize : new BigDecimal( 1, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

			System.out.println( "New Relative Size: " + newRelativeSize + "; New L2 Efficiency: " + newL2Efficiency );
			System.out.println( "a0: " + a0.doubleValue() + "; b0: " + b0.doubleValue() );

			ke = 1 / a0.doubleValue();
			ks = 1 / b0.doubleValue();

			kS = new BigDecimal( ks, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
			kE = new BigDecimal( ke, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
			BigDecimal two = new BigDecimal( 2, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

			System.out.println( "ke = " + ke + ";\tks = " + ks );

			for ( DMU dmu : dmus ) {

				boolean isTargetDMU = dmu.getName().equals( dmu0.getName() );

				if ( isTargetDMU ) {
					dmu.setKaoVirtualInput( dmu0.getKaoVirtualInput() );
					dmu.setKaoVirtualOutput( dmu0.getKaoVirtualOutput() );
					dmu.setNewVirtualInput( dmu0.getNewVirtualInput() );
					dmu.setNewVirtualOutput( dmu0.getNewVirtualOutput() );
					dmu.setInputs( dmu0.getInputs() );
					dmu.setOutputs( dmu0.getOutputs() );
				}

				BigDecimal kaoVI = new BigDecimal( isTargetDMU ? dmu.getNewVirtualInput() : dmu.getKaoNormalizedVirtualInput(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
				BigDecimal kaoVO = new BigDecimal( isTargetDMU ? dmu.getNewVirtualOutput() : dmu.getKaoNormalizedVirtualOutput(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

				BigDecimal a = kaoVI.multiply( new BigDecimal( Math.sqrt( kS.divide( kE, LinearProgrammingModel.DEFAULT_MATH_CONTEXT ).doubleValue() ), LinearProgrammingModel.DEFAULT_MATH_CONTEXT ) );
				BigDecimal b = kaoVO.multiply( new BigDecimal( Math.sqrt( kS.multiply( kE ).doubleValue() ), LinearProgrammingModel.DEFAULT_MATH_CONTEXT ) );

				BigDecimal e = b.divide( a, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
				BigDecimal s = ( a.multiply( b ) ).divide( two, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

				if ( !isTargetDMU ) {
					dmu.setKaoVirtualInput( a.doubleValue() );
					dmu.setKaoVirtualOutput( b.doubleValue() );
				}

				dmu.setNewRelativeSize( s.doubleValue() );
				if ( isTargetDMU )
					dmu0.setNewRelativeSize( s.doubleValue() );

				dmu.setNewL2Efficiency( e.doubleValue() );
				if ( isTargetDMU )
					dmu0.setNewL2Efficiency( e.doubleValue() );

				System.out.println( "\nDMU = " + dmu.getName() + "\nisTarget = " + isTargetDMU + ";\tkaoVI = " + kaoVI.doubleValue() + ";\tkaoVO = " + kaoVO.doubleValue() + ";\ta = " + a.doubleValue() + ";\tb = " + b.doubleValue() + ";\te = " + e.doubleValue() + ";\ts = " + s.doubleValue() );
			}
		} else {
			for ( DMU dmu : dmus ) {
				if ( dmu.getName().equals( dmu0.getName() ) ) {
					dmu.setNewL2Efficiency( dmu0.getNewL2Efficiency() );
					dmu.setNewRelativeSize( dmu0.getNewRelativeSize() );
					dmu.setKaoVirtualInput( dmu0.getKaoVirtualInput() );
					dmu.setKaoVirtualOutput( dmu0.getKaoVirtualOutput() );
					dmu.setNewVirtualInput( dmu0.getNewVirtualInput() );
					dmu.setNewVirtualOutput( dmu0.getNewVirtualOutput() );
					dmu.setInputs( dmu0.getInputs() );
					dmu.setOutputs( dmu0.getOutputs() );
				} else {
					dmu.setNewL2Efficiency( dmu.getL2Efficiency() );
					dmu.setNewRelativeSize( dmu.getRelativeSize() );
				}
			}
		}

		dmu0.setNewL2Efficiency( round( 2, dmu0.getNewL2Efficiency() ) );
		dmu0.setNewRelativeSize( round( 7, dmu0.getNewRelativeSize() ) );

		if ( dmu0.getNewL2Efficiency() > 1 )
			dmu0.setNewL2Efficiency( 1d );
		if ( dmu0.getNewRelativeSize() > 1 )
			dmu0.setNewRelativeSize( 1d );

		for ( DMU dmu : dmus ) {

			dmu.setNewL2Efficiency( round( 2, dmu.getNewL2Efficiency() ) );
			dmu.setNewRelativeSize( round( 7, dmu.getNewRelativeSize() ) );

			if ( dmu.getNewL2Efficiency() > 1 )
				dmu.setNewL2Efficiency( 1d );
			if ( dmu.getNewRelativeSize() > 1 )
				dmu.setNewRelativeSize( 1d );
		}

		singleDMUSolution.setDmus( dmus );
	}

	public OversizeAndSuperEfficiencyCommandForSingleDMU( List<DMU> dmus ) {
		super();
		this.dmus = dmus;
	}

	private Double round( int precision, Double value ) {
		return new BigDecimal( value ).setScale( precision, RoundingMode.HALF_UP ).doubleValue();
	}

}