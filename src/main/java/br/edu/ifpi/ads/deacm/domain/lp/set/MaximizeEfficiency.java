package br.edu.ifpi.ads.deacm.domain.lp.set;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.ampl.AMPL;
import com.ampl.DataFrame;
import com.ampl.Objective;
import com.ampl.Parameter;
import com.ampl.Set;
import com.ampl.Variable;

import br.edu.ifpi.ads.deacm.domain.DMU;
import br.edu.ifpi.ads.deacm.domain.Input;
import br.edu.ifpi.ads.deacm.domain.ModelMode;
import br.edu.ifpi.ads.deacm.domain.Output;
import br.edu.ifpi.ads.deacm.domain.lp.LinearProgrammingModel;
import br.edu.ifpi.ads.deacm.domain.lp.Solution;
import br.edu.ifpi.ads.deacm.domain.lp.solutions.SetDMUEfficiencyMaximizationSolution;
import br.edu.ifpi.ads.deacm.domain.util.RuntimeExceptionOutputHandler;

public class MaximizeEfficiency implements LinearProgrammingModel {

	public static final String DMU_SET_MAXIMIZE_EFFICIENCY_MODEL_ALTRUISTIC_FILE = "set_dmu-maximize_efficiency_altruistic.mod";

	public static final String DMU_SET_MAXIMIZE_EFFICIENCY_MODEL_ALTRUISTIC_FILE_SUPER_EFICIENCY = "set_dmu-maximize_efficiency_altruistic_se.mod";

	public static final String DMU_SET_MAXIMIZE_EFFICIENCY_MODEL_SELFISH_FILE = "set_dmu-maximize_efficiency_selfish.mod";

	public static final String DMU_SET_MAXIMIZE_EFFICIENCY_MODEL_SELFISH_FILE_SUPER_EFICIENCY = "set_dmu-maximize_efficiency_selfish_se.mod";

	private String modelFilePath;

	private double capital;

	private double kaoDistance;

	private boolean executeWithSuperEfficiency;

	private List<DMU> targets;

	private List<DMU> dmus;

	private ModelMode mode;

	@Override
	public Solution solve() {
		AMPL ampl = new AMPL();

		try {

			ampl.read( modelFilePath );

			DMU firstDMU = dmus.get( 0 );

			Object[] dmusNames = new Object[dmus.size()];
			Object[] dmusTargetNames = new Object[targets.size()];
			Object[] dmusNotTargetNames = new Object[dmus.size() - targets.size()];
			Object[] targetsRelativeSize = new Object[targets.size()];
			Object[] targetsRelativeSizeCoefficients = new Object[targets.size()];

			int inputsLength = firstDMU.getInputs().size();
			int outputsLength = firstDMU.getOutputs().size();

			Object[] inputNames = new Object[inputsLength];
			Object[] inputMatrix = new Object[dmus.size() * firstDMU.getInputs().size()];
			Object[] inputCostMatrix = new Object[dmus.size() * inputsLength];
			Object[] inputBoundsMatrix = new Object[targets.size() * inputsLength];
			Object[] normalizedInputWeights = new Object[inputsLength];

			Object[] outputNames = new Object[outputsLength];
			Object[] outputMatrix = new Object[dmus.size() * firstDMU.getOutputs().size()];
			Object[] outputBoundsMatrix = new Object[targets.size() * outputsLength];
			Object[] normalizedOutputWeights = new Object[outputsLength];

			Object[] efficiencyMatrix = new Double[dmus.size()];

			for ( int i = 0, j = 0; i < dmus.size(); i++ ) {
				DMU dmu = dmus.get( i );

				dmusNames[i] = dmu.getName();
				if ( !targets.contains( dmu ) ) {
					dmusNotTargetNames[j++] = dmu.getName();
				}

				for ( int k = 0; k < dmu.getInputs().size(); k++ ) {
					int index = i * inputsLength + k;
					Input input = dmu.getInputs().get( k );

					inputMatrix[index] = input.getValue();
					inputCostMatrix[index] = input.getCost();
				}

				for ( int k = 0; k < dmu.getOutputs().size(); k++ ) {
					int index = i * outputsLength + k;
					Output output = dmu.getOutputs().get( k );

					outputMatrix[index] = output.getValue();
				}

				Double efficiency = dmu.getDeaEfficiency();
				if ( executeWithSuperEfficiency ) {
					for ( DMU target : targets ) {
						if ( dmu.getName().equals( target.getName() ) ) {
							efficiency = dmu.getDeaSuperEfficiency();
							break;
						}
					}
				}

				BigDecimal eBD = new BigDecimal( efficiency, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
				eBD = eBD.setScale( 2, RoundingMode.HALF_UP );

				efficiencyMatrix[i] = eBD.doubleValue();
			}

			for ( int i = 0; i < targets.size(); i++ ) {
				DMU dmu = targets.get( i );

				dmusTargetNames[i] = dmu.getName();
				targetsRelativeSizeCoefficients[i] = dmu.getCoefficientSize();
				targetsRelativeSize[i] = dmu.getRelativeSize();

				for ( int k = 0; k < dmu.getInputs().size(); k++ ) {
					int index = i * inputsLength + k;
					Input input = dmu.getInputs().get( k );
					inputBoundsMatrix[index] = input.getBound();
				}

				for ( int k = 0; k < dmu.getOutputs().size(); k++ ) {
					int index = i * outputsLength + k;
					Output output = dmu.getOutputs().get( k );

					outputBoundsMatrix[index] = output.getBound();
				}
			}

			for ( int i = 0; i < inputsLength; i++ ) {
				Input input = firstDMU.getInputs().get( i );

				inputNames[i] = input.getName();
				normalizedInputWeights[i] = input.getKaoNormalizedWeight();
			}

			for ( int i = 0; i < outputsLength; i++ ) {
				Output output = firstDMU.getOutputs().get( i );

				outputNames[i] = output.getName();
				normalizedOutputWeights[i] = output.getKaoNormalizedWeight();
			}

			Set dmuSet = ampl.getSet( "O" );
			dmuSet.setValues( dmusNames );

			Set targetSet = ampl.getSet( "S" );
			targetSet.setValues( dmusTargetNames );

			Set notTargetSet = ampl.getSet( "NS" );
			notTargetSet.setValues( dmusNotTargetNames );

			Set inputSet = ampl.getSet( "input" );
			inputSet.setValues( inputNames );

			Set outputSet = ampl.getSet( "output" );
			outputSet.setValues( outputNames );

			Parameter x = ampl.getParameter( "X" );
			x.setValues( inputMatrix );

			Parameter y = ampl.getParameter( "Y" );
			y.setValues( outputMatrix );

			Parameter e = ampl.getParameter( "E" );
			e.setValues( efficiencyMatrix );

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

			Parameter l = ampl.getParameter( "l" );
			l.set( kaoDistance );

			Parameter cParam = ampl.getParameter( "c" );
			cParam.set( capital );

			Parameter t = ampl.getParameter( "t" );
			t.setValues( targetsRelativeSizeCoefficients );

			Parameter s = ampl.getParameter( "s" );
			s.setValues( targetsRelativeSize );

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
			double minCapital = z.value();

			System.out.println( z.name() );
			System.out.println( minCapital );

			DataFrame inputsOffsets = ampl.getVariable( "d" ).getValues();
			DataFrame outputsOffsets = ampl.getVariable( "f" ).getValues();

			BigDecimal kaoDistanceB = new BigDecimal( kaoDistance, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );
			BigDecimal capitalB = new BigDecimal( capital, LinearProgrammingModel.DEFAULT_MATH_CONTEXT );

			return new SetDMUEfficiencyMaximizationSolution( capitalB.doubleValue(), kaoDistanceB.doubleValue(), dmus, targets, inputsOffsets, outputsOffsets, executeWithSuperEfficiency, mode );
		} catch ( Exception e ) {
			throw new RuntimeException( e.getMessage() );
		} finally {
			ampl.close();
		}
	}

	public MaximizeEfficiency( String modelFilePath, double capital, double kaoDistance, List<DMU> targets, List<DMU> dmus, boolean executeWithSuperEfficiency, ModelMode mode ) {
		super();
		this.modelFilePath = modelFilePath;
		this.capital = capital;
		this.kaoDistance = kaoDistance;
		this.targets = targets;
		this.dmus = dmus;
		this.executeWithSuperEfficiency = executeWithSuperEfficiency;
		this.mode = mode;
	}

}