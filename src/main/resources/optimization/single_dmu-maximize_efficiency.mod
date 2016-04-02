# Maximização da Eficiência - single DMU

set input;
set output;

param X {input};
param Y {output};
param s0;
param t;
param v {input};
param u {output};
param A {input};
param D {input};
param F {output};
param c0;

var d {input};
var f {output};

maximize z: (sum {r in output} (Y[r]+f[r])*u[r]) / (sum {i in input} A[i]*(X[i]+d[i])*v[i]);

subject to capitalconsumption: sum {i in input} A[i]*d[i] = c0;
subject to sizechange: (sum {r in output} (Y[r]+f[r])*u[r])*(sum {i in input} A[i]*(X[i]+d[i])*v[i]) = 2*t*s0;

subject to LimitInput {i in input}: -X[i]+D[i]<=d[i];
subject to LimitOutput {r in output}: -Y[r]+F[r]<=f[r];