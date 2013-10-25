package Graph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class EdgeWeightedGraph
{
    public static final int WEIGHT_SIZE = 1000;
    private int V;
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

    @SuppressWarnings("unchecked")
    public EdgeWeightedGraph(String fileName)
    {
        System.out.println("Read Graph From: " + fileName);
        System.out.println();
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            Scanner scanner;
            scanner = new Scanner(reader.readLine());
            V = scanner.nextInt();
            E = 0;
            int numEdge = scanner.nextInt();
            adj = (LinkedList<Edge>[]) new LinkedList[V];
            for (int i = 0; i < V; i++)
            {
                adj[i] = new LinkedList<Edge>();
            }
            for (int j = 0; j < numEdge; j++)
            {
                String s = reader.readLine();
                if (s != "")
                {
                    scanner = new Scanner(s);
                    int v = scanner.nextInt();
                    int w = scanner.nextInt();
                    int weight = scanner.nextInt();
                    Edge e = new Edge(v, w, weight);
                    System.out.println(e.toString());
                    addEdge(e);
                }
                scanner.close();
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
