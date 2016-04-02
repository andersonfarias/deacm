package br.edu.ifpi.ads.deacm.domain.lp.single;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

import com.ampl.AMPL;
import com.ampl.DataFrame;
import com.ampl.Objective;
import com.ampl.Parameter;
import com.ampl.Set;
import com.ampl.Variable;

import br.edu.ifpi.ads.deacm.domain.DMU;
import br.edu.ifpi.ads.deacm.domain.Input;
import br.edu.ifpi.ads.deacm.domain.Output;
import br.edu.ifpi.ads.deacm.domain.lp.LinearProgrammingModel;
import br.edu.ifpi.ads.deacm.domain.lp.Solution;
import br.edu.ifpi.ads.deacm.domain.lp.solutions.SingleDMUCapitalManagementSolution;
import br.edu.ifpi.ads.deacm.domain.util.RuntimeExceptionOutputHandler;

public class MinimizeCapital implements LinearProgrammingModel {

	public static final String SINGLE_DMU_MINIMIZE_MODEL_FILE = "single_dmu-minimize_capital.mod";

	private String modelFilePath;

	private double ks;

	private double ke;

	private DMU dmu0;

	@Override
	public Solution solve() {
		AMPL ampl = new AMPL();

		try {

			ampl.read( modelFilePath );

			int inputsLength = dmu0.getInputs().size();
			int outputsLength = dmu0.getOutputs().size();

			Object[] inputNames = new Object[inputsLength];
			Object[] inputMatrix = new Object[inputsLength];
			Object[] inputCostMatrix = new Object[inputsLength];
			Object[] inputBoundsMatrix = new Object[inputsLength];
			Object[] normalizedInputWeights = new Object[inputsLength];

			Object[] outputNames = new Object[outputsLength];
			Object[] outputMatrix = new Object[outputsLength];
			Object[] outputBoundsMatrix = new Object[outputsLength];
			Object[] normalizedOutputWeights = new Object[outputsLength];

			for ( int i = 0; i < inputsLength; i++ ) {
				Input input = dmu0.getInputs().get( i );

				inputNames[i] = input.getName();
				inputMatrix[i] = input.getValue();
				inputCostMatrix[i] = input.getCost();
				inputBoundsMatrix[i] = input.getBound();
				normalizedInputWeights[i] = input.getKaoNormalizedWeight();
			}

			for ( int i = 0; i < outputsLength; i++ ) {
				Output output = dmu0.getOutputs().get( i );

				outputNames[i] = output.getName();
				outputMatrix[i] = output.getValue();
				outputBoundsMatrix[i] = output.getBound();
				normalizedOutputWeights[i] = output.getKaoNormalizedWeight();
			}

			Set inputSet = ampl.getSet( "input" );
			inputSet.setValues( inputNames );

			Set outputSet = ampl.getSet( "output" );
			outputSet.setValues( outputNames );

			Parameter x = ampl.getParameter( "X" );
			x.setValues( inputMatrix );

			Parameter y = ampl.getParameter( "Y" );
			y.setValues( outputMatrix );

			Parameter a = ampl.getParameter( "A" );
			a.setValues( inputCostMatrix );

			Parameter d = ampl.getParameter( "D" );
			d.setValues( inputBoundsMatrix );

			Parameter f = ampl.getParameter( "F" );
			f.setValues( outputBoundsMatrix );

			Parameter u = ampl.getParameter( "u" );
			u.setValues( normalizedOutputWeights );

			Parameter v = ampl.getParameter( "v" );
			v.setValues( normalizedInputWeights );

			Parameter e0 = ampl.getParameter( "e0" );

			BigDecimal eBD = new BigDecimal( dmu0.getL2Efficiency(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
			eBD = eBD.setScale( 2, RoundingMode.HALF_UP );

			e0.set( eBD.doubleValue() );

			Parameter s0 = ampl.getParameter( "s0" );
			s0.set( dmu0.getRelativeSize() );

			Parameter t = ampl.getParameter( "t" );
			t.set( ks );

			Parameter w = ampl.getParameter( "w" );
			w.set( ke );

			ampl.setOutputHandler( new RuntimeExceptionOutputHandler() );
			ampl.solve();

			System.out.println( "\n\n --- RESULTS --- \n\n" );

			for ( Set resultSet : ampl.getSets() ) {
				System.out.println( resultSet.name() );
				System.out.println( resultSet.get() );
			}

			for ( Parameter resultParameter : ampl.getParameters() ) {
				System.out.println( resultParameter.name() );
				System.out.println( resultParameter.getValues() );
			}

			for ( Variable resultParameter : ampl.getVariables() ) {
				System.out.println( resultParameter.name() );
				System.out.println( resultParameter.getValues() );
			}

			Objective z = ampl.getObjective( "z" );
			double minCapital = new BigDecimal( z.value(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT ).doubleValue();

			System.out.println( z.name() );
			System.out.println( minCapital );

			Double[] inputsOffset = new Double[inputsLength];
			Double[] outputsOffset = new Double[outputsLength];

			DataFrame inputsOffsets = ampl.getVariable( "d" ).getValues();
			DataFrame outputsOffsets = ampl.getVariable( "f" ).getValues();

			for ( int i = 0; i < inputsLength; i++ ) {
				Object[] row = inputsOffsets.getRowByIndex( i );

				String name = (String) row[0];
				BigDecimal offset = new BigDecimal( (Double) row[1], LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

				int index = 0;

				for ( int j = 0; j < inputNames.length; j++ ) {
					if ( name.equals( inputNames[j] ) ) {
						index = j;
						break;
					}
				}

				inputsOffset[index] = offset.doubleValue();
			}

			for ( int i = 0; i < outputsLength; i++ ) {
				Object[] row = outputsOffsets.getRowByIndex( i );

				String name = (String) row[0];
				BigDecimal offset = new BigDecimal( (Double) row[1], LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

				int index = 0;
				for ( int j = 0; j < outputNames.length; j++ ) {
					if ( name.equals( outputNames[j] ) ) {
						index = j;
						break;
					}
				}

				outputsOffset[index] = offset.doubleValue();
			}

			System.out.println( "Input Offsets: " + Arrays.deepToString( inputsOffset ) );
			System.out.println( "Output Offsets: " + Arrays.deepToString( outputsOffset ) );

			return new SingleDMUCapitalManagementSolution( dmu0, minCapital, ks, ke, Arrays.asList( inputsOffset ), Arrays.asList( outputsOffset ) );
		} catch ( Exception e ) {
			throw new RuntimeException( e.getMessage() );
		} finally {
			ampl.close();
		}
	}

	public MinimizeCapital( String modelFilePath, double ks, double ke, DMU dmu0 ) {
		super();
		this.modelFilePath = modelFilePath;
		this.ks = ks;
		this.ke = ke;
		this.dmu0 = dmu0;
	}

}