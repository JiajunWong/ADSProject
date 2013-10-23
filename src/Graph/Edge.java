package Graph;

import Util.StdRandom;

public class Edge implements Comparable<Edge>
{
    private final int v;
    private final int w;
    private final int weight;

    public Edge(int v, int w, int weight)
    {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }
    
    public Edge(int v, int w){
//        double weight = (double) (Math.random() * EdgeWeightedGraph.WEIGHT_SIZE);
//        double weight = (double) (StdRandom.uniform() * EdgeWeightedGraph.WEIGHT_SIZE);
        int weight = StdRandom.uniform(EdgeWeightedGraph.WEIGHT_SIZE) + 1;
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public int weight()
    {
        return weight;
    }

    public int either()
    {
        return v;
    }

    public int other(int vertex)
    {
        if (vertex == v)
            return w;
        else if (vertex == w)
            return v;
        else
            throw new IllegalArgumentException();
    }

    @Override
    public int compareTo(Edge edge)
    {
        if (this.weight() > edge.weight())
            return 1;
        else if (this.weight() < edge.weight())
            return -1;
        else
            return 0;
    }

    @Override
    public boolean equals(Object e)
    {
        if (e instanceof Edge)
        {
            Edge edge = (Edge) e;
            if (this.v == edge.v && this.w == edge.w)
            {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Edge: ").append(either()).append(" + ").append(other(either())).append(" : Weight: ").append(weight());
        return sb.toString();
    }
    
}
