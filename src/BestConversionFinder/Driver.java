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
        /*double[][] rates = {
            {0.0,0.35,0.5 },
            {0.35,0.0,0.25 },
            {0.5,0.25,0.0 },
        };*/
        double[][] rates =
        {
            {
                1, 1.36, 1, 0.87, 7.85, 0.99, 147.98, 1.57, 82.45, 7.3, 5.00E-05
            },
            {
                0.73, 1, 0.74, 0.64, 5.76, 0.73, 108.65, 1.15, 60.53, 5.36, 4.00E-05
            },
            {
                1, 1.36, 1, 0.87, 7.82, 0.99, 147.42, 1.56, 82.13, 7.28, 5.00E-05
            },
            {
                1.15,1.56, 1.15, 1, 8.99, 1.14, 169.57, 1.79, 94.47, 8.37, 6.00E-05
            },
            {
                0.13,0.17, 0.13, 0.11, 1, 0.13, 18.85, 0.2, 10.5, 0.93, 1.00E-05
            },
            {
                1.01,1.37, 1.01, 0.88, 7.89, 1, 148.75, 1.57, 82.88, 7.34, 5.00E-05
            },
            {
                0.01,0.01, 0.01, 0.01, 0.05, 0.01, 1, 0.01, 0.56, 0.05, 0
            },
            {
                0.64,0.87, 0.64, 0.56, 5.01, 0.64, 94.47, 1, 52.64, 4.66, 3.00E-05
            },
            {
                0.01,0.02, 0.01, 0.01, 0.1, 0.01, 1.79, 0.02, 1, 0.09, 0
            },
            {
                0.14,0.19, 0.14, 0.12, 1.07, 0.14, 20.26, 0.21, 11.29, 1, 1.00E-05
            },
            {
                20099.36,27374.32, 20175.56, 17540.37, 157770.76, 19995.1, 2974275.41, 31482.71, 1657102.82, 146823.8,1
            },
        };
        String[] names = {"USD", "CAD", "EUR", "GBP", "HKD", "CHF", "JPY", "AUD", "INR","CNY","BTC"};
        
        
        BestConversionFinder<String> graph = new BestConversionFinder<String>(rates, names);
        
        BestConversionFinder.ShortestPathsResultSet results = BestConversionFinder.floydWarshall(graph);
        for (int i = 0; i < results.d.length; i++)
        {
            for (int k = 0; k < results.d[i].length; k++)
            {
                for (int c = 0; c < results.d[i][k].length; c++)
                    System.out.println(results.d[i][k][c]);
            }
        }
        
        graph.cliMenu();
    }
}
