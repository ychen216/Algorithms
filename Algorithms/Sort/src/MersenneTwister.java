/*
参考了
https://github.com/neetsdkasu/mt19937ar-MersenneTwister/blob/master/mt19937ar/src/mt19937ar/MersenneTwister.java
算法实现
*/

public class MersenneTwister {
    /* Period parameters */
    private static final int N = 624;
    private static final int M = 397;
    private static final long MATRIX_A = 0x9908b0dfL;  /* constant vector a */
    private static final long UMASK = 0x80000000L; /* most significant w-r bits */
    private static final long LMASK = 0x7fffffffL; /* least significant r bits */

    private static final long mixBits(long u, long v) {
        return (u & MersenneTwister.UMASK) | (v & MersenneTwister.LMASK);
    }

    private static final long twist(long u, long v) {
        return (mixBits(u, v) >> 1) ^ ((v & 1) == 1 ? MersenneTwister.MATRIX_A : 0);
    }

    private long[] state; /* the array for the state vector  */
    private int left = 1;
    private int initf = 0;
    private int next = 0;
    private long seed;

    public MersenneTwister() {
        this(0);
    }

    public MersenneTwister(long seed) {
        state = new long[MersenneTwister.N];
        init_genrand(seed);
    }

    public long getSeed() {
        return seed;
    }

    /* initializes state[N] with a seed */
    public void init_genrand(long seed) {
        this.seed = seed & 0xffffffffL;
        state[0] = seed & 0xffffffffL;
        for (int j = 1; j < MersenneTwister.N; j++) {
            state[j] = (1812433253L * (state[j - 1] ^ (state[j - 1] >> 30)) + j);
	        /* See Knuth TAOCP Vol2. 3rd Ed. P.106 for multiplier. */
	        /* In the previous versions, MSBs of the seed affect   */
	        /* only MSBs of the array state[].                        */
	        /* 2002/01/09 modified by Makoto Matsumoto             */
            state[j] &= 0xffffffffL;  /* for >32 bit machines */
        }
        left  = 1; initf = 1;
    }

    /* initialize by an array with array-length */
	/* init_key is the array for initializing keys */
	/* key_length is its length */
	/* slight change for C++, 2004/2/26 */
    public void init_by_array(long[] init_key, int key_length) {
        int i, j, k;
        init_genrand(19650218L);
        i = 1; j = 0;
        k = (MersenneTwister.N > key_length ? MersenneTwister.N : key_length);
        for (; k > 0; k--) {
            state[i] = (state[i] ^ ((state[i-1] ^ (state[i-1] >> 30)) * 1664525L))
                    + init_key[j] + j; /* non linear */
            state[i] &= 0xffffffffL; /* for WORDSIZE > 32 machines */
            i++; j++;
            if (i >= MersenneTwister.N) { state[0] = state[MersenneTwister.N - 1]; i = 1; }
            if (j >= key_length) j = 0;
        }
        for (k = MersenneTwister.N - 1; k > 0; k--) {
            state[i] = (state[i] ^ ((state[i - 1] ^ (state[i - 1] >> 30)) * 1566083941L))
                    - i; /* non linear */
            state[i] &= 0xffffffffL; /* for WORDSIZE > 32 machines */
            i++;
            if (i >= MersenneTwister.N) { state[0] = state[MersenneTwister.N - 1]; i = 1; }
        }

        state[0] = 0x80000000L; /* MSB is 1; assuring non-zero initial array */
        left = 1; initf = 1;
    }

    public void next_state() {
        int p = 0;
        int j;

	    /* if init_genrand() has not been called, */
	    /* a default initial seed is used         */
        if (initf == 0) init_genrand(5489L);

        left = MersenneTwister.N;
        next = 0;

        for (j = MersenneTwister.N - MersenneTwister.M + 1; --j > 0; p++)
            state[p] = state[p + MersenneTwister.M] ^ twist(state[p], state[p + 1]);

        for (j = MersenneTwister.M; --j > 0; p++)
            state[p] = state[p + MersenneTwister.M -MersenneTwister.N] ^ twist(state[p], state[p + 1]);

        state[p] = state[p + MersenneTwister.M - MersenneTwister.N] ^ twist(state[p], state[0]);
    }

    /* generates a random number on [0,0xffffffff]-interval */
    public long genrand_int32() {
        long y;

        if (--left == 0) next_state();
        y = state[next++];

	    /* Tempering */
        y ^= (y >> 11);
        y ^= (y << 7) & 0x9d2c5680L;
        y ^= (y << 15) & 0xefc60000L;
        y ^= (y >> 18);

        return y;
    }

}