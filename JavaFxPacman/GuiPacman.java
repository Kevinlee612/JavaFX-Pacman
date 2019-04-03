import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;

/**
 * 
 * @author Suk Chan Lee A15427567 Sukchan.kevin@gmail.com
 * This class takes the arguments from the console and instantiates a board with the input size.
 * If an input file is given, it is loaded. An output file must be provided if the game is to be saved.
 * This class puts each object in the game into tiles within a GridPane, which is in a StackPane.
 * It also enables the user to input commands through arrow keys to move pacman and 'S' to save.
 *
 */

@SuppressWarnings("restriction")
public class GuiPacman extends Application
{
  private String outputBoard; // The filename for where to save the Board
  private Board board; // The Game Board

  // Fill colors to choose
  private static final Color COLOR_GAME_OVER = Color.rgb(238, 228, 218, 0.73);
  private static final Color COLOR_VALUE_LIGHT = Color.rgb(249, 246, 242);
  private static final Color COLOR_VALUE_DARK = Color.rgb(119, 110, 101);
  /** Add your own Instance Variables here */
  private static Tile[][] tiles; //tiles to be stored
  Text score = new Text(); //Keep track of score
  Text cherryturns = new Text(); // Keep track of turns cherry has left
  GridPane panel = new GridPane(); //GridPane to keep tiles
  StackPane stack = new StackPane();//StackPane to display end game screen
  GridPane end = new GridPane(); //End game Display
  private int rowcount; //Counts Rows in grid[][]
  private int colcount; //Counts Columns in grid[][]

  /* 
   * Name:      start
   * Purpose:   Start and keep the game running.
   */
  @Override
  public void start(Stage primaryStage)
  {
    // Process Arguments and Initialize the Game Board
    processArgs(getParameters().getRaw().toArray(new String[0]));
    tiles = new Tile[board.GRID_SIZE][board.GRID_SIZE]; //Initializes tile 2D array
    panel.setStyle("-fx-background-color: grey;");
    panel.setHgap(10); //Sets horizontal gap between tiles to 10
    panel.setVgap(10);//Sets Vertical gap between tiles to 10
    Text title = new Text();
    title.setText("Pacman Game"); //Sets the text for the title
    title.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 27));
    GridPane.setMargin(title, new Insets(0, 0, 0, 0));
    panel.add(title,1,0,4,1); //Sets title at coordinates (1,0) with 4 horizontal slots
    score.setText("Score: "+board.getScore());
    score.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 15));
    GridPane.setMargin(score, new Insets(0, 0, 0, 0));
    panel.add(score,5,0,2,1);//Sets title at coordinates (5,0) with 2 horizontal slots
    cherryturns.setText("Cherry Turns Left:"+board.getCherryTurns());
    cherryturns.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 15));
    GridPane.setMargin(cherryturns, new Insets(0, 0, 0, 0));
    panel.add(cherryturns,7,0,3,1);
    for (int i = 0;i < board.GRID_SIZE; i++) { //iterates through grid[][] and creates a tile for
    	colcount = 0;							// each character
    	for (int j = 0; j < board.GRID_SIZE; j++) {
    		tiles[i][j] = new Tile(board.getGrid()[i][j]);
    		panel.add(tiles[i][j].getNode(), j ,i+1);
    		colcount++;
    	}
    	rowcount++;
    }
    stack.getChildren().add(panel);
    Scene scene = new Scene(stack, 50 * board.GRID_SIZE, 50 * (board.GRID_SIZE+1));
    myKeyHandler handler = new myKeyHandler();
    scene.setOnKeyPressed(handler); //Handles key inputs
    primaryStage.setTitle("Pacman Game");
    primaryStage.setScene(scene);
    primaryStage.show(); //Displays GUI
    
  }



  /** Add your own Instance Methods Here */

  /*
   * Name:       myKeyHandler
   *
   * Purpose:     
   *
   *
   */
  private class myKeyHandler implements EventHandler<KeyEvent> {

   /* 
    * Name:      handle
    * Purpose:   handle the KeyEvent of user's input.
    * Parameter: 
    * Return:    
    */
    @Override
    public void handle (KeyEvent e) {
      if (e.getCode() == KeyCode.UP && board.canMove(Direction.UP)) {
    	  board.move(Direction.UP);//Moves pacman up if valid move
    	  System.out.println("Moving Up");
      }
      else if (e.getCode().equals(KeyCode.DOWN) && board.canMove(Direction.DOWN)) {
    	  board.move(Direction.DOWN);//Moves pacman down if valid move
    	  System.out.println("Moving Down");
      }
      else if (e.getCode().equals(KeyCode.RIGHT) && board.canMove(Direction.RIGHT)) {
    	  board.move(Direction.RIGHT);//Moves pacman right if valid move
    	  System.out.println("Moving Right");
      }
      else if (e.getCode().equals(KeyCode.LEFT) && board.canMove(Direction.LEFT)) {
    	  board.move(Direction.LEFT);//Moves pacman left if valid move
    	  System.out.println("Moving Left");
      }
      else if (e.getCode() == KeyCode.S) {
    	  try {
			board.saveBoard(outputBoard);//Saves board to defined outputBoard
		} catch (IOException e1) {
			e1.printStackTrace();
		}
      }
      rowcount = 0;
      for (int i = 0;i < board.GRID_SIZE; i++) {
    	colcount = 0;
      	for (int j = 0; j < board.GRID_SIZE; j++) {
      		tiles[i][j].updateImageView(board.getGrid()[i][j]);//Updates GUI to reflect grid[][]
      		colcount++;
      	}
      rowcount++;
      }
      score.setText("score: "+board.getScore());//Updates Score on GUI
      cherryturns.setText("Cherry Turns Left:"+board.getCherryTurns());
      //Updates Turns of cherry left
      gameIsOver();//Checks if game is over
    }


    /* 
     * Name:      gameIsOver
     * Purpose:   Check if the game is over and show the gameover board.
     * Parameter: 
     * Return:    
     */
    private void gameIsOver() {
      if (board.isGameOver()) {
    	  Text endtext = new Text();
    	  endtext.setText("GAME OVER");
    	  endtext.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 40));
    	  GridPane.setMargin(endtext, new Insets(250, 100, 100, 150));
    	  end.add(endtext,board.GRID_SIZE/2,board.GRID_SIZE/2);
    	  Rectangle screen = new Rectangle(700,700);
    	  screen.setOpacity(.72);
    	  screen.setFill(COLOR_GAME_OVER);
    	  stack.getChildren().addAll(screen,end);
      }
    }
  } // End of Inner Class myKeyHandler.



  /*
   * Name:        Tile
   *
   * Purpose:     This class tile helps to make the tiles in the board 
   *              presented using JavaFX. Whenever a tile is needed,
   *              the constructor taking one char parameter is called
   *              and create certain ImageView fit to the char representation
   *              of the tile.
   * 
   *
   */
  private class Tile {

    private ImageView repr;   // This field is for the Rectangle of tile.
 
    /* 
     * Constructor
     * Creates a tile with the ImageView representation of the character 
     * Parameter: tileAppearance: the character representation of the object at grid[][]
     *
     */
    public Tile(char tileAppearance) {
    	if (tileAppearance == '*') {
    		repr = new ImageView((new Image("dot_uneaten.png", 40,40,true,true)));
		}
		else if (tileAppearance == ' ') {
			repr = new ImageView((new Image("dot_eaten.png", 40,40,true,true)));
		}
		else if (tileAppearance == 'P') {
			repr = new ImageView((new Image("pacman_right.png", 40,40,true,true)));
		}
		else if (tileAppearance == 'G' && board.getCherryTurns() == 0) {
			if (board.getGhosts()[0] != null && board.getGhosts()[0].getRow() == rowcount &&
					board.getGhosts()[0].getCol() == colcount) {
				repr = new ImageView((new Image("blinky_left.png", 40,40,true,true)));
			}
			else if (board.getGhosts()[1] != null && board.getGhosts()[1].getRow() == rowcount &&
					board.getGhosts()[1].getCol() == colcount) {
				repr = new ImageView((new Image("inky_down.png", 40,40,true,true)));
			}
			else if (board.getGhosts()[2] != null && board.getGhosts()[2].getRow() == rowcount &&
					board.getGhosts()[2].getCol() == colcount) {
				repr = new ImageView((new Image("clyde_up.png", 40,40,true,true)));
			}
			else if (board.getGhosts()[3] != null && board.getGhosts()[3].getRow() == rowcount &&
					board.getGhosts()[3].getCol() == colcount) {
				repr = new ImageView((new Image("pinky_left.png", 40,40,true,true)));
			}
				
				
		}
		else if (tileAppearance == 'G' && board.getCherryTurns() > 0) {
			repr = new ImageView((new Image("ghostblue.png", 40,40,true,true)));
		}
		else if (tileAppearance == 'C') {
			repr = new ImageView((new Image("cherry.png", 40,40,true,true)));
		}
		else if (tileAppearance == 'X') {
			repr = new ImageView((new Image("pacman_dead.png", 40,40,true,true)));
		}
    }

    /*
     * Getter method for repr ImageView
     * return: ImageView repr
     */
    public ImageView getNode() {
      return repr;
    }
    
    /*
     * Updates the image of the repr ImageView.
     * Used to update GUI
     */
    public void updateImageView(char tileAppearance) {
    	if (tileAppearance == '*') {
    		repr.setImage(new Image("dot_uneaten.png", 40,40,true,true));
		}
		else if (tileAppearance == ' ') {
			repr.setImage(new Image("dot_eaten.png", 40,40,true,true));
		}
		else if (tileAppearance == 'P') {
			repr.setImage(new Image("pacman_right.png", 40,40,true,true));
		}
		else if (tileAppearance == 'G' && board.getCherryTurns() == 0) {
			if (board.getGhosts()[0] != null && board.getGhosts()[0].getRow() == rowcount &&
					board.getGhosts()[0].getCol() == colcount) {
				repr.setImage(new Image("blinky_left.png", 40,40,true,true));
			}
			else if (board.getGhosts()[1] != null && board.getGhosts()[1].getRow() == rowcount &&
					board.getGhosts()[1].getCol() == colcount) {
				repr.setImage(new Image("inky_down.png", 40,40,true,true));
			}
			else if (board.getGhosts()[2] != null && board.getGhosts()[2].getRow() == rowcount &&
					board.getGhosts()[2].getCol() == colcount) {
				repr.setImage(new Image("clyde_up.png", 40,40,true,true));
			}
			else if (board.getGhosts()[3] != null && board.getGhosts()[3].getRow() == rowcount &&
					board.getGhosts()[3].getCol() == colcount) {
				repr.setImage(new Image("pinky_left.png", 40,40,true,true));
			}
				
				
		}
		else if (tileAppearance == 'G' && board.getCherryTurns() > 0) {
			repr.setImage(new Image("ghostblue.png", 40,40,true,true));
		}
		else if (tileAppearance == 'C') {
			repr.setImage(new Image("cherry.png", 40,40,true,true));
		}
		else if (tileAppearance == 'X') {
			repr.setImage(new Image("pacman_dead.png", 40,40,true,true));
		}
    }

  }  // End of Inner class Tile




  
  
  
  
  
  
  
  
  
  
  /** DO NOT EDIT BELOW */

  // The method used to process the command line arguments
  private void processArgs(String[] args)
  {
    String inputBoard = null;   // The filename for where to load the Board
    int boardSize = 0;          // The Size of the Board

    // Arguments must come in pairs
    if((args.length % 2) != 0)
    {
      printUsage();
      System.exit(-1);
    }

    // Process all the arguments 
    for(int i = 0; i < args.length; i += 2)
    {
      if(args[i].equals("-i"))
      {   // We are processing the argument that specifies
        // the input file to be used to set the board
        inputBoard = args[i + 1];
      }
      else if(args[i].equals("-o"))
      {   // We are processing the argument that specifies
        // the output file to be used to save the board
        outputBoard = args[i + 1];
      }
      else if(args[i].equals("-s"))
      {   // We are processing the argument that specifies
        // the size of the Board
        boardSize = Integer.parseInt(args[i + 1]);
      }
      else
      {   // Incorrect Argument 
        printUsage();
        System.exit(-1);
      }
    }

    // Set the default output file if none specified
    if(outputBoard == null)
      outputBoard = "Pac-Man.board";
    // Set the default Board size if none specified or less than 2
    if(boardSize < 3)
      boardSize = 10;

    // Initialize the Game Board
    try{
      if(inputBoard != null)
        board = new Board(inputBoard);
      else
        board = new Board(boardSize);
    }
    catch (Exception e)
    {
      System.out.println(e.getClass().getName() + " was thrown while creating a " +
          "Board from file " + inputBoard);
      System.out.println("Either your Board(String, Random) " +
          "Constructor is broken or the file isn't " +
          "formated correctly");
      System.exit(-1);
    }
  }

  // Print the Usage Message 
  private static void printUsage()
  {
    System.out.println("GuiPacman");
    System.out.println("Usage:  GuiPacman [-i|o file ...]");
    System.out.println();
    System.out.println("  Command line arguments come in pairs of the form: <command> <argument>");
    System.out.println();
    System.out.println("  -i [file]  -> Specifies a Pacman board that should be loaded");
    System.out.println();
    System.out.println("  -o [file]  -> Specifies a file that should be used to save the Pac-Man board");
    System.out.println("                If none specified then the default \"Pac-Man.board\" file will be used");
    System.out.println("  -s [size]  -> Specifies the size of the Pac-Man board if an input file hasn't been");
    System.out.println("                specified.  If both -s and -i are used, then the size of the board");
    System.out.println("                will be determined by the input file. The default size is 10.");
  }
}


