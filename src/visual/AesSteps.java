package visual;

public class AesSteps {
    public String stepName;
    public int[][] dataState;

    public AesSteps(String stepName, int[][] dataState) {
        this.stepName = stepName;
        this.dataState = dataState;
    }

    public void printState() {
        System.out.println("Step: " + stepName);
        for (int[] row : dataState) {
            for (int col : row) {
                System.out.print(String.format("%02X ", col)); // Format as hex
            }
            System.out.println();
        }
        System.out.println("----------------------------");
    }
}