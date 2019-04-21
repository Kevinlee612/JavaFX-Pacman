##In the respective order, these classes are able to: <br>
**Board Class:**<br>
-Instantiates a board object with grid[][] and visited[][] that keep track of PacCharacter movements.<br>
-Has methods to keep track of score and when game ends<br><br>
**Direction Class:**<br>
-Movement options for the PacCharacter objects to move around the board (grid[][])<br><br>
**PacCharacter Class:**<br>
-Objects within the game. Each has their own row and column position and character representation in grid[][].<br><br>
**GuiPacman Class:**<br>
-Intakes arguments from the console and instantiates a board of a defined size. Default size is 10.<br>
-If defined, a saved game may be loaded. If an output file is also designated, the game may be saved.<br>
-The game as represented in grid[][] is made into a GUI form that is interactable with the player.<br>
-The GuiPacman class intakes arrowkey button presses to move pacman. 'S' is used to save the game.<br>
-The class uses a GridPane object to keep track of the game. The GridPane is updated with every movement of pacman.<br><br>

**Extra----**<br>
- Cherry worth 200pts<br>
- Cherry lasts 3 movements and stalls ghost movement (pacman is invincible while it is active)<br>
- Cherry is randomly spawned in grid that does not contain pacman or ghosts.<br>
- Ghosts have their unique identity (not homogenous images)<br>
	

## Author

* **Suk Chan Lee**
- [cs11wme](mailto:scl002@ucsd.edu)


## Acknowledgments

* A thank you to freeCodeCamp and Professor James Gappy who taught me Java.
Gratitudes to Professor Zaitsev who is currently teaching me Java as well.
