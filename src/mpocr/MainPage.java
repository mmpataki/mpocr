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
        canvas.zoomIn(1);
                
        plt = new Plotter("error_graph.html", "");
        
        net = new NeuralNetwork(nnnc, new SigmoidFunction(), 0.43, 1);
        net.setMaxError(1.5);
        net.setMaxIterations(800);
        
        net.setTrainingCallBack(new CallBack() {
            @Override
            public void function(Object param) {
                plt.addPoint(net.getNetworkError());
                System.out.println(net.getNetworkError());
            }
        });
        
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
        hack = new javax.swing.JCheckBox();
        Train = new javax.swing.JButton();
        nnv = new mpocr.NNVisualizer();
        ExtractTextButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Output = new javax.swing.JTextArea();
        Test = new javax.swing.JButton();
        Load = new javax.swing.JButton();
        Save = new javax.swing.JButton();

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
            .addGap(0, 406, Short.MAX_VALUE)
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
            .addGap(0, 328, Short.MAX_VALUE)
        );

        ExtractTextButton.setText("Extract Text");
        ExtractTextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExtractTextButtonActionPerformed(evt);
            }
        });

        Output.setColumns(20);
        Output.setRows(5);
        jScrollPane1.setViewportView(Output);

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(ImagePath))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nnv, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(Train)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Test)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Load)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Save)))
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
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
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
                    .addComponent(ImportButton)
                    .addComponent(Test)
                    .addComponent(Load)
                    .addComponent(Save))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(nnv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2))
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
            
            //String path = Util.choseFile(true, MainPage.this, "Choose the training set image folder.");
            String path = "/home/mmp/Desktop/foo/test";
            
            TrainingSet set = TrainingDataLoader.load(
                    path, 62, featureSetMagic
            );
            System.out.println("Loading done....");

            net.train(set);

            System.out.println("Training Done");
            plt.plot();
        } catch (IOException ex) {
            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_TrainActionPerformed

    private void ExtractTextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExtractTextButtonActionPerformed

        String output = "", soutput = "";
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
            soutput += ((char) mi);
        }
        System.out.println(output);
        Output.setText(soutput);
        DocumentCreator dc = new DocumentCreator();
        dc.create(segs);
        dc.flush("output.html");
    }//GEN-LAST:event_ExtractTextButtonActionPerformed

    private void TestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TestActionPerformed
        try {

            int correct = 0, uni = 0;
            int[] opset = new int[62];

            //String path = Util.choseFile(true, MainPage.this, "Choose the training set image folder.");
            String path = "/home/mmp/Desktop/foo/";
            
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

            System.out.println(Arrays.toString(opset));
            System.out.println("Unique : " + uni + "");
            System.out.println(correct);
            System.out.println("Accuracy : " + ((double) correct / set.size()) * 100.0);
        } catch (IOException ex) {
            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_TestActionPerformed

    private void LoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoadActionPerformed
        try {
            net = NeuralNetwork.load(storePath);
        } catch (IOException | ClassNotFoundException ex) {
            notifyUser("Invalid file format");
        }
    }//GEN-LAST:event_LoadActionPerformed

    private void SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveActionPerformed
        try {
            net.save(storePath);
        } catch (IOException ex) {
            notifyUser("Access Denied!" + ex);
        }
    }//GEN-LAST:event_SaveActionPerformed

    private void notifyUser(String err) {
        System.out.println(err);
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
    private javax.swing.JButton ExtractTextButton;
    private javax.swing.JLabel ImagePath;
    private javax.swing.JButton ImportButton;
    private javax.swing.JButton Load;
    private javax.swing.JTextArea Output;
    private javax.swing.JButton Save;
    private javax.swing.JButton Test;
    private javax.swing.JButton Train;
    private javax.swing.JButton ZoomIn;
    private javax.swing.JButton ZoomOut;
    private mpocr.mCanvas canvas;
    private javax.swing.JCheckBox hack;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private mpocr.NNVisualizer nnv;
    // End of variables declaration//GEN-END:variables
}
