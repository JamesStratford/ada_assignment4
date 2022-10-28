/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BestConversionFinder;

import Graph.AdjacencyListGraph;
import Graph.Edge;
import Graph.Vertex;
import static java.lang.Math.log;
import java.util.Scanner;

/**
 *
 * @author jestr
 * @param <E>
 */
public class BestConversionFinder<E> extends AdjacencyListGraph<E>
{
    public static final double INFINITY = Double.MAX_VALUE;
    private static final int NO_VERTEX = -1;
    private final double[][] exchangeRateTable;
    private final ConversionFinderVertex[] verticesTable;
    private Scanner scannerInput;
    private ShortestPathsResultSet results;
    
    public BestConversionFinder(double[][] rates, E[] vertexNames)
    {
        this.exchangeRateTable = rates;
        this.verticesTable = (ConversionFinderVertex[]) new Object[exchangeRateTable.length];
        for (int i =0; i < exchangeRateTable.length; i++)
        {
            this.verticesTable[i] = this.addVertex(vertexNames[i]); // vertexNames must be in same order of exchangeRateTable
            this.verticesTable[i].index = i;
        }
        for (int i = 0; i < exchangeRateTable.length; i++)
        {
            for (int k = 0; k < exchangeRateTable[i].length; k++)
            {
                this.addEdge(this.verticesTable[i], this.verticesTable[k], exchangeRateTable[i][k]);
            }
        }
        
        
        scannerInput = new Scanner(System.in);
    }
    
       // adds and returns a new isolated vertex to the graph
    @Override
    public ConversionFinderVertex addVertex(E element)
    {
        ConversionFinderVertex vertex = new ConversionFinderVertex(element, 0);
        addVertex(vertex);
        return vertex;
    }
    
    public void printPath(int startIndex, int endIndex, boolean startStack)
    {
        getPaths();
        if (startIndex != endIndex)
            printPath(results.p[startIndex][startIndex][startIndex], results.p[startIndex][startIndex][endIndex], false);
        System.out.print(verticesTable[results.p[startIndex][endIndex][startIndex]].getUserObject());
        if (!startStack)
            System.out.print(" -> ");
    }
    
    public void printConversion(int startIndex, int endIndex, Double amount)
    {
        getPaths();
        if (startIndex != endIndex)
            printConversion(results.p[startIndex][startIndex][startIndex], results.p[startIndex][startIndex][endIndex], (amount));
        amount = results.d[startIndex][startIndex][endIndex] * amount;
        System.out.print(amount + ", ");
        //if (!startStack)
        //    System.out.print(" -> ");
    }
    
    
    private ShortestPathsResultSet getPaths()
    {
        if (results != null)
        {
            return results;
        }
        //results = floydWarshall(this);
        int[] leastPaths = this.bellmanFord(this, exchangeRateTable, NO_VERTEX);
        //results = 

        return results;
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
            for (int i = 0; i < verticesTable.length; i++)
            {
                if (verticesTable[i].getUserObject().equals(input))
                {
                    input = scannerInput.next();
                    for (int k = 0; k < verticesTable.length; k++)
                    {
                        if (verticesTable[k].getUserObject().equals(input))
                        {
                            System.out.println("Path : ");
                            //printPath(i, k, true);
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

    
    public Edge<E> addEdge(ConversionFinderVertex vertex0, ConversionFinderVertex vertex1, double weighting)
    {  // first add the end vertices if not already in graph
        if (!containsVertex(vertex0))
        {
            addVertex(vertex0);
        }
        if (!containsVertex(vertex1))
        {
            addVertex(vertex1);
        }
        // create the new edge
        Edge<E> edge = new ConversionFinderEdge(vertex0, vertex1, weighting);
        edges.add(edge);
        // update the adjacency list for one or both end vertices
        adjacencyLists.get(vertex0).add(edge);
        if (type == GraphType.UNDIRECTED) // add the reverse edge 
        {
            adjacencyLists.get(vertex1).add(edge);
        }
        return edge;
    }
    
    public int[] bellmanFord(BestConversionFinder G, double[][] w, int s)
    {
        double[] d = new double[w.length];
        int[] leastEdge = new int[w.length];
        ConversionFinderEdge[] edgeSet = (ConversionFinderEdge[]) G.edgeSet().toArray();
        int E = edgeSet.length;
        for (int v = 0; v < G.verticesTable.length; v++)
        {
            d[v] = INFINITY;
            leastEdge[v] = -1;
        }
        d[s] = 0;
        int n = G.verticesTable.length;
        for (int i = 1; i < n-1; i++)
        {
            for (int k = 0; k< E; k++)
            {
                int u = edgeSet[k].vertex1.index;
                int v = edgeSet[k].vertex2.index;
                double weight = edgeSet[k].weighting;
                if (d[u] + weight < d[v])
                {
                    d[v] = d[u] + weight;
                    leastEdge[v] = u;
                }
            }
        }
        for (int k = 1; k < E; k++)
        {
            int u = edgeSet[k].vertex1.index;
            int v = edgeSet[k].vertex2.index;
            double weight = edgeSet[k].weighting;
            if (d[u] != Integer.MAX_VALUE
                    && d[u] + weight < d[v])
            {
                System.out.println(
                        "Graph contains negative weight cycle");
                return null;
            }
        }
        
        return leastEdge;
    }
    
    public static ShortestPathsResultSet floydWarshall(BestConversionFinder graph)
    {
        double[][] weights = graph.exchangeRateTable;
        int n = weights.length;
        double[][][] d = new double[n+1][][];
        int[][][] p = new int[n+1][][];
        for (int i = 0; i < weights.length; i++)
        {
            for (int k =0; k < weights[i].length; k++)
            {
                weights[i][k] = log(1/weights[i][k]);
            }
        }
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
    
    protected class ConversionFinderVertex extends AdjacencyListVertex
    {
        int index;
        
        public ConversionFinderVertex(E element, int index)
        {
            super(element);
            this.index = index;
        }
    }
    
    protected class ConversionFinderEdge implements Edge<E>
    {
        // for a directed graph edge is from vertex1 to vertex2
        
        private ConversionFinderVertex vertex1, vertex2;
        private double weighting;

        public ConversionFinderEdge(ConversionFinderVertex vertex1, ConversionFinderVertex vertex2, double weighting)
        {
            this.vertex1 = vertex1;
            this.vertex2 = vertex2;
            this.weighting = weighting;
        }
        
        public double getWeight()
        {
            return weighting;
        }

        // returns the two end vertices for this edge as an array
        public Vertex<E>[] endVertices()
        {
            Vertex<E>[] vertices = (Vertex<E>[]) (new Vertex[2]);//unchecked
            vertices[0] = vertex1;
            vertices[1] = vertex2;
            return vertices;
        }

        // returns the end vertex opposite the specified vertex
        public Vertex<E> oppositeVertex(Vertex<E> vertex)
        {
            if (vertex1.equals(vertex))
            {
                return vertex2;
            } else
            {
                return vertex1;
            }
        }

        public String toString()
        {
            return "(" + vertex1 + "-" + vertex2 + ")";
        }
    }
}
