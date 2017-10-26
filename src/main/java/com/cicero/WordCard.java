package com.cicero;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class WordCard extends ImageView {

    private int fileNumber;
    private Image frontFace;
    private Pile containingPile;
    private DropShadow dropShadow;

    public static final int WIDTH = 263; // original: 263 pixels
    public static final int HEIGHT = 206; // original: 206 pixels

    public WordCard(int fileNumber) {
        this.fileNumber = fileNumber;
        this.dropShadow = new DropShadow(2, Color.gray(0, 0.75));
        frontFace = loadCardImage(fileNumber);
        setImage(frontFace);
        setEffect(dropShadow);
    }


    public Image getFrontFace() {
        return frontFace;
    }

    public DropShadow getDropShadow() {
        return this.dropShadow;
    }

    public Pile getContainingPile() {
        return this.containingPile;
    }

    public void setContainingPile(Pile containingPile) {
        this.containingPile = containingPile;
    }

    public void moveToPile(Pile destPile) {
        this.getContainingPile().getWordCards().remove(this);
        destPile.addCard(this);
    }


    public static List<WordCard> createNewDeck() {
        List<WordCard> result = new ArrayList<>();
        for (int fileNumber = 1; fileNumber < 41; fileNumber++) {
            result.add(new WordCard(fileNumber));
        }
        return result;
    }

    public Image loadCardImage(int fileNumber) {
        String wordCardName = "word_" + Integer.toString(fileNumber);
        String imageFileName = "word_images/" + wordCardName + ".png";
        return new Image(imageFileName, 263, 206, true, true);
    }

    @Override
    public String toString() {
        return  "WordCard{" +
                "fileNumber=" + fileNumber +
                ", frontFace=" + frontFace +
                ", containingPile=" + containingPile +
                ", dropShadow=" + dropShadow +
                '}';
    }
}
