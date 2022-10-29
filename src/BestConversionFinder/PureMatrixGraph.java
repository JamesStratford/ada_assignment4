/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BestConversionFinder;

import static java.lang.Math.log;
import java.util.Scanner;

/**
 *
 * @author jestr
 */
public class PureMatrixGraph
{
    public static final double INFINITY = Double.MAX_VALUE;
    private static final int NO_VERTEX = -1;
    private String[] vertices;
    private final double[][] rates;
    public final double[][] weights;
    private Scanner scannerInput;
    private ShortestPathsResultSet results;
    double[][][] d;
    int[][][] p;
    int n;
    
    public PureMatrixGraph(double[][] rates, String[] vertices)
    {
        this.n = vertices.length;
        this.rates = rates;
        this.vertices = vertices;
        this.weights = rates.clone();
//        for (int i = 0; i < weights.length; i++)
//        {
//            for (int k =0; k < weights[i].length; k++)
//            {
//                weights[i][k] = log(1/weights[i][k]);
//            }
//        }
        
        scannerInput = new Scanner(System.in);
        getPaths();
    }
    
    public static ShortestPathsResultSet floydWarshall(double[][] rates)
    {
        double[][] weights = rates;
        int n = weights.length;
        double[][][] d = new double[n+1][][];
        int[][][] p = new int[n+1][][];
       
        d[0] = weights;
        p[0] = new int[n][n];
        for (int i = 0; i < n; i++)
        {
            for (int j =0; j < n; j++)
            {
                if (weights[i][j] < INFINITY)
                {
                    p[0][i][j] = i;
                }
                else
                {
                    p[0][i][j] = NO_VERTEX;
                }
            }
        }
        
        for (int k = 1; k <= n; k++)
        {
            d[k] = new double[n][n];
            p[k] = new int[n][n];
            for (int i = 0; i <n; i++)
            {
                for (int j = 0; j<n; j++)
                {
                    double s;
                    if (d[k-1][i][k-1] != INFINITY && d[k-1][k-1][j] != INFINITY)
                    {
                        s = d[k-1][i][k-1] + d[k-1][k-1][j];
                    }
                    else
                    {
                        s = INFINITY;
                    }
                    if (d[k-1][i][j] <= s)
                    {
                        d[k][i][j] = d[k-1][i][j];
                        p[k][i][j] = p[k - 1][i][j];
                    }
                    else
                    {
                        d[k][i][j] = s;
                        p[k][i][j] = p[k-1][k-1][j];
                    }
                }
            }
        }
        
        return new ShortestPathsResultSet(d,p);
    }
    
    public String toString()
    {
        String output = "Shortest lengths\n";
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                if (d[n][i][j] != INFINITY)
                {
                    output += ("\t" + d[n][i][j]);
                } else
                {
                    output += "\tinfin";
                }
            }
            output += "\n";
        }
        output += "Previous vertices on shortest paths\n";
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                if (p[n][i][j] != NO_VERTEX)
                {
                    output += ("\t" + p[n][i][j]);
                } else
                {
                    output += "\tnull";
                }
            }
            output += "\n";
        }
        return output;
    }
    
    private ShortestPathsResultSet getPaths()
    {
        if (results != null)
        {
            return results;
        }
        results = floydWarshall(this.weights);
        p = results.p;
        d = results.d;
        //int[] leastPaths = this.bellmanFord(this, exchangeRateTable, NO_VERTEX);
        //results = 

        return results;
    }
    
    public void printPath(int startIndex, int endIndex, boolean startStack)
    {
        getPaths();
        if (startIndex != endIndex)
        {
            printPath(results.p[startIndex][startIndex][startIndex], results.p[startIndex][startIndex][endIndex], false);
        }
        System.out.print(vertices[results.p[startIndex][endIndex][startIndex]]);
        if (!startStack)
        {
            System.out.print(" -> ");
        }
    }

    public void printConversion(int startIndex, int endIndex, Double amount)
    {
        getPaths();
        if (startIndex != endIndex)
        {
            printConversion(results.p[startIndex][startIndex][startIndex], results.p[startIndex][startIndex][endIndex], (amount));
        }
        amount =results.d[startIndex][startIndex][endIndex] * amount;
        System.out.print(amount + ", ");
        //if (!startStack)
        //    System.out.print(" -> ");
    }
    
    public void cliMenu()
    {
        String input = "";
        do
        {
            System.out.println("type QUIT to exit");
            System.out.print("Type a real number to represent an amount of currency: ");
            double amount = scannerInput.nextDouble();
            
            System.out.println("Type a currency followed by another currency to get best path");
            input = scannerInput.next();
            for (int i = 0; i < vertices.length; i++)
            {
                if (vertices[i].equals(input))
                {
                    input = scannerInput.next();
                    for (int k = 0; k < vertices.length; k++)
                    {
                        if (vertices[k].equals(input))
                        {
                            System.out.println("Path : ");
                            printPath(i, k, true);
                            System.out.println();
                            printConversion(i, k, amount);
                            System.out.println();
                            break;
                        }
                    }
                    break;
                }
            }
        } while (!input.equals("QUIT"));
    }
    
    protected static class ShortestPathsResultSet
    {
        public double[][][] d;
        public int[][][] p;
        
        public ShortestPathsResultSet(double[][][] d, int[][][] p)
        {
            this.d = d;
            this.p = p;
        }
    }
}
