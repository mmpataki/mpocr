/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpocr;

import java.util.Arrays;

/**
 *
 * @author mmp
 */
public class FaltuTrainer {

    NeuralNetwork network;
    private int trainingSetSize;

    public FaltuTrainer(NeuralNetwork network, int trainingSetSize) {
        this.network = network;
        this.trainingSetSize = trainingSetSize;
    }
    
    public void train() throws Exception {
        
        int ia, ib, eop;
        double[] op, ip;
        TrainingSet set = new TrainingSet();
        Plotter p = new Plotter("hist.html", "");
        
        for (int i = 0; i < trainingSetSize; i++) {
            
            ip = new double[2];
            op = new double[2];
            
            ib = i & 1;
            ia = ((i & 2) >> 1);
            
            ip[0] = ia;
            ip[1] = ib;
            op[(ia | ib)] = 1;
            
            set.add(new TrainingElement(ip, op));
        }
        
        
        p.setType(Plotter.REGION);
        p.addLayer("green");
        
        
        network.setCallBack(new Callback() {
            @Override
            public void function(Object param) {
                
                System.out.println(network);
                
                TrainingElement cte = ((NeuralNetwork)param).getCurrentTrainingElement();
                
                double[] eop = cte.getExpectedOutput();
                double[] aop = network.getOutput();
                
                Util.puts(Arrays.toString(cte.getInputVector()) + "\n");
                Util.puts(Arrays.toString(eop) + "\n");
                Util.puts(Arrays.toString(aop) + " ");
                
                int op = aop[0] > aop[1] ? 0 : 1;
                
                System.out.print((eop[op] != 0) ? " :)\n" : "\n");
                
                p.chooseLayer(1);
                p.addPoint(network.getNetworkError());
                p.chooseLayer(0);
                p.addPoint(network.getCost());
                
            }
        });
        
        network.train(set);
        
        System.out.println();
        System.out.println("training set size: " + trainingSetSize);
        System.out.println("Accuracy         : " + network.getTrainingAccuracy() + "%");
        p.plot();
    }
}

