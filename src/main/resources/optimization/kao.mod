set dmu;
set input;
set output;

param A {dmu,input};
param X {dmu,input};
param Y {dmu,output};
param E {dmu};
param p;

var v {input};
var u {output};

minimize z: (sum {k in dmu} (E[k] - (sum {r in output} (Y[k,r]*u[r]) / sum {i in input} (A[k,i]*X[k,i]*v[i])))^p)^1/p;

subject to efficiencyfrontier1{k in dmu}: sum {r in output} (Y[k,r]*u[r]) - E[k] * sum {i in input} (A[k,i]*X[k,i]*v[i]) <=  0;

subject to LimitInputv {i in input}: 0.0001 <= v[i];
subject to LimitOutput {r in output}: 0.0001 <= u[r];