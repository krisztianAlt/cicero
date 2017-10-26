package com.cicero;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Pile extends Pane {

    private PileType pileType;
    private String name;
    private double cardGap;
    private ObservableList<WordCard> wordCards = FXCollections.observableArrayList();

    public Pile(PileType pileType, String name, double cardGap) {
        this.pileType = pileType;
        this.name = name;
        this.cardGap = cardGap;
    }

    public PileType getPileType() {
        return pileType;
    }

    public double getCardGap() {
        return cardGap;
    }

    public ObservableList<WordCard> getWordCards() {
        return wordCards;
    }

    public int numOfCards() {

        return wordCards.size();
    }


    public boolean isEmpty() {
        return wordCards.isEmpty();
    }

    public void clear() {
        wordCards.clear();
    }


    public void addCard(WordCard wordCard) {
        wordCards.add(wordCard);
        wordCard.setContainingPile(this);
        wordCard.toFront();
        layoutCard(wordCard);
    }

    private void layoutCard(WordCard wordCard) {
        wordCard.relocate(wordCard.getLayoutX() + wordCard.getTranslateX(), wordCard.getLayoutY() + wordCard.getTranslateY());
        wordCard.setTranslateX(0);
        wordCard.setTranslateY(0);
        wordCard.setLayoutX(getLayoutX());
        wordCard.setLayoutY(getLayoutY() + (wordCards.size() - 1) * cardGap);
    }

    public WordCard getTopCard() {
        if (wordCards.isEmpty())
            return null;
        else
            return wordCards.get(wordCards.size() - 1);
    }

    public void setBlurredBackground(boolean isStack) {
        setPrefSize(WordCard.WIDTH, WordCard.HEIGHT);
        BackgroundFill backgroundFill = new BackgroundFill(Color.gray(0.0, 0.2), null, null);

        if (isStack){
            backgroundFill = new BackgroundFill(Color.color(0.3961, 0.2627, 0.1294, 0.4392), null, null);
        }

        Background background = new Background(backgroundFill);
        GaussianBlur gaussianBlur = new GaussianBlur(10);
        setBackground(background);
        setEffect(gaussianBlur);
    }

    public enum PileType {
        STACK,
        WORDCARDPLACE
    }

    @Override
    public String toString() {
        return "Pile{" +
                "pileType=" + pileType +
                ", name='" + name + '\'' +
                ", cardGap=" + cardGap + "}";
    }
}
