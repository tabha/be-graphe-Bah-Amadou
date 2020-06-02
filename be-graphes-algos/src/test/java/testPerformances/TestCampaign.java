package testPerformances;

import org.junit.Test;

public class TestCampaign {
	public static String mapDirectory = "/home/amadou/CoursINSA3MIC/BE_graphe/cartes/";
	public static String testFilesDirectory ="/home/amadou/CoursINSA3MIC/BE_graphe/tesFiles/";
	public static String resultatTestDir = "/home/amadou/CoursINSA3MIC/BE_graphe/ResultatTests/";
	
	public static String[] mapsNames = {
			"bretagne.mapgr",
			"haute-garonne.mapgr",
			"insa.mapgr",
			"midi-pyrenees.mapgr",
	};
	public static String[] testFileNames = {
			"bretagne_15_25.txt",
			"bretagne_50_100.txt",
			"bretagne_150_170.txt"
	};
	public static String[] resultatTestNames = {
			"resultat_performance_bretagne_15_25_temps.csv",
			"resultat_performance_bretagne_15_25_distance.csv",
			"resultat_performance_bretagne_50_100_temps.csv",
			"resultat_performance_bretagne_50_100_distance.csv",
			"resultat_performance_bretagne_150_170_temps.csv",
			"resultat_performance_bretagne_150_170_distance.csv"
	};
	
	public static String getFullPath(String fileName,int mode) {
		String path = "";
		switch(mode) {
			case 1: // map;
				path = mapDirectory+fileName;
				break;
			case 2: // test;
				path = testFilesDirectory + fileName;
				break;
			case 3:
				path = resultatTestDir + fileName;
				break;
			default:
				return null;
		}
		return path;
	}
	@Test
	public void testsCampaingn() {
		TestPerformance firstTestTemps = new TestPerformance();
		//firstTestTemps.doRun(getFullPath(testFileNames[0],2),getFullPath(resultatTestNames[0],3) , 0);
		TestPerformance secondTestDistance = new TestPerformance();
		//secondTestDistance.doRun(getFullPath(testFileNames[0],2), getFullPath(resultatTestNames[1],3), 1);
		TestPerformance testThree = new TestPerformance();
		testThree.doRun(getFullPath(testFileNames[1],2),getFullPath(resultatTestNames[2],3) , 0);
		TestPerformance tesFour = new TestPerformance();
		tesFour.doRun(getFullPath(testFileNames[1],2), getFullPath(resultatTestNames[3],3), 1);
		TestPerformance testDostance50_70Temps = new TestPerformance();
		testDostance50_70Temps.doRun(getFullPath(testFileNames[2],2),getFullPath(resultatTestNames[4],3) , 0);
		TestPerformance testDostance50_70Distance = new TestPerformance();
		testDostance50_70Distance.doRun(getFullPath(testFileNames[2],2), getFullPath(resultatTestNames[5],3), 1);
	}
}
