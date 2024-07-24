import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

public class SAP {
    private final Digraph digraph;
    private final int numVertices;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        this.digraph = G;
        this.numVertices = digraph.V();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || w < 0 || v >= numVertices || w >= numVertices) throw new IllegalArgumentException();

        BreadthFirstDirectedPaths bfsFromV = new BreadthFirstDirectedPaths(this.digraph, v);
        BreadthFirstDirectedPaths bfsFromW = new BreadthFirstDirectedPaths(this.digraph, w);

        int shortestLength = Integer.MAX_VALUE;

        for (int vertex = 0; vertex < this.numVertices; vertex++){
            if (bfsFromV.hasPathTo(vertex) && bfsFromW.hasPathTo(vertex)){
                int sumDist = bfsFromV.distTo(vertex) + bfsFromW.distTo(vertex);
                if (sumDist < shortestLength){
                    shortestLength = sumDist;
                }
            }
        }
        if (shortestLength == Integer.MAX_VALUE) return -1;
        return shortestLength;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || w < 0 || v >= numVertices || w >= numVertices) throw new IllegalArgumentException();

        BreadthFirstDirectedPaths bfsFromV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsFromW = new BreadthFirstDirectedPaths(digraph, w);

        int shortestLength = Integer.MAX_VALUE;
        int ancestor = -1;

        for (int vertex = 0; vertex < digraph.V(); vertex++){
            if (bfsFromV.hasPathTo(vertex) && bfsFromW.hasPathTo(vertex)){
                int sumDist = bfsFromV.distTo(vertex) + bfsFromW.distTo(vertex);
                if (sumDist < shortestLength){
                    ancestor = vertex;
                    shortestLength = sumDist;
                }
            }
        }

        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();

        BreadthFirstDirectedPaths bfsFromV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsFromW = new BreadthFirstDirectedPaths(digraph, w);

        int shortestLength = Integer.MAX_VALUE;

        for (int vertex = 0; vertex < this.numVertices; vertex++){
            if (bfsFromV.hasPathTo(vertex) && bfsFromW.hasPathTo(vertex)){
                int sumDist = bfsFromV.distTo(vertex) + bfsFromW.distTo(vertex);
                if (sumDist < shortestLength){
                    shortestLength = sumDist;
                }
            }
        }
        return shortestLength;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();

        BreadthFirstDirectedPaths bfsFromV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsFromW = new BreadthFirstDirectedPaths(digraph, w);

        int shortestLength = Integer.MAX_VALUE;
        int ancestor = -1;

        for (int vertex = 0; vertex < this.numVertices; vertex++){
            if (bfsFromV.hasPathTo(vertex) && bfsFromW.hasPathTo(vertex)){
                int sumDist = bfsFromV.distTo(vertex) + bfsFromW.distTo(vertex);
                 if (sumDist < shortestLength){
                     shortestLength = sumDist;
                     ancestor = vertex;
                 }
            }
        }
        return ancestor;

    }

    public static void main(String[] args) {

    }
}