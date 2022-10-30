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
    int n; // n of vertices
    int e; // n of edges
    SimpleEdge[] edges;
    
    public PureMatrixGraph(double[][] rates, String[] vertices)
    {
        this.n = vertices.length;
        this.e = rates.length * rates[0].length;
        this.rates = rates;
        this.vertices = vertices;
        this.weights = rates.clone();
        for (int i = 0; i < weights.length; i++)
        {
            for (int k =0; k < weights[i].length; k++)
            {
                if (weights[i][k] == 0.0)
                    weights[i][k] = INFINITY;
                else
                    weights[i][k] = log(1/weights[i][k]);
            }
        }
        
        this.edges = new SimpleEdge[e];
        for (int i = 0; i < e;)
        {
            for (int j = 0; j < rates.length; j++)
            {
                for (int k= 0; k < rates.length; k++)
                {
                    edges[i++] = new SimpleEdge(j, k, weights[j][k]);
                }
            }
        }
        
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
    
    public void printPath(int startIndex, int endIndex, boolean startStack, int src)
    {
        double[] path = BellmanFord(startIndex);
        int penultimate = -1;
        double temp = INFINITY;
        for (int i = 0; i < path.length; i++)
        {
            if (path[i] < temp)
            {
                temp = path[i];
                penultimate = i;
            }
        }
        if (startIndex != penultimate)
            printPath(startIndex, penultimate, startStack, src);
        System.out.println(vertices[endIndex]);
    }
    
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

    public void printConversion(int startIndex, int endIndex, Double amount,  int src)
    {
        getPaths();
        if (startIndex != endIndex)
        {
            printConversion(results.p[5][startIndex][startIndex], results.p[5][startIndex][endIndex], (amount), src);
        }
        amount = Math.pow(10, results.d[5][startIndex][endIndex]) * amount;
//        amount = results.d[5][startIndex][endIndex] * amount;

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
                            //printPath(i, k, true, i);
                            double[] newRates = BellmanFord(i);
                            System.out.println("Conversion: " + 1/Math.pow(10, newRates[k]) * amount);
                            //System.out.println();
                            //printConversion(i, k, amount, i);
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
    
    double[] BellmanFord(int s)
    {
        double[] dist = new double[this.n];
        for (int V = 0; V < vertices.length; V++)
        {
            dist[V] = INFINITY;
        }
        dist[s] = 0.0;
        for (int i = 1; i < vertices.length; i++)
        {
            for (int j = 0; j < this.e; j++)
            {
                int u = this.edges[j].src;
                int v = this.edges[j].dest;
                double w = this.edges[j].weight;
                if (dist[u] != Integer.MAX_VALUE && dist[u] + w < dist[v])
                    dist[v] = dist[u] + w;
            }
        }
        for (int j = 0; j < this.e; ++j)
        {
            int u = this.edges[j].src;
            int v = this.edges[j].dest;
            double w = this.edges[j].weight;
            if (dist[u] != Integer.MAX_VALUE && dist[u] + w < dist[v])
            {
                System.out.println("Arbitrage -> (" + vertices[u] +" - " + vertices[v] + ")");
                //return;
            }
        }
        
        System.out.println("Vertex Distance from Source");
        for (int i = 0; i < this.n; ++i)
            System.out.println(i + "\t\t" + dist[i]);
        
        return dist;
    }
    
    private class SimpleEdge
    {
        protected int src;
        protected int dest;
        protected double weight;
        
        SimpleEdge(int src, int dest, double weight)
        {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }
    }
}
