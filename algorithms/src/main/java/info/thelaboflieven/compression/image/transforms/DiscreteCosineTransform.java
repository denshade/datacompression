package info.thelaboflieven.compression.image.transforms;

public class DiscreteCosineTransform {

    public static double[] dct(double[] input) {
        int N = input.length;
        double[] output = new double[N];

        // Precompute the scaling factor
        double scale = Math.sqrt(2.0 / N);

        for (int k = 0; k < N; k++) { // For each element of DCT output
            double sum = 0.0;
            double c;
            if (k == 0) {
                c = 1.0 / Math.sqrt(N); // Special scaling factor for k=0
                for (int n = 0; n < N; n++) {
                    sum += input[n] * Math.cos(Math.PI * (n + 0.5) * k / N);
                }
            } else {
                c = scale;
                for (int n = 0; n < N; n++) {
                    sum += input[n] * Math.cos(Math.PI * (n + 0.5) * k / N);
                }
            }

            output[k] = c * sum;
        }

        return output;
    }

    public static double[] idct(double[] input) {
        int N = input.length;
        double[] output = new double[N];

        // Precompute the scaling factor for k=0, which is different from other coefficients
        double scale = Math.sqrt(2.0 / N);

        for (int n = 0; n < N; n++) { // For each element of the IDCT output
            double sum = 0.0;
            double c;
            if (n == 0) {
                c = 1.0 / Math.sqrt(N); // Special scaling factor for n=0
                for (int k = 0; k < N; k++) {
                    sum += input[k] * Math.cos(Math.PI * (k + 0.5) * n / N);
                }
            } else {
                c = scale;
                for (int k = 0; k < N; k++) {
                    sum += input[k] * Math.cos(Math.PI * (k + 0.5) * n / N);
                }
            }

            output[n] = c * sum;
        }

        return output;
    }


}
