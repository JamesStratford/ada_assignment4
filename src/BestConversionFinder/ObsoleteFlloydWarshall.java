/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BestConversionFinder;

/**
 *
 * @author jestr
 */
public class ObsoleteFlloydWarshall
{
    
    
    /**
     * Flloyd Warshall attempt
     *
     */
    
    //    public static ShortestPathsResultSet floydWarshall(double[][] rates)
//    {
//        double[][] weights = rates;
//        int n = weights.length;
//        double[][][] d = new double[n+1][][];
//        int[][][] p = new int[n+1][][];
//       
//        d[0] = weights;
//        p[0] = new int[n][n];
//        for (int i = 0; i < n; i++)
//        {
//            for (int j =0; j < n; j++)
//            {
//                if (weights[i][j] < INFINITY)
//                {
//                    p[0][i][j] = i;
//                }
//                else
//                {
//                    p[0][i][j] = NO_VERTEX;
//                }
//            }
//        }
//        
//        for (int k = 1; k <= n; k++)
//        {
//            d[k] = new double[n][n];
//            p[k] = new int[n][n];
//            for (int i = 0; i <n; i++)
//            {
//                for (int j = 0; j<n; j++)
//                {
//                    double s;
//                    if (d[k-1][i][k-1] != INFINITY && d[k-1][k-1][j] != INFINITY)
//                    {
//                        s = d[k-1][i][k-1] + d[k-1][k-1][j];
//                    }
//                    else
//                    {
//                        s = INFINITY;
//                    }
//                    if (d[k-1][i][j] <= s)
//                    {
//                        d[k][i][j] = d[k-1][i][j];
//                        p[k][i][j] = p[k - 1][i][j];
//                    }
//                    else
//                    {
//                        d[k][i][j] = s;
//                        p[k][i][j] = p[k-1][k-1][j];
//                    }
//                }
//            }
//        }
//        
//        return new ShortestPathsResultSet(d,p);
//    }
//    private ShortestPathsResultSet getPaths()
//    {
//        if (results != null)
//        {
//            return results;
//        }
//        results = floydWarshall(this.weights);
//        p = results.p;
//        d = results.d;
//        //int[] leastPaths = this.bellmanFord(this, exchangeRateTable, NO_VERTEX);
//        //results = 
//
//        return results;
//    }

//    protected static class ShortestPathsResultSet
//    {
//        public double[][][] d;
//        public int[][][] p;
//        
//        public ShortestPathsResultSet(double[][][] d, int[][][] p)
//        {
//            this.d = d;
//            this.p = p;
//        }
//    }
//    public void printPath(int startIndex, int endIndex, boolean startStack, int src)
//    {
//        double[] path = BellmanFord(startIndex);
//        int penultimate = -1;
//        double temp = INFINITY;
//        for (int i = 0; i < path.length; i++)
//        {
//            if (path[i] < temp)
//            {
//                temp = path[i];
//                penultimate = i;
//            }
//        }
//        if (startIndex != penultimate)
//            printPath(startIndex, penultimate, startStack, src);
//        System.out.println(vertices[endIndex]);
//    }
//    public void printPath(int startIndex, int endIndex, boolean startStack, int src)
//    {
//        double[] path = BellmanFord(startIndex);
//        if (startIndex != endIndex)
//        {
//            printPath(results.p[n][startIndex][startIndex], results.p[n][startIndex][endIndex], false, src);
//        }
//        System.out.print(vertices[results.p[n][startIndex][endIndex]]);
//        if (!startStack)
//        {
//            System.out.print(" -> ");
//        }
//    }
//    public void printConversion(int startIndex, int endIndex, Double amount,  int src)
//    {
//        getPaths();
//        if (startIndex != endIndex)
//        {
//            printConversion(results.p[5][startIndex][startIndex], results.p[5][startIndex][endIndex], (amount), src);
//        }
//        amount = Math.pow(10, results.d[5][startIndex][endIndex]) * amount;
////        amount = results.d[5][startIndex][endIndex] * amount;
//
//        System.out.print(amount + ", ");
//        //if (!startStack)
//        //    System.out.print(" -> ");
//    }
}
