package org.insa.base;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.insa.algo.ArcInspectorFactory;
import org.insa.algo.shortestpath.DijkstraAlgorithm;
import org.insa.algo.shortestpath.ShortestPathAlgorithm;
import org.insa.algo.shortestpath.ShortestPathData;
import org.insa.graph.Graph;
import org.insa.graph.io.BinaryGraphReader;
import org.insa.graph.io.GraphReader;

public class PerformanceTestLaunch
{
	static Graph graph;
	
	
	
	private static void createRandomTestData(String testDir, String mapName, int nbTest, int typeNumber) throws IOException
	{
		String type = "";
		if (typeNumber == 0) { type = "_distance_"; } else type = "_temps_";
		String absoluteFilePath = testDir + mapName + type + String.valueOf(nbTest) + "_data.txt";
        File file = new File(absoluteFilePath);
        if (file.exists()) file.delete();
        if (file.createNewFile()) System.out.println("File Created: " + absoluteFilePath);
        
        String contents = "Test file - " + mapName + "\r\n" + String.valueOf(typeNumber) + "\r\n" + String.valueOf(nbTest) + "\r\n";
        for (int i = 0; i < nbTest; i++)
        {
        	int origine = ThreadLocalRandom.current().nextInt(0, graph.size());
        	int destination = ThreadLocalRandom.current().nextInt(0, graph.size());
        	contents += origine + "-" + destination +"\r\n";
        }
        Files.write(Paths.get(absoluteFilePath), contents.getBytes("UTF-8"));
	}
	
	
	
	private static ArrayList<Stat> runPerformanceTest(String testDir, String mapName, int nbTest, int typeNumber, Class<ShortestPathAlgorithm> classSPA)
	{
		ArrayList<Stat> stats = new ArrayList<Stat>();
		String type = "";
		if (typeNumber == 0) { type = "_distance_"; } else type = "_temps_";
		String absoluteFilePath = testDir + mapName + type + String.valueOf(nbTest) + "_data.txt";
		List<String> contents = Files.readAllLines(Paths.get(absoluteFilePath));
		ShortestPathData SPD;
		ShortestPathAlgorithm SPA;
		for (int i = 3; i < nbTest; i++)
		{
			String line = contents.get(i);
			int origine = Integer.parseInt(line.split("[-]")[0]);
			int destination = Integer.parseInt(line.split("[-]")[1]);
	        SPD = new ShortestPathData(graph, graph.get(origine), graph.get(destination), ArcInspectorFactory.getAllFilters().get(0));
	        SPA = classSPA.getDeclaredConstructor(ShortestPathData.class).newInstance(SPD); // changer data sans réinstancier
			SPA.run();
			stats.add(SPA.getStat());
		}
	}
	
	

	public static void main(String[] args) throws Exception {

        // Map file
		String testDirectory = "C:/Users/pouli/Downloads/";
        String mapFileName = "bretagne.mapgr";

        // Create a graph reader
        GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(testDirectory + mapFileName))));

        // Read the graph
        graph = reader.read();
        
        // créer des jeux de test
        createRandomTestData(testDirectory, mapFileName.split("[.]")[0], 100, 0); // 100 paires, 0 pour distance
        createRandomTestData(testDirectory, mapFileName.split("[.]")[0], 100, 1); // 100 paires, 1 pour distance
        
        // faire les tests
        ArrayList<Stat> stats = runPerformanceTest(testDirectory, mapFileName.split("[.]")[0], 100, 0, DijkstraAlgorithm.class);
        
        
        // créer des résulats/comparatif
        
    }
}
