package info.thelaboflieven.compression.image.transforms;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

class DiscreteSineTransformTest {
    @Test
    void check() {
        double m[][]={
                {0*0,1*0,2*0,3*0},
                {0*1,1*1,2*1,3*1},
                {0*2,1*2,2*2,3*2},
                {0*3,1*3,2*3,3*3}
        };
        var res = DiscreteSineTransform.idst2(DiscreteSineTransform.dst2(m));
        for (double[] d : res){
            for (double b : d){
                System.out.print(b+",");
            }
            System.out.println();
        }
    }

}