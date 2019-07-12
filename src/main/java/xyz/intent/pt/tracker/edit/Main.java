package xyz.intent.pt.tracker.edit;

import xyz.intent.pt.tracker.edit.util.FileUtils;
import xyz.intent.pt.tracker.edit.util.TorrentUtils;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        System.out.print("选择软件运行模式：\n 0: GUI\n 1: 命令行\n");
        System.out.print("你的选择：");
        int mode = scanner.nextInt();
        switch (mode) {
            case 0:
                MainApplication mainApplication = new MainApplication();
                mainApplication.run();
                break;
            case 1:
                startCMD();
                break;
            default:
                System.out.println("没有该选项");
        }
    }

    private static void startCMD() {
        AtomicInteger count = new AtomicInteger();
        AtomicInteger success = new AtomicInteger();
        System.out.print("请输入种子目录: ");
        String torrentPath = scanner.next();
        System.out.print("请输入旧的Tracker: ");
        String oldTracker = scanner.next();
        System.out.print("请输入新的Tracker: ");
        String newTracker = scanner.next();
        System.out.print("请输入存储种子的目录: ");
        String savePath = scanner.next();
        System.out.print("选择多Tracker替换方案:\n 0: 只替换第一个Tracker\n 1: 删除全部并添加新的Tracker\n ");
        System.out.print("你的选择：");
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
