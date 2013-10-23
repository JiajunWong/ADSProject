import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import DataStructure.FibonacciHeap;
import DataStructure.FibonacciHeapNode;
import Graph.Edge;
import Graph.EdgeWeightedGraph;

public class MST
{
    public static enum SchemType
    {
        SIMPLE_SCHEME, F_HEAP_SCHEME;
    }

    private Edge[] edgeTo;
    private int[] distTo;
    private boolean[] marked;
    private HashMap<Integer, Integer> array;
    private FibonacciHeap<Integer> fibonacciHeap;

    public MST(EdgeWeightedGraph edgeWeightedGraph, SchemType type)
    {
        edgeTo = new Edge[edgeWeightedGraph.V()];
        distTo = new int[edgeWeightedGraph.V()];
        marked = new boolean[edgeWeightedGraph.V()];
        for (int v = 0; v < edgeWeightedGraph.V(); v++)
        {
            distTo[v] = Integer.MAX_VALUE;
            marked[v] = false;
        }
        if (type == SchemType.F_HEAP_SCHEME)
        {
            fibonacciHeap = new FibonacciHeap<Integer>();
            for (int i = 0; i < edgeWeightedGraph.V(); i++)
            {
                if (!marked[i])
                {
                    FiPrim(edgeWeightedGraph, i);
                }
            }
        }
        else if (type == SchemType.SIMPLE_SCHEME)
        {
            array = new HashMap<Integer, Integer>();
            SiPrim(edgeWeightedGraph, 0);
        }
        else
        {
            throw new IllegalArgumentException();
        }

        //TODO need assert check attribute;
    }

    private void SiPrim(EdgeWeightedGraph edgeWeightedGraph, int i)
    {
        distTo[i] = 0;
        array.put(i, distTo[i]);
        while (!array.isEmpty())
        {
            int sourceV = getMin(array);
            SiScan(edgeWeightedGraph, sourceV);
        }
    }

    private void SiScan(EdgeWeightedGraph edgeWeightedGraph, int v)
    {
        marked[v] = true;
        for (Edge e : edgeWeightedGraph.adj(v))
        {
            int w = e.other(v);
            if (marked[w])
            {
                continue;
            }
            if (e.weight() < distTo[w])
            {
                distTo[w] = e.weight();
                edgeTo[w] = e;
                array.put(w, distTo[w]);
            }
        }
    }

    private int getMin(HashMap<Integer, Integer> arrayList)
    {
        int value = Integer.MAX_VALUE;
        int index = Integer.MAX_VALUE;
        Iterator<Integer> it = arrayList.keySet().iterator();
        while (it.hasNext())
        {
            int key = it.next();
            if (arrayList.get(key) < value)
            {
                value = arrayList.get(key);
                index = key;
            }
        }
        arrayList.remove(index);
        return index;
    }

    private void FiPrim(EdgeWeightedGraph edgeWeightedGraph, int s)
    {
        distTo[s] = 0;
        FibonacciHeapNode<Integer> source = new FibonacciHeapNode<Integer>(s);
        fibonacciHeap.insert(source, distTo[s]);
        while (!fibonacciHeap.isEmpty())
        {
            FibonacciHeapNode<Integer> minNode = fibonacciHeap.removeMin();
            FiScan(edgeWeightedGraph, minNode);
        }
    }

    private void FiScan(EdgeWeightedGraph edgeWeightedGraph, FibonacciHeapNode<Integer> minNode)
    {

    }

    public Iterable<Edge> edges()
    {
        ArrayList<Edge> edges = new ArrayList<Edge>();
        for (int v = 0; v < edgeTo.length; v++)
        {
            Edge edge = edgeTo[v];
            if (edge != null)
            {
                edges.add(edge);
            }
        }
        return edges;
    }

    public int weight()
    {
        int weight = 0;
        for (Edge e : edges())
        {
            weight = +e.weight();
        }

        return weight;
    }

    public static void main(String[] args)
    {
        EdgeWeightedGraph edgeWeightedGraph = GraphFactory.simple(1000, 0.5);
        for(int i = 0; i < edgeWeightedGraph.V(); i++){
            for(Edge e : edgeWeightedGraph.adj(i)){
                System.out.println(":::"+ e);
            }
        }
        MST mst = new MST(edgeWeightedGraph, SchemType.SIMPLE_SCHEME);
        for (Edge e : mst.edges())
        {
            System.out.println(e);
        }
    }
}
