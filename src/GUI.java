import javafx.scene.text.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.effect.*;
import javafx.animation.*;

public class GUI extends Application implements EventHandler<ActionEvent>{
	
	int WIDTH = 7;
	int HEIGHT = 6;
	int CELL_SIZE = 80;
	
	Stage window;
	GridPane discGrid = createBoard();
	GameLogic gameLogic = new GameLogic();
	Pane bottom = setBottomDisplay();
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void run(Stage stage) {
		window = stage;
		window.setTitle("Connect Four");

		BorderPane root = new BorderPane();		
		root.setTop(createButtons());
		StackPane completeBoard = new StackPane();
		completeBoard.getChildren().addAll(discGrid);
		root.setCenter(completeBoard);
		root.setBottom(bottom);
		Scene scene = new Scene(root,CELL_SIZE*7,CELL_SIZE*8);
		
		window.setScene(scene);
		
		stage.show();
	}
	
	// Creating the buttons for the top of the interface to all users to make moves
		
	private HBox createButtons() {		
		HBox buttons = new HBox();
		buttons.setPadding(new Insets(10,10,10,10));
		buttons.setStyle("-fx-background-color: #336699;");
		buttons.setSpacing(10);
		Button buttonArray[] = new Button[WIDTH];
		for (int columns = 0; columns < WIDTH; columns++) {
			int colNum = columns+1;
			buttonArray[columns] = new Button(String.valueOf(colNum));
			buttonArray[columns].setOnAction(e->gameLogic.makeMove(colNum, this));		
			buttonArray[columns].setPrefSize(70,30);
			buttons.getChildren().addAll(buttonArray[columns]);
		}
		
		return buttons;
	}
	
	
	
	public void placeDisc(int player ,int row ,int col) {
		Circle disc = new Circle(30);

		Light.Distant light = new Light.Distant();
		light.setAzimuth(40.0);
		light.setElevation(100.0);
		
		Lighting lighting = new Lighting();
		lighting.setLight(light);
		lighting.setSurfaceScale(3.0);
		if (player == 1) {disc.setFill(Color.RED);}
		else {disc.setFill(Color.GOLD);}
		disc.setEffect(lighting);
		GridPane.setHalignment(disc, HPos.CENTER);
		disc.setTranslateY(-6* CELL_SIZE);
		discGrid.add(disc, col, row);
		
		TranslateTransition animation = new TranslateTransition(Duration.seconds(1),disc);
		animation.setToY(0);
		animation.play();		
	}	
	
	// Creating the game board with individual cells
	
	private StackPane createCell() {
		StackPane cell = new StackPane();
		Shape fill = new Rectangle(CELL_SIZE,CELL_SIZE);
		Circle hole = new Circle(30);
		hole.setCenterX(CELL_SIZE/2);
		hole.setCenterY(CELL_SIZE/2);
		fill = Shape.subtract(fill, hole);
		cell.getChildren().addAll(fill);
		
		Light.Distant light = new Light.Distant();
		light.setAzimuth(40.0);
		light.setElevation(100.0);
		
		Lighting lighting = new Lighting();
		lighting.setLight(light);
		lighting.setSurfaceScale(3.0);
		
		fill.setEffect(lighting);		
		fill.setFill(Color.BLUE);
		
		return cell;
	}
	
	
	private GridPane createBoard() {
		GridPane board = new GridPane();
		for (int columns = 0; columns< WIDTH; columns++) {
			for(int rows = 0; rows< HEIGHT; rows++) {
				board.add(createCell(), columns, rows);
			}
		}
		return board;
	}
	
	// Bottom of the interface dealing with the current player and the winner.
	// Also allows user to reset game and exit the game.
	
	private Pane bottomTextMaker() {
		Pane bottomText = new StackPane();
		bottomText.setPrefSize(560, 60);
		bottomText.setStyle("-fx-background-color:#1c1a75");
		Text text = new Text();	

		if (gameLogic.isGameOver()) text.setText("Winner is: "+ gameLogic.returnWinner());
		else text.setText("Current Player: "+ gameLogic.returnCurrentPlayer());
		
		text.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
	    text.setFill(Color.WHITE);
	    bottomText.getChildren().add(text);
		return bottomText;
	}
	
	private Pane setBottomDisplay() {		
		HBox lowerBar = new HBox();
		lowerBar.setPadding(new Insets(10,10,10,10));
		lowerBar.setStyle("-fx-background-color: #336699;");
		lowerBar.setSpacing(10);
		Button newGame = new Button("New Game");
		Button exitGame = new Button("Exit Game");
		
		newGame.setPrefSize(265, 30);
		newGame.setOnAction(e-> resetGame());
		exitGame.setPrefSize(265,30);
		exitGame.setOnAction(e->window.close());

		lowerBar.getChildren().addAll(newGame,exitGame);
		VBox bottomBar = new VBox();
		bottomBar.getChildren().addAll(bottomTextMaker(),lowerBar);
		
		return bottomBar;
	}
	
	public void updateBottomDisplay() {
		bottom.getChildren().set(0,bottomTextMaker());
	}
	
	public void resetGame() {
		gameLogic.resetBoard();
		discGrid.getChildren().clear();
		discGrid = createBoard();
		updateBottomDisplay();
		run(window);
	}
	
	// Glowing effect after a player wins
	
	public void winningResult(){
		int winningCells[][] = gameLogic.winningCombination();
		Circle winning[] = new Circle[4];
		
		for(int i = 0; i<4;i++) {
			winning[i] = new Circle(6,Color.ALICEBLUE);
			
			GridPane.setHalignment(winning[i], HPos.CENTER);
			winning[i].setEffect(new BoxBlur(15,15,3));
			ScaleTransition winningAnimation = new ScaleTransition(Duration.seconds(1.5),winning[i]);
			
			
			discGrid.add(winning[i],winningCells[i][1],winningCells[i][0]);
			
			winningAnimation.setByX(5f);
			winningAnimation.setByY(5f);
			winningAnimation.setAutoReverse(true);
			winningAnimation.setCycleCount(21);
			
			SequentialTransition sequence = new SequentialTransition(
					new PauseTransition(Duration.seconds(1)),
					winningAnimation);
			sequence.play();
		}
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception{
		run(primaryStage);	
	}
	@Override
	public void handle(ActionEvent event) {		
	}
}

