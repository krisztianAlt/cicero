package com.cicero;

import java.util.List;

public class Move {

    private List<WordCard> movedWordCards;
    private Pile movedFromPile;
    private boolean lastNotToBeMoved;
    private boolean lastWasFaceDown;

    public Move(List<WordCard> movedWordCards, Pile movedFromPile) {
        this.movedWordCards = movedWordCards;
        this.movedFromPile = movedFromPile;
        this.lastNotToBeMoved = false;
        this.lastWasFaceDown = false;
    }

    /*public Move(List<WordCard> movedWordCards, Pile movedFromPile, boolean wasFaceDown, boolean lastNotToBeMoved, boolean lastWasFaceDown) {
        this.movedWordCards = movedWordCards;
        this.movedFromPile = movedFromPile;
        this.wasFaceDown = wasFaceDown;
        this.lastNotToBeMoved = lastNotToBeMoved;
        this.lastWasFaceDown = lastWasFaceDown;
    }*/

    public List<WordCard> getMovedWordCards() {
        return movedWordCards;
    }

    public Pile getMovedFromPile() {
        return movedFromPile;
    }


    public boolean isLastNotToBeMoved() {
        return lastNotToBeMoved;
    }

    public boolean wasLastFaceDown() {
        return lastWasFaceDown;
    }
}

