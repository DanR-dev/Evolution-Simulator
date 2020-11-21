

import javafx.application.Application;
import javafx.stage.Stage;

public class AppRoot extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		stage = new SimWindow(3, 4);
		stage.show();
	}
	
	@Override
	public void init() {
		
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
