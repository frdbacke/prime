__kernel void add_floats(__global const float* a, __global const float* b, __global float* out, int n) 
{
    int i = get_global_id(0);
    if (i >= n)
        return;

    out[i] = a[i] + b[i];
}

__kernel void isprime(__global const float* a, __global float* out, int n) 
{
    int i = get_global_id(0);
    if (i >= n)
        return;

    float p = a[i];
    float res = 1; 
    for(int i=0;i<p-1;i++){
        res *= 2;
        res = fmod(res, p); // this step is valid because (a*b)%c = ((a%c)*(b%c))%c
    }
    out[i] = fmod(res,p);

}