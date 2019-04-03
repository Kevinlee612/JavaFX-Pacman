import java.lang.StringBuilder;
import java.util.*;
import java.io.*;
import java.util.Random;

/**
 * @author      Suk Chan Lee A15427567 Sukchan.kevin@gmail.com
 * This class instantiates the game board of a defined size.
 * PacCharacter objects are used to represent characters within the pacman game.
 * A grid[][] and visited[][] are used to keep track of character movements and
 * points.
 */
public class Board{

    // FIELD
    public final int GRID_SIZE;

    private char[][] grid;          // String Representation of Pac-man Game Board
    private boolean[][] visited;    // Record of where Pac-man has visited
    private PacCharacter pacman;    // Pac-man that user controls
    private PacCharacter[] ghosts;  // 4 Ghosts that controlled by the program
    private PacCharacter cherry;	// Cherry that enables pacman to destroy ghosts
    private int score;              // Score Recorded for the gamer
    private int cherryTurns;		// Number of turns Cherry is active for
    private boolean cherryeaten = false; //Has cherry been consumed yet


    /*
     * Constructor
     *
     * <p> Description: Initializes a board of the indicated size
     * By default ghosts are instantiated in corners of grid[][]
     * Pacman is instantiated in the center of the grid[][]
     * Cherry is instantiated in a random slot not occupied by ghosts or pacman
     *
     * @param:  size: The size of the board
     */
    public Board(int size) {

        // Initialize instance variables
    	Random rand = new Random();
    	int n = rand.nextInt(size);
    	int m = rand.nextInt(size);
    	while ((n == size/2 && m == size/2) || (n == 0 && m == 0)
    		|| (n == size-1 && m == size-1) || (n == 0 && m == size-1)
    		|| (n == size-1 && m == 0)) {
    		n = rand.nextInt(size); //Ensures cherry does on spawn on pacman or ghost
    		m = rand.nextInt(size);
    	}
        GRID_SIZE = size;
        grid = new char[GRID_SIZE][GRID_SIZE];
        visited = new boolean[GRID_SIZE][GRID_SIZE];
        score = 0;

        pacman = new PacCharacter(GRID_SIZE/2, GRID_SIZE/2, 'P');
        ghosts = new PacCharacter[4];
        ghosts[0] = new PacCharacter(          0,           0, 'G');
        ghosts[1] = new PacCharacter(          0, GRID_SIZE-1, 'G');
        ghosts[2] = new PacCharacter(GRID_SIZE-1,           0, 'G');
        ghosts[3] = new PacCharacter(GRID_SIZE-1, GRID_SIZE-1, 'G');
        cherry = new PacCharacter(n,m,'C');

        setVisited(GRID_SIZE/2, GRID_SIZE/2);

        refreshGrid();
    }



    // To Tutors: this is for PA6
    public Board(String inputBoard) throws IOException {
        // Create a scanner to scan the inputBoard.
        Scanner input = new Scanner(new File(inputBoard));

        // First integer in inputBoard is GRID_SIZE.
        GRID_SIZE = input.nextInt();
        // Second integer in inputBoard is score.
        score = input.nextInt();

        grid = new char[GRID_SIZE][GRID_SIZE];
        visited = new boolean[GRID_SIZE][GRID_SIZE];
        String line = input.nextLine(); // Skip current line (score line)

        char rep;
        ghosts = new PacCharacter[4];
        for ( int rowIndex = 0; rowIndex < GRID_SIZE; rowIndex++ )
        {
            line = input.nextLine();
            for ( int colIndex = 0; colIndex < GRID_SIZE; colIndex++ ) {
                rep = line.charAt(colIndex);
                grid[rowIndex][colIndex] = rep;

                switch (rep) {
                    case 'P': //'P' Character sets pacman PacCharacter object
                        setVisited(rowIndex, colIndex);
                        pacman = new PacCharacter(rowIndex, colIndex, 'P');
                        break;
                    case 'G'://'G' Character sets ghost PacCharacter object
                        for (int i = 0; i < ghosts.length; i++) {
                            if (ghosts[i] == null) {
                                ghosts[i] = new PacCharacter(rowIndex, colIndex, 'G');;
                                break;
                            }
                        }
                        break;
                    case 'X'://'X' Character represents game over
                        pacman = new PacCharacter(rowIndex, colIndex, 'P');
                        for (int i = 0; i < ghosts.length; i++) {
                            if (ghosts[i] == null) {
                                ghosts[i] = new PacCharacter(rowIndex, colIndex, 'G');;
                                break;
                            }
                        }
                        break;
                    case 'C'://'C' Character represents Cherry PacCharacter Object
                    	cherry = new PacCharacter(rowIndex, colIndex, 'C');
                    	break;
                    case ' ':
                        setVisited(rowIndex, colIndex);
                        break;
                }
            }
        }
        input.close(); //Closes input file


    }


    public int getScore() {
        return score;
    }


    public char[][] getGrid() {
        return grid;
    }

    public void setVisited(int x, int y) {
        if (x < 0 || y < 0 || x >= GRID_SIZE || y > GRID_SIZE) return;
        visited[x][y] = true;
    }

    /* Refreshes the grid[][] to reflect where the PacCharacter objects are
     * Updates game progress and keeps track of score
     * 
     */
    public void refreshGrid() {

        for (int i = 0; i < GRID_SIZE; i++)
            for (int j = 0; j < GRID_SIZE; j++) {
                if (!visited[i][j])
                    grid[i][j] = '*';
                else
                    grid[i][j] = ' ';
            }
        if (cherryeaten == false) {
        	grid[cherry.getRow()][cherry.getCol()] = cherry.getAppearance();
        }

        grid[pacman.getRow()][pacman.getCol()] = pacman.getAppearance();
        for (PacCharacter ghost : ghosts) {
        	if (ghost != null) {
	            if (cherryTurns == 0) {
	            	if (pacman.getRow() == ghost.getRow() && pacman.getCol() == ghost.getCol())
	            		grid[ghost.getRow()][ghost.getCol()] = 'X';
	                else grid[ghost.getRow()][ghost.getCol()] = ghost.getAppearance();
	            }
	            else if (cherryTurns > 0) {
	            	if (pacman.getRow() == ghost.getRow() && pacman.getCol() == ghost.getCol()) {
	            		grid[ghost.getRow()][ghost.getCol()] = pacman.getAppearance();
	            		ghost = null;
	            		//Remove ghost if cherry is active and on same spot as pacman
	            	}
	                else {
	                	grid[ghost.getRow()][ghost.getCol()] = ghost.getAppearance();
	                }
	            }
        	}
        }
        //Decrement cherry turns
        if (cherryTurns > 0) {
        	cherryTurns--;
        }
        

    }


    public boolean canMove(Direction direction) {
    	if (isGameOver()) return false;
        if (direction == null) return false;
        // Calculate Coordinate after Displacement
        int pacmanRow = pacman.getRow() + direction.getY();
        int pacmanCol = pacman.getCol() + direction.getX();

        return pacmanRow >= 0 && pacmanRow < GRID_SIZE && pacmanCol >= 0 && pacmanCol < GRID_SIZE;
    }


    public void move(Direction direction) {
        // Calculate Coordinate after Displacement
	        int pacmanRow = pacman.getRow() + direction.getY();
	        int pacmanCol = pacman.getCol() + direction.getX();
	
	        pacman.setPosition(pacmanRow, pacmanCol);
	        if (!visited[pacmanRow][pacmanCol]) {
	            score += 10;
	            visited[pacmanRow][pacmanCol] = true;
	        }
	        if (pacman.getCol() == cherry.getCol() && pacman.getRow() == cherry.getRow() && cherryeaten == false) {
	        	score += 190;
	        	cherryTurns = 4;
	        	cherryeaten = true;
	        }
	        for (PacCharacter ghost : ghosts) {
	        	if (ghost != null) {
	            	ghostMove(ghost);
	        	}
	        }
	
	        refreshGrid();
    }


    public boolean isGameOver() {
        int pacmanRow = pacman.getRow();
        int pacmanCol = pacman.getCol();

        for (PacCharacter ghost : ghosts) {
        	if (ghost != null && cherryTurns == 0) {
        		if (ghost.getRow() == pacmanRow && ghost.getCol() == pacmanCol)
                    return true;
        	}
        }
        return false;
    }

    /*
     * Moves ghosts toward pacman unless pacman has eaten the cherry
     * Cherry grants invulnerability for 3 turns
     */
    public Direction ghostMove(PacCharacter ghost) {
        int pacmanRow = pacman.getRow();
        int pacmanCol = pacman.getCol();

        int ghostRow = ghost.getRow();
        int ghostCol = ghost.getCol();

        int rowDist = Math.abs(ghostRow - pacmanRow);
        int colDist = Math.abs(ghostCol - pacmanCol);

        if (cherryTurns == 0) {
	        if (rowDist == 0 && colDist > 0) {
	            if (ghostCol - pacmanCol > 0) {
	                ghost.setPosition(ghostRow, ghostCol - 1);
	                return Direction.LEFT;
	            } else { // ghostCol - pacmanCol < 0
	                ghost.setPosition(ghostRow, ghostCol + 1);
	                return Direction.RIGHT;
	            }
	        }
	        else if (rowDist > 0 && colDist == 0 ) {
	            if (ghostRow - pacmanRow > 0) {
	                ghost.setPosition(ghostRow - 1, ghostCol);
	                return Direction.UP;
	            } else { // ghostRow - pacmanRow < 0
	                ghost.setPosition(ghostRow + 1, ghostCol);
	                return Direction.DOWN;
	            }
	        }
	        else if (rowDist == 0 && colDist == 0) {
	            return Direction.STAY;
	        }
	        else {
	            if (rowDist < colDist) {
	                if (ghostRow - pacmanRow > 0) {
	                    ghost.setPosition(ghostRow - 1, ghostCol);
	                    return Direction.UP;
	                } else { // ghostRow - pacmanRow < 0
	                    ghost.setPosition(ghostRow + 1, ghostCol);
	                    return Direction.DOWN;
	                }
	            } else {
	                if (ghostCol - pacmanCol > 0) {
	                    ghost.setPosition(ghostRow, ghostCol - 1);
	                    return Direction.LEFT;
	                } else { // ghostCol - pacmanCol < 0
	                    ghost.setPosition(ghostRow, ghostCol + 1);
	                    return Direction.RIGHT;
	                }
	            }
	        }
        }
        else{
        	if((ghostRow == 0 && ghostCol == 0) || 
        			(ghostRow == GRID_SIZE -1 && ghostCol == GRID_SIZE -1) ||
        			(ghostRow == 0 && ghostCol == GRID_SIZE -1) ||
        			(ghostRow == GRID_SIZE -1 && ghostCol == 0)) {
        		return Direction.STAY; //Don't move if ghost is backed into corner of matrix
        	}
        	else if (rowDist >= colDist){ //If rowDist >= colDist, prioritize vertical movement
        		if (ghostRow != 0 && ghostRow != GRID_SIZE-1) {
        			if (ghostRow-pacmanRow >0) {
        				return Direction.DOWN;
        			}
        			else {
        				return Direction.UP;
        			}
        		}
        		else if (ghostCol != 0 && ghostCol != GRID_SIZE -1){
        			if (ghostCol-pacmanCol >0) {
        				return Direction.RIGHT;
        			}
        			else {
        				return Direction.LEFT;
        			}
        		}
        	}
        	else if (rowDist < colDist) {//If rowDist < colDist, prioritize horizontal movement
        		if (ghostCol != 0 && ghostCol != GRID_SIZE -1){
        			if (ghostCol-pacmanCol >0) {
        				return Direction.RIGHT;
        			}
        			else {
        				return Direction.LEFT;
        			}
        		}
        		else if (ghostRow != 0 && ghostRow != GRID_SIZE-1) {
        			if (ghostRow-pacmanRow >0) {
        				return Direction.DOWN;
        			}
        			else {
        				return Direction.UP;
        			}
        		}
        		
        	}
    		return Direction.STAY;
        }

    }




    // To Tutors: this is for PA6
    public void saveBoard(String outputBoard) throws IOException {
    	try {
    		PrintWriter output = new PrintWriter(new File(outputBoard));
            // First print out the GRID_SIZE.
            output.println(GRID_SIZE);
            // Second print out the score.
            output.println(score);

            for ( int rowIndex = 0; rowIndex < GRID_SIZE; rowIndex++ )
            {
                for ( int colIndex = 0; colIndex < GRID_SIZE; colIndex++ )
                    output.print(grid[rowIndex][colIndex]);
                output.print("\n");
            }
            output.close();
    	}
        catch (IOException e){
        	System.out.println("saveBoard threw an Exception");
        }
    }

    public int getCherryTurns() {
    	return cherryTurns;
    }
    
    public PacCharacter[] getGhosts() {
    	return ghosts;
    }

    public PacCharacter getPacMan() {
    	return pacman;
    }
    
    public String toString(){

        StringBuilder outputString = new StringBuilder();
        outputString.append(String.format("Score: %d\n", this.score));

        for (int row = 0; row < GRID_SIZE; row++)
        {
            for (int column = 0; column < GRID_SIZE; column++) {
                outputString.append("  ");
                outputString.append(grid[row][column]);
            }

            outputString.append("\n");
        }
        return outputString.toString();

    }




}
