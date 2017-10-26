package com.cicero;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
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


        primaryStage.setTitle("Cicero Memory Game");

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        ImageView mainTitle = new ImageView();
        mainTitle.setImage(new Image("/cicero.jpg"));

        // Create menu bar
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("Game");
        menuBar.getMenus().addAll(menuFile);

        root.setTop(menuBar);
        root.setCenter(mainTitle);

        Menu newGame = new Menu("New Game");

        MenuItem difficultyLevel1 = new MenuItem("Student");
        difficultyLevel1.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                game = new Game(1);
                game.setTableBackground(new Image("/table/marble.jpg"), true);
                root.setCenter(game);
            }
        });

        MenuItem difficultyLevel2 = new MenuItem("Master");
        difficultyLevel2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                game = new Game(2);
                game.setTableBackground(new Image("/table/marble.jpg"), true);
                root.setCenter(game);
            }
        });

        difficultyLevel1.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        difficultyLevel2.setAccelerator(new KeyCodeCombination(KeyCode.M, KeyCombination.CONTROL_DOWN));
        newGame.getItems().addAll(difficultyLevel1, new SeparatorMenuItem(), difficultyLevel2);


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

        primaryStage.setScene(scene);
        primaryStage.show();
    }

}

