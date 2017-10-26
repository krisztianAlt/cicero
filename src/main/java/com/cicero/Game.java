package com.cicero;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.*;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;

import static com.cicero.Pile.PileType.*;

public class Game extends Pane {

    private int difficultyLevel;

    private List<WordCard> deck = new ArrayList<>();

    private Pile stackPile;
    private Pile wordPile1;
    private Pile wordPile2;
    private Pile wordPile3;
    private Pile wordPile4;
    private Pile wordPile5;
    private Pile wordPile6;
    private Pile wordPile7;
    private Pile wordPile8;

    private Pile discardPile;
    // private List<Pile> foundationPiles = FXCollections.observableArrayList();
    // private List<Pile> tableauPiles = FXCollections.observableArrayList();

    private double dragStartX, dragStartY;
    private List<WordCard> draggedWordCards = FXCollections.observableArrayList();

    private static double STACK_GAP = 1;
    private static double WORD_CARD_PLACE_GAP = 1;

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    /*private EventHandler<MouseEvent> onMouseClickedHandler = e -> {
        WordCard wordCard = (WordCard) e.getSource();
        Pile originPile = wordCard.getContainingPile();

        int clickCount = e.getClickCount();

        if (clickCount == 1 && originPile.getPileType().equals(STACK) &&
                wordCard.equals(stackPile.getTopCard())) {
            wordCard.moveToPile(discardPile);
            wordCard.setMouseTransparent(false);
            System.out.println("Placed " + wordCard + " to the waste.");


        }

        *//*if (clickCount == 1 && originPile.getPileType().equals(STACK) &&
                wordCard.equals(stackPile.getTopCard())) {
            wordCard.moveToPile(discardPile);
            wordCard.setMouseTransparent(false);
            System.out.println("Placed " + wordCard + " to the waste.");


        }*//*
    };*/


    private EventHandler<MouseEvent> onMousePressedHandler = e -> {
        dragStartX = e.getSceneX();
        dragStartY = e.getSceneY();
    };

    private EventHandler<MouseEvent> onMouseDraggedHandler = e -> {
        WordCard wordCard = (WordCard) e.getSource();

        Pile activePile = wordCard.getContainingPile();
        if (activePile.getPileType().equals(STACK))
            return;
        /*if (activePile.getPileType().equals(DISCARD) && wordCard != activePile.getTopCard())
            return;*/
        double offsetX = e.getSceneX() - dragStartX;
        double offsetY = e.getSceneY() - dragStartY;

        draggedWordCards.clear();
        draggedWordCards.add(wordCard);
        dragCard(wordCard, offsetX, offsetY);

        // If tableau pile and not last dragged, drag all below wordCard as well
        /*if (activePile.getPileType().equals(TABLEAU) && (wordCard != activePile.getTopCard())) {
            List<WordCard> tableauWordCards = activePile.getWordCards();
            int indexOfDraggedCard = tableauWordCards.indexOf(wordCard);
            ListIterator<WordCard> listIterator = tableauWordCards.listIterator(indexOfDraggedCard+1);
            while (listIterator.hasNext()) {
                WordCard draggedWordCard = listIterator.next();
                draggedWordCards.add(draggedWordCard);
                dragCard(draggedWordCard, offsetX, offsetY);
            }
        }*/
    };

    private void dragCard(WordCard draggedWordCard, double offsetX, double offsetY) {
        draggedWordCard.getDropShadow().setRadius(20);
        draggedWordCard.getDropShadow().setOffsetX(10);
        draggedWordCard.getDropShadow().setOffsetY(10);
        draggedWordCard.toFront();
        draggedWordCard.setTranslateX(offsetX);
        draggedWordCard.setTranslateY(offsetY);
    }

    private EventHandler<MouseEvent> onMouseReleasedHandler = e -> {
        if (draggedWordCards.isEmpty())
            return;
        WordCard wordCard = (WordCard) e.getSource();
        Pile originalPile = wordCard.getContainingPile();

        List<Pile> targetPiles = new ArrayList<>();
        targetPiles.add(stackPile);
        /*List<Pile> targetPiles = FXCollections.observableArrayList(tableauPiles);
        targetPiles.addAll(foundationPiles);*/
        Pile pile = getValidIntersectingPile(wordCard, targetPiles);

        if (pile != null) {

            // save originPile before action
            Pile originPile = wordCard.getContainingPile();

            List<WordCard> wordCardList = new LinkedList<>();
            // System.out.println(draggedWordCards);
            wordCardList.addAll(draggedWordCards);

            // do action
            handleValidMove(wordCard, pile);


        } else {
            draggedWordCards.forEach(MouseUtil::slideBack);
            draggedWordCards.clear();
        }
    };


    public boolean isActualLevelSucceeded(){
        return true;
    }

    public boolean isGameWon() {

        boolean gameWon = false;
        int counter = 0;

        /*for (Pile pile : foundationPiles) {
            if (pile.numOfCards() == 13) {
                counter++;
            }
            if (counter == 3 && pile.numOfCards() == 12) {
                gameWon = true;
            }
        }*/

        return gameWon;
    }

    public void checkProceedToNextLevel() {
        if (isActualLevelSucceeded()) {
            System.out.println("ENTERED inside if isActualLevelSucceeded");
            //TODO: remove all event listeners!
            showLevelSucceeded();
        }
    }

    public void checkGameWon() {
        if (isGameWon()) {
            System.out.println("ENTERED inside if isGameWon");
            //TODO: remove all event listeners!
            showPrize();
        }
    }

    public Game() {
        deck = WordCard.createNewDeck();
        Collections.shuffle(deck);

        for (int index = deck.size() -1; index >= 8; index--){
            deck.remove(index);
        }

        addEventListeners(deck);
        initPiles();
        dealWordCards();
    }

    private void addEventListeners(List<WordCard> deck) {
        for (WordCard wordCard : deck) {
            wordCard.setOnMousePressed(onMousePressedHandler);
            wordCard.setOnMouseDragged(onMouseDraggedHandler);
            wordCard.setOnMouseReleased(onMouseReleasedHandler);
            // wordCard.setOnMouseClicked(onMouseClickedHandler);
            this.getChildren().add(wordCard);
        }
    }


    public boolean isMoveValid(WordCard wordCard, Pile destPile) {

        WordCard topWordCard = destPile.getTopCard();
        boolean validMove = true;



        /*if (topWordCard != null) {
            int rankDifference = wordCard.getRank().compareTo(topWordCard.getRank());
            switch (destPile.getPileType()) {

                case TABLEAU:
                    if (WordCard.isOppositeColor(wordCard, topWordCard) && (rankDifference == -1)) {
                        validMove = true;
                    }
                    break;

                case FOUNDATION:
                    if (WordCard.isSameSuit(wordCard, topWordCard) && (rankDifference == 1)) {
                        validMove = true;
                    }
                    break;
            }
        } else {

            switch (destPile.getPileType()) {

                case TABLEAU:
                    if (wordCard.getRank() == KING) {
                        validMove = true;
                    }
                    break;

                case FOUNDATION:
                    if (wordCard.getRank() == ACE) {
                        validMove = true;
                    }
                    break;
            }
        }*/

        return validMove;
    }


    private Pile getValidIntersectingPile(WordCard wordCard, List<Pile> piles) {
        Pile result = null;
        for (Pile pile : piles) {
            if (!pile.equals(wordCard.getContainingPile()) &&
                    isOverPile(wordCard, pile) &&
                    isMoveValid(wordCard, pile))
                result = pile;
        }
        return result;
    }

    private boolean isOverPile(WordCard wordCard, Pile pile) {
        if (pile.isEmpty())
            return wordCard.getBoundsInParent().intersects(pile.getBoundsInParent());
        else
            return wordCard.getBoundsInParent().intersects(pile.getTopCard().getBoundsInParent());
    }

    private void handleValidMove(WordCard wordCard, Pile destPile) {
        String msg = null;
        if (destPile.isEmpty()) {
            /*if (destPile.getPileType().equals(Pile.PileType.FOUNDATION))
                msg = String.format("Placed %s to the foundation.", wordCard);

            if (destPile.getPileType().equals(TABLEAU))
                msg = String.format("Placed %s to a new pile.", wordCard);*/

        } else {
            msg = String.format("Placed %s to %s.", wordCard, destPile.getTopCard());
        }
        // System.out.println(msg);
        MouseUtil.slideToDest(draggedWordCards, destPile);
        draggedWordCards.clear();
    }

    private void showLevelSucceeded() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("You proceed to the next level!");
        String message ="Just relax... :)";
        alert.setContentText(message);
        alert.show();
    }
    private void showPrize() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success!!!");
        alert.setHeaderText("YOU WON!");
        String message ="Congratulations!!";
        alert.setContentText(message);
        alert.show();
    }


    private void initPiles() {

        int firstColumn = 60;
        int secondColumn = 500;
        int thirdColumn = 950;

        int firstRow = 30;
        int secondRow = 250;
        int thirdRow = 470;

        stackPile = new Pile(STACK, "Stack", STACK_GAP);
        stackPile.setBlurredBackground(true);
        stackPile.setLayoutX(secondColumn);
        stackPile.setLayoutY(secondRow);
        getChildren().add(stackPile);

        wordPile1 = new Pile(Pile.PileType.WORDCARDPLACE1, "Word place 1", WORD_CARD_PLACE_GAP);
        wordPile1.setBlurredBackground(false);
        wordPile1.setLayoutX(firstColumn);
        wordPile1.setLayoutY(firstRow);
        getChildren().add(wordPile1);

        wordPile2 = new Pile(WORDCARDPLACE2, "Word place 2", WORD_CARD_PLACE_GAP);
        wordPile2.setBlurredBackground(false);
        wordPile2.setLayoutX(secondColumn);
        wordPile2.setLayoutY(firstRow);
        getChildren().add(wordPile2);

        wordPile3 = new Pile(WORDCARDPLACE3, "Word place 3", WORD_CARD_PLACE_GAP);
        wordPile3.setBlurredBackground(false);
        wordPile3.setLayoutX(thirdColumn);
        wordPile3.setLayoutY(firstRow);
        getChildren().add(wordPile3);

        wordPile4 = new Pile(WORDCARDPLACE4, "Word place 4", WORD_CARD_PLACE_GAP);
        wordPile4.setBlurredBackground(false);
        wordPile4.setLayoutX(firstColumn);
        wordPile4.setLayoutY(secondRow);
        getChildren().add(wordPile4);

        wordPile5 = new Pile(WORDCARDPLACE5, "Word place 5", WORD_CARD_PLACE_GAP);
        wordPile5.setBlurredBackground(false);
        wordPile5.setLayoutX(thirdColumn);
        wordPile5.setLayoutY(secondRow);
        getChildren().add(wordPile5);

        wordPile6 = new Pile(WORDCARDPLACE6, "Word place 6", WORD_CARD_PLACE_GAP);
        wordPile6.setBlurredBackground(false);
        wordPile6.setLayoutX(firstColumn);
        wordPile6.setLayoutY(thirdRow);
        getChildren().add(wordPile6);

        wordPile7 = new Pile(WORDCARDPLACE7, "Word place 7", WORD_CARD_PLACE_GAP);
        wordPile7.setBlurredBackground(false);
        wordPile7.setLayoutX(secondColumn);
        wordPile7.setLayoutY(thirdRow);
        getChildren().add(wordPile7);

        wordPile8 = new Pile(WORDCARDPLACE8, "Word place 8", WORD_CARD_PLACE_GAP);
        wordPile8.setBlurredBackground(false);
        wordPile8.setLayoutX(thirdColumn);
        wordPile8.setLayoutY(thirdRow);
        getChildren().add(wordPile8);

        /*discardPile = new Pile(Pile.PileType.DISCARD, "Discard", STOCK_GAP);
        discardPile.setBlurredBackground();
        discardPile.setLayoutX(285);
        discardPile.setLayoutY(50);
        getChildren().add(discardPile);

        for (int i = 0; i < 4; i++) {
            Pile foundationPile = new Pile(Pile.PileType.FOUNDATION, "Foundation " + i, FOUNDATION_GAP);
            foundationPile.setBlurredBackground();
            foundationPile.setLayoutX(610 + i * 180);
            foundationPile.setLayoutY(50);
            foundationPiles.add(foundationPile);
            getChildren().add(foundationPile);
        }
        for (int i = 0; i < 7; i++) {
            Pile tableauPile = new Pile(TABLEAU, "Tableau " + i, TABLEAU_GAP);
            tableauPile.setBlurredBackground();
            tableauPile.setLayoutX(95 + i * 180);
            tableauPile.setLayoutY(325);
            tableauPiles.add(tableauPile);
            getChildren().add(tableauPile);
        }*/
    }

    public void dealWordCards() {

        wordPile1.addCard(deck.get(0));
        wordPile2.addCard(deck.get(1));
        wordPile3.addCard(deck.get(2));
        wordPile4.addCard(deck.get(3));
        wordPile5.addCard(deck.get(4));
        wordPile6.addCard(deck.get(5));
        wordPile7.addCard(deck.get(6));
        wordPile8.addCard(deck.get(7));

        /*Iterator<WordCard> deckIterator = deck.iterator();
        //TODO

        int cardNumber = 0;
        int columnIndex = 0;
        int rowIndex = 0;

        for (WordCard wordCard : deck) {
            if (cardNumber < 28) {
                tableauPiles.get(columnIndex).addCard(wordCard);
                columnIndex++;
                if (columnIndex == 7) {
                    rowIndex = rowIndex + 1;
                    columnIndex = rowIndex;
                }

            } else {
                stackPile.addCard(wordCard);
            }
            cardNumber++;
        }*/

    }

    public void setTableBackground(Image tableBackground, boolean autoBackgroundSize) {
        setBackground(new Background(new BackgroundImage(tableBackground,
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.CENTER, autoBackgroundSize == true ? new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, true) : BackgroundSize.DEFAULT)));
    }

    public void restart(boolean withShuffle) {
        System.out.println("RESTARTING GAME");


        /*for (Pile tableauPile : tableauPiles){
            tableauPile.clear();
        }

        for (Pile tableauPile : foundationPiles){
            tableauPile.clear();
        }*/

        stackPile.clear();

        wordPile1.clear();
        wordPile2.clear();
        wordPile3.clear();
        wordPile4.clear();
        wordPile5.clear();
        wordPile6.clear();
        wordPile7.clear();
        wordPile8.clear();

        if (withShuffle) {
            Collections.shuffle(deck);
        }

        dealWordCards();
    }


}
