package br.edu.ifpi.ads.deacm.domain.command;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ampl.DataFrame;

import br.edu.ifpi.ads.deacm.domain.DMU;
import br.edu.ifpi.ads.deacm.domain.Input;
import br.edu.ifpi.ads.deacm.domain.Output;
import br.edu.ifpi.ads.deacm.domain.lp.LinearProgrammingModel;
import br.edu.ifpi.ads.deacm.domain.lp.Solution;
import br.edu.ifpi.ads.deacm.domain.lp.solutions.SetDMUCapitalMinimizationSolution;
import br.edu.ifpi.ads.deacm.domain.lp.solutions.SetDMUEfficiencyMaximizationSolution;
import br.edu.ifpi.ads.deacm.domain.lp.solutions.SingleDMUCapitalManagementSolution;

public class UpdateXYCommand implements Command {

	@Override
	public void execute( Solution sol ) {

		if ( sol instanceof SingleDMUCapitalManagementSolution ) {
			SingleDMUCapitalManagementSolution singleDMUSolution;
			try {
				singleDMUSolution = (SingleDMUCapitalManagementSolution) sol;
			} catch ( ClassCastException e ) {
				throw new RuntimeException( "A solução informada não é uma solução SingleDMUMinCapitalSolution.\n Solução passada: " + sol.toString() );
			}

			updateXY( singleDMUSolution.getDmu0(), singleDMUSolution.getInputsOffset(), singleDMUSolution.getOutputsOffset() );
		} else if ( sol instanceof SetDMUEfficiencyMaximizationSolution ) {
			SetDMUEfficiencyMaximizationSolution solution;

			try {
				solution = (SetDMUEfficiencyMaximizationSolution) sol;
			} catch ( ClassCastException e ) {
				throw new RuntimeException( "A solução informada não é uma solução SetDMUEfficiencyMaximizationSolution.\n Solução passada: " + sol.toString() );
			}

			for ( DMU dmu0 : solution.getTargets() ) {

				DataFrame inputOffsets = solution.getInputsOffsets();
				DataFrame outputOffsets = solution.getOutputsOffsets();

				List<Double> xOffsets = new ArrayList<>( dmu0.getInputs().size() );
				List<Double> yOffsets = new ArrayList<>( dmu0.getOutputs().size() );

				for ( int i = 0; i < solution.getDmus().size() * dmu0.getInputs().size(); i++ ) {

					Object[] row = inputOffsets.getRowByIndex( i );

					System.out.println( "Row: " + Arrays.deepToString( row ) );

					String dmuName = (String) row[0];
					String inputName = (String) row[1];
					BigDecimal offset = new BigDecimal( (Double) row[2], LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

					if ( dmuName.equals( dmu0.getName() ) ) {
						for ( Input input : dmu0.getInputs() ) {
							if ( input.getName().equals( inputName ) ) {
								xOffsets.add( offset.doubleValue() );
								break;
							}
						}
					}
				}

				for ( int i = 0; i < solution.getDmus().size() * dmu0.getOutputs().size(); i++ ) {

					Object[] row = outputOffsets.getRowByIndex( i );

					System.out.println( "Row: " + Arrays.deepToString( row ) );

					String dmuName = (String) row[0];
					String outputName = (String) row[1];
					BigDecimal offset = new BigDecimal( (Double) row[2], LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

					if ( dmuName.equals( dmu0.getName() ) ) {
						for ( Output output : dmu0.getOutputs() ) {
							if ( output.getName().equals( outputName ) ) {
								yOffsets.add( offset.doubleValue() );
								break;
							}
						}
					}
				}

				updateXY( dmu0, xOffsets, yOffsets );
			}
		} else if ( sol instanceof SetDMUCapitalMinimizationSolution ) {
			SetDMUCapitalMinimizationSolution solution;

			try {
				solution = (SetDMUCapitalMinimizationSolution) sol;
			} catch ( ClassCastException e ) {
				throw new RuntimeException( "A solução informada não é uma solução SetDMUCapitalMinimizationSolution.\n Solução passada: " + sol.toString() );
			}

			for ( DMU dmu0 : solution.getTargets() ) {

				DataFrame inputOffsets = solution.getInputsOffsets();
				DataFrame outputOffsets = solution.getOutputsOffsets();

				Double[] xOffsets = new Double[dmu0.getInputs().size()];
				Double[] yOffsets = new Double[dmu0.getOutputs().size()];

				for ( int i = 0; i < solution.getDmus().size() * dmu0.getInputs().size(); i++ ) {

					Object[] row = inputOffsets.getRowByIndex( i );

					String dmuName = (String) row[0];
					String inputName = (String) row[1];
					BigDecimal offset = new BigDecimal( (Double) row[2], LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

					if ( dmuName.equals( dmu0.getName() ) ) {
						for ( int j = 0; j < dmu0.getInputs().size(); j++ ) {
							Input input = dmu0.getInputs().get( j );

							if ( input.getName().equals( inputName ) ) {
								xOffsets[j] = offset.doubleValue();
								break;
							}
						}
					}
				}

				for ( int i = 0; i < solution.getDmus().size() * dmu0.getOutputs().size(); i++ ) {

					Object[] row = outputOffsets.getRowByIndex( i );

					String dmuName = (String) row[0];
					String outputName = (String) row[1];
					BigDecimal offset = new BigDecimal( (Double) row[2], LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

					if ( dmuName.equals( dmu0.getName() ) ) {
						for ( int j = 0; j < dmu0.getOutputs().size(); j++ ) {
							Output output = dmu0.getOutputs().get( j );

							if ( output.getName().equals( outputName ) ) {
								yOffsets[j] = offset.doubleValue();
								break;
							}
						}
					}
				}

				updateXY( dmu0, Arrays.asList( xOffsets ), Arrays.asList( yOffsets ) );
			}
		}

	}

	private void updateXY( DMU dmu0, List<Double> xOffsets, List<Double> yOffsets ) {

		BigDecimal inputSum = new BigDecimal( 0, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
		BigDecimal outputSum = new BigDecimal( 0, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

		for ( int i = 0; i < dmu0.getInputs().size(); i++ ) {
			Input input = dmu0.getInputs().get( i );
			input.setOffset( xOffsets.get( i ) );
			input.setNewValue( input.getValue() + input.getOffset() );

			BigDecimal newValue = new BigDecimal( input.getNewValue(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
			BigDecimal weight = new BigDecimal( input.getKaoNormalizedWeight(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
			BigDecimal cost = new BigDecimal( input.getCost(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

			inputSum = inputSum.add( newValue.multiply( cost ).multiply( weight ) );
		}

		for ( int i = 0; i < dmu0.getOutputs().size(); i++ ) {
			Output output = dmu0.getOutputs().get( i );
			output.setOffset( yOffsets.get( i ) );
			output.setNewValue( output.getValue() + output.getOffset() );

			BigDecimal newValue = new BigDecimal( output.getNewValue(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
			BigDecimal weight = new BigDecimal( output.getKaoNormalizedWeight(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

			outputSum = outputSum.add( newValue.multiply( weight ) );
		}

		dmu0.setNewVirtualInput( inputSum.doubleValue() );
		dmu0.setNewVirtualOutput( outputSum.doubleValue() );
	}

}