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
        
        double[] x = imgs.get(0).getData();
        
        for (int i = 0; i < x.length; i++) {
            if(i % 28 == 0){
                System.out.println();
            }
            System.out.print(((int)x[i]));
        }
        
        System.out.println("Loading done....");
    }

    public void train() throws Exception {
        
        if (imgs == null) {
            loadTrainingSet();
        }
        
        TrainingSet set = new TrainingSet();
        double[] expectedOutput;
        
        for (DigitImage img : imgs) {
            expectedOutput = new double[10];
            expectedOutput[img.getLabel()] = 1;
            set.add(new TrainingElement(img.getData(), expectedOutput));
        }
        
        network.train(set);
    }
    
    public void test() throws Exception {

        if (imgs == null) {
            loadTrainingSet();
        }

        int k = 0, counter = 0;
        double[] outputVector = null;
        
        double nwe=0, cost=0;
        Plotter p = new Plotter("hist.html", "");
        p.setType(Plotter.REGION);
        p.addLayer("green");

        int mi = 0; //serves as index to maximum value in output vector.
        double m = 0;

        for (DigitImage pair : imgs) {

            int eop = pair.getLabel();

            //build the expected output vector.
            eOutputVector[eop] = 1;

            Util.puts("Expected output : [" + (eop) + "]\n");
            
            network.fpropagate(pair.getData());
            outputVector = network.getOutput();

            
            cost += network.getCost();
            nwe += network.getNetworkError();
            
            if((k % 10) == 0) {
                p.chooseLayer(1);
                p.addPoint(nwe / 10);
                p.chooseLayer(0);
                p.addPoint(cost / 10);
                nwe = cost = 0;
            }
            
            Util.puts("\nOutput vector " + Arrays.toString(outputVector) + "\n");

            mi = 0;
            m = 0;
            for (int i = 0; i < outputVector.length; i++) {
                if (outputVector[i] > m) {
                    m = outputVector[i];
                    mi = i;
                }
            }
            Util.putsf(" eop : " + eop + " op : " +  mi + " with " + m + Arrays.toString(outputVector) + "\n");
            
            Util.puts(" op [" + (mi) + "]");
            if (mi == eop) {
                Util.puts(" :)");
                counter++;
            }
            Util.puts("\n");
            
            k++;
        }
        System.out.println();
        System.out.println(Arrays.toString(outputVector));
        System.out.println("training set size: " + k);
        System.out.println("Correct outputs  : " + counter);
        System.out.println("Accuracy         : " + ((counter * 100.0) / (double) k) + "%");
        p.plot();
    }

}
