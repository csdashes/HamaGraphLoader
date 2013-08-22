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
        if (this.getSuperstepCount() == 0L) {
            setValue(new LongWritable(0));
            sendMessageToNeighbors(new LongWritable(1L));            
        } else {
            while (messages.hasNext()) {
                LongWritable msg = messages.next();
                this.setValue(new LongWritable(this.getValue().get() + msg.get()));
            }
            voteToHalt();
        }        
    }
    
}
