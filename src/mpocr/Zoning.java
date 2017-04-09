/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpocr;

/**
 *
 * @author mmp
 */
public class Zoning extends FeatureSet {

    Zoning() {
        magic = 0x0001;
    }
    
    @Override
    public int getmagic() {
        return magic;
    }

    @Override
    public double compare(FeatureSet fs1, FeatureSet fs2) {
        return 0.0;
    }
    
}
