/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hamagraphloader;

import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hama.graph.Edge;
import org.apache.hama.graph.Vertex;
import org.apache.hama.graph.VertexInputReader;

/**
 *
 * @author Anastasis Andronidis <anastasis90@yahoo.gr>
 */
public class GraphLoaderTextReader extends VertexInputReader<LongWritable, Text, Text, NullWritable, DoubleWritable> {

    String lastVertexId = null;
    List<String> adjacents = new ArrayList<String>();
    
    @Override
    public boolean parseVertex(LongWritable key, Text value, Vertex<Text, NullWritable, DoubleWritable> vertex) throws Exception {
        String line = value.toString();
        String[] lineSplit = line.split("\t");
        if (!line.startsWith("#")) {
            if (lastVertexId == null) {
                lastVertexId = lineSplit[0];
            }
            if (lastVertexId.equals(lineSplit[0])) {
                adjacents.add(lineSplit[1]);
            } else {
                vertex.setVertexID(new Text(lastVertexId));
                for (String adjacent : adjacents) {
                    vertex.addEdge(new Edge<Text, NullWritable>(new Text(adjacent),
                            null));
                }
                adjacents.clear();
                lastVertexId = lineSplit[0];
                adjacents.add(lineSplit[1]);
                return true;
            }
        }
        return false;
    }
    
}
