package BestConversionFinder;

import Graph.AdjacencyListGraph;
import Graph.Edge;
import Graph.GraphADT;
import Graph.Vertex;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
    From my brief understanding, the weights have been added as a map to each edge, 
    so that we can determine weights similar to the bellman ford example in the lectures
*/

public class BestConversionFinderYeran<E>
{
    private GraphADT<E> graph;
    private Map<Edge<E>,Float> weights;

    public BestConversionFinderYeran(GraphADT<E> graph, Map<Edge<E>,Float> weights)
    {  
        this.graph = graph;
        this.weights = weights;
    }
    
    public Set<Edge<E>> getShortestPathsTree(Vertex<E> source)
    {
        Map<Vertex<E>,Edge<E>> leastEdges = new HashMap<Vertex<E>,Edge<E>>();
        final Map<Vertex<E>,Float> ValuablePathEstimates = new HashMap<Vertex<E>,Float>();

        for (Vertex v : graph.vertexSet())
        {
            //System.out.println("putting " + 0 + "into " + v);
            ValuablePathEstimates.put(v, (float)(-Math.log(0)));
            leastEdges.put(v, null);
        }
        
        ValuablePathEstimates.put(source, (0.0f));
        int n = graph.vertexSet().size();
        
        for (int i = 1; i < n; i++)
        {
            for (Edge e : graph.edgeSet())
            {
                Vertex start = e.endVertices()[0];
                Vertex end = e.endVertices()[1];
                float value1Toln = ValuablePathEstimates.get(start);
                float value2Toln = weights.get(e);
                float value3Toln = ValuablePathEstimates.get(end);
                
                float placeholderValue1 = value1Toln;
                float placeholderValue2 = (float)(Math.log(1/value2Toln));
                float placeholderValue3 = value3Toln;

                System.out.println("comparing " + start + " against " + end);
                System.out.println("Comparing " + value1Toln + " + " + value2Toln + " against " + value3Toln);
                System.out.println("Comparing " + placeholderValue1 + " + " + placeholderValue2 + " against " + placeholderValue3);
                if (((placeholderValue1 + placeholderValue2) < placeholderValue3) && (placeholderValue3 != 0.0f))
                {
                    System.out.println("  ~adding " + start + " against " + end);
                    if (end != source)
                    {
                        ValuablePathEstimates.put(end, (ValuablePathEstimates.get(start) + placeholderValue2));
                        leastEdges.put(end, e);
                    }
                }
            }
        }
        

        for (Edge e : graph.edgeSet())
        {
            Vertex u = e.endVertices()[0];
            Vertex v = e.endVertices()[1];
            if (ValuablePathEstimates.get(u) + weights.get(e) < ValuablePathEstimates.get(v))
            {
                return null;
            }
        }
        
        return new HashSet(leastEdges.values());
    }
    
    /*
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
    */
    
    public static void main(String[] args)
    {  
        GraphADT<String> graph = new AdjacencyListGraph<String>(GraphADT.GraphType.DIRECTED);
        
        String[] names = {"USD", "CAD", "EUR", "GBP", "HKD", "CHF", "JPY", "AUD", "INR","CNY","BTC"};
        Vertex<String> USD = graph.addVertex("USD");
        Vertex<String> CAD = graph.addVertex("CAD");
        Vertex<String> EUR = graph.addVertex("EUR");
        Vertex<String> GBP = graph.addVertex("GBP");
        /*
        Vertex<String> HKD = graph.addVertex("HKD");
        Vertex<String> CHF = graph.addVertex("CHF");
        Vertex<String> JPY = graph.addVertex("JPY");
        Vertex<String> AUD = graph.addVertex("AUD");
        Vertex<String> INR = graph.addVertex("INR");
        Vertex<String> CNY = graph.addVertex("CNY");
        Vertex<String> BTC = graph.addVertex("BTC");
        */

       Edge<String> USDCAD = graph.addEdge(USD, CAD);
       Edge<String> USDEUR = graph.addEdge(USD, EUR);
       Edge<String> CADGBP= graph.addEdge(CAD, GBP);
       Edge<String> EURGBP = graph.addEdge(EUR, GBP);
       Edge<String> CADUSD = graph.addEdge(CAD, USD);
       Edge<String> EURUSD = graph.addEdge(EUR, USD);
       Edge<String> GBPCAD = graph.addEdge(GBP, CAD);
       Map<Edge<String>,Float> weights = new HashMap<Edge<String>,Float>();
       weights.put(USDCAD, 1.36f);
       weights.put(USDEUR, 1.0f);
       weights.put(CADGBP, 0.64f);
       weights.put(EURGBP, 0.87f);
       weights.put(CADUSD, 0.75f);
       weights.put(EURUSD, 1.0f);
       weights.put(GBPCAD, 1.56f);
       System.out.println("Example Graph:\n" + graph);
       BestConversionFinderYeran<String> Bellman = new BestConversionFinderYeran<String>(graph, weights);
       System.out.println("Finding shortest paths tree from a");
       System.out.print("Edges:");
       
       /*
            This will find the lowest transfer rate, from what I understand, from the source
            So the lowest rate from USD to each currency will be determined
       
            The example with this brief thing, is that 
       */
      
       /*
        System.out.println("\n\nNew shortestpath for USD:");
       for (Edge<String> edge : Bellman.getShortestPathsTree(USD))
           System.out.print(" "+edge);
       System.out.println();
       */
       
        System.out.println("\n\nNew shortestpath for EUR:");
       for (Edge<String> edge : Bellman.getShortestPathsTree(EUR))
           System.out.print(" "+edge);
       System.out.println();
       
   }
}
