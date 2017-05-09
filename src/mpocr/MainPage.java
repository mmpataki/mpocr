package mpocr;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class MainPage extends javax.swing.JFrame {

    NeuralNetwork net;
    
    public MainPage() {
        
        initComponents();
        canvas.zoomIn(1);
        
        double[][][] wts = {
            {{1,1}, {1,1}, {1,1}},
            {{1,1,1},{1,1,1}}
        };
        double[][] biases = {
            {0,0},
            {0,0,0},
            {0,0}
        };
        
        //int[] nnnc = {2, 3, 2};
        //int[] nnnc = {784, 30, 10};  // mnist database
        int[] nnnc = {9,50,90,128};  // my mpocr
        try {
            //net = new NeuralNetwork(nnnc, wts, biases, new SigmoidFunction(), 0.02, 10);
            net = new NeuralNetwork(nnnc, null, null, new SigmoidFunction(), 0.001, 10);
        } catch (Exception ex) {
            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        nnv.setNN(net);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        ImportButton = new javax.swing.JButton();
        ImagePath = new javax.swing.JLabel();
        ZoomIn = new javax.swing.JButton();
        ZoomOut = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        canvas = new mpocr.mCanvas();
        hack = new javax.swing.JCheckBox();
        Train = new javax.swing.JButton();
        nnv = new mpocr.NNVisualizer();
        ExtractTextButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Consolas", 3, 36)); // NOI18N
        jLabel1.setText("mpocr");

        ImportButton.setText("Import");
        ImportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ImportButtonActionPerformed(evt);
            }
        });

        ImagePath.setText("Path");

        ZoomIn.setText(" + ");
        ZoomIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ZoomInActionPerformed(evt);
            }
        });

        ZoomOut.setText(" - ");
        ZoomOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ZoomOutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout canvasLayout = new javax.swing.GroupLayout(canvas);
        canvas.setLayout(canvasLayout);
        canvasLayout.setHorizontalGroup(
            canvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1160, Short.MAX_VALUE)
        );
        canvasLayout.setVerticalGroup(
            canvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 382, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(canvas);

        hack.setSelected(true);
        hack.setText("Use Hack");

        Train.setText("Train");
        Train.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TrainActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout nnvLayout = new javax.swing.GroupLayout(nnv);
        nnv.setLayout(nnvLayout);
        nnvLayout.setHorizontalGroup(
            nnvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 525, Short.MAX_VALUE)
        );
        nnvLayout.setVerticalGroup(
            nnvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        ExtractTextButton.setText("Extract Text");
        ExtractTextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExtractTextButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nnv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Train))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(hack)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ImportButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ExtractTextButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 330, Short.MAX_VALUE)
                                .addComponent(ZoomOut)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ZoomIn))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(ImagePath))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ImagePath)
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ZoomIn)
                    .addComponent(ZoomOut)
                    .addComponent(Train)
                    .addComponent(ExtractTextButton)
                    .addComponent(hack)
                    .addComponent(ImportButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nnv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ZoomOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ZoomOutActionPerformed
        canvas.zoomOut(1);
    }//GEN-LAST:event_ZoomOutActionPerformed

    private void ZoomInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ZoomInActionPerformed
        canvas.zoomIn(1);
    }//GEN-LAST:event_ZoomInActionPerformed

    private void ImportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ImportButtonActionPerformed
        
        //String path = "/home/mmp/miniproject/project/mpocr/testimages/AZVF.bmp";
        //String path = "/home/mmp/miniproject/project/mpocr/testimages/skewset1/skewset1.jpg";
        String path = "/home/mmp/miniproject/project/mpocr/testimages/unskewedLorem.bmp";      //segments perfectly
        //String path = "/home/mmp/miniproject/project/mpocr/testimages/skewset2/unskewed.jpeg"; //full noisy, small font
        //String path = "/home/mmp/miniproject/project/mpocr/testimages/skewset2/unskewed1.png";   //less noisy
        
        if(hack.isSelected()) {
        canvas.setImage(path);
        canvas.setOffset(0);
        ImagePath.setText("Path : " + path);
        }
        else {
        JFileChooser ch = new JFileChooser();
        ch.setFileFilter(new BMPImageFilter());
        ch.setCurrentDirectory(new File(path));
        if(ch.showOpenDialog(MainPage.this) == JFileChooser.APPROVE_OPTION) {
            File file = ch.getSelectedFile();
            path = file.getAbsolutePath();
            canvas.setImage(path);
            canvas.setOffset(1);
            ImagePath.setText("Path : " + path);
        }
        }
    }//GEN-LAST:event_ImportButtonActionPerformed

    private void TrainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TrainActionPerformed
        
        try {                                      
            
            Plotter p = new Plotter("hist.html", "");
            TrainingDataLoader loader = new TrainingDataLoader(
                                            "/home/mmp/Desktop/foo/", 
                                            "charcterindex.txt",
                                            "", "", 128
                                        );

            loader.load();
            p.setType(Plotter.REGION);
            p.addLayer("green");
            
            net.setCallBack(new Callback() {
                @Override
                public void function(Object param) {
                    p.chooseLayer(1);
                    p.addPoint(net.getNetworkError());
                    p.chooseLayer(0);
                    p.addPoint(net.getCost());
                }
            });
            
            net.train(loader.getTrainingSet());
            
            System.out.println();
            System.out.println("training set size: " + loader.size());
            System.out.println("Accuracy         : " + net.getTrainingAccuracy() + "%");
            p.plot();
            
            
            //(new MNistTrainer(net, "/home/mmp/train-images.idx3-ubyte", "/home/mmp/train-labels.idx1-ubyte", 100)).train();
            //(new MNistTrainer(net, "/home/mmp/t10k-images.idx3-ubyte", "/home/mmp/t10k-labels.idx1-ubyte", 100)).test();
             
        } catch (Exception ex) {
            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_TrainActionPerformed

    int ip = 0;
    private void ExtractTextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExtractTextButtonActionPerformed

        int a = ip & 1;
        int b = (ip & 2) >> 1;
        ip++;
        double[] ips = new double[2];
        ips[0]=a; ips[1]=b;
        net.fpropagate(ips);
        double[] op = net.getOutput();
        System.out.println("ip " + a + ", " + b);
        System.out.println(op[0]>op[1]?0:1);

        if(canvas.oimg != null) {
            ArrayList<Character> chars = (new TextRecognizer(net, canvas.oimg)).extract();
            for (Character aChar : chars) {
                System.out.print(aChar);// + " [" + ((int)aChar) + "]\n");
            }
        }
    }//GEN-LAST:event_ExtractTextButtonActionPerformed

    public static void main(String args[]) {
        try {
            javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MainPage().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ExtractTextButton;
    private javax.swing.JLabel ImagePath;
    private javax.swing.JButton ImportButton;
    private javax.swing.JButton Train;
    private javax.swing.JButton ZoomIn;
    private javax.swing.JButton ZoomOut;
    private mpocr.mCanvas canvas;
    private javax.swing.JCheckBox hack;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private mpocr.NNVisualizer nnv;
    // End of variables declaration//GEN-END:variables
}
