package assignment;

import java.util.Date;
import java.util.Random;

public class Recommendation {

	/*
	 * Inscrivez votre nom complet (prénom et nom de famille)
	 * ainsi que votre numéro sciper ci-dessous :
	 */
	
	/* Etudiant 1 */
	public static String NAME1 = "Ali Hosseiny";
	public static int SCIPER1 = 237452;
	
	/* Etudiant 2 - laissez tel quel si vous avez codé le projet tout seul */
	public static String NAME2 = "Grégoire Clément";
	public static int SCIPER2 = 238122;

    private static Date debut = new Date();
	
	static Random random = new Random();

    public static void generateTest(int rowsM, int columnsM, int d,double rapportZero){
        double[][] U = createRandomMatrix(rowsM,d,0,2);
        double[][] V = createRandomMatrix(d,columnsM,0,2);
        double[][] Pinit = multiplyMatrix(U,V);
        double[][] M = addZeroToMatrix(Pinit,rapportZero);
        int[] myRecommendation = recommend(M, d);
        score(Pinit,M,myRecommendation);
    }

    public static void score(double[][] P, double[][] M, int[] myRecommendation){
        int row = myRecommendation.length;
        int[] recommendation = new int[row];
        int[] recommendation2= new int[row];

        for(int i = 0; i < row; ++i){
            recommendation[i] = indexMax(P[i], M[i]);
        }
        for(int i = 0; i < row; ++i){
            recommendation2[i] = indexSecondMax(P[i], M[i]);
        }
        double score=0;
        for(int i=0; i<M.length;++i){
            if(myRecommendation[i]==recommendation[i]){
                ++score;
            }
            else if(myRecommendation[i]==recommendation2[i]){
                score += 0.5;
            }
        }
    }

    public static double[][] addZeroToMatrix(double[][] P, double proportion){
        if(!isMatrix(P)){
            return null;
        }
        double[][] M = new double[P.length][P[0].length];
        for(int i=0; i<P.length;++i){
            for(int j=0; j<P[0].length;++j){
                if(random.nextDouble()<proportion){
                    M[i][j]=0.;
                }
                else{
                    M[i][j]=P[i][j];
                }
            }
        }
        return M;
    }

    public static int indexMax(double[] tab, double[] ref){
        double max = Double.NEGATIVE_INFINITY;
        int index = -1;
        boolean allZero=true;
        for(int i = 0; i < tab.length; ++i){
            if(tab[i] > max && ref[i] == 0){
                index = i;
                max = tab[i];
            }
            if(tab[i]!=0){
                allZero=false;
            }
        }

        if(allZero){
            return -1;
        }
        else {
            return index;
        }
    }

    public static int indexSecondMax(double[] tab, double[] ref){
        double max = Double.NEGATIVE_INFINITY;
        double max2= Double.NEGATIVE_INFINITY;
        int index = -1;
        int index2 = -1;
        boolean allZero=true;
        for(int i = 0; i < tab.length; ++i){
            if(tab[i] > max  && ref[i] == 0){
                max2=max;
                index2=index;
                max = tab[i];
                index=i;
            }
            else if(tab[i] < max && tab[i] > max2 && ref[i]==0){
                max2 = tab[i];
                index2 = i;
            }
            if(tab[i]!=0){
                allZero=false;
            }
        }

        if(allZero){
            return -1;
        }
        else {
            return index2;
        }
    }

    public static int indexMin(double[] tab){
        double min;
        int index = 0;
        if(tab == null || tab.length == 0){
            return -1;
        }
        else if(tab.length == 1){
            return 0;
        }
        else{
            min = tab[0];
            for(int i = 1; i < tab.length; ++i){
                if(tab[i] < min){
                    index = i;
                    min = tab[i];
                }
            }
        }

        return index;
    }

    public static String tabToString(double[] tab, boolean noSemilcon){
        if(tab == null){
            return "null tab";
        }
        String txt = "{ ";
        for(int i = 0; i < tab.length; ++i){
            txt += tab[i];
            if(i != tab.length - 1){
                txt += ", ";
            }
        }
        txt += " }";
        if(!noSemilcon){
            txt += ";";
        }

        return txt;
    }

    public static String tabToString(double[] tab){
        return tabToString(tab, false);
    }

    public static String tabToString(double[] tab, String nom){
        return nom + " = " + tabToString(tab);
    }

    public static String tabToString(int[] tab){
        if(tab == null){
            return "null array";
        }

        double[] tab1 = new double[tab.length];
        for(int i = 0; i < tab.length; ++i){
            tab1[i] = tab[i];
        }

        return tabToString(tab1);
    }

    public static String tabToString(int[] tab, String nom){
        return nom + " = " + tabToString(tab);
    }

	public static String matrixToString(double[][] A) {
		if(!isMatrix(A)){
            return "null matrix";
        }
        String matrice = "{\n";

        for(int i = 0; i < A.length; ++i){
            matrice += "\t" + tabToString(A[i], true) + ",\n";
        }

        matrice += "};";

        return matrice;
	}

    public static String matrixToString(double[][] A, String nom){
        return nom + " = " + matrixToString(A);
    }
	
	public static boolean isMatrix( double[][] A ) {
		if(A == null || A.length == 0){
            return false;
        }

        int l = A[0].length;
        if(A[0].length == 0){
            return false;
        }

        for(int i = 1; i < A.length; ++i){
            if(A[i].length != l){
                return false;
            }
        }

		return true;
	}
	
	public static double[][] multiplyMatrix(double[][] A, double[][] B) {
        if(!isMatrix(A) || !isMatrix(B) || A[0].length != B.length){
            return null;
        }

        double[][] c = new double[A.length][B[0].length];
        double s;

		for(int j = 0; j < c[0].length; ++j){
            for(int i = 0; i < c.length; ++i){
                s = 0;
                for(int k = 0; k < A[0].length; ++k){
                    s += A[i][k] * B[k][j];
                }
                c[i][j] = s;
            }
        }
		return c;
	}
	
	public static double[][] createRandomMatrix( int n, int m, double k, double l) {
		if(n == 0 || m == 0 || k > l){
            return null;
        }

        double[][] a = new double[n][m];
		for(int i = 0; i < n; ++i){
            for(int j = 0; j < m; ++j){
                a[i][j] = k + random.nextDouble() * ( l - k );
            }
        }

        return a;
	}

    public static double[][] createMatrix(int n, int m, double v) {
        if(n == 0 || m == 0){
            return null;
        }

        double[][] A = new double[n][m];
        for(int i = 0; i < n; ++i){
            for(int j = 0; j < m; ++j){
                A[i][j] = v;
            }
        }

        return A;
    }

    public static double meanValue(double[][] M, boolean includeZero){
        if(!isMatrix(M)){
            return -1;
        }

        double mean = 0;
        int count = 0;

        for(int i = 0; i < M.length; ++i){
            for(int j = 0; j < M[i].length; ++j){
                if(M[i][j] != 0 || includeZero){
                    mean += M[i][j];
                    ++count;
                }
            }
        }

        mean /= count;
        return mean;
    }

	public static double rmse(double[][] M, double[][] P) {
        if(!isMatrix(M) || !isMatrix(P) || M.length != P.length || M[0].length != P[0].length){
            return -1;
        }
        double rmse = 0;
        int count = 0;
        for(int i = 0; i < M.length; ++i){
            for(int j = 0; j < M[0].length; ++j){
                if(M[i][j] != 0){
                    rmse += Math.pow(M[i][j] - P[i][j], 2);
                    ++count;
                }
            }
        }

        rmse /= count;
        rmse = Math.sqrt(rmse);

		return rmse;
	}

	public static double updateUElem(double[][] M, double[][] U, double[][] V, int r, int s ) {
        double numer=0,insideSum=0,denom=0;
        for(int  j=0 ; j<M[0].length ; ++j) {
            if(M[r][j]!=0){
                insideSum = 0;
                for (int k = 0; k < V.length; ++k) {
                    if (k != s) {
                        insideSum += U[r][k] * V[k][j];
                    }
                }
                numer += V[s][j] * (M[r][j] - insideSum);
                denom += V[s][j] * V[s][j];
            }
        }
		return numer/denom;
	}
	
	public static double updateVElem( double[][] M, double[][] U, double[][] V, int r, int s ) {
        double numer=0,insideSum=0,denom=0;
        for(int  i=0 ; i<M.length ; ++i){
            if(M[i][s]!=0){
                insideSum=0;
                for(int k=0; k < V.length ; ++k) {
                    if (k != r) {
                        insideSum += U[i][k] * V[k][s];
                    }
                }
                numer += U[i][r] * ( M[i][s] - insideSum );
                denom += U[i][r]*U[i][r];

            }
        }
        return numer/denom;
    }
	
	public static double[][] optimizeU( double[][] M, double[][] U, double[][] V) {
        double lastRmse=rmse(M,multiplyMatrix(U,V));
        double newRmse, dif;

        do{
           if((new Date().getTime() - debut.getTime()) > 120000){
               return U;
           }
           for(int i=0; i<U.length ; ++i){
                for(int j=0; j<U[0].length; ++j){
                    U[i][j]=updateUElem(M,U,V,i,j);
                }
           }
            newRmse=rmse(M,multiplyMatrix(U,V));
            dif=lastRmse-newRmse;
            lastRmse=newRmse;
        }while(dif>Math.pow(10,-6));

        return U;
    }

	public static double[][] optimizeV( double[][] M, double[][] U, double[][] V) {
        double lastRmse=rmse(M,multiplyMatrix(U,V));
        double newRmse, dif;

        do{
            if((new Date().getTime() - debut.getTime()) > 120000){
                return V;
            }
            for(int i=0; i<V.length ; ++i){
                for(int j=0; j<V[0].length; ++j){
                    V[i][j]=updateVElem(M,U,V,i,j);
                }
            }
            newRmse=rmse(M,multiplyMatrix(U,V));
            dif=lastRmse-newRmse;
            lastRmse=newRmse;
        }while(dif>Math.pow(10,-6));

        return V;
    }

    /*
     * Construis les points de départs U, V et leur produit P
     * Calcule le rmse des P
     * Si une matrice P avec un rmse de 0 est trouvée, renvoie son index
     * Sinon, renvoie -1
     */
    public static int buildUVP(double[][] M, double[][][] U, double[][][] V, double[][][] P, double[] rmse, int d, double optimalValueM, int pDeparts){
        /*
        Lorsque 6 points de départ, on veut 10 itérations (choix)
        Lorsque 26 points de départs, on veut 4 itérations
         */
        int
                iterations= pDeparts*10,
                separationQtMatrices= (int)(Math.round(4.*pDeparts/5.));
        double rangeValMoins, rangeValPlus;
        /*
        * rangeVal: une valeur autour de la valeur optimale proposée dans le cours pour remplir U et V
        * Pour la première matrice, on prend simplement la valeur optimale (d'où le ternaire)
        * separationQtMatrices: on veut produire les 4/5 des matrices à partir de rangeVal
        * les 1/5 qui restent seront complètement aléatoires
        */
        for(int i = 0; i < separationQtMatrices; ++i){
            /*
             * rangeVal: une valeur autour de la valeur optimale proposée dans le cours pour remplir U et V
             * Pour la première matrice, on prend simplement la valeur optimale (d'où le ternaire)
            */
            rangeValMoins = (i == 0) ? optimalValueM : optimalValueM*(1-random.nextDouble());
            rangeValPlus = (i == 0) ? optimalValueM : optimalValueM*(1+random.nextDouble());
            U[i] = createRandomMatrix(U[0].length, d, rangeValMoins, rangeValPlus);

            rangeValMoins = (i == 0) ? optimalValueM : optimalValueM*(1-random.nextDouble());
            rangeValPlus = (i == 0) ? optimalValueM : optimalValueM*(1+random.nextDouble());
            V[i] = createRandomMatrix(d, V[0][0].length, rangeValMoins, rangeValPlus);
            for(int j = 0; j < iterations; ++j){
                U[i] = optimizeU(M, U[i], V[i]);
                V[i] = optimizeV(M, U[i], V[i]);
            }
            P[i] = multiplyMatrix(U[i], V[i]);
            rmse[i] = rmse(M, P[i]);
            if(rmse[i] == 0.){
                return i;
            }
        }

        for(int i = separationQtMatrices; i < U.length; ++i){
            U[i] = createRandomMatrix(U[0].length, d, 0, 5);
            V[i] = createRandomMatrix(d, V[0][0].length, 0, 5);
            for(int j = 0; j < iterations; ++j){
                U[i] = optimizeU(M, U[i], V[i]);
                V[i] = optimizeV(M, U[i], V[i]);
            }
            P[i] = multiplyMatrix(U[i], V[i]);
            rmse[i] = rmse(M, P[i]);
            if(rmse[i] == 0.){
                return i;
            }
        }

        return -1;
    }
	
	public static int[] recommend( double[][] M, int d) {
        if(!isMatrix(M)){
            return null;
        }

        int[] recommendation = new int[M.length];
        int rowsM = M.length, columnsM = M[0].length, indexBestP = 0,
            pDeparts= (int) (1+ Math.round(3000/((rowsM+columnsM)*d)));
        double[] rmse = new double[pDeparts];
        double optimalValueM = Math.sqrt(meanValue(M, false)/d);
        double[][][]
                U = new double[pDeparts][rowsM][d],
                V = new double[pDeparts][d][columnsM],
                P = new double[pDeparts][rowsM][columnsM];

        indexBestP = buildUVP(M, U, V, P, rmse, d, optimalValueM, pDeparts);

        if(indexBestP == -1){
            indexBestP = indexMin(rmse);
        }

        for(int i = 0; i < M.length; ++i){
            recommendation[i] = indexMax(P[indexBestP][i], M[i]);
        }

		return recommendation;
	}
}