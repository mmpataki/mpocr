package mpocr;

import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MainPage extends javax.swing.JFrame {

    private Plotter plt;
    private NeuralNetwork net;
    private String storePath = "neuralnet.net";
    private int featureSetMagic = PixelBuffer.magic;

    public MainPage() throws NeuralNetworkException {

        Vector<Integer> nnnc = new Vector<>();
        nnnc.add(100);
        nnnc.add(70);
        nnnc.add(62);
        
        initComponents();

        plt = new Plotter("error_graph.html", "");
        net = new NeuralNetwork(nnnc, new SigmoidFunction(), 0, 1);
        
        /* attach to the visualizer. */
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
        Train = new javax.swing.JButton();
        nnv = new mpocr.NNVisualizer();
        ExtractTextButton = new javax.swing.JButton();
        Test = new javax.swing.JButton();
        Load = new javax.swing.JButton();
        Save = new javax.swing.JButton();
        LearningRate = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        MaxError = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        MaxIterations = new javax.swing.JTextField();
        PlotChart = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        EpochSize = new javax.swing.JTextField();
        Notice = new javax.swing.JLabel();

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
            .addGap(0, 425, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(canvas);

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

        Test.setText("Test");
        Test.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TestActionPerformed(evt);
            }
        });

        Load.setText("Load");
        Load.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoadActionPerformed(evt);
            }
        });

        Save.setText("Save");
        Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveActionPerformed(evt);
            }
        });

        LearningRate.setText("0.43");

        jLabel2.setText("Learning Rate");

        jLabel3.setText("Max Error");

        MaxError.setText("0.5");

        jLabel4.setText("Max Iterations");

        MaxIterations.setText("0");

        PlotChart.setSelected(true);
        PlotChart.setText("Plot Error graph");

        jLabel5.setText("Epoch Size");

        EpochSize.setText("1");

        Notice.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        Notice.setForeground(new java.awt.Color(255, 0, 0));
        Notice.setText(" ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nnv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(Train)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Test)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Load)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Save)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Notice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(MaxIterations))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel3))
                                        .addGap(15, 15, 15)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(LearningRate, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(MaxError, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(PlotChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel5)
                                    .addComponent(EpochSize))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(ImportButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ExtractTextButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 420, Short.MAX_VALUE)
                                .addComponent(ZoomOut)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ZoomIn))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(ImagePath, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(44, 44, 44)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ZoomIn)
                    .addComponent(ZoomOut)
                    .addComponent(Train)
                    .addComponent(ExtractTextButton)
                    .addComponent(ImportButton)
                    .addComponent(Test)
                    .addComponent(Load)
                    .addComponent(Save)
                    .addComponent(Notice))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LearningRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(PlotChart)
                    .addComponent(ImagePath, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(MaxError, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(MaxIterations, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(EpochSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nnv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE))
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
        String path = Util.choseFile(false, MainPage.this, "Choose an Image.");
        canvas.setImage(path);
        canvas.setOffset(0);
        ImagePath.setText("Path : " + path);
    }//GEN-LAST:event_ImportButtonActionPerformed

    private void TrainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TrainActionPerformed
        try {
            
            String path = Util.choseFile(true, MainPage.this, "Choose the training set image folder.");
            //String path = "/home/mmp/Desktop/foo/test";
            
            try {
                net.setLearningRate(Double.parseDouble(LearningRate.getText()));
                net.setMaxError(Double.parseDouble(MaxError.getText()));
                net.setMaxIterations(Integer.parseInt(MaxIterations.getText()));
                net.setEpochSize(Integer.parseInt(EpochSize.getText()));
            } catch(NumberFormatException ne) {
                notifyUser("Bad training prameter formats");
                return;
            }
        
            if(PlotChart.isSelected()) {
                net.setTrainingCallBack(new CallBack() {
                    @Override
                    public void function(Object param) {
                        plt.addPoint(net.getNetworkError());
                        System.out.println(net.getNetworkError());
                        notifyUser("Error" + net.getNetworkError());
                    }
                });
            }
            
            TrainingSet set= TrainingDataLoader.load(path, 62, featureSetMagic);
            
            net.train(set);
            notifyUser("Training Done");
            if(PlotChart.isSelected()) {
                plt.plot();
                net.setTrainingCallBack(null);
            }
        } catch (IOException ex) {
            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_TrainActionPerformed

    private void ExtractTextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExtractTextButtonActionPerformed

        String output = "";
        double[] op;

        canvas.binarize();

        Segment[] segs = Segmentation.segmentImage(canvas.getImage());
        for (Segment seg : segs) {

            if (seg.getHeight() < 4 || seg.getWidth() < 4) {
                continue;
            }

            Segment tmp = new Segment(
                    seg.getBounded()
                    .resizeImage(100, 100)
                    .getImageData()
            );
            
            net.fpropagate(tmp.getFeature(featureSetMagic).getFeatures());
            op = net.getOutput();

            int mi = 0;
            for (int j = 0; j < op.length; j++) {
                if (op[j] > op[mi]) {
                    mi = j;
                }
            }
            output += " [" + mi + "] : ";
            if (mi < 10) {
                mi += '0';
            } else if (mi < 36 && mi > 9) {
                mi += 'a' - 10;
            } else {
                mi += 'A' - 36;
            }
            
            seg.setDetection((char) mi);
            output += ((char) mi);
        }
        System.out.println(output);
        DocumentCreator dc = new DocumentCreator();
        dc.create(segs);
        dc.flush("output.html");
    }//GEN-LAST:event_ExtractTextButtonActionPerformed

    private void TestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TestActionPerformed
        try {

            int correct = 0, uni = 0;
            int[] opset = new int[62];

            String path = Util.choseFile(true, MainPage.this, "Choose the training set image folder.");
            //String path = "/home/mmp/Desktop/foo/";
            
            TrainingSet set = TrainingDataLoader.load(
                    path, 62, featureSetMagic
            );

            double[] op;
            double[] eop;

            for (int i = 0; i < set.size(); i++) {

                net.fpropagate(set.elementAt(i).getInputVector());
                op = net.getOutput();
                
                eop = set.elementAt(i).getExpectedOutput();
                int mi = 0;
                for (int j = 0; j < op.length; j++) {
                    if (op[j] > op[mi]) {
                        mi = j;
                    }
                }
                
                if (opset[mi] == 0) {
                    uni++;
                }
                opset[mi]++;

                if (eop[mi] == 1) {
                    correct++;
                }
            }
            double accuracy = ((double) correct / set.size()) * 100.0;
            System.out.println(Arrays.toString(opset));
            System.out.println("Unique : " + uni + "");
            System.out.println(correct);
            System.out.println("Accuracy : " + accuracy);
            notifyUser("Accuracy : " + (accuracy+"").substring(0, 5) + "%, Unique : " + uni);
        } catch (IOException ex) {
            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_TestActionPerformed

    private void LoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoadActionPerformed
        try {
            net = NeuralNetwork.load(storePath);
            notifyUser("Loading done");
        } catch (IOException | ClassNotFoundException ex) {
            notifyUser("Invalid file format");
        }
    }//GEN-LAST:event_LoadActionPerformed

    private void SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveActionPerformed
        try {
            net.save(storePath);
            notifyUser("Saved the network");
        } catch (IOException ex) {
            notifyUser("Access Denied!" + ex);
        }
    }//GEN-LAST:event_SaveActionPerformed

    private void notifyUser(String err) {
        Notice.setText(err);
    }
    
    public static void main(String args[]) {
        try {
            javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            System.out.println("Theme error : " + ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new MainPage().setVisible(true);
            } catch (NeuralNetworkException ex) {
                Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField EpochSize;
    private javax.swing.JButton ExtractTextButton;
    private javax.swing.JLabel ImagePath;
    private javax.swing.JButton ImportButton;
    private javax.swing.JTextField LearningRate;
    private javax.swing.JButton Load;
    private javax.swing.JTextField MaxError;
    private javax.swing.JTextField MaxIterations;
    private javax.swing.JLabel Notice;
    private javax.swing.JCheckBox PlotChart;
    private javax.swing.JButton Save;
    private javax.swing.JButton Test;
    private javax.swing.JButton Train;
    private javax.swing.JButton ZoomIn;
    private javax.swing.JButton ZoomOut;
    private mpocr.mCanvas canvas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private mpocr.NNVisualizer nnv;
    // End of variables declaration//GEN-END:variables
}
