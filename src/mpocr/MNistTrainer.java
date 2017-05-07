/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpocr;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author mmp
 */
public class MNistTrainer {

    NeuralNetwork network;
    String imgfile;
    String lblfile;
    double[] eOutputVector;
    List<DigitImage> imgs = null;
    private int tsize;

    MNistTrainer(
            NeuralNetwork network,
            String imgfile,
            String lblfile,
            int tsize
    ) throws FileNotFoundException, IOException, Exception {

        if (network == null) {
            throw new Exception("network cannot be null");
        }

        this.tsize = tsize;
        this.lblfile = lblfile;
        this.imgfile = imgfile;
        this.network = network;
        eOutputVector = new double[10];
    }

    public void loadTrainingSet() throws IOException {
        
        MNistReader reader = new MNistReader(lblfile, imgfile, tsize);
        imgs = reader.loadDigitImages();
        
        System.out.println("Loading done....");
    }

    public void train() throws Exception {
        train_n_test(true);
    }
    
    void test() throws Exception {
        train_n_test(false);
    }
    
    public void train_n_test(boolean train) throws Exception {

        if (imgs == null) {
            loadTrainingSet();
        }

        int k = 0, counter = 0;
        double[] outputVector;

        int mi = 0; //serves as index to maximum value in output vector.
        double m = 0;

        for (DigitImage pair : imgs) {

            int eop = pair.getLabel();

            //build the expected output vector.
            eOutputVector[eop] = 1;

            Util.puts("Expected output : [" + (eop) + "]\n");

            
            if(train) { //train
                network.propagate(pair.getData(), eOutputVector);
            } else {   //test
                double[] xip = pair.getData();
                network.fpropagate(xip);
            }
            outputVector = network.getOutput();

            Util.puts("\nOutput vector " + Arrays.toString(outputVector) + "\n");

            mi = 0;
            m = 0;
            for (int i = 0; i < outputVector.length; i++) {
                if (outputVector[i] > m) {
                    m = outputVector[i];
                    mi = i;
                }
            }
            
            //if(!train) 
            {
                Util.putsf(" eop : " + eop + " op : " +  mi + " with " + m + Arrays.toString(outputVector) + "\n");
            }
            
            Util.puts(" op [" + (mi) + "]");
            if (mi == eop) {
                Util.puts(" :)");
                counter++;
            }
            Util.puts("\n");
            
            //flush the expected output vector for reuse.
            eOutputVector[eop] = 0;
            k++;
        }
        System.out.println();
        System.out.println("training set size: " + k);
        System.out.println("Correct outputs  : " + counter);
        System.out.println("Accuracy         : " + ((counter * 100.0) / (double) k) + "%");
    }

}
