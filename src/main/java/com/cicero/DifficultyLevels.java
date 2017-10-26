package com.cicero;

public enum DifficultyLevels {

    STUDENT (8, 263, 206, 3, 3, 450, 220),
    MASTER (24, 263 / 1.75, 206/ 1.75, 5, 5, 450 / 1.8, 220 / 1.7),
    CAESAR (48, 263 / 2.5, 206 / 2.5, 7, 7, 450 / 2.5, 220 / 2.4);

    private int wordNumber;
    private double cardWidth; // original image file's width: 263 pixels
    private double cardHeight; // original image file's height: 206 pixels
    private int numberOfColumns;
    private int numberOfRows;
    private double columnGap;
    private double rowGap;

    DifficultyLevels(int wordNumber, double cardWidth, double cardHeight,
                     int numberOfColumns, int numberOfRows,
                     double columnGap, double rowGap){
        this.wordNumber = wordNumber;
        this.cardWidth = cardWidth;
        this.cardHeight = cardHeight;
        this.numberOfColumns = numberOfColumns;
        this.numberOfRows = numberOfRows;
        this.columnGap = columnGap;
        this.rowGap = rowGap;
    }

    public int getWordNumber() {
        return wordNumber;
    }

    public double getCardWidth() {
        return cardWidth;
    }

    public double getCardHeight() {
        return cardHeight;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public double getColumnGap() {
        return columnGap;
    }

    public double getRowGap() {
        return rowGap;
    }

}