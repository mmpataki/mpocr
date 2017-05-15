package mpocr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Loads the training images located in the folder pointed by dirpath
 * The images are needed to be named according to a convention. You can
 * find it and also generate these images using this tool
 * Refer : https://github.com/mmpataki/ocrimgen
 */
public class TrainingDataLoader {
    
    /**
     * Loads the images in to a TrainingSet.
     * @param dirpath : path to the image folder
     * @param outputSize : output vector size
     * @param featureSetMagic: magic number of feature to be extracted
     * @return : TrainingSet.
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static TrainingSet load (
                    String dirpath,
                    int outputSize,
                    int featureSetMagic
                ) throws FileNotFoundException, IOException {

        
        File folder;
        double[] expectedOutput;
        TrainingSet trainingSet = new TrainingSet();
        
        folder = new File(dirpath);

        /* read the directory and populate the training-set */
        for (final File file : folder.listFiles()) {

            String name = file.getName();
            if (!name.contains(".png")) {
                continue;
            }

            int asciiop;
            try {
                asciiop  = Integer.parseInt(name.split("-")[0]);
            } catch(NumberFormatException ne) {
                continue;
            }

            OImage oi = new OImage(Paths.get(dirpath, name).toAbsolutePath().toString());
            if (!oi.isBinarized()) {
                oi.binarize();
            }
            
            //Segment[] segs = Segmentation.segmentImage(oi);
            Segment[] segs = { new Segment(oi.getImageData()) };
            
            for (Segment seg : segs) {
                if (seg.getHeight() < 3 || seg.getWidth() < 3) {
                    continue;
                }

                expectedOutput = new double[outputSize];
                expectedOutput[asciiop] = 1;
                
                trainingSet.add(
                        new TrainingElement(
                                seg.getFeature(featureSetMagic).getFeatures(),
                                expectedOutput
                        )
                );
            }
        }
        
        return trainingSet;
    }
}
