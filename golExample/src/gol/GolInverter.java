package gol;

public class GolInverter implements GolGenerator {

  private int[][] grid;
  private int width;
  private int heigth;

  public GolInverter(int[][] grid, int width, int heigth) {
    this.grid = grid;
    this.width = width;
    this.heigth = heigth;
  }


  /*
   * Conteo de celulas vecinas vivas
   */
  private static int countLiveNeighbors(int[][] grid, int x, int y) {
    int liveNeighbors = 0;

    int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
    int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

    for (int i = 0; i < 8; i++) {
      int newX = x + dx[i];
      int newY = y + dy[i];

      // si mi celula esta dentro de los  limites se evalua [NO DEBE SALIR DEL TABLERO]
      if (newX >= 0 && newX < grid.length && newY >= 0 && newY < grid[0].length) {
        if (grid[newX][newY] == 1) {
          liveNeighbors++;
        }
      }
    }
    return liveNeighbors;
  }


  /*
   * Generacion de la proxima Generacion de celulas
   * Implementacion de las reglas del Game Of Life
   */
  private static int[][] generateNextGeneration(int[][] currentGeneration) {
    int[][] nextGeneration = new int[currentGeneration.length][currentGeneration[0].length];

    for (int i = 0; i < currentGeneration.length; i++) {
      for (int j = 0; j < currentGeneration[i].length; j++) {
        int liveNeighbors = countLiveNeighbors(currentGeneration, i, j);

        if (currentGeneration[i][j] == 1) {
          nextGeneration[i][j] = (liveNeighbors == 2 || liveNeighbors == 3) ? 1 : 0;
        } else if (currentGeneration[i][j] == 1) {
          nextGeneration[i][j] = (liveNeighbors < 2 || liveNeighbors > 3) ? 0 : 1;
        } else nextGeneration[i][j] = (liveNeighbors == 3) ? 1 : 0;
      }
    }
    return nextGeneration;
  }


  /*
  * Converts a matrix to a flat String
  * [0,0,0][1,1,1][0,1,0] => "...\nXXX\n.X."
   */
  private String toGridString(int[][] matrix, int cols, int rows) {
    String grid = new String("");
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        grid += (matrix[i][j] == 1 ? "X" : ".");
      }
      grid += "\n";
    }
    return grid;
  }


  @Override
  public String getNextGenerationAsString(long generation) {
    if (generation != 0) {
      // STEP 7. We calculate the next generation
      grid = generateNextGeneration(grid);

    }
    // STEP 8. We return the next generation (as a String)
    return toGridString(grid, width, heigth);
  }

}