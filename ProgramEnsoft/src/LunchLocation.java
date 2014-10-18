import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LunchLocation {
	public static void addEdge(String data, Graph graph, String avoid) {
		int spaceIndex = data.indexOf(' ');
		String start = data.substring(0, spaceIndex);
		String end = data.substring(spaceIndex + 1);
		if (avoid.contains(start) || avoid.contains(end))
			return;
		graph.addEdge(start, end);
		//System.out.println(start + "\t" + end);
	}

	public static void bfs(Graph graph, LinkedList<String> visited, 
			String end, List<String> outputFile) {
		LinkedList<String> nodes = graph.adjacentNodes(visited.getLast());
		for (String node : nodes) {
			if (visited.contains(node)) {
				continue;
			}
			if (node.equals(end)) {
				visited.add(node);
				for (String nodeString : visited) {
					outputFile.add(nodeString);
				}
				visited.removeLast();
				break;
			}
		}
		for (String node : nodes) {
			if (visited.contains(node) || node.equals(end)) {
				continue;
			}
			visited.addLast(node);
			bfs(graph, visited, end, outputFile);
			visited.removeLast();
		}
	}

	public static void main(String[] args) {
		//String filename = "input.txt";
		String filename = args[0];
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		List<String> inputFile = new ArrayList<String>();
		List<String> outputFile = new ArrayList<String>();
		
		String data = "";
		do {
			try {
				data = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			inputFile.add(data);
		} while (data != null);

		Graph graph = new Graph();
		String avoid = inputFile.get(inputFile.indexOf("Avoid:") + 1);
		int inputIndex = 1;
		while (!inputFile.get(inputIndex).equals("Avoid:")) {
			addEdge(inputFile.get(inputIndex), graph, avoid);
			inputIndex++;
		}
		
		List<String> startList = new ArrayList<String>();
		List<String> endList = new ArrayList<String>();
		String start = inputFile.get(inputFile.indexOf("Peggy:") + 1);
		String end = inputFile.get(inputFile.indexOf("Sam:") + 1);
		int startIndex = 0;
		int endIndex = 0;
		if (start.contains(" ")) {
			int startLastSpace = start.lastIndexOf(' ');
			while (startIndex < startLastSpace) {
				if(!avoid.contains(start.substring(startIndex, 
						start.indexOf(' ', startIndex)))) startList.add(start.substring(startIndex, 
						start.indexOf(' ', startIndex)));
				startIndex = start.indexOf(' ', startIndex) + 1;
			}
			if(!avoid.contains(start.substring(startLastSpace))) startList.add(start.substring(startLastSpace + 1));
		}
		else startList.add(start);
		
		if (end.contains(" ")) {
			int endLastSpace = end.lastIndexOf(' ');
			while (endIndex < endLastSpace) {
				if(!avoid.contains(end.substring(endIndex, end.indexOf(' ', endIndex)))) 
					endList.add(end.substring(endIndex, end.indexOf(' ', endIndex)));
				endIndex = end.indexOf(' ', endIndex) + 1;
			}
			if(!avoid.contains(end.substring(endLastSpace))) endList.add(end.substring(endLastSpace + 1));
		}
		else endList.add(end);
		
		for (String startNode : startList) {
			for (String endNode : endList) {
				LinkedList<String> visited = new LinkedList<String>();
		        visited.add(startNode);
		        bfs(graph, visited, endNode, outputFile);
			}
		}
		
		removeDuplicates(outputFile);
		try {
			FileWriter writer = new FileWriter("output.txt");
			for (String s : outputFile) {
				writer.write(s + '\r');
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private static void removeDuplicates(List<String> outputFile) {
		outputFile.sort(null);
		for (int i = 1; i < outputFile.size(); ) {
			if (outputFile.get(i).equals(outputFile.get(i - 1))) {
				outputFile.remove(i);
			}
			else i++;
		}
	}
}
