# Objective: Minimize Capital  - Target: Efficiency - Control Parameter: Size (DMU Subset)

set O;
set S;
set NS;
set input;
set output;

param X {O,input};
param Y {O,output};
param A {O,input};

param E {O};
param l;
param D {S, input};
param F {S, output};
param s {S};

param v {input};
param u {output};

param td;
param t {S};


var d {O,input};
var f {O,output};
var c {S};

minimize z: (sum {k in S} c[k]);

subject to distanceminimization: (sum {k in S} (E[k] - (sum {r in output} ((Y[k,r]+f[k,r])*u[r]) /  sum {i in input} (A[k,i]*(X[k,i]+d[k,i])*v[i])))^2 )^1/2 = td*l;


subject to sizechange1 {k in S}: (sum {r in output} (Y[k,r]+f[k,r])*u[r])*(sum {i in input} A[k,i]*(X[k,i]+d[k,i])*v[i]) >= 2*t[k]*s[k];

subject to capitalconsumption {k in S}: sum {i in input} A[k,i]*d[k,i] = c[k];

subject to efficiencymaintenance {k in S}: sum {r in output} ((Y[k,r]+f[k,r])*u[r]) - E[k]*sum {i in input} (A[k,i]*(X[k,i]+d[k,i])*v[i]) <=  0;

subject to LimitInput {k in S, i in input}: -X[k,i]+D[k,i]<=d[k,i];
subject to LimitOutput {k in S, r in output}: -Y[k,r]+F[k,r]<=f[k,r];