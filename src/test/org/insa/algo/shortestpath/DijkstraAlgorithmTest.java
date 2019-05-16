package org.insa.algo.shortestpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.insa.algo.AbstractSolution.Status;
import org.insa.algo.ArcInspectorFactory;
import org.insa.graph.Graph;
import org.insa.graph.io.BinaryGraphReader;
import org.insa.graph.io.GraphReader;
import org.junit.BeforeClass;
import org.junit.Test;

public class DijkstraAlgorithmTest
{
	// Graph
    private static Graph graph;
    
    // Paths Data
    private static ShortestPathData infeasiblePath, singleNodePath, shortPathBicycle, shortPathCarDist, longPathBicycle, longPathCarDist, invalidPath, shortPathCarTime, longPathCarTime;

<<<<<<< HEAD
    // List of nodes
    private static Node[] nodes;

    // List of arcs in the graph, a2b is the arc from node A (0) to B (1).
    @SuppressWarnings("unused")
    private static Arc a2b, a2c, a2e, b2c, c2d_1, c2d_2, c2d_3, c2a, d2a, d2e, e2d;

    // Some paths...
    private static Path emptyPath, singleNodePath, shortPath, longPath, loopPath, longLoopPath,
            invalidPath;

=======
    // Algo and solution
    private static DijkstraAlgorithm DA;
    ShortestPathSolution SPS;
    
>>>>>>> b1fe8939d89cdaa476560a911d80cf5ffdc92ebc
    @BeforeClass
    public static void initAll() throws IOException 
    {
    	// Graph file
    	String mapName = "C:/Users/pouli/Downloads/bretagne.mapgr";

        // Create a graph reader.
        GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));

        // Read the graph.
        graph = reader.read();

        // Init paths data
        invalidPath = new ShortestPathData(graph, null, null, ArcInspectorFactory.getAllFilters().get(0));
        singleNodePath = new ShortestPathData(graph, graph.get(634713), graph.get(634713), ArcInspectorFactory.getAllFilters().get(0)); // A to A
        infeasiblePath = new ShortestPathData(graph, graph.get(564287), graph.get(372189), ArcInspectorFactory.getAllFilters().get(0)); // A to B with B unattainable
        shortPathBicycle = new ShortestPathData(graph, graph.get(248218), graph.get(248354), ArcInspectorFactory.getAllFilters().get(3)); // short A to B with bicycle
        shortPathCarDist = new ShortestPathData(graph, graph.get(248218), graph.get(248354), ArcInspectorFactory.getAllFilters().get(1)); // short A to B with car Distance
        shortPathCarTime = new ShortestPathData(graph, graph.get(248218), graph.get(248354), ArcInspectorFactory.getAllFilters().get(2)); // short A to B with car Time
        longPathBicycle = new ShortestPathData(graph, graph.get(267704), graph.get(412660), ArcInspectorFactory.getAllFilters().get(3)); // long A to B with bicycle
        longPathCarDist = new ShortestPathData(graph, graph.get(267704), graph.get(412660), ArcInspectorFactory.getAllFilters().get(1)); // long A to B with car Distance
        longPathCarTime = new ShortestPathData(graph, graph.get(267704), graph.get(412660), ArcInspectorFactory.getAllFilters().get(2)); // long A to B with car Time
    }
    
    @Test
    public void testAlgorithmSolution() {
        DA = new DijkstraAlgorithm(invalidPath);
        SPS = DA.doRun();
        assertEquals(SPS.getStatus(), Status.UNKNOWN); // Solution is invalid (missing arguments)
        
        DA = new DijkstraAlgorithm(infeasiblePath);
        SPS = DA.doRun();
        assertEquals(SPS.getStatus(), Status.INFEASIBLE); // Solution is infeasible (A can't reach B)
        
        DA = new DijkstraAlgorithm(singleNodePath);
        SPS = DA.doRun();
        assertTrue(SPS.isFeasible()); // A -> A : on se trouve d�ja a destination (chemin de longueur nulle -> valide)
        assertTrue(SPS.getPath().isValid());
        
        // Chemins normaux valides Car/Bicycle sur Distance/Temps
        
        DA = new DijkstraAlgorithm(shortPathBicycle);
        SPS = DA.doRun();
        assertTrue(SPS.isFeasible());
        assertTrue(SPS.getPath().isValid());
        
        DA = new DijkstraAlgorithm(shortPathCarDist);
        SPS = DA.doRun();
        assertTrue(SPS.isFeasible());
        assertTrue(SPS.getPath().isValid());
        
        DA = new DijkstraAlgorithm(shortPathCarTime);
        SPS = DA.doRun();
        assertTrue(SPS.isFeasible());
        assertTrue(SPS.getPath().isValid());
        
        DA = new DijkstraAlgorithm(longPathBicycle);
        SPS = DA.doRun();
        assertTrue(SPS.isFeasible());
        assertTrue(SPS.getPath().isValid());
        
        DA = new DijkstraAlgorithm(longPathCarDist);
        SPS = DA.doRun();
        assertTrue(SPS.isFeasible());
        assertTrue(SPS.getPath().isValid());
        
        DA = new DijkstraAlgorithm(longPathCarTime);
        SPS = DA.doRun();
        assertTrue(SPS.isFeasible());
        assertTrue(SPS.getPath().isValid());
    }
    
}
