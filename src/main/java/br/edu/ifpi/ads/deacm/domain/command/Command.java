package br.edu.ifpi.ads.deacm.domain.command;

import br.edu.ifpi.ads.deacm.domain.lp.Solution;

public interface Command {

	public void execute( Solution s );
	
}