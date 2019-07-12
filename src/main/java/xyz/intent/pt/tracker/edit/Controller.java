package xyz.intent.pt.tracker.edit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import xyz.intent.pt.tracker.edit.util.FileUtils;
import xyz.intent.pt.tracker.edit.util.TorrentUtils;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

public class Controller {
    @FXML
    private FlowPane rootLayout;
    public TextField torrentDirField;
    public TextField saveDirField;
    public TextField oldTrackerField;
    public TextField newTrackerField;
    public ChoiceBox trackerListChoiceBox;

    public void getTorrentDir(ActionEvent actionEvent) {
        String torrentDir = selectDir("选择种子目录", torrentDirField.getText());
        if (torrentDir != null) {
            torrentDirField.setText(torrentDir);
        }
    }

    public void getSaveDir(ActionEvent actionEvent) {
        String saveDir = selectDir("选择保存目录", saveDirField.getText());
        if (saveDir != null) {
            saveDirField.setText(saveDir);
        }
    }

    public void ok(ActionEvent actionEvent) {
        if (StringUtils.isNotBlank(torrentDirField.getText())
                && StringUtils.isNotBlank(saveDirField.getText())
                && StringUtils.isNotBlank(oldTrackerField.getText())
                && StringUtils.isNotBlank(newTrackerField.getText())) {
            if (trackerListChoiceBox.getValue().equals("只替换第一个Tracker")) {
                okPolicy(TorrentUtils.DEFAULT_POLICY);
            } else if (trackerListChoiceBox.getValue().equals("删除全部并添加新的Tracker")) {
                okPolicy(TorrentUtils.DELETE_POLICY);
            }
        } else {
            alert("提示", "种子目录 | 保存目录 | tracker其中一项或多项为空！");
        }
    }

    private void okPolicy(int policy) {
//        alert("运行中", "正在批量替换tracker...");
        AtomicInteger count = new AtomicInteger();
        AtomicInteger success = new AtomicInteger();
        FileUtils.getTorrentFilePathList(torrentDirField.getText()).forEach(file -> {
            try {
                count.addAndGet(1);
                if (TorrentUtils.genTorrent(file, oldTrackerField.getText().replaceAll("\\s*", ""),
                        newTrackerField.getText().replaceAll("\\s*", ""),
                        saveDirField.getText(), policy) != null) {
                    success.addAndGet(1);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                alert("错误", "错误信息: " + e.getMessage());
            }
        });
        alert("完成", "全部替换已完成\n共: " + count + " 个种子\n成功替换: " + success
                + " 个种子\n替换失败: " + (count.get() - success.get()) + " 个种子");
    }

    public static void alert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.titleProperty().set(title);
        alert.headerTextProperty().set(message);
        alert.showAndWait();
    }

    private String selectDir(String title, String path) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        if (StringUtils.isNotBlank(path)) {
            directoryChooser.setInitialDirectory(new File(path));
        }
        directoryChooser.setTitle(title);
        File file = directoryChooser.showDialog(getStage());
        if (file != null) {
            return file.getPath();
        }
        return null;
    }

    private Stage getStage() {
        return (Stage) rootLayout.getScene().getWindow();
    }
}
