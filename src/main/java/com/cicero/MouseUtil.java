package com.cicero;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.util.List;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;

// import static com.cicero.Pile.PileType.FOUNDATION;

public class MouseUtil {

    public static void slideBack(WordCard wordCard) {
        double sourceX = wordCard.getLayoutX() + wordCard.getTranslateX();
        double sourceY = wordCard.getLayoutY() + wordCard.getTranslateY();
        double targetX = wordCard.getLayoutX();
        double targetY = wordCard.getLayoutY();
        animateCardMovement(wordCard, sourceX, sourceY,
                targetX, targetY, Duration.millis(150), e -> {
                    wordCard.getDropShadow().setRadius(2);
                    wordCard.getDropShadow().setOffsetX(0);
                    wordCard.getDropShadow().setOffsetY(0);
                });
    }

    public static void slideToDest(List<WordCard> cardsToSlide, Pile destPile) {
        if (cardsToSlide == null)
            return;
        double destCardGap = destPile.getCardGap();
        double targetX;
        double targetY;

        if (destPile.isEmpty()) {
            targetX = destPile.getLayoutX();
            targetY = destPile.getLayoutY();
        } else {
            targetX = destPile.getTopCard().getLayoutX();
            targetY = destPile.getTopCard().getLayoutY();
        }

        for (int i = 0; i < cardsToSlide.size(); i++) {
            WordCard currentWordCard = cardsToSlide.get(i);
            double sourceX = currentWordCard.getLayoutX() + currentWordCard.getTranslateX();
            double sourceY = currentWordCard.getLayoutY() + currentWordCard.getTranslateY();

            animateCardMovement(currentWordCard, sourceX, sourceY, targetX,
                    targetY + ((destPile.isEmpty() ? i : i + 1) * destCardGap), Duration.millis(150),
                    e -> {
                        currentWordCard.moveToPile(destPile);
                        currentWordCard.getDropShadow().setRadius(2);
                        currentWordCard.getDropShadow().setOffsetX(0);
                        currentWordCard.getDropShadow().setOffsetY(0);
                    });
        }

        boolean destTypeIsStack = destPile.getPileType().equals(Pile.PileType.STACK);
        if (destTypeIsStack) {
            System.out.println("ENTERED inside MouseUtil");
            Cicero.game.checkGameWon();
            Cicero.game.checkProceedToNextLevel();
        }
    }

    private static void animateCardMovement(
            WordCard wordCard, double sourceX, double sourceY,
            double targetX, double targetY, Duration duration,
            EventHandler<ActionEvent> doAfter) {

        Path path = new Path();
        path.getElements().add(new MoveToAbs(wordCard, sourceX, sourceY));
        path.getElements().add(new LineToAbs(wordCard, targetX, targetY));

        PathTransition pathTransition = new PathTransition(duration, path, wordCard);
        pathTransition.setInterpolator(Interpolator.EASE_IN);
        pathTransition.setOnFinished(doAfter);

        Timeline blurReset = new Timeline();
        KeyValue bx = new KeyValue(wordCard.getDropShadow().offsetXProperty(), 0, Interpolator.EASE_IN);
        KeyValue by = new KeyValue(wordCard.getDropShadow().offsetYProperty(), 0, Interpolator.EASE_IN);
        KeyValue br = new KeyValue(wordCard.getDropShadow().radiusProperty(), 2, Interpolator.EASE_IN);
        KeyFrame bKeyFrame = new KeyFrame(duration, bx, by, br);
        blurReset.getKeyFrames().add(bKeyFrame);

        ParallelTransition pt = new ParallelTransition(wordCard, pathTransition, blurReset);
        pt.play();
    }

    private static class MoveToAbs extends MoveTo {
        MoveToAbs(Node node, double x, double y) {
            super(x - node.getLayoutX() + node.getLayoutBounds().getWidth() / 2,
                    y - node.getLayoutY() + node.getLayoutBounds().getHeight() / 2);
        }
    }

    private static class LineToAbs extends LineTo {
        LineToAbs(Node node, double x, double y) {
            super(x - node.getLayoutX() + node.getLayoutBounds().getWidth() / 2,
                    y - node.getLayoutY() + node.getLayoutBounds().getHeight() / 2);
        }
    }

}
