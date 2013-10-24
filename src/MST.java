import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import Util.GraphFactory;

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
            long start = 0, stop = 0;
            start = System.currentTimeMillis();

            fibonacciHeap = new FibonacciHeap<Integer>();
            FiPrim(edgeWeightedGraph, 0);

            stop = System.currentTimeMillis();
            long time = stop - start;
            System.out.println("**************************************");
            System.out.println("FibonacciHeap running time: " + time);
        }
        else if (type == SchemType.SIMPLE_SCHEME)
        {
            long start = 0, stop = 0;
            start = System.currentTimeMillis();

            array = new HashMap<Integer, Integer>();
            SiPrim(edgeWeightedGraph, 0);

            stop = System.currentTimeMillis();
            long time = stop - start;
            System.out.println("Simple running time: " + time);
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
            FibonacciHeapNode<Integer> minNode = fibonacciHeap.extractMin();
            FiScan(edgeWeightedGraph, minNode);
        }
    }

    private void FiScan(EdgeWeightedGraph edgeWeightedGraph, FibonacciHeapNode<Integer> node)
    {
        int v = node.getIndex();
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
                FibonacciHeapNode<Integer> newNode = new FibonacciHeapNode<Integer>(w);
                if (fibonacciHeap.contains(newNode))
                {
                    fibonacciHeap.decreaseKey(newNode, distTo[w]);
                }
                else
                {
                    fibonacciHeap.insert(newNode, distTo[w]);

                }
            }
        }
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
            weight = weight + e.weight();
        }

        return weight;
    }

    private static boolean isEqual(MST mstFi, MST mstSi)
    {
        if (mstFi.weight() == mstSi.weight())
        {
            System.out.println("**************************************");
            System.out.println("Fiweight: " + mstFi.weight());
            System.out.println("Siweight: " + mstSi.weight());
            return true;
        }
        return false;
    }

    public static void main(String[] args)
    {
        EdgeWeightedGraph edgeWeightedGraph = null;
        if (args != null)
        {
            if (args.length == 3)
            {
                String value1 = args[1];
                String value2 = args[2];
                Integer numNodes = new Integer(value1);
                Double density = new Double(value2);
                density = density / 100;
                edgeWeightedGraph = GraphFactory.simple(numNodes, density);
            }
            else if (args.length == 2)
            {
                String fileName = args[1];
                edgeWeightedGraph = new EdgeWeightedGraph(fileName);
            }
            MST mstFi = new MST(edgeWeightedGraph, SchemType.F_HEAP_SCHEME);
            MST mstSi = new MST(edgeWeightedGraph, SchemType.SIMPLE_SCHEME);
            //                assert isEqual(mstFi, mstSi);
            if (isEqual(mstFi, mstSi))
            {
                System.out.println("**************************************");
                System.out.println("NOTE: Two Prim's weights are equal!!!");
                System.out.println("**************************************");
            }
        }
        else
        {
            System.out.println("************************************************");
            System.out.println("*Please Enter Values:                          *");
            System.out.println("************************************************");
        }
    }
}
