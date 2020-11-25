package frontEnd;

import javafx.application.Application;
import javafx.stage.Stage;

public class AppRoot extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		stage = new SimWindow(15, 7);
		stage.show();
	}
	
	@Override
	public void init() {
		
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
