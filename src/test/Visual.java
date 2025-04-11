package test;

import java.util.Base64;
import java.util.LinkedList;
import java.util.Scanner;
import algorithm.AES;
import visual.AESVisualizerGUI;

public class Visual {

    private byte[] key;
    private byte[] iv;
    private LinkedList<stateRecord> enc;
    private AES aes;
    private String keyString = "M2Y2ZDYzMmM3NWQzOTkwMDNmZTBmNTA2MmE3NTZkNjk=";
    private String ivString = "M2ZlNDRhOWJiMzMyNmRiYzNmZGYwNzcxNDU3YTFkMzQ=";
    private AESVisualizerGUI visualizerGUI;

    public Visual() {
        initVisual();
    }

    public void initVisual() {
        this.key = getByteStream(keyString);
        this.iv = getByteStream(ivString);
        this.aes = new AES();
        this.enc = new LinkedList<>();
        this.aes.setKey(key);
        this.aes.setIV(iv);
        this.visualizerGUI = new AESVisualizerGUI(this.enc,);
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

    public static void main(String[] args) {

        Visual v = new Visual();
        byte[] enc = v.encrypt("Hello");
        System.out.println("Enc: " + Base64.getEncoder().encodeToString(enc));

        Visual decrypt = new Visual();
        byte[] newText = decrypt.encrypt("Null Value");

        // LinkedList<stateRecord> enc = new LinkedList<>();
        // String text = "Hello, john";
        // AES aes = new AES();
        // text = aes.fillBlock(text);
        // byte[] key = aes.getKey();
        // System.out.println("Key: " + Base64.getEncoder().encodeToString(key));
        // byte[] iv = aes.getIV();
        // System.out.println("Iv: " + Base64.getEncoder().encodeToString(iv));
        // aes.setKey(key);
        // aes.setIV(iv);

        // aes.setSteps(enc);
        // byte[] encode = aes.CBC_encrypt(text.getBytes());
        // enc = aes.getSteps();
        // System.out.println("Enc: " + Base64.getEncoder().encodeToString(encode));

    }
}