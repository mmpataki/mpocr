/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpocr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import javafx.util.Pair;

/**
 *
 * @author mmp
 */
public class Trainer {

    NeuralNetwork network;
    String dirpath;
    byte[] chars8;
    double[] eOutputVector;
    ArrayList<Pair<Integer, FeatureSet>> trainingSet = null;

    Trainer(
            NeuralNetwork network,
            String dirpath,
            String charactersFile,
            String fontsFile, /* not used */
            String stylesFile /* not used */
    ) throws FileNotFoundException, IOException, Exception {

        if (network == null) {
            throw new Exception("network cannot be null");
        }

        this.dirpath = dirpath;
        this.network = network;
        FileReader reader = new FileReader(Paths.get(dirpath, charactersFile).toAbsolutePath().toString());
        BufferedReader br = new BufferedReader(reader);

        String line;
        int i = 0;
        chars8 = new byte[128];
        while ((line = br.readLine()) != null) {
            chars8[i++] = (byte) line.charAt(0);
        }
        eOutputVector = new double[128];
    }
    
    public void loadTrainingSet() {
        
        trainingSet = new ArrayList<>();
        
        /* read the directory and populate the training-set */
        File folder = new File(dirpath);
        for (final File file : folder.listFiles()) {

            String name = file.getName();
            if (!name.contains(".png")) {
                continue;
            }

            //TEMPORARY-REMOVE BELOW CONDITION & COMMENT AFTER TEST
            int index = Integer.parseInt(name.split("-")[0]);
            //if(index > 9)
                //continue;
            int asciiop = chars8[index];//index%2;//
            //-----------------------------------------------------
            
            OImage oi = new OImage(Paths.get(dirpath, name).toAbsolutePath().toString());
            if (!oi.isBinarized()) {
                oi.xbinarize();
            }
            Segment[] segs = Segmentation.segmentImage(oi);
            
            for (Segment seg : segs) {
                if (seg.getHeight() < 3 || seg.getWidth() < 3) {
                    continue;
                }
                //seg.printImageForce();
                seg.extractFeatures();
                trainingSet.add(new Pair<>(asciiop, seg.features.get(Zones.magic)));
            }
        }
    }

    public void train() throws Exception {

        if(trainingSet == null) {
            loadTrainingSet();
        }
        
        int tsize = 10000;
        int k = 0, counter = 0;
        double[] outputVector;

        int mi = 0; //serves as index to maximum value in output vector.
        double m = 0;

        while (k < tsize) {

            for (Pair<Integer, FeatureSet> pair : trainingSet) {

                int eop = pair.getKey();

                //build the expected output vector.
                eOutputVector[eop] = 1;

                Util.puts("Expected output : [" + (eop) + "]\n");

                network.propagate(pair.getValue().getFeatures(), eOutputVector);
                outputVector = network.getOutput();

                Util.puts("\nOutput vector [ ");

                mi = 0;
                m = 0;
                for (int i = 0; i < outputVector.length; i++) {
                    Util.puts(outputVector[i] + ", ");
                    if (outputVector[i] > m) {
                        m = outputVector[i];
                        mi = i;
                    }
                }
                Util.puts("]\n");
                Util.puts("Predicted output : [" + (mi) + "]\n\n\n");
                if (mi == eop) {
                    counter++;
                }

                //flush the expected output vector for reuse.
                eOutputVector[eop] = 0;
                k++;
            }
            break;
        }
        System.out.println();
        System.out.println("training set size: " + k);
        System.out.println("Correct outputs  : " + counter);
        System.out.println("Accuracy         : " + ((counter * 100.0) / (double) k) + "%");
    }
}
