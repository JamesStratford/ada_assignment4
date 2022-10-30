/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BestConversionFinder;

import static java.lang.Math.log;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

/**
 *
 * @author jestr
 */
public class ArbitageFinder
{
    public static final double INFINITY = Double.MAX_VALUE;
    private static final int NO_VERTEX = -1;
    private String[] vertices;
    private final double[][] rates;
    public final double[][] weights;
    private Scanner scannerInput;
//    private ShortestPathsResultSet results;
    double[][][] d;
    int[][][] p;
    int n; // n of vertices
    int e; // n of edges
    SimpleEdge[] edges;
    
    public ArbitageFinder(double[][] rates, String[] vertices)
    {
        this.n = vertices.length;
        this.e = rates.length * rates[0].length;
        this.rates = rates;
        this.vertices = vertices;
//        this.weights = rates.clone();
        this.weights = new double[n][n];
        for (int i = 0; i < n; i++)
            System.arraycopy(rates[i], 0, weights[i], 0, n);

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
//        getPaths();
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
    
    public void cliMenu()
    {
        String input = "";
        do
        {
            System.out.println("type QUIT to exit");
           
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
                            BellmanFordResultSet newRates = bellmanFord(i);
                            printBellman(i, k, newRates);
                            //System.out.println("Conversion: " + 1/Math.pow(10, newRates.d[k]) * amount);
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
    
//    void printBellman(int src, int target, BellmanFordResultSet result)
//    {
//        if (src != target && !result.negativeCycles.contains(new SimpleEdge(src, target, INFINITY)))
//        {
//            printBellman(src, result.p[target], result);
//        }
//        System.out.println(vertices[target] + " -> ");
//    }
    
    void printBellman(int src, int target, BellmanFordResultSet result)
    {
        HashMap<String, Integer> visited = new HashMap<>();
        System.out.println("Best conversion rate: " + Math.max(rates[src][target], 1/Math.pow(10, result.d[target])));

        while (src != target)
        {
            System.out.print(vertices[target] + " <- ");
            target = result.p[target];
            if (visited.containsKey(vertices[target]))
            {
                if (!result.negativeCycles.contains(new SimpleEdge(src,target, weights[src][target])))
                {
                    System.out.println(vertices[src]);
                    System.out.println("Arbitage detected (" + vertices[src] +"," + vertices[target] +  ") : ending cycle");
                    return;
                }
                visited.put(vertices[target], visited.get(vertices[target]) + 1);
            }
            else
                visited.put(vertices[target], 1);
        }
        System.out.println(vertices[src]);
    }

    
    BellmanFordResultSet bellmanFord(int s)
    {
        double[] dist = new double[this.n];
        int[] penult = new int[this.n];
        for (int v = 0; v < vertices.length; v++)
        {
            dist[v] = INFINITY;
            penult[v] = -1;
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
                {
                    dist[v] = dist[u] + w;
                    penult[v] = u;
                }
            }
        }
        HashSet<SimpleEdge> neg = new HashSet<SimpleEdge>();
        for (int j = 0; j < this.e; ++j)
        {
            int u = this.edges[j].src;
            int v = this.edges[j].dest;
            double w = this.edges[j].weight;
            if (dist[u] != Integer.MAX_VALUE && dist[u] + w < dist[v])
            {
                neg.add(this.edges[j]);
                System.out.println("Arbitrage -> (" + vertices[u] +" - " + vertices[v] + ") - " + (dist[u] + w));
                //return;
            }
        }
        
        return new BellmanFordResultSet(s, penult, dist, neg);
    }
    
    private class BellmanFordResultSet
    {
        int src;
        int[] p;
        double[] d;
        HashSet<SimpleEdge> negativeCycles;
        
        BellmanFordResultSet(int src, int[] p, double[] d, HashSet<SimpleEdge> negativeCycles)
        {
            this.src = src;
            this.p= p;
            this.d = d;
            this.negativeCycles = negativeCycles;
        }
        
        @Override
        public boolean equals(Object o)
        {
            return this.src == (int)o;
        }
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
        
        @Override
        public boolean equals(Object o)
        {
            return (((SimpleEdge)o).src == this.src && ((SimpleEdge)o).dest == this.dest);
        }
    }
}
