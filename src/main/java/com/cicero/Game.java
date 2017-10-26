package com.cicero;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
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
    private int actualGameLevel;
    private int lastGameLevel;

    private List<WordCard> deck = new ArrayList<>();
    private List<WordCard> previousStack = new ArrayList<>();
    private List<WordCard> actualStack = new ArrayList<>();

    private Pile stackPile;
    private List<Pile> wordPiles = FXCollections.observableArrayList();

    private double dragStartX, dragStartY;
    private List<WordCard> draggedWordCards = FXCollections.observableArrayList();

    private static double STACK_GAP = 1;
    private static double WORD_CARD_PLACE_GAP = 1;

    private EventHandler<MouseEvent> onMousePressedHandler = e -> {
        dragStartX = e.getSceneX();
        dragStartY = e.getSceneY();
    };

    private EventHandler<MouseEvent> onMouseDraggedHandler = e -> {
        WordCard wordCard = (WordCard) e.getSource();

        Pile activePile = wordCard.getContainingPile();
        if (activePile.getPileType().equals(STACK))
            return;

        double offsetX = e.getSceneX() - dragStartX;
        double offsetY = e.getSceneY() - dragStartY;

        draggedWordCards.clear();
        draggedWordCards.add(wordCard);
        dragCard(wordCard, offsetX, offsetY);

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

        List<Pile> targetPiles = new ArrayList<>();
        targetPiles.add(stackPile);

        Pile pile = getValidIntersectingPile(wordCard, targetPiles);

        if (pile != null) {

            List<WordCard> wordCardList = new LinkedList<>();
            wordCardList.addAll(draggedWordCards);

            // do action
            handleValidMove(wordCard, pile);


        } else {
            draggedWordCards.forEach(MouseUtil::slideBack);
            draggedWordCards.clear();
        }
    };

    public Game(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
        this.actualGameLevel = 1;

        if (difficultyLevel == 1){
            WordCard.WIDTH = DifficultyLevels.STUDENT.getCardWidth();
            WordCard.HEIGHT = DifficultyLevels.STUDENT.getCardHeight();
            deck = WordCard.createNewDeck(DifficultyLevels.STUDENT.getWordNumber());
            this.lastGameLevel = DifficultyLevels.STUDENT.getWordNumber() - 1;
        } else if (difficultyLevel == 2){
            WordCard.WIDTH = DifficultyLevels.MASTER.getCardWidth();
            WordCard.HEIGHT = DifficultyLevels.MASTER.getCardHeight();
            deck = WordCard.createNewDeck(DifficultyLevels.MASTER.getWordNumber());
            this.lastGameLevel = DifficultyLevels.MASTER.getWordNumber() - 1;
        } else if (difficultyLevel == 3){
            WordCard.WIDTH = DifficultyLevels.CAESAR.getCardWidth();
            WordCard.HEIGHT = DifficultyLevels.CAESAR.getCardHeight();
            deck = WordCard.createNewDeck(DifficultyLevels.CAESAR.getWordNumber());
            this.lastGameLevel = DifficultyLevels.CAESAR.getWordNumber() - 1;
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
            this.getChildren().add(wordCard);
        }
    }

    private Pile getValidIntersectingPile(WordCard wordCard, List<Pile> piles) {
        Pile result = null;
        for (Pile pile : piles) {
            if (!pile.equals(wordCard.getContainingPile()) &&
                    isOverPile(wordCard, pile))
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
        MouseUtil.slideToDest(draggedWordCards, destPile);
        draggedWordCards.clear();
    }

    public List<WordCard> getActualStack() {
        return actualStack;
    }

    public void checkWordCardOrder(){
        int actualStackSize = actualStack.size();
        int previousStackSize = previousStack.size();

        if (actualGameLevel == 1 && actualStackSize == 2){
            proceedToNextLevel();
        } else if (actualGameLevel > 1 && actualStackSize > previousStackSize){
            proceedToNextLevel();
        } else if (actualGameLevel > 1 && actualStackSize <= previousStackSize) {
            List<WordCard> portionStack = new ArrayList<>();

            for (int index = 0; index < actualStackSize; index++){
                portionStack.add(previousStack.get(index));
            }

            if (portionStack.equals(actualStack)){
                System.out.println("Good order.");
            } else {
                failure();
            }
        }
    }

    private void proceedToNextLevel(){
        actualGameLevel++;
        if (actualGameLevel > lastGameLevel){
            showWinning();
        } else {
            previousStack.clear();

            for (WordCard wordCard : actualStack){
                previousStack.add(wordCard);
            }

            actualStack.clear();

            showLevelSucceeded();

            for (Pile wordPile : wordPiles){
                wordPile.clear();
            }

            stackPile.clear();

            Collections.shuffle(deck);
            dealWordCards();
        }
    }

    private void failure(){
        showLosing();
        restart(false);
    }

    private void showLevelSucceeded() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("The gods are hopeful...");
        alert.setHeaderText("You proceed to the " + actualGameLevel + ". level!");
        String message = "Just keep breathing and relax.";
        alert.setContentText(message);
        alert.show();
    }

    private void showWinning() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("The gods are happy...");
        alert.setHeaderText("YOU WON!");
        String message ="Congratulations.\nYou are blissful.";
        alert.setContentText(message);
        alert.show();
    }

    private void showLosing() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("The gods are sad...");
        alert.setHeaderText("You have lost.\nThe order was not correct.");
        String message ="Now I restart this game from the beginning,\nbut you can start a new game if you want.";
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
            colGap = DifficultyLevels.STUDENT.getColumnGap();
            rowGap = DifficultyLevels.STUDENT.getRowGap();
            stackPileColumn = 2;
            stackPileRow = 2;
        } else if (difficultyLevel == 2){
            numberOfColumns = DifficultyLevels.MASTER.getNumberOfColumns();
            numberOfRows = DifficultyLevels.MASTER.getNumberOfRows();
            colGap = DifficultyLevels.MASTER.getColumnGap();
            rowGap = DifficultyLevels.MASTER.getRowGap();
            stackPileColumn = 3;
            stackPileRow = 3;
        } else if (difficultyLevel == 3){
            numberOfColumns = DifficultyLevels.CAESAR.getNumberOfColumns();
            numberOfRows = DifficultyLevels.CAESAR.getNumberOfRows();
            colGap = DifficultyLevels.CAESAR.getColumnGap();
            rowGap = DifficultyLevels.CAESAR.getRowGap();
            stackPileColumn = 4;
            stackPileRow = 4;
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

        previousStack.clear();
        actualStack.clear();
        actualGameLevel = 1;
    }


}
