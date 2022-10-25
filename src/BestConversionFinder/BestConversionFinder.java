/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BestConversionFinder;

import Graph.AdjacencyListGraph;
import Graph.Edge;
import Graph.Vertex;

/**
 *
 * @author jestr
 * @param <E>
 */
public class BestConversionFinder<E> extends AdjacencyListGraph<E>
{
    public static final double INFINITY = Double.MAX_VALUE;
    private static final int NO_VERTEX = -1;
    private double[][] exchangeRateTable;
    private Vertex[] vertices;
    
    public BestConversionFinder(double[][] rates, E[] vertexNames)
    {
        this.exchangeRateTable = rates;
        this.vertices = new Vertex[exchangeRateTable.length];
        for (int i =0; i < exchangeRateTable.length; i++)
        {
            this.vertices[i] = this.addVertex(vertexNames[i]); // vertexNames must be in same order of exchangeRateTable
        }
        for (int i = 0; i < exchangeRateTable.length; i++)
        {
            for (int k = 0; k < exchangeRateTable[i].length; k++)
            {
                this.addEdge(this.vertices[i], this.vertices[k], exchangeRateTable[i][k]);
            }
        }
    }
    
    public Edge<E> addEdge(Vertex<E> vertex0, Vertex<E> vertex1, double weighting)
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
    
    public static ShortestPathsResultSet floydWarshall(BestConversionFinder graph)
    {
        double[][] weights = graph.exchangeRateTable;
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
    
    protected class ConversionFinderEdge implements Edge<E>
    {
        // for a directed graph edge is from vertex1 to vertex2
        
        private Vertex<E> vertex1, vertex2;
        private double weighting;

        public ConversionFinderEdge(Vertex<E> vertex1, Vertex<E> vertex2, double weighting)
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
