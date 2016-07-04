package br.edu.ifpi.ads.deacm.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpi.ads.deacm.Application;
import br.edu.ifpi.ads.deacm.domain.DMU;
import br.edu.ifpi.ads.deacm.domain.ModelMode;
import br.edu.ifpi.ads.deacm.domain.command.CapitalConsumptionDMU;
import br.edu.ifpi.ads.deacm.domain.command.Command;
import br.edu.ifpi.ads.deacm.domain.command.DEACommand;
import br.edu.ifpi.ads.deacm.domain.command.KAOCommand;
import br.edu.ifpi.ads.deacm.domain.command.KAONormalizedWeightsCommand;
import br.edu.ifpi.ads.deacm.domain.command.NEfficiencyCommand;
import br.edu.ifpi.ads.deacm.domain.command.OversizeAndSuperEfficiencyCommandForSetOfDMU;
import br.edu.ifpi.ads.deacm.domain.command.OversizeAndSuperEfficiencyCommandForSingleDMU;
import br.edu.ifpi.ads.deacm.domain.command.RelativeAreaCommand;
import br.edu.ifpi.ads.deacm.domain.command.RelativeSizeCommand;
import br.edu.ifpi.ads.deacm.domain.command.SuperEfficiencyDEACommand;
import br.edu.ifpi.ads.deacm.domain.command.UpdateXYCommand;
import br.edu.ifpi.ads.deacm.domain.lp.DEA;
import br.edu.ifpi.ads.deacm.domain.lp.KAO;
import br.edu.ifpi.ads.deacm.domain.lp.LinearProgrammingModel;
import br.edu.ifpi.ads.deacm.domain.lp.Solution;
import br.edu.ifpi.ads.deacm.domain.lp.SuperEfficiencyDEA;
import br.edu.ifpi.ads.deacm.domain.lp.single.MaximizeEfficiency;
import br.edu.ifpi.ads.deacm.domain.lp.single.MaximizeSize;
import br.edu.ifpi.ads.deacm.domain.lp.single.MinimizeCapital;
import br.edu.ifpi.ads.deacm.domain.lp.solutions.KAOSolution;

@Service
@Transactional
public class CapitalManagementService {

	public List<DMU> superEficiencyDEA( List<DMU> dmus ) {
		Command command = new SuperEfficiencyDEACommand();

		for ( DMU dmu : dmus ) {
			SuperEfficiencyDEA sedea = new SuperEfficiencyDEA( dmu, dmus );
			command.execute( sedea.solve() );
		}

		return dmus;
	}

	public List<DMU> dea( List<DMU> dmus ) {
		Command command = new DEACommand();

		for ( DMU dmu : dmus ) {
			LinearProgrammingModel dea = new DEA( dmu, dmus );

			command.execute( dea.solve() );
		}

		return dmus;
	}

	public Solution kao( List<DMU> dmus ) throws IOException {
		File kaoFile = getFile( KAO.KAO_MODEL_FILE );

		List<Command> commands = Arrays.asList( new KAOCommand(), new RelativeAreaCommand(), new RelativeSizeCommand(), new NEfficiencyCommand(), new KAONormalizedWeightsCommand() );

		LinearProgrammingModel kao = new KAO( dmus, kaoFile.getAbsolutePath() );
		Solution kaoSolution = kao.solve();

		for ( Command command : commands ) {
			command.execute( kaoSolution );
		}

		return kaoSolution;
	}

	public Solution minCapitalOfSingleDMU( List<DMU> dmus, DMU target, Double ks, Double ke ) throws IOException {
		File modelFile = getFile( MinimizeCapital.SINGLE_DMU_MINIMIZE_MODEL_FILE );

		LinearProgrammingModel minCapital = new MinimizeCapital( modelFile.getAbsolutePath(), ks, ke, target );
		Solution solution = minCapital.solve();

		List<Command> commands = new ArrayList<>( 2 );
		commands.add( new UpdateXYCommand() );
		commands.add( new OversizeAndSuperEfficiencyCommandForSingleDMU( dmus ) );

		for ( Command command : commands ) {
			command.execute( solution );
		}

		return solution;
	}

	public Solution maxSizeOfSingleDMU( List<DMU> dmus, DMU target, Double capital, Double ke ) throws IOException {
		File modelFile = getFile( MaximizeSize.SINGLE_DMU_MAXIMIZE_SIZE_MODEL_FILE );

		LinearProgrammingModel maxSize = new MaximizeSize( modelFile.getAbsolutePath(), capital, ke, target );
		Solution solution = maxSize.solve();

		List<Command> commands = new ArrayList<>( 2 );
		commands.add( new UpdateXYCommand() );
		commands.add( new OversizeAndSuperEfficiencyCommandForSingleDMU( dmus ) );

		for ( Command command : commands ) {
			command.execute( solution );
		}

		return solution;
	}

	public Solution maxEfficiencyOfSingleDMU( List<DMU> dmus, DMU target, Double capital, Double ks ) throws IOException {
		File modelFile = getFile( MaximizeEfficiency.SINGLE_DMU_MAXIMIZE_EFFICIENCY_MODEL_FILE );

		LinearProgrammingModel maxEfficiency = new MaximizeEfficiency( modelFile.getAbsolutePath(), capital, ks, target );
		Solution solution = maxEfficiency.solve();

		List<Command> commands = new ArrayList<>( 2 );
		commands.add( new UpdateXYCommand() );
		commands.add( new OversizeAndSuperEfficiencyCommandForSingleDMU( dmus ) );

		for ( Command command : commands ) {
			command.execute( solution );
		}

		return solution;
	}

	public Solution minCapitalOfDMUSet( List<DMU> dmus, List<DMU> targets, Double td, Double kaoDistance, boolean executeWithSuperEfficiency, ModelMode mode ) throws IOException {
		String path = null;

		switch ( mode ) {
			case ALTRUISTIC:
				if ( executeWithSuperEfficiency )
					path = br.edu.ifpi.ads.deacm.domain.lp.set.MinimizeCapital.DMU_SET_MINIMIZE_MODEL_ALTRUISTIC_FILE_SUPER_EFICIENCY;
				else
					path = br.edu.ifpi.ads.deacm.domain.lp.set.MinimizeCapital.DMU_SET_MINIMIZE_MODEL_ALTRUISTIC_FILE;
				break;
			case SELFISH:
				if ( executeWithSuperEfficiency )
					path = br.edu.ifpi.ads.deacm.domain.lp.set.MinimizeCapital.DMU_SET_MINIMIZE_MODEL_SELFISH_FILE_SUPER_EFICIENCY;
				else
					path = br.edu.ifpi.ads.deacm.domain.lp.set.MinimizeCapital.DMU_SET_MINIMIZE_MODEL_SELFISH_FILE;

				File kaoFile = getFile( KAO.KAO_MODEL_FILE );
				KAOSolution kaoSolution = (KAOSolution) new KAO( targets, kaoFile.getAbsolutePath() ).solve();

				kaoDistance = kaoSolution.getKaoDistance();
		}

		File modelFile = getFile( path );

		for ( DMU dmu0 : targets ) {
			SuperEfficiencyDEA sedea = new SuperEfficiencyDEA( dmu0, dmus );
			Solution solution = sedea.solve();

			Command command = new SuperEfficiencyDEACommand();
			command.execute( solution );
		}

		LinearProgrammingModel minCapital = new br.edu.ifpi.ads.deacm.domain.lp.set.MinimizeCapital( modelFile.getAbsolutePath(), targets, dmus, td, kaoDistance, executeWithSuperEfficiency, mode );

		Solution solution = minCapital.solve();

		List<Command> commands = new ArrayList<>( 2 );
		commands.add( new UpdateXYCommand() );
		commands.add( new OversizeAndSuperEfficiencyCommandForSetOfDMU() );

		for ( Command command : commands ) {
			command.execute( solution );
		}

		return solution;
	}

	public Solution maxEfficiencyOfDMUSet( List<DMU> dmus, List<DMU> targets, Double capital, Double kaoDistance, boolean executeWithSuperEfficiency, ModelMode mode ) throws IOException {
		String path = null;

		switch ( mode ) {
			case ALTRUISTIC:
				if ( executeWithSuperEfficiency )
					path = br.edu.ifpi.ads.deacm.domain.lp.set.MaximizeEfficiency.DMU_SET_MAXIMIZE_EFFICIENCY_MODEL_ALTRUISTIC_FILE_SUPER_EFICIENCY;
				else
					path = br.edu.ifpi.ads.deacm.domain.lp.set.MaximizeEfficiency.DMU_SET_MAXIMIZE_EFFICIENCY_MODEL_ALTRUISTIC_FILE;
				break;
			case SELFISH:
				if ( executeWithSuperEfficiency )
					path = br.edu.ifpi.ads.deacm.domain.lp.set.MaximizeEfficiency.DMU_SET_MAXIMIZE_EFFICIENCY_MODEL_SELFISH_FILE_SUPER_EFICIENCY;
				else
					path = br.edu.ifpi.ads.deacm.domain.lp.set.MaximizeEfficiency.DMU_SET_MAXIMIZE_EFFICIENCY_MODEL_SELFISH_FILE;

				File kaoFile = getFile( KAO.KAO_MODEL_FILE );
				KAOSolution kaoSolution = (KAOSolution) new KAO( targets, kaoFile.getAbsolutePath() ).solve();

				kaoDistance = kaoSolution.getKaoDistance();
		}

		File modelFile = getFile( path );

		for ( DMU dmu0 : targets ) {
			SuperEfficiencyDEA sedea = new SuperEfficiencyDEA( dmu0, dmus );
			Solution solution = sedea.solve();

			Command command = new SuperEfficiencyDEACommand();
			command.execute( solution );
		}

		LinearProgrammingModel maxEfficiency = new br.edu.ifpi.ads.deacm.domain.lp.set.MaximizeEfficiency( modelFile.getAbsolutePath(), capital, kaoDistance, targets, dmus, executeWithSuperEfficiency, mode );
		Solution solution = maxEfficiency.solve();

		List<Command> commands = new ArrayList<>( 3 );
		commands.add( new UpdateXYCommand() );
		commands.add( new OversizeAndSuperEfficiencyCommandForSetOfDMU() );
		commands.add( new CapitalConsumptionDMU() );

		for ( Command command : commands ) {
			command.execute( solution );
		}

		return solution;
	}

	// private File getFile( String file ) throws IOException {
	// return resourceLoader.getResource( String.format(
	// "classpath:optimization%s%s", File.separator, file ) ).getFile();
	// }

	private File getFile( String f ) {
		return new File( Application.MODELS_LOCATION + File.separator + f );
	}
}