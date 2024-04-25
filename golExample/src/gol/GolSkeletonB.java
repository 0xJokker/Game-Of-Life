package gol;

import java.util.Random;

public class GolSkeletonB {

  public static void main(String[] args) throws Exception {
    // STEP 1. You need to get these values from args
    String w = "";
    String h = "";
    String g = "";
    String s = "";
    String p = "";

    for (String arg : args) {
      String[] keyValue = arg.split("=");
      String key = keyValue[0];

      switch (key) {
        case "w":
          w = keyValue[1];
          break;
        case "h":
          h = keyValue[1];
          break;
        case "g":
          g = keyValue[1];
          break;
        case "s":
          s = keyValue[1];
          break;
        case "p":
          p = keyValue[1];
      }
    }

    // STEP 2. You need to validate each parameter
    boolean checkValidation = validationArgs(args);
    if (checkValidation) {
      int width = parseArgs(w);
      int heigth = parseArgs(h);
      int generations = parseArgs(g);
      int speed = parseArgs(s);

      // STEP 3. And define the goal settings with these input params:
      GolSettings goLSettings = new GolSettings(heigth, width, speed, generations);

      String valuePopulation = validationPopulation(p, heigth, width);
      if (valuePopulation != null) {

        int[][] population = transformPopulation(valuePopulation, heigth, width);

        // STEP 4. You need to convert the initial input string p into a data structure for the grid of cells, on this example we use ints and a hard coded representation
        final int[][] grid = population;

        // STEP 5. We create a Gol generator that will process the grid
        final GolGenerator generator = new GolInverter(grid, width, heigth);

        // Will use a Terminal Console
        // STEP 6. You define the rendering process
        SwingRenderer.render(generator, goLSettings);
      }
    }
  }


  /*
   * Validacion de Presencia de los Parametros Ingresados por CLI
   *
   * Los parametros deben estar presentes
   *
   * Variable letterFound : Bandera para validar si la letra actual está presente
   * Variable isValid : Bandera para validar si al menos una letra está presente
   */
  public static boolean validationArgs(String[] args) {
    boolean checkValidation = true;
    boolean checkParameters = false;

    // Array con las letras a validar
    String[] lettersToValidate = {"w", "h", "g", "s", "p"};

    boolean isValid = false;

    for (String letter : lettersToValidate) {
      boolean letterFound = false;

      // Iteracion de los argumentos para buscar la letra actual
      for (String arg : args) {
        String[] keyValue = arg.split("=");
        String key = keyValue[0];

        if (key.contains(letter)) {
          letterFound = true;
          isValid = true;
        }
      }

      // Si el parametro no fue encontrado
      if (!letterFound) {
        System.out.println(letter + " = [No  Presente]");
        checkValidation = false;
      }
    }

    // Si hay al menos un perametro presente
    if (isValid) {
      boolean validate = parseValidationArgs(args);
      if (validate) {
        checkParameters = true;
      }
    }
    return checkValidation && checkParameters;
  }


  /*
  * Validacion de cada Parametro Ingresado por CLI
  *
  * Cada parametro debe cumplir con las condiciones para el retorno true
  * e inicializar el Juego
}   */
  public static boolean parseValidationArgs(String[] args) {
    boolean isValidParameterW = false;
    boolean isValidParameterH = false;
    boolean isValidParameterG = false;
    boolean isValidParameterS = false;
    for (String arg : args) {
      String[] keyValue = arg.split("=");
      String key = keyValue[0];

      switch (key) {
        case "w":
          try {
            int valueWidth = parseArgs(keyValue[1]);
            if (isValidWidth(valueWidth)) {
              System.out.println("width = " + valueWidth);
              isValidParameterW = true;
            } else {
              System.out.println("width = [Invalido]");
            }
          } catch (NumberFormatException e) {
            System.out.println("width = [Invalido]");
          }
          break;

        case "h":
          try {
            int valueHeight = parseArgs(keyValue[1]);
            if (isValidHeight(valueHeight)) {
              System.out.println("height = " + valueHeight);
              isValidParameterH = true;
            } else {
              System.out.println("height = [Invalido]");
            }

          } catch (NumberFormatException e) {
            System.out.println("heigth = [Invalido]");
          }
          break;

        case "g":
          try {
            int valueGenerations = parseArgs(keyValue[1]);
            if (isValidGeneration(valueGenerations)) {
              System.out.println("generations = " + valueGenerations);
              isValidParameterG = true;
            } else {
              System.out.println("generations = [Invalido]");
            }

          } catch (NumberFormatException e) {
            System.out.println("generations = [Invalido]");
          }
          break;

        case "s":
          try {
            int valueSpeed = parseArgs(keyValue[1]);
            if (isValidSpeed(valueSpeed)) {
              System.out.println("speed = " + valueSpeed);
              isValidParameterS = true;
            } else {
              System.out.println("speed = [Invalido]");
            }

          } catch (NumberFormatException e) {
            System.out.println("speed = [Invalido]");
          }
          break;
      }
    }
    return isValidParameterW && isValidParameterH && isValidParameterG && isValidParameterS;
  }


  /*
   * Validacion de Condiciones de cada Parametro
   * width: (10, 20, 40, 80)
   * height: (10, 20, 40)
   * generation: (>=0)
   * speed: Velocidad en Milisegundos ([250,1000])
   */
  private static boolean isValidWidth(int width) {
    return width == 10 || width == 20 || width == 40 || width == 80;
  }

  private static boolean isValidHeight(int height) {
    return height == 10 || height == 20 || height == 40;
  }

  private static boolean isValidGeneration(int generation) {
    return generation >= 0;
  }

  private static boolean isValidSpeed(int speed) {
    return speed >= 250 && speed <= 1000;
  }


  /*
   * Metodo de Parseo(int) de Parametros
   * Cada llamada procesa cada Parametro en String y lo convirte en un int
   */
  public static int parseArgs(String parameter) {
    int valueParameter = 0;
    valueParameter = Integer.parseInt(parameter);

    return valueParameter;
  }


  /*
   * Parseo de String a una Matriz(int) del Parametro Population
   *
   * Transformacion de Population en una matrix con la logitud dependiendo de los
   * parametros ingresados width y height
   */
  public static int[][] transformPopulation(String population, int width, int height) {

    String[] keyPopulation = population.split("#");
    int[][] valuePopulation = new int[width][height];

    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        valuePopulation[i][j] = Character.getNumericValue(
            keyPopulation[i].charAt(j)); //Conversion del caracter ASCII a su valor numerico
      }
    }
    return valuePopulation;
  }


  /*
   * Validacion del Argumento ingresado en 'p' Population
   *
   * Validacion de Population con sus valores ingresados [01#] y
   * caso especial 'rnd' que genera un Population random
   */
  public static String validationPopulation(String population, int width, int height) {
    String valuePopulation = "";
    Random random = new Random();

    if (population.matches("[01#]+")) {
      valuePopulation = population;
      System.out.println("population = " + valuePopulation);
    } else if (population.equals("rnd")) {
      for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
          valuePopulation = valuePopulation.concat(String.valueOf(random.nextInt(2)));
        }
        valuePopulation = valuePopulation.concat("#");
      }
      valuePopulation = valuePopulation.substring(0,
          valuePopulation.length() - 1); //Eliminar el último #
      System.out.println("population = " + valuePopulation);
    } else {
      System.out.println("population = [Invalido]");
      valuePopulation = null;
    }

    return valuePopulation;
  }

}