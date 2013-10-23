package Graph;

import java.util.LinkedList;

public class EdgeWeightedGraph
{
    public static final int WEIGHT_SIZE = 1000;
    private final int V;
    private int E;
    private LinkedList<Edge>[] adj;

    @SuppressWarnings("unchecked")
    public EdgeWeightedGraph(int V)
    {
        if (V < 0)
        {
            throw new IllegalArgumentException();
        }
        this.V = V;
        this.E = 0;
        adj = (LinkedList<Edge>[]) new LinkedList[V];
        for (int i = 0; i < V; i++)
        {
            adj[i] = new LinkedList<Edge>();
        }
    }

    public EdgeWeightedGraph(int V, int E)
    {
        this(V);
        if (E < 0)
            throw new IllegalArgumentException();
        for (int i = 0; i < E; i++)
        {
            int v = (int) (Math.random() * V);
            int w = (int) (Math.random() * V);
            Edge e = new Edge(v, w);
            addEdge(e);
        }
    }

    public int V()
    {
        return V;
    }

    public int E()
    {
        return E;
    }

    public void addEdge(Edge e)
    {
        int v = e.either();
        int w = e.other(v);
        E++;
        adj[v].add(e);
        adj[w].add(e);
    }
    
    public void addEdge(int v, int w)
    {
        Edge e = new Edge(v, w);
        addEdge(e);
    }

    public Iterable<Edge> adj(int v)
    {
        if (v < 0 || v > V)
        {
            throw new IllegalArgumentException();
        }
        return adj[v];
    }

}
