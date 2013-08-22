/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hamagraphloader;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hama.HamaConfiguration;
import org.apache.hama.bsp.HashPartitioner;
import org.apache.hama.bsp.TextInputFormat;
import org.apache.hama.bsp.TextOutputFormat;
import org.apache.hama.graph.GraphJob;

/**
 *
 * @author Anastasis Andronidis <anastasis90@yahoo.gr>
 * @author Ilias Trichopoulos <itrichop@csd.auth.gr>
 */
public class HamaGraphLoader {

    /**
     * @param args the command line arguments
     * @throws IOException
     * @throws InterruptedException
     * @throws ClassNotFoundException  
     */
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        if (args.length != 2) {
            printUsage();
        }

        HamaConfiguration conf = new HamaConfiguration(new Configuration());
        GraphJob graphJob = createJob(args, conf);

//        System.out.println(graphJob.getNumBspTask());
//        System.out.println(graphJob.get("bsp.local.tasks.maximum"));
        
        long startTime = System.currentTimeMillis();
        if (graphJob.waitForCompletion(true)) {
            System.out.println("Job Finished in "
                    + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds");
        }

    }

    private static void printUsage() {
        System.out.println("Usage: <input> <output>");
        System.exit(-1);
    }

    private static GraphJob createJob(String[] args, HamaConfiguration conf) throws IOException {
        conf.set("bsp.local.tasks.maximum", "1");
//        conf.set("bsp.tasks.maximum", "2");
//        conf.set("bsp.max.tasks.per.job", "2");
//        conf.set("mapred.map.tasks", "1");
//        conf.set("mapred.min.split.size", String.valueOf(Long.MAX_VALUE));
        
        GraphJob graphJob = new GraphJob(conf, HamaGraphLoader.class);
        
        graphJob.setNumBspTask(1);
        graphJob.setJobName("Hama Graph Loader");
        
        graphJob.setVertexClass(GraphLoaderVertex.class);
        graphJob.setInputPath(new Path(args[0]));
        graphJob.setOutputPath(new Path(args[1]));

        graphJob.setMaxIteration(2);
        
        graphJob.setVertexIDClass(Text.class);
        graphJob.setVertexValueClass(LongWritable.class);
        graphJob.setEdgeValueClass(NullWritable.class);

        graphJob.setInputFormat(TextInputFormat.class);
        
        graphJob.setInputKeyClass(LongWritable.class);
        graphJob.setInputValueClass(Text.class);
        graphJob.setVertexInputReaderClass(GraphLoaderTextReader.class);

        graphJob.setPartitioner(HashPartitioner.class);

        graphJob.setOutputFormat(TextOutputFormat.class);
        graphJob.setOutputKeyClass(Text.class);
        graphJob.setOutputValueClass(LongWritable.class);
        
        return graphJob;
    }
}
