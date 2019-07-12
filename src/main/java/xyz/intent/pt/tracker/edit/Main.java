package xyz.intent.pt.tracker.edit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import xyz.intent.pt.tracker.edit.util.FileUtils;
import xyz.intent.pt.tracker.edit.util.TorrentUtils;

import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;


public class Main extends Application {

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


    public static void main(String[] args) {
        launch(args);
        startCMD();
    }

    private static void startCMD() {
        AtomicInteger count = new AtomicInteger();
        AtomicInteger success = new AtomicInteger();
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入种子目录: ");
        String torrentPath = scanner.next();
        System.out.print("请输入旧的Tracker: ");
        String oldTracker = scanner.next();
        System.out.print("请输入新的Tracker: ");
        String newTracker = scanner.next();
        System.out.print("请输入存储种子的目录: ");
        String savePath = scanner.next();
        System.out.print("选择多Tracker替换方案:\n 0: 只替换第一个Tracker\n 1: 删除全部并添加新的Tracker\n ");
        int policy = scanner.nextInt();
        FileUtils.getTorrentFilePathList(torrentPath).forEach(file -> {
            try {
                count.addAndGet(1);
                if (TorrentUtils.genTorrent(file, oldTracker.replaceAll("\\s*", ""),
                        newTracker.replaceAll("\\s*", ""), savePath, policy) != null) {
                    success.addAndGet(1);
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });
        System.out.println("完成, 全部替换已完成\n共: " + count + " 个种子\n成功替换: " + success
                + " 个种子\n替换失败: " + (count.get() - success.get()) + " 个种子");
    }
}
