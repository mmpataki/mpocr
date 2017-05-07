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

    public FaltuTrainer(NeuralNetwork nw) {
        network = nw;
    }
    
    public void train() throws Exception {
        
        double[] op = new double[2];
        double[] ip = new double[2];
        int counter = 0;
        int tsize = 4;
        int notice = (tsize / 100)|1;
        for (int i = 0; i < tsize; i++) {
            
            int ib = /**/i & 1;          /** (int) ((Math.random()* 10000000) % 2);  /**/
            int ia = /**/((i & 2) >> 1); /** (int) ((Math.random() * 10000000) % 2); /**/
            int eop= ia | ib;
            ip[0] = ia;
            ip[1] = ib;
            
            System.out.println("\nnetwork : " + network);
            
            Util.putsf("\ninput [ " + ia + ", " + ib + "]\n");
            
            for (int j = 0; j < op.length; j++) op[j] = 0;
            op[0] = eop;
            
            network.propagate(ip, op);
            
            double[] aop = network.getOutput();
            
            int lop = (aop[0] < 0.5 ? 0 : 1);
            
            Util.putsf("\noutput : " + Arrays.toString(aop));
            Util.putsf("\noutput : [" + lop + "]\n");
            Util.putsf("expected : [" + eop + "]\n");
            
            if(lop == eop) {
                counter++;
            }
            
            if((i % notice)==0) {
                System.out.print("\rProcessed " + ((i*100.0)/tsize) + "% training set");
                System.out.flush();
            }
        }
        System.out.println();
        System.out.println("training set size: " + tsize);
        System.out.println("Correct outputs  : " + counter);
        System.out.println("Accuracy         : " + ((counter*100.0)/(double)tsize) + "%");
    }
}

