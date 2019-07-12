package xyz.intent.pt.tracker.edit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * @author intent
 * @date 2019/7/12 8:32
 * @about <link href='http://zzyitj.xyz/'/>
 */
public class MainApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL url = getClass().getClassLoader().getResource("sample.fxml");
        if (url != null) {
            Parent root = FXMLLoader.load(url);
            primaryStage.setTitle("批量tracker修改器");
            primaryStage.setScene(new Scene(root, 600, 350));
            primaryStage.show();
        } else {
            Controller.alert("错误", "没有布局文件");
        }
    }


    public void run() {
        launch();
    }
}
