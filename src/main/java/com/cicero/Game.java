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

    private List<Pile> wordPiles = FXCollections.observableArrayList();

    private Pile discardPile;
    // private List<Pile> foundationPiles = FXCollections.observableArrayList();
    // private List<Pile> tableauPiles = FXCollections.observableArrayList();

    private double dragStartX, dragStartY;
    private List<WordCard> draggedWordCards = FXCollections.observableArrayList();

    private static double STACK_GAP = 1;
    private static double WORD_CARD_PLACE_GAP = 1;

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

    public Game(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;

        if (difficultyLevel == 1){
            WordCard.WIDTH = DifficultyLevels.STUDENT.getCardWidth();
            WordCard.HEIGHT = DifficultyLevels.STUDENT.getCardHeight();
            deck = WordCard.createNewDeck(DifficultyLevels.STUDENT.getWordNumber());
        } else if(difficultyLevel == 2){
            WordCard.WIDTH = DifficultyLevels.MASTER.getCardWidth();
            WordCard.HEIGHT = DifficultyLevels.MASTER.getCardHeight();
            deck = WordCard.createNewDeck(DifficultyLevels.MASTER.getWordNumber());
        }

        Collections.shuffle(deck);

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
        int firstColumnStartPos = 60;
        int firstRowStartPos = 30;

        double nextColumnStartPos;
        double nextRowStartPos;

        int numberOfColumns = 0;
        int numberOfRows = 0;
        int numberOfWordPiles = 1;
        double colGap = 0;
        double rowGap = 0;

        int stackPileColumn = 0;
        int stackPileRow = 0;

        if (difficultyLevel == 1){
            numberOfColumns = DifficultyLevels.STUDENT.getNumberOfColumns();
            numberOfRows = DifficultyLevels.STUDENT.getNumberOfRows();
            // numberOfWordPiles = DifficultyLevels.STUDENT.getWordNumber();
            colGap = DifficultyLevels.STUDENT.getColumnGap();
            rowGap = DifficultyLevels.STUDENT.getRowGap();
            stackPileColumn = 2;
            stackPileRow = 2;
        } else if(difficultyLevel == 2){
            numberOfColumns = DifficultyLevels.MASTER.getNumberOfColumns();
            numberOfRows = DifficultyLevels.MASTER.getNumberOfRows();
            // numberOfWordPiles = DifficultyLevels.MASTER.getWordNumber();
            colGap = DifficultyLevels.MASTER.getColumnGap();
            rowGap = DifficultyLevels.MASTER.getRowGap();
            stackPileColumn = 3;
            stackPileRow = 3;
        }

        nextRowStartPos = firstRowStartPos;

        for (int rowIndex = 1; rowIndex <= numberOfRows; rowIndex++){
            nextColumnStartPos = firstColumnStartPos;

            for (int colIndex = 1; colIndex <= numberOfColumns; colIndex++){

                if(stackPileColumn == colIndex && stackPileRow == rowIndex) {
                    // generate stackPile:
                    stackPile = new Pile(STACK, "Stack", STACK_GAP);
                    stackPile.setBlurredBackground(true);
                    stackPile.setLayoutX(nextColumnStartPos);
                    stackPile.setLayoutY(nextRowStartPos);
                    getChildren().add(stackPile);
                } else {
                    // generate wordPile:
                    Pile wordPile = new Pile(WORDCARDPLACE, "wordPile" + numberOfWordPiles, WORD_CARD_PLACE_GAP);
                    wordPile.setBlurredBackground(false);
                    wordPile.setLayoutX(nextColumnStartPos);
                    wordPile.setLayoutY(nextRowStartPos);
                    wordPiles.add(wordPile);
                    getChildren().add(wordPile);
                    numberOfWordPiles++;
                }

                nextColumnStartPos = nextColumnStartPos + colGap;

            }

            nextRowStartPos = nextRowStartPos + rowGap;
        }

    }

    public void dealWordCards() {
        int wordIndex = 0;

        for (Pile wordPile : wordPiles){
            wordPile.addCard(deck.get(wordIndex));
            wordIndex++;
        }

    }

    public void setTableBackground(Image tableBackground, boolean autoBackgroundSize) {
        setBackground(new Background(new BackgroundImage(tableBackground,
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.CENTER, autoBackgroundSize == true ? new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, true) : BackgroundSize.DEFAULT)));
    }

    public void restart(boolean withShuffle) {
        System.out.println("RESTARTING GAME");

        stackPile.clear();

        for (Pile wordPile : wordPiles){
            wordPile.clear();
        }


        if (withShuffle) {
            Collections.shuffle(deck);
        }

        dealWordCards();
    }


}
