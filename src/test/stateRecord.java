package test;

public class stateRecord {
    private String plainText;
    private String encryptedText;
    private String roundName;
    private int[][] subBytesMatrix;
    private int[][] shiftRowsMatrix;
    private int[][] mixColumnsMatrix;
    private int[][] addRoundKeyMatrix;

    public stateRecord(String roundName) {
        this.roundName = roundName;
        this.subBytesMatrix = null;
        this.shiftRowsMatrix = null;
        this.mixColumnsMatrix = null;
        this.addRoundKeyMatrix = null;
    }

    // 🔠 Plain and Encrypted Text Setters/Getters
    public void setPlainText(String text) {
        this.plainText = text;
    }

    public String getPlainText() {
        return plainText;
    }

    public void setEncryptedText(String text) {
        this.encryptedText = text;
    }

    public String getEncryptedText() {
        return encryptedText;
    }

    // 🔄 AES Round Matrix Setters
    public void setSubBytesMatrix(int[][] matrix) {
        this.subBytesMatrix = matrix;
    }

    public void setShiftRowsMatrix(int[][] matrix) {
        this.shiftRowsMatrix = matrix;
    }

    public void setMixColumnsMatrix(int[][] matrix) {
        this.mixColumnsMatrix = matrix;
    }

    public void setAddRoundKeyMatrix(int[][] matrix) {
        this.addRoundKeyMatrix = matrix;
    }

    // 🔍 AES Round Matrix Getters
    public int[][] getSubBytesMatrix() {
        return subBytesMatrix;
    }

    public int[][] getShiftRowsMatrix() {
        return shiftRowsMatrix;
    }

    public int[][] getMixColumnsMatrix() {
        return mixColumnsMatrix;
    }

    public int[][] getAddRoundKeyMatrix() {
        return addRoundKeyMatrix;
    }

    public String getRoundName() {
        return roundName;
    }

    // 🚀 Print AES Step Matrix for GUI Visualization
    public String printState() {
        StringBuilder sb = new StringBuilder();
        sb.append("========== ").append(roundName).append(" ==========\n");

        sb.append("SubBytes:\n");
        sb.append(subBytesMatrix != null ? formatMatrix(subBytesMatrix) : "Skipped\n");

        sb.append("ShiftRows:\n");
        sb.append(shiftRowsMatrix != null ? formatMatrix(shiftRowsMatrix) : "Skipped\n");

        sb.append("MixColumns:\n");
        sb.append(mixColumnsMatrix != null ? formatMatrix(mixColumnsMatrix) : "Skipped\n");

        sb.append("AddRoundKey:\n");
        sb.append(addRoundKeyMatrix != null ? formatMatrix(addRoundKeyMatrix) : "Skipped\n");

        return sb.toString();
    }

    // 🛠 Helper Method: Format Matrix for Display
    private String formatMatrix(int[][] matrix) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : matrix) {
            for (int value : row) {
                sb.append(String.format("%02X ", value)); // Hex format
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
