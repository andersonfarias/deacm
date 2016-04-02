package br.edu.ifpi.ads.deacm.domain.lp;

import java.math.MathContext;
import java.math.RoundingMode;

public interface LinearProgrammingModel {

	public static final MathContext DEFAULT_MATH_CONTEXT = new MathContext( 8, RoundingMode.HALF_UP );

	public Solution solve();

}