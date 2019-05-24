package org.insa.base;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.insa.algo.ArcInspectorFactory;
import org.insa.algo.shortestpath.DijkstraAlgorithm;
import org.insa.algo.shortestpath.ShortestPathAlgorithm;
import org.insa.algo.shortestpath.ShortestPathData;
import org.insa.algo.shortestpath.Stat;
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
        
        String contents = "Test file - " + mapName + "\r\n" + String.valueOf(typeNumber) + "\r\n" + String.valueOf(nbTest) + "\r\nOrigine-Destination\r\n";
        for (int i = 0; i < nbTest; i++)
        {
        	int origine = ThreadLocalRandom.current().nextInt(0, graph.size());
        	int destination = ThreadLocalRandom.current().nextInt(0, graph.size());
        	contents += origine + "-" + destination +"\r\n";
        }
        Files.write(Paths.get(absoluteFilePath), contents.getBytes("UTF-8"));
	}
	
	
	
	private static void runPerformanceTest(String testDir, String mapName, int nbTest, int typeNumber, ArrayList<String> algoClassNameList) throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException
	{
		// Lecture fichier test
		String type = "";
		if (typeNumber == 0) { type = "_distance_"; } else type = "_temps_";
		String absoluteFilePath = testDir + mapName + type + String.valueOf(nbTest) + "_data.txt";
		List<String> contents = Files.readAllLines(Paths.get(absoluteFilePath));
		
		// Creation fichier resultat
		for (int j = 0; j < algoClassNameList.size(); j++)
		{
			absoluteFilePath = testDir + mapName + type + String.valueOf(nbTest) + "_" + algoClassNameList.get(j).split("[.]")[4] + "_result_.txt";
	        File file = new File(absoluteFilePath);
	        if (file.exists()) file.delete();
	        if (file.createNewFile()) System.out.println("File Created: " + absoluteFilePath);
	        
	        String contentsToWrite = "Test file - " + mapName + "\r\n" + String.valueOf(typeNumber) + "\r\n" + String.valueOf(nbTest) + "\r\nOrigine-Destination-Distance-Temps-CPU-Visité-Marqué-MaxTas\r\n";
			
			ShortestPathData SPD;
			ShortestPathAlgorithm SPA;
			for (int i = 0; i < nbTest; i++)
			{
				String line = contents.get(i + 4); // a partir de la ligne 4
				int origine = Integer.parseInt(line.split("[-]")[0]);
				int destination = Integer.parseInt(line.split("[-]")[1]);
		        SPD = new ShortestPathData(graph, graph.get(origine), graph.get(destination), ArcInspectorFactory.getAllFilters().get(0));
		        SPA = (ShortestPathAlgorithm)Class.forName(algoClassNameList.get(j)).getDeclaredConstructor(ShortestPathData.class).newInstance(SPD);
				SPA.run();
				
				Stat stat = SPA.getStat();
				contentsToWrite += String.valueOf(origine) + "-" + String.valueOf(destination) + "-" + stat.getLengthSolution() + "-" + stat.getTimeSolution() + "-"
				+ stat.getCPUTime() + "-" + stat.getNbSommetVisites() + "-" + stat.getNbSommetMarques() + "-" + stat.getTailleTasMax() + "\r\n";
			}
			Files.write(Paths.get(absoluteFilePath), contentsToWrite.getBytes("UTF-8"));
		}
	}
	
	
	
	private static void compareFromResults(String testDir, String mapName, int nbTest, int typeNumber, ArrayList<String> algoClassNameList) throws IOException
	{
		ArrayList<double[]> moyAlgo = new ArrayList<double[]>();
		// Lecture fichier test
		String type = "";
		if (typeNumber == 0) { type = "_distance_"; } else type = "_temps_";
		
		// récupération result
		for (int j = 0; j < algoClassNameList.size(); j++)
		{
			String absoluteFilePath = testDir + mapName + type + String.valueOf(nbTest) + "_" + algoClassNameList.get(j).split("[.]")[4] + "_result_.txt";
			List<String> contents = Files.readAllLines(Paths.get(absoluteFilePath));
			double moySommetMarque = 0, moySommetVisite = 0, moyTailleTasMax = 0, moyCPUTime = 0, moyLengthSolution = 0, moyTimeSolution = 0;
			for (int i = 0; i < nbTest; i++)
			{
				String line = contents.get(i + 4); // a partir de la ligne 4
				moyCPUTime += Double.parseDouble(line.split("[-]")[4]);
				moyLengthSolution += Double.parseDouble(line.split("[-]")[2]);
				moyTimeSolution += Double.parseDouble(line.split("[-]")[3]);
				moySommetMarque += Double.parseDouble(line.split("[-]")[6]);
				moySommetVisite += Double.parseDouble(line.split("[-]")[5]);
				moyTailleTasMax += Double.parseDouble(line.split("[-]")[7]);
			}
			moySommetMarque /= nbTest; moySommetVisite /= nbTest; moyTailleTasMax /= nbTest;
			moyCPUTime /= nbTest; moyLengthSolution /= nbTest; moyTimeSolution /= nbTest;
			moyAlgo.add(new double[] {moyLengthSolution, moyTimeSolution, moyCPUTime, moySommetVisite, moySommetMarque, moyTailleTasMax});
		}
		System.out.print("Type: " + type + " |      Length      |      Time       |       CPU       |      Visité      |      Marqué      |      TasMax      |\r\n");
		double[] current = moyAlgo.get(0);
		for (int i = 0; i < algoClassNameList.size(); i++)
		{
			System.out.print(algoClassNameList.get(i).split("[.]")[4] + "  |");
			for (int j = 0; j < 6; j++)
			{
				DecimalFormat df = new DecimalFormat("#.##");
				System.out.print("     " + df.format(moyAlgo.get(i)[j]) + "(" + df.format((((moyAlgo.get(i)[j]/current[j])-1)*100))  + "%) |");
			}
			System.out.print("\r\n");
			current = moyAlgo.get(i);
		}
	}
	
	

	public static void main(String[] args) throws Exception {

        // Map file
		String testDirectory = "C:/Users/pouli/Downloads/";
        String mapFileName = "bretagne.mapgr";
        int nbTest = 100;
        
        // Algo choisis
        ArrayList<String> algoClass = new ArrayList<String>();
        algoClass.add("org.insa.algo.shortestpath.AStarAlgorithm");
        algoClass.add("org.insa.algo.shortestpath.DijkstraAlgorithm");
        //algoClass.add("org.insa.algo.shortestpath.BellmanFordAlgorithm");

        // Create a graph reader
        GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(testDirectory + mapFileName))));
        // Read the graph
        graph = reader.read();
        
        // jeux de test
        createRandomTestData(testDirectory, mapFileName.split("[.]")[0], nbTest, 0); // 0 pour distance
        createRandomTestData(testDirectory, mapFileName.split("[.]")[0], nbTest, 1); // 1 pour distance
        
        // run tests
        runPerformanceTest(testDirectory, mapFileName.split("[.]")[0], nbTest, 0, algoClass);
        runPerformanceTest(testDirectory, mapFileName.split("[.]")[0], nbTest, 1, algoClass);

        // comparatif
        compareFromResults(testDirectory, mapFileName.split("[.]")[0], nbTest, 0, algoClass);
        compareFromResults(testDirectory, mapFileName.split("[.]")[0], nbTest, 1, algoClass);
        
    }
}
