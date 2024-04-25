package gol;

import java.util.Random;

public class GolExampleD {
    private static int rows = 10;
    private static int cols = 20;

    static String createRandomGrid(int rows, int cols) {
        var random = new Random();
        StringBuilder grid = new StringBuilder(); // Using StringBuilder for efficient string concatenation
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid.append(random.nextBoolean() ? "X" : ".");
            }
            grid.append("\n");
        }
        return grid.toString();
    }


    private static String initialRandomGrid = createRandomGrid(rows, cols); //generacion random una sola vez

    public static void main(String[] args) throws Exception {
        // Convertir la cadena en una matriz de caracteres


        char[][] gridArray = new char[rows][cols];

        String[] lines = initialRandomGrid.split("\n");

        for (int i = 0; i < lines.length; i++) {
            gridArray[i] = lines[i].toCharArray();
        }

        // Aplicar las reglas del juego de la vida
        final char[][][] nextGeneration = {new char[rows][cols]};

        for (int i = 0; i < gridArray.length; i++) {
            for (int j = 0; j < gridArray[i].length; j++) {
                int liveNeighbors = countLiveNeighbors(gridArray, i, j);
                if (gridArray[i][j] == 'X') {
                    nextGeneration[0][i][j] = (liveNeighbors == 2 || liveNeighbors == 3) ? 'X' : '.';
                } else {
                    nextGeneration[0][i][j] = (liveNeighbors == 3) ? 'X' : '.';
                }
            }
        }

        // Convertir la matriz de caracteres de la nueva generación de nuevo a una cadena
        StringBuilder nextGenGrid = new StringBuilder();
        for (char[] row : nextGeneration[0]) {
            nextGenGrid.append(row).append("\n");
        }

        //System.out.println("Next Generation Grid:");
        //System.out.println(nextGenGrid);

        final GolGenerator generator = new GolGenerator() {
            @Override
            public String getNextGenerationAsString(long generation) {
                if (generation == 0) {
                    return initialRandomGrid;
                } else {
                    // Actualizar la matriz para la siguiente generación
                    nextGeneration[0] = generateNextGeneration(nextGeneration[0]);

                    // Convertir la matriz de caracteres de la nueva generación de nuevo a una cadena
                    StringBuilder newGenGrid = new StringBuilder();
                    for (char[] row : nextGeneration[0]) {
                        newGenGrid.append(row).append("\n");
                    }
                    return newGenGrid.toString();
                }
            }
        };

        // Will use a Terminal Console
        SwingRenderer.render(generator, new GolSettings(rows, cols, 1000, 0));
    }

    // Conteo de celulas vecinas vivas
    private static int countLiveNeighbors(char[][] grid, int x, int y) {
        int liveNeighbors = 0;

        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];

            if (newX >= 0 && newX < grid.length && newY >= 0 && newY < grid[0].length) {
                if (grid[newX][newY] == 'X') {
                    liveNeighbors++;
                }
            }
        }

        return liveNeighbors;
    }

    // Generacion de la proxima Generacion de celulas
    // Implementacion de las reglas del Game Of Life
    private static char[][] generateNextGeneration(char[][] currentGeneration) {
        char[][] nextGeneration = new char[currentGeneration.length][currentGeneration[0].length];

        for (int i = 0; i < currentGeneration.length; i++) {
            for (int j = 0; j < currentGeneration[i].length; j++) {
                int liveNeighbors = countLiveNeighbors(currentGeneration, i, j);

                if (currentGeneration[i][j] == 'X') {
                    nextGeneration[i][j] = (liveNeighbors == 2 || liveNeighbors == 3) ? 'X' : '.';
                } else {
                    nextGeneration[i][j] = (liveNeighbors == 3) ? 'X' : '.';
                }
            }
        }
        return nextGeneration;
    }
}