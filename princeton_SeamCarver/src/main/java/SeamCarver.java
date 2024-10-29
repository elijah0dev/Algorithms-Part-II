import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;

public class SeamCarver {
    private Picture picture;
    private int width;
    private int height;
    private double energy[][];

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();

        this.picture = picture;
        this.width = picture.width();
        this.height = picture.height();
        calculateEnergy();
    }

    private void calculateEnergy() {
        energy = new double[height][width];

        for (int i = 0; i < width; i++) {
            energy[0][i] = 1000;
            energy[height - 1][i] = 1000;
        }
        for (int i = 0; i < height; i++) {
            energy[i][0] = 1000;
            energy[i][width - 1] = 1000;
        }
        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                energy[j][i] = Math.sqrt(energyX(i, j) + energyY(i, j));
            }
        }
    }

    private double energyX(int x, int y) {
        if (x <= 0 || x >= width - 1) return 1000;

        int topPixelRGB = picture.getRGB(x, y + 1);
        int bottomPixelRGB = picture.getRGB(x, y - 1);
        return calcGradient(topPixelRGB, bottomPixelRGB);
    }

    private double energyY(int x, int y) {
        if (y <= 0 || y >= height - 1) return 1000;
        int leftPixelRGB = picture.getRGB(x - 1, y);
        int rightPixelRGB = picture.getRGB(x + 1, y);
        return calcGradient(rightPixelRGB, leftPixelRGB);
    }

    private double calcGradient(int firstPixel, int secondPixel) {
        double redDifference = Math.abs(decodeRed(firstPixel) - decodeRed(secondPixel));
        double greenDifference = Math.abs(decodeGreen(firstPixel) - decodeGreen(secondPixel));
        double blueDifference = Math.abs(decodeBlue(firstPixel) - decodeBlue(secondPixel));
        return redDifference * redDifference + greenDifference * greenDifference + blueDifference * blueDifference;
    }

    private int decodeRed(int rgb) {
        return (rgb >> 16) & 0xFF;
    }

    private int decodeGreen(int rgb) {
        return (rgb >> 8) & 0xFF;
    }

    private int decodeBlue(int rgb) {
        return rgb & 0xFF;
    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x > width - 1 || y < 0 || y > height - 1) throw new IllegalArgumentException();
        return energy[y][x];
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        double[][] distTo = new double[height][width];
        int[][] edgeTo = new int[height][width];

        // Initialize arrays
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                distTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        // Set the first column
        for (int i = 0; i < height; i++) {
            distTo[i][0] = 1000; // Arbitrary large value for initialization
            edgeTo[i][0] = i;
        }

        // Fill the distTo and edgeTo tables
        for (int j = 1; j < width; j++) {
            for (int i = 0; i < height; i++) {
                // Relax left
                if (i > 0 && distTo[i][j] > energy[i - 1][j] + distTo[i - 1][j - 1]) {
                    distTo[i][j] = energy[i - 1][j] + distTo[i - 1][j - 1];
                    edgeTo[i][j] = i - 1;
                }
                // Relax bottom left
                if (i < height - 1 && distTo[i][j] > energy[i + 1][j] + distTo[i + 1][j - 1]) {
                    distTo[i][j] = energy[i + 1][j] + distTo[i + 1][j - 1];
                    edgeTo[i][j] = i + 1;
                }
                // Relax top left
                if (i > 0 && distTo[i][j] > energy[i - 1][j] + distTo[i - 1][j - 1]) {
                    distTo[i][j] = energy[i - 1][j] + distTo[i - 1][j - 1];
                    edgeTo[i][j] = i - 1;
                }
            }
        }

        // Find the minimum value in the last column
        double min = Double.POSITIVE_INFINITY;
        int minIndex = -1;
        for (int i = 0; i < height; i++) {
            if (distTo[i][width - 1] < min) {
                min = distTo[i][width - 1];
                minIndex = i;
            }
        }

        // Backtrack to find the seam
        Stack<Integer> path = new Stack<>();
        int currentRow = minIndex;
        int currentCol = width - 1;

        while (currentCol >= 0) {
            path.push(currentRow);
            currentRow = edgeTo[currentRow][currentCol];
            currentCol--;
        }

        int[] seam = new int[width];
        int index = 0;
        while (!path.isEmpty()) {
            seam[index++] = path.pop();
        }

        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[][] edgeTo = new int[height][width + 2];
        double[][] distTo = new double[height][width + 2];

        for (int i = 1; i < height; i++) {
            for (int j = 0; j < width + 2; j++) {
                distTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        for (int i = 0; i < width + 2; i++) {
            distTo[0][i] = 1000;
            edgeTo[0][i] = i;
        }

        for (int i = 1; i < height; i++) {
            for (int j = 1; j < width + 1; j++) {
                //relax top edge
                if (distTo[i][j] > energy[i][j - 1] + distTo[i - 1][j]) {
                    edgeTo[i][j] = j - 1;
                    distTo[i][j] = energy[i][j - 1] + distTo[i - 1][j];
                }
                //relax top-left edge
                if (distTo[i][j] > energy[i][j - 1] + distTo[i - 1][j - 1]) {
                    edgeTo[i][j] = j - 2;
                    distTo[i][j] = energy[i][j - 1] + distTo[i - 1][j - 1];
                }
                //relax top-right
                if (distTo[i][j] > energy[i][j - 1] + distTo[i - 1][j + 1]) {
                    edgeTo[i][j] = j;
                    distTo[i][j] = energy[i][j - 1] + distTo[i - 1][j + 1];
                }
            }
        }

        int minIndex = 0;
        double min = Double.POSITIVE_INFINITY;
        for (int i = 1; i < width + 1; i++) {
            if (min > distTo[height - 1][i]) {
                min = distTo[height - 1][i];
                minIndex = i;
            }
        }

        Stack<Integer> path = new Stack<>();
        path.push(minIndex - 1);
        int currentHeight = height - 1;
        int currentStep = edgeTo[currentHeight][minIndex];
        while (currentHeight > 0) {
            path.push(currentStep);
            currentStep = edgeTo[--currentHeight][currentStep + 1];
        }

        int[] seam = new int[height];
        int currentItem = 0;
        while (!path.isEmpty()) {
            seam[currentItem++] = path.pop();
        }

        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || seam.length != width || height == 1) throw new IllegalArgumentException();
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) throw new IllegalArgumentException();
        }
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= height) throw new IllegalArgumentException();
        }

        Picture newPicture = new Picture(width, --height);
        for (int j = 0; j < width; j++) {
            int offset = 0;
            int i = 0;
            while (i < height) {
                if (seam[j] == i) offset = 1;
                newPicture.setRGB(j, i, picture.getRGB(j, i + offset));
                i++;
            }
        }
        picture = new Picture(newPicture);
        calculateEnergy();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || seam.length != height || width == 1) throw new IllegalArgumentException();
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) throw new IllegalArgumentException();
        }
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= width) throw new IllegalArgumentException();
        }

        Picture newPicture = new Picture(--width, height);
        for (int i = 0; i < height; i++) {
            int offset = 0;
            int j = 0;
            while (j < width) {
                if (seam[i] == j) offset++;
                newPicture.setRGB(j, i, picture.getRGB(j + offset, i));
                j++;
            }
        }
        picture = new Picture(newPicture);
        calculateEnergy();
    }

    //  unit testing (optional)
    public static void main(String[] args) {

    }
}
