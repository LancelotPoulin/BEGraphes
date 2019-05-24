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

public class AStarAlgorithmTest
{
	// Graph
    private static Graph graph;
    
    // Paths Data
    private static ShortestPathData shortPathNoRoad, longPathNoRoad, infeasiblePath, singleNodePath, shortPathBicycle, shortPathCarDist, longPathBicycle, longPathCarDist, invalidPath, shortPathCarTime, longPathCarTime;

    // Algo and solution
    private static AStarAlgorithm ASA;
    ShortestPathSolution SPS;
    
    @BeforeClass
    public static void initAll() throws IOException 
    {
    	// Graph file
    	String mapName = "C:/Users/pouli/Downloads/bretagne.mapgr"; // map routière

        // Create a graph reader.
        GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));

        // Read the graph.
        graph = reader.read();

        // Init paths Data
        invalidPath = new ShortestPathData(graph, null, null, ArcInspectorFactory.getAllFilters().get(0));
        singleNodePath = new ShortestPathData(graph, graph.get(634713), graph.get(634713), ArcInspectorFactory.getAllFilters().get(0)); // A to A
        infeasiblePath = new ShortestPathData(graph, graph.get(564287), graph.get(372189), ArcInspectorFactory.getAllFilters().get(0)); // A to B with B unattainable
        shortPathBicycle = new ShortestPathData(graph, graph.get(248218), graph.get(248354), ArcInspectorFactory.getAllFilters().get(3)); // short A to B with bicycle
        shortPathCarDist = new ShortestPathData(graph, graph.get(248218), graph.get(248354), ArcInspectorFactory.getAllFilters().get(1)); // short A to B with car Distance
        shortPathCarTime = new ShortestPathData(graph, graph.get(248218), graph.get(248354), ArcInspectorFactory.getAllFilters().get(2)); // short A to B with car Time
        longPathBicycle = new ShortestPathData(graph, graph.get(267704), graph.get(412660), ArcInspectorFactory.getAllFilters().get(3)); // long A to B with bicycle
        longPathCarDist = new ShortestPathData(graph, graph.get(267704), graph.get(412660), ArcInspectorFactory.getAllFilters().get(1)); // long A to B with car Distance
        longPathCarTime = new ShortestPathData(graph, graph.get(267704), graph.get(412660), ArcInspectorFactory.getAllFilters().get(2)); // long A to B with car Time
        
        mapName = "C:/Users/pouli/Downloads/carre-dense.mapgr"; // Carré dense, non routier
        reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
        graph = reader.read();
        shortPathNoRoad = new ShortestPathData(graph, graph.get(131767), graph.get(183686), ArcInspectorFactory.getAllFilters().get(2)); // short A to B non routier
        longPathNoRoad = new ShortestPathData(graph, graph.get(14718), graph.get(42778), ArcInspectorFactory.getAllFilters().get(3)); // long A to B routier
    }
    
    @Test
    public void testAlgorithm() {
        ASA = new AStarAlgorithm(invalidPath);
        SPS = ASA.doRun();
        assertEquals(SPS.getStatus(), Status.UNKNOWN); // Solution is invalid (missing arguments)
        
        ASA = new AStarAlgorithm(infeasiblePath);
        SPS = ASA.doRun();
        assertEquals(SPS.getStatus(), Status.INFEASIBLE); // Solution is infeasible (A can't reach B)
        
        ASA = new AStarAlgorithm(singleNodePath);
        SPS = ASA.doRun();
        assertTrue(SPS.isFeasible()); // A -> A : on se trouve déja a destination (chemin de longueur nulle -> valide)
        assertTrue(SPS.getPath().isValid());
        
        
        // Chemins valides (Optimalité?)
        
        ASA = new AStarAlgorithm(shortPathBicycle);
        SPS = ASA.doRun();
        assertTrue(SPS.isFeasible());
        assertTrue(SPS.getPath().isValid());
        
        ASA = new AStarAlgorithm(shortPathCarDist);
        SPS = ASA.doRun();
        assertTrue(SPS.isFeasible());
        assertTrue(SPS.getPath().isValid());
        
        ASA = new AStarAlgorithm(shortPathCarTime);
        SPS = ASA.doRun();
        assertTrue(SPS.isFeasible());
        assertTrue(SPS.getPath().isValid());
        
        ASA = new AStarAlgorithm(longPathBicycle);
        SPS = ASA.doRun();
        assertTrue(SPS.isFeasible());
        assertTrue(SPS.getPath().isValid());
        
        ASA = new AStarAlgorithm(longPathCarDist);
        SPS = ASA.doRun();
        assertTrue(SPS.isFeasible());
        assertTrue(SPS.getPath().isValid());
        
        ASA = new AStarAlgorithm(longPathCarTime);
        SPS = ASA.doRun();
        assertTrue(SPS.isFeasible());
        assertTrue(SPS.getPath().isValid());
        
        // Carré dense
        
        ASA = new AStarAlgorithm(shortPathNoRoad);
        SPS = ASA.doRun();
        assertTrue(SPS.isFeasible());
        assertTrue(SPS.getPath().isValid());
        
        ASA = new AStarAlgorithm(longPathNoRoad);
        SPS = ASA.doRun();
        assertTrue(SPS.isFeasible());
        assertTrue(SPS.getPath().isValid());
    }
    
}
