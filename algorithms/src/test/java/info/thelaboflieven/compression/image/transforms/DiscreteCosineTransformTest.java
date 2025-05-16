package info.thelaboflieven.compression.image.transforms;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

class DiscreteCosineTransformTest {
    @Test
    void check() {
        var res = DiscreteCosineTransform.idct(DiscreteCosineTransform.dct(new double[]{100.0,200.0,123.0}));
        for (double d : res){
            System.out.println(d+",");
        }
    }

}