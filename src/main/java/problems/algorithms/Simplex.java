package problems.algorithms;

/**
 * Created by Amanda Curyło on 21.03.2018.
 */
public class Simplex {
    private final OptimType type;
    private final int N;     //number of variables
    private final int M;     //number of constraints
    private double[] c;      //coefficients of the objective function
    private double[][] A;    //matrix of variable coefficients in constraints
    private double[] b;      //values ​​limiting the equations in A matrix
    private double[] cB;     //c coefficients of the base variables
    private double[] z;
    private double zB;        //value of the objective function
    private double[] ratio;   //values ​​limiting the incoming variable
    private double[] cMinusZ; //coefficients of the new objective function
    private int[] xB;         //indices of basic variables

    public Simplex(double[][] aMatrix, double[] cVector, double[] bVector, OptimType type) {
        //dual problem
        if(type == OptimType.MINIMIZE){
            int rows = aMatrix.length;
            int columns = aMatrix[0].length;

            double[][] aHelper = new double[columns][rows];
            double[] bHelper = bVector;

            bVector = cVector;
            cVector = bHelper;

            for(int i = 0; i < rows; i++){
                for (int j = 0; j < columns; j++) {
                    aHelper[j][i] = aMatrix[i][j];
                }
            }
            aMatrix = aHelper;
        }

        //dimension check
        N = aMatrix[0].length; //number of columns
        M = aMatrix.length;    //number of rows
        if(cVector.length != N || bVector.length != M)
            throw new IllegalArgumentException("Dimensions does not match.");

        //arrays initialization
        this.type = type;
        c = new double[N+M];
        A = new double[M][N+M];
        b = new double[M];
        cB = new double[M];
        z = new double[N+M];
        ratio = new double[M];
        cMinusZ = new double[M+N];
        xB = new int[M];

        double[][] Acopy;
        double[] bCopy;

        //filling A,b,c based on given parameters
        b = bVector;
        for(int i = 0; i < N; i++)
            c[i] = cVector[i];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                A[i][j] = aMatrix[i][j];
            }
            A[i][N+i] = 1;
        }

        //filling other vectors with initial values
        for(int j = 0; j < M+N; j++) {
            for(int i = 0; i < M; i++) {
                z[j] += cB[i]*A[i][j];
            }
        }
        for(int i = 0; i < M; i++) {
            zB += cB[i]*b[i];
        }
        for (int j = 0; j < M+N; j++)
            cMinusZ[j] = c[j] - z[j];
        for (int i = 0; i < M; i++)
            xB[i] = N + i;

        //simplex algorithm
        while(!isOptim()) {
            Acopy = new double[M][N+M];
            bCopy = new double[M];

            int column = findIncomingVarIndex(cMinusZ);

            for (int j = 0; j < M; j++) {
                ratio[j] = b[j] / A[j][column];
            }

            //unbounded or infeasible solution
            double[] columnOfIncomingVariable = new double[M];
            for (int i = 0; i < M; i++){
                columnOfIncomingVariable[i] = A[i][column];
            }
            if(isUnboundedOrInfeasible(ratio,columnOfIncomingVariable))
                throw new IllegalStateException("Unbounded or infeasible solution.");

            int row = findOutgoingVarIndex(ratio, column);
            double value = A[row][column];
            cB[row] = c[column];
            xB[row] = column;

            for(int i = 0; i < M; i++) {
                for (int j = 0; j < M+N; j++) {
                    if (i == row) {
                        Acopy[i][j] = A[i][j]/value;
                    }
                    else {
                        Acopy[i][j] = A[i][j] - (A[i][column]*A[row][j])/value;
                    }
                }
            }

            for(int i = 0; i < M; i++) {
                if (i == row) {
                    bCopy[i] = b[i]/value;
                }
                else {
                    bCopy[i] = b[i] - (A[i][column]*b[row])/value;
                }
            }

            A = Acopy;
            b = bCopy;

            for(int j = 0; j < M+N; j++) {
                z[j] = 0;
                for(int i = 0; i < M; i++) {
                    z[j] += cB[i]*A[i][j];
                }
            }

            zB = 0;
            for(int i = 0; i < M; i++) {
                zB += cB[i]*b[i];
            }

            for (int j = 0; j < M+N; j++)
                cMinusZ[j] = c[j] - z[j];
        }
    }

    private boolean isOptim(){
        boolean isOptim = false;
        int count = 0;
        for (int j = 0; j < M+N; j++) {
            if (cMinusZ[j] <= 0)
                count++;
        }
        if(count == M+N)
            isOptim = true;
        return isOptim;
    }

    private int findIncomingVarIndex(double[] array) {
        for (int j = 0; j < array.length; j++) {
            if (array[j] > 0)
                return j;
        }
        return -1;
    }

    private int findOutgoingVarIndex(double[] array, int column) {
        int index = 0;
            for (int i = 0; i < array.length; i++) {
                if (i == 0)
                    i = 0;
                else {
                    if (A[i][column] > 0 && array[i] < array[index])
                        index = i;
                }
            }
        return index;
    }

    public double[] getSolution(){
        double[] solution;
        if(type == OptimType.MAXIMIZE) {
            solution = new double[N];
            for (int i = 0; i < xB.length; i++)
                if (xB[i] < N)
                    solution[xB[i]] = b[i];
        }
        else {
            solution = new double[M];
            for (int i = N; i < N+M; i++){
                solution[i-N] = cMinusZ[i]*(-1);
            }
        }

        return solution;
    }

    public double getOptim() {
        return zB;
    }

    private boolean isAnyNotNegative(double[] array)  {
        for (double anArray : array) {
            if (anArray >= 0)
                return true;
        }
        return false;
    }

    private boolean isAnyPositive(double[] array){
        for (double anArray : array) {
            if (anArray > 0)
                return true;
        }
        return false;
    }

    private boolean isUnboundedOrInfeasible(double[] ratio, double[] column) {
        return !isAnyNotNegative(ratio) || !isAnyPositive(column);
    }

}

