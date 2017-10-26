package com.cicero;

public enum DifficultyLevels {

    STUDENT (8, 263, 206, 3, 3, 450, 220),
    MASTER (24, 263 / 1.75, 206/ 1.75, 5, 5, 450 / 1.8, 220 / 1.7);

    private int wordNumber;
    private double cardWidth;
    private double cardHeight;
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