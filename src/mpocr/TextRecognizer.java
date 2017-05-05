/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpocr;

import java.util.ArrayList;

/**
 *
 * @author mmp
 */
public class TextRecognizer {

    NeuralNetwork network;
    OImage image;

    TextRecognizer(NeuralNetwork network, OImage img) {
        this.network = network;
        this.image = img;
    }

    public ArrayList extract() {
        
        if (!image.isBinarized()) {
            image.xbinarize();
        }
        
        Segment[] segs = Segmentation.segmentImage(image);
        ArrayList<Character> output = new ArrayList<>();

        for (Segment seg : segs) {
            if (seg.getHeight() < 3 || seg.getWidth() < 3) {
                continue;
            }
            seg.printImageForce();
            seg.extractFeatures();
            network.fpropagate(seg.features.get(Zones.magic).getFeatures());

            double[] outputVector = network.getOutput();
            int mi = 0;
            double m = 0;
            for (int i = 0; i < outputVector.length; i++) {
                Util.puts(outputVector[i] + ", ");
                if (outputVector[i] > m) {
                    m = outputVector[i];
                    mi = i;
                }
            }
            output.add((char)mi);
        }
        return output;
    }
}
