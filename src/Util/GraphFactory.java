package Util;

import java.util.HashSet;

import DataStructure.MinPQ;
import Graph.Edge;
import Graph.EdgeWeightedGraph;

public class GraphFactory
{

    public static EdgeWeightedGraph tree(int V)
    {
        EdgeWeightedGraph edgeWeightedGraph = new EdgeWeightedGraph(V);

        if (V == 1)
            return edgeWeightedGraph;

        int[] prufer = new int[V - 2];
        for (int i = 0; i < prufer.length; i++)
        {
            prufer[i] = (int) (Math.random() * V);
        }
        int[] degree = new int[V];
        for (int i = 0; i < degree.length; i++)
        {
            degree[i] = 1;
        }
        for (int i = 0; i < prufer.length; i++)
        {
            degree[prufer[i]]++;
        }
        MinPQ<Integer> minPQ = new MinPQ<Integer>();
        for (int i = 0; i < V; i++)
        {
            if (degree[i] == 1)
            {
                minPQ.insert(i);
            }
        }
        for (int i = 0; i < V - 2; i++)
        {
            int v = minPQ.delMin();
            edgeWeightedGraph.addEdge(v, prufer[i]);
            degree[v]--;
            degree[prufer[i]]--;
            if (degree[prufer[i]] == 1)
            {
                minPQ.insert(prufer[i]);
            }
        }
        edgeWeightedGraph.addEdge(minPQ.delMin(), minPQ.delMin());
        return edgeWeightedGraph;
    }

    public static EdgeWeightedGraph simple(int V, int E)
    {
        EdgeWeightedGraph edgeWeightedGraph = tree(V);
        int newE = E - V + 1;
        if (E > (long) V * (V - 1) / 2 || newE < 0)
        {
            throw new IllegalArgumentException();
        }

        HashSet<Edge> edges = new HashSet<Edge>();
        while (edgeWeightedGraph.E() < newE)
        {
            int v = (int) (Math.random() * V);
            int w = (int) (Math.random() * V);
            if ((v != w))
            {
                Edge e = new Edge(v, w);
                if (!edges.contains(e))
                {
                    edges.add(e);
                    edgeWeightedGraph.addEdge(e);
                }
            }
        }

        return edgeWeightedGraph;
    }

    public static EdgeWeightedGraph simple(int V, double density)
    {
        long maxEdges = V * (V - 1) / 2;
        int E = (int) (maxEdges * density);
        EdgeWeightedGraph edgeWeightedGraph = simple(V, E);
        return edgeWeightedGraph;
    }
}
