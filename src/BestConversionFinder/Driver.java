/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BestConversionFinder;

import BestConversionFinder.BestConversionFinder.ConversionFinderEdge;
import Graph.Edge;

/**
 *
 * @author jestr
 */
public class Driver
{
    public static void main(String[] args)
    {
        double[][] rates = {
            {0.0,0.35,0.5 },
            {0.35,0.0,0.25 },
            {0.5,0.25,0.0 },
        };
        String[] names = {"NZD","USD","AUD"};
        
        BestConversionFinder<String> graph = new BestConversionFinder<String>(rates, names);
        
        System.out.println(graph);
        for (Edge<String> x : graph.edgeSet())
        {
            System.out.println(((ConversionFinderEdge)x).getWeight());
            
        }
        
        BestConversionFinder.ShortestPathsResultSet results = BestConversionFinder.floydWarshall(graph);
        for (int i = 0; i < results.d.length; i++)
        {
            for (int k = 0; k < results.d[i].length; k++)
            {
                System.out.println(results.d[i][k][k]);
            }
        }
    }
}
