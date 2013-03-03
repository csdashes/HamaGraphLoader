/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hamagraphloader;

import java.io.IOException;
import java.util.Iterator;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hama.graph.Vertex;

/**
 *
 * @author Anastasis Andronidis <anastasis90@yahoo.gr>
 */
public class GraphLoaderVertex extends Vertex<Text, NullWritable, LongWritable> {

    @Override
    public void compute(Iterator<LongWritable> messages) throws IOException {
        if (this.getSuperstepCount() == 0) {
            sendMessageToNeighbors(new LongWritable(1L));            
        } else {
            Long sum = 0L;
            while (messages.hasNext()) {
                LongWritable msg = messages.next();
                sum += msg.get();
            }
        }
    }
    
}
