package visual;

import java.util.Base64;
import java.util.LinkedList;
import java.util.Scanner;
import algorithm.AES;
import visual.AESVisualizerGUI;
import test.stateRecord;

public class Visual {

    private byte[] key;
    private byte[] iv;
    private LinkedList<stateRecord> enc;
    private AES aes;
    private String keyString = "M2Y2ZDYzMmM3NWQzOTkwMDNmZTBmNTA2MmE3NTZkNjk=";
    private String ivString = "M2ZlNDRhOWJiMzMyNmRiYzNmZGYwNzcxNDU3YTFkMzQ=";
    private AESVisualizerGUI visualizerGUI;
    private LinkedList<stateRecord> dec;
    private String processName;

    public Visual(String processName) {
        this.processName = processName;
        initVisual();
    }

    public void initVisual() {

        this.key = getByteStream(keyString);
        this.iv = getByteStream(ivString);
        this.aes = new AES();
        this.enc = new LinkedList<>();
        this.aes.setKey(key);
        this.aes.setIV(iv);
        this.visualizerGUI = new AESVisualizerGUI(this.enc, this.processName);
        this.dec = new LinkedList<>();
    }

    public byte[] getByteStream(String text) {
        byte[] encoded = Base64.getDecoder().decode(text);

        return encoded;
    }

    public byte[] encrypt(String text) {

        if (this.enc.size() > 0) {
            this.enc.clear();
        }

        text = this.aes.fillBlock(text);
        this.aes.setSteps(this.enc);
        byte[] encryptedText = this.aes.CBC_encrypt(text.getBytes());

        this.enc = this.aes.getSteps();
        this.visualizerGUI.updateVisualization(this.enc);
        // System.out.println("Enc: " +
        // Base64.getEncoder().encodeToString(encryptedText));

        return encryptedText;

    }

    public String decrypt(String encryptedMsg) {

        if (this.dec.size() > 0) {
            this.dec.clear();
        }
        this.aes.setDecryptionSteps(dec);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedMsg);

        byte[] decryptedBytes = this.aes.CBC_decrypt(encryptedBytes);
        this.dec = this.aes.getDecryptionSteps();
        this.visualizerGUI.updateVisualization(dec);
        return new String(decryptedBytes);
    }

}
