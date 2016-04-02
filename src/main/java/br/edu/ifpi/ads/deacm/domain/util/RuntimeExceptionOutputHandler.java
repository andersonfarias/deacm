package br.edu.ifpi.ads.deacm.domain.util;

import com.ampl.AMPLOutput;
import com.ampl.AMPLOutput.Kind;
import com.ampl.OutputHandler;

public class RuntimeExceptionOutputHandler implements OutputHandler {

	@Override
	public void output( AMPLOutput output ) {
		Kind k = output.getKind();
		String m = output.getMessage();

		if ( k != Kind.SOLVE )
			return;

		if ( m.startsWith( "infeasible problem" ) ) {
			System.out.println( "********	********	******** OutputHandler ********	********	********" );
			System.out.println( "Kind = '" + k.name() + "'\tMessage = '" + m + "'" );
			throw new RuntimeException( "Infeasible Problem." );
		}
	}

}