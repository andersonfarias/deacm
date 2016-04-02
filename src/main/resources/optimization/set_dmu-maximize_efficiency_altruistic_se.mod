	# Objective: Maximize Overall Eficiency - Target: Size - Control Parameter: Efficiency (DMU Subset)

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

param t {S};
param c;

var d {O,input};
var f {O,output};


minimize z: (sum {k in O} (E[k] - (sum {r in output} ((Y[k,r]+f[k,r])*u[r]) /  sum {i in input} (A[k,i]*(X[k,i]+d[k,i])*v[i])))^2 )^1/2;

subject to sizechange1 {k in S}: (sum {r in output} (Y[k,r]+f[k,r])*u[r])*(sum {i in input} A[k,i]*(X[k,i]+d[k,i])*v[i]) >= 2*t[k]*s[k];

subject to capitalconsumption : sum {k in S} sum {i in input} A[k,i]*d[k,i] = c;

subject to efficiencymaintenance {k in S}: sum {r in output} ((Y[k,r]+f[k,r])*u[r]) - E[k]*sum {i in input} (A[k,i]*(X[k,i]+d[k,i])*v[i]) <=  0;

subject to NInputNS {k in NS, i in input}: d[k,i]=0;
subject to NOutputNS {k in NS, r in output}: f[k,r]=0;

subject to LimitInput {k in S, i in input}: -X[k,i]+D[k,i]<=d[k,i];
subject to LimitOutput {k in S, r in output}: -Y[k,r]+F[k,r]<=f[k,r];