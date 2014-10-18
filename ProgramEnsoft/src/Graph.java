import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;

public class Graph {
    private Map<String, LinkedHashSet<String>> map = 
    		new HashMap<String, LinkedHashSet<String>>();

    public void addEdge(String start, String end) {
        LinkedHashSet<String> adjacent = map.get(start);
        if(adjacent==null) {
            adjacent = new LinkedHashSet<String>();
            map.put(start, adjacent);
        }
        adjacent.add(end);
    }
    
    public LinkedList<String> adjacentNodes(String last) {
        LinkedHashSet<String> adjacent = map.get(last);
        if(adjacent==null) {
            return new LinkedList<String>();
        }
        return new LinkedList<String>(adjacent);
    }

    public boolean isConnected(String start, String end) {
        LinkedHashSet<String> adjacent = map.get(start);
        if(adjacent==null) {
            return false;
        }
        return adjacent.contains(end);
    }

    public LinkedList<String> adjacentNodes(String last) {
        LinkedHashSet<String> adjacent = map.get(last);
        if(adjacent==null) {
            return new LinkedList<String>();
        }
        return new LinkedList<String>(adjacent);
    }
}