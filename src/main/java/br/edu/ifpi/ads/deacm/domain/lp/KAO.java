package br.edu.ifpi.ads.deacm.domain.lp;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import com.ampl.AMPL;
import com.ampl.Objective;
import com.ampl.Parameter;
import com.ampl.Set;
import com.ampl.Variable;

import br.edu.ifpi.ads.deacm.domain.DMU;
import br.edu.ifpi.ads.deacm.domain.Input;
import br.edu.ifpi.ads.deacm.domain.Output;
import br.edu.ifpi.ads.deacm.domain.lp.solutions.KAOSolution;
import br.edu.ifpi.ads.deacm.domain.util.RuntimeExceptionOutputHandler;

public class KAO implements LinearProgrammingModel {

	public static final String KAO_MODEL_FILE = "kao.mod";

	private List<DMU> dmus;

	private String kaoFilePath;

	private static final int P = 2;

	@Override
	public Solution solve() {
		AMPL ampl = new AMPL();

		try {

			ampl.read( kaoFilePath );

			Parameter p = ampl.getParameter( "p" );
			p.set( P );

			DMU firstDMU = dmus.get( 0 );

			Object[] dmusNames = new Object[dmus.size()];
			Object[] inputNames = new Object[firstDMU.getInputs().size()];
			Object[] outputNames = new Object[firstDMU.getOutputs().size()];

			Set dmuSet = ampl.getSet( "dmu" );
			Set inputSet = ampl.getSet( "input" );
			Set outputSet = ampl.getSet( "output" );

			for ( int i = 0; i < firstDMU.getInputs().size(); i++ )
				inputNames[i] = firstDMU.getInputs().get( i ).getName();

			for ( int i = 0; i < firstDMU.getOutputs().size(); i++ )
				outputNames[i] = firstDMU.getOutputs().get( i ).getName();

			Object[] inputMatrix = new Object[dmus.size() * firstDMU.getInputs().size()];
			Object[] inputCostMatrix = new Object[dmus.size() * firstDMU.getInputs().size()];
			Object[] outputMatrix = new Object[dmus.size() * firstDMU.getOutputs().size()];
			Object[] efficiencyMatrix = new Double[dmus.size()];

			for ( int i = 0; i < dmus.size(); i++ ) {
				DMU dmu = dmus.get( i );

				dmusNames[i] = dmu.getName();

				for ( int j = 0; j < dmu.getInputs().size(); j++ ) {
					int index = i * ( dmu.getInputs().size() ) + j;

					Input input = dmu.getInputs().get( j );

					inputMatrix[index] = input.getValue();
					inputCostMatrix[index] = input.getCost();
				}

				for ( int j = 0; j < dmu.getOutputs().size(); j++ ) {
					Output output = dmu.getOutputs().get( j );
					outputMatrix[i * ( dmu.getOutputs().size() ) + j] = output.getValue();
				}

				BigDecimal eBD = new BigDecimal( dmu.getDeaSuperEfficiency(), LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
				eBD = eBD.setScale( 2, RoundingMode.HALF_UP );

				efficiencyMatrix[i] = eBD.doubleValue();
			}

			dmuSet.setValues( dmusNames );
			inputSet.setValues( inputNames );
			outputSet.setValues( outputNames );

			Parameter x = ampl.getParameter( "X" );
			x.setValues( inputMatrix );

			Parameter a = ampl.getParameter( "A" );
			a.setValues( inputCostMatrix );

			Parameter y = ampl.getParameter( "Y" );
			y.setValues( outputMatrix );

			Parameter e = ampl.getParameter( "E" );
			e.setValues( efficiencyMatrix );

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

			Variable u = ampl.getVariable( "u" );
			Variable v = ampl.getVariable( "v" );

			Double[] vValues = new Double[firstDMU.getInputs().size()];
			Double[] uValues = new Double[firstDMU.getOutputs().size()];

			for ( int i = 0; i < u.getValues().getNumRows(); i++ ) {
				Object[] row = u.getValues().getRowByIndex( i );
				String name = (String) row[0];

				for ( int j = 0; j < outputNames.length; j++ ) {
					if ( outputNames[j].equals( name ) ) {
						uValues[j] = (Double) row[1];
						break;
					}
				}
			}

			for ( int i = 0; i < v.getValues().getNumRows(); i++ ) {
				Object[] row = v.getValues().getRowByIndex( i );
				String name = (String) row[0];

				for ( int j = 0; j < inputNames.length; j++ ) {
					if ( inputNames[j].equals( name ) ) {
						vValues[j] = (Double) row[1];
						break;
					}
				}
			}

			System.out.println( "uValues = " + Arrays.deepToString( uValues ) );
			System.out.println( "vValues = " + Arrays.deepToString( vValues ) );

			Objective z = ampl.getObjective( "z" );
			System.out.println( "z = " + z.value() );

			return new KAOSolution( z.value(), uValues, vValues, dmus );
		} catch ( IOException e ) {
			throw new RuntimeException( e );
		} finally {
			ampl.close();
		}
	}

	public KAO( List<DMU> dmus, String kaoFilePath ) {
		super();
		this.dmus = dmus;
		this.kaoFilePath = kaoFilePath;
	}

}