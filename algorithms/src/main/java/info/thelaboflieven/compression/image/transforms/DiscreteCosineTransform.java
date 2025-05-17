package info.thelaboflieven.compression.image.transforms;

public class DiscreteCosineTransform {

    private static double M_PI = Math.PI;

    static double[][] dct(double[][] matrix){
        var N = matrix.length;
        var M = matrix[0].length;
        double[][] dctmatrix = new double[N][M];
        int i, j, u, v;
        for (u = 0; u < N; ++u) {
            for (v = 0; v < M; ++v) {
                dctmatrix[u][v] = 0;
                for (i = 0; i < N; i++) {
                    for (j = 0; j < M; j++) {
                        dctmatrix[u][v] += matrix[i][j] * Math.cos(M_PI/((float)N)*(i+1./2.)*u)*Math.cos(M_PI/((float)M)*(j+1./2.)*v);
                    }
                }
            }
        }
        return dctmatrix;
    }

    static double[][] idct(double[][] DCTMatrix){
        var N = DCTMatrix.length;
        var M = DCTMatrix[0].length;
        int i, j, u, v;
        double[][] Matrix = new double[N][M];

        for (u = 0; u < N; ++u) {
            for (v = 0; v < M; ++v) {
                Matrix[u][v] = 1.0/4 * DCTMatrix[0][0];
                for(i = 1; i < N; i++){
                    Matrix[u][v] += 1.0/2 *DCTMatrix[i][0];
                }
                for(j = 1; j < M; j++){
                    Matrix[u][v] += 1.0/2 *DCTMatrix[0][j];
                }

                for (i = 1; i < N; i++) {
                    for (j = 1; j < M; j++) {
                        Matrix[u][v] += DCTMatrix[i][j] * Math.cos(M_PI/((float)N)*(u+1./2.)*i)*Math.cos(M_PI/((float)M)*(v+1./2.)*j);
                    }
                }
                Matrix[u][v] *= 2./((float)N)*2./((float)M);
            }
        }
        return Matrix;
    }

}
