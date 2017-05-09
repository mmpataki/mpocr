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

/**
 *
 * @author mmp
 */
public class TrainingDataLoader {
    
    public static TrainingSet load (
                    String dirpath,
                    String charactersFile,
                    String fontsFile,  /* not used */
                    String stylesFile, /* not used */
                    int outputSize
                ) throws FileNotFoundException, IOException {

        byte[] chars8;
        TrainingSet trainingSet;
        
        int i = 0;
        String line;
        chars8 = new byte[128];
        
        FileReader reader = new FileReader(
                    Paths.get(dirpath, charactersFile)
                            .toAbsolutePath()
                            .toString()
                );
        BufferedReader br = new BufferedReader(reader);

        while ((line = br.readLine()) != null) {
            chars8[i++] = (byte) line.charAt(0);
        }
        
        double[] expectedOutput;
        File folder;

        folder = new File(dirpath);
        trainingSet = new TrainingSet();

        /* read the directory and populate the training-set */
        for (final File file : folder.listFiles()) {

            String name = file.getName();
            if (!name.contains(".png")) {
                continue;
            }

            int index = Integer.parseInt(name.split("-")[0]);
            int asciiop = chars8[index];

            OImage oi = new OImage(Paths.get(dirpath, name).toAbsolutePath().toString());
            if (!oi.isBinarized()) {
                oi.xbinarize();
            }
            
            //Segment[] segs = Segmentation.segmentImage(oi);
            Segment[] segs = { new Segment(oi.getImageData()) };
            
            for (Segment seg : segs) {
                if (seg.getHeight() < 3 || seg.getWidth() < 3) {
                    continue;
                }

                seg.extractFeatures();

                expectedOutput = new double[outputSize];
                expectedOutput[asciiop] = 1;
                trainingSet.add(
                        new TrainingElement(
                                seg.features.get(PixelBuffer.magic).getFeatures(),
                                expectedOutput
                        )
                );
            }
        }
        return trainingSet;
    }
}
