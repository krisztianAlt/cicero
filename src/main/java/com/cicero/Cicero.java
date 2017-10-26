package com.cicero;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Cicero extends Application {

    private static final double WINDOW_WIDTH = 1400;
    private static final double WINDOW_HEIGHT = 800;
    public static Game game = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        game = new Game();
        game.setDifficultyLevel(1);
        game.setTableBackground(new Image("/table/marble.jpg"), true);

        primaryStage.setTitle("Cicero Memory Game");


        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);


        // Create menu bar
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("Game");
        /*Menu menuSettings = new Menu("Settings");
        Menu menuMove = new Menu("Move");

        menuBar.getMenus().addAll(menuFile, menuSettings, menuMove);*/
        menuBar.getMenus().addAll(menuFile);

        root.setTop(menuBar);
        root.setCenter(game);

        // Add menu items

        // Undo menu

        /*MenuItem undoMenuItem = new MenuItem("Undo");
        undoMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                game.undoMove();
            }
        });

        undoMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));

        menuMove.getItems().addAll(undoMenuItem);*/


        // File menu

        /*// Original
        MenuItem newGame = new MenuItem("New Game");
        newGame.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                boolean withShuffle = true;
                game.restart(withShuffle);
            }
        });*/

        //NEW
        Menu newGame = new Menu("New Game");

        MenuItem difficultyLevel1 = new MenuItem("Student");
        difficultyLevel1.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                boolean withShuffle = true;
                game.setDifficultyLevel(1);
                game.restart(withShuffle);
            }
        });

        MenuItem difficultyLevel2 = new MenuItem("Master");
        difficultyLevel2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                boolean withShuffle = true;
                game.setDifficultyLevel(2);
                game.restart(withShuffle);
            }
        });

        difficultyLevel1.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        difficultyLevel2.setAccelerator(new KeyCodeCombination(KeyCode.M, KeyCombination.CONTROL_DOWN));
        newGame.getItems().addAll(difficultyLevel1, new SeparatorMenuItem(), difficultyLevel2);


        // newGame.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));

        MenuItem restartGame = new MenuItem("Restart");
        restartGame.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                boolean withShuffle = false;
                game.restart(withShuffle);
            }
        });

        restartGame.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN));

        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                System.exit(0);
            }
        });

        menuFile.getItems().addAll(newGame, new SeparatorMenuItem(), restartGame, new SeparatorMenuItem(), exit);


        // Settings menu

        /*// Change Background
        Menu changeBackground = new Menu("Change Background");
        final ToggleGroup groupBackground = new ToggleGroup();

        RadioMenuItem defaultBackground = new RadioMenuItem("Default");
        defaultBackground.setUserData("default");
        defaultBackground.setToggleGroup(groupBackground);
        changeBackground.getItems().add(defaultBackground);

        RadioMenuItem whiteBackground = new RadioMenuItem("White");
        whiteBackground.setUserData("white");
        whiteBackground.setToggleGroup(groupBackground);
        changeBackground.getItems().add(whiteBackground);

        RadioMenuItem carpetBackground = new RadioMenuItem("Carpet");
        carpetBackground.setUserData("carpet");
        carpetBackground.setToggleGroup(groupBackground);
        changeBackground.getItems().add(carpetBackground);

        RadioMenuItem tableClothBackground = new RadioMenuItem("Tablecloth");
        tableClothBackground.setUserData("tablecloth");
        tableClothBackground.setToggleGroup(groupBackground);
        changeBackground.getItems().add(tableClothBackground);

        RadioMenuItem moonBackground = new RadioMenuItem("Moon");
        moonBackground.setUserData("moon");
        moonBackground.setToggleGroup(groupBackground);
        changeBackground.getItems().add(moonBackground);

        String auto = new String("auto");
        groupBackground.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (groupBackground.getSelectedToggle() != null) {
                    String userChoice = (String) groupBackground.getSelectedToggle().getUserData();
                    if (userChoice.equals("default")){
                        game.setTableBackground(new Image("/table/green.png"), false);
                    } else if (userChoice.equals("white")){
                        game.setTableBackground(new Image("/table/white.png"), false);
                    } else if (userChoice.equals("carpet")){
                        game.setTableBackground(new Image("/table/carpet.jpg"), true);
                    } else if (userChoice.equals("tablecloth")){
                        game.setTableBackground(new Image("/table/tablecloth.jpg"), true);
                    } else if (userChoice.equals("moon")){
                        game.setTableBackground(new Image("/table/moon.jpg"), true);
                    }
                }
            }
        });

        // Change WordCard Layout
        Menu changeCardLayout = new Menu("Change WordCard Theme");
        final ToggleGroup groupCardLayout = new ToggleGroup();

        RadioMenuItem defaultCardLayout = new RadioMenuItem("Default");
        defaultCardLayout.setUserData("default");
        defaultCardLayout.setToggleGroup(groupCardLayout);
        changeCardLayout.getItems().add(defaultCardLayout);

        RadioMenuItem birdCardLayout = new RadioMenuItem("Bird");
        birdCardLayout.setUserData("bird");
        birdCardLayout.setToggleGroup(groupCardLayout);
        changeCardLayout.getItems().add(birdCardLayout);

        groupCardLayout.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (groupCardLayout.getSelectedToggle() != null) {
                    String userChoice = (String) groupCardLayout.getSelectedToggle().getUserData();
                    if (userChoice.equals("default")){
                        game.changeCardBackImage(new Image("card_images/card_back.png"));
                    } else if (userChoice.equals("bird")){
                        game.changeCardBackImage(new Image("card_images/card_back4.png"));
                    }
                }
            }
        });

        // Change Theme
        Menu changeTheme = new Menu("Change Theme Suit");
        final ToggleGroup groupThemes = new ToggleGroup();

        RadioMenuItem defaultTheme = new RadioMenuItem("Default");
        defaultTheme.setUserData("default");
        defaultTheme.setToggleGroup(groupThemes);
        changeTheme.getItems().add(defaultTheme);

        RadioMenuItem goldTheme = new RadioMenuItem("Gold Rush");
        goldTheme.setUserData("goldRush");
        goldTheme.setToggleGroup(groupThemes);
        changeTheme.getItems().add(goldTheme);

        RadioMenuItem footballTheme = new RadioMenuItem("Let's play football");
        footballTheme.setUserData("football");
        footballTheme.setToggleGroup(groupThemes);
        changeTheme.getItems().add(footballTheme);

        groupThemes.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (groupThemes.getSelectedToggle() != null) {
                    String userChoice = (String) groupThemes.getSelectedToggle().getUserData();
                    if (userChoice.equals("default")){
                        game.setTableBackground(new Image("/table/green.png"), false);
                        game.changeCardBackImage(new Image("card_images/card_back.png"));
                    } else if (userChoice.equals("goldRush")){
                        game.setTableBackground(new Image("/table/wood2.jpeg"), true);
                        game.changeCardBackImage(new Image("card_images/card_back2.png"));
                    } else if (userChoice.equals("football")){
                        game.setTableBackground(new Image("/table/grass.jpg"), true);
                        game.changeCardBackImage(new Image("card_images/card_back3.png"));
                    }
                }
            }
        });

        menuSettings.getItems().addAll(changeBackground, changeCardLayout, changeTheme);*/

        primaryStage.setScene(scene);
        primaryStage.show();
    }

}

