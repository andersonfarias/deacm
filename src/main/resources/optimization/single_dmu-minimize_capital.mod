# Minimização de Capital - single DMU

set input; 						# inputs
set output;						# outputs

param X {input};				# valor de cada input
param Y {output};				# valor de cada output
param e0;						# eficiência l2 da dmu em análise
param w;						# parâmetro alvo da eficiência l2
param s0;						# porte relativo da dmu em análise
param t;						# parâmetro alvo do porte relativo
param v {input};				# peso KAO para inputs
param u {output};				# peso KAO para outputs
param A {input};				# matriz de custos dos inputs
param D {input};				# matriz de limites para cada input
param F {output};				# matriz de limites para cada output

var d {input};
var f {output};

minimize z: sum {i in input} A[i]*d[i];

subject to sizechange: (sum {r in output} (Y[r]+f[r])*u[r])*(sum {i in input} A[i]*(X[i]+d[i])*v[i]) = 2*t*s0;

subject to efficiencymaintenance: sum {r in output} ((Y[r]+f[r])*u[r]) - w*e0*sum {i in input} (A[i]*(X[i]+d[i])*v[i]) =  0;

subject to LimitInput {i in input}: -X[i]+D[i]<=d[i];
subject to LimitOutput {r in output}: -Y[r]+F[r]<=f[r];