package info.thelaboflieven.compression.image.transforms;

public class DiscreteSineTransform {

    static double[][] dst2(double[][] matrix) {
        int N = matrix.length;
        int M = matrix[0].length;
        double[][] dst = new double[N][M];

        for (int u = 0; u < N; u++) {
            for (int v = 0; v < M; v++) {
                double sum = 0.0;
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < M; j++) {
                        sum += matrix[i][j] *
                                Math.sin(Math.PI * (i + 0.5) * (u + 1) / N) *
                                Math.sin(Math.PI * (j + 0.5) * (v + 1) / M);
                    }
                }
                dst[u][v] = sum * 2.0; // Scaling factor to match most DST-II norms
            }
        }

        return dst;
    }

    static double[][] idst2(double[][] matrix) {
        int N = matrix.length;
        int M = matrix[0].length;
        double[][] idst = new double[N][M];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                double sum = 0.0;
                for (int u = 0; u < N; u++) {
                    for (int v = 0; v < M; v++) {
                        sum += matrix[u][v] *
                                Math.sin(Math.PI * (i + 0.5) * (u + 1) / N) *
                                Math.sin(Math.PI * (j + 0.5) * (v + 1) / M);
                    }
                }
                idst[i][j] = sum * (2.0 / N) * (2.0 / M); // Normalize
            }
        }

        return idst;
    }
}
