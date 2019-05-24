package xyz.intent.pt.tracker.edit.util;

import com.github.cdefgah.bencoder4j.io.BencodeStreamReader;
import com.github.cdefgah.bencoder4j.model.BencodedByteSequence;
import com.github.cdefgah.bencoder4j.model.BencodedDictionary;
import com.github.cdefgah.bencoder4j.model.BencodedList;
import xyz.intent.pt.tracker.edit.model.Torrent;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;

public class TorrentUtils {
    private static final String SEPARATOR = File.separator;
    public static final int DEFAULT_POLICY = 0;
    public static final int DELETE_POLICY = 1;

    /**
     * 根据torrent文件替换passkey并生成新的文件
     *
     * @param oldPasskey        oldPasskey
     * @param passkey           passkey
     * @param torrentPath       torrent文件目录
     * @param savePath          生成的文件目录
     * @param fileName          文件名
     * @param trackerListPolicy 替换trackerList策略
     * @return Torrent
     * @throws Exception
     */
    public static Torrent genTorrent(String oldPasskey, String passkey,
                                     String torrentPath, String savePath, String fileName,
                                     int trackerListPolicy)
            throws Exception {
        Torrent torrent = new Torrent();
        // 读取文件
        byte[] torrentData = FileUtils.readFileToByteArray(new File(torrentPath + SEPARATOR + fileName));
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(torrentData);
        BencodeStreamReader bsr = new BencodeStreamReader(byteArrayInputStream);
        byteArrayInputStream.close();
        BencodedDictionary torrentDictionary = new BencodedDictionary(bsr);
        Iterator<BencodedByteSequence> torrentIterable = torrentDictionary.getKeysIterator();
        while (torrentIterable.hasNext()) {
            BencodedByteSequence key = torrentIterable.next();
            switch (key.toUTF8String()) {
                case "announce":
                    BencodedByteSequence announce = (BencodedByteSequence) torrentDictionary.get(key);
                    torrent.setAnnounce(announce.toUTF8String());
                    break;
                case "announce-list":
                    BencodedList announceList = (BencodedList) torrentDictionary.get(key);
                    announceList.forEach(announces -> {
                        BencodedList announceUrlList = (BencodedList) announces;
                        announceUrlList.forEach(announceObj -> {
                            BencodedByteSequence announceByte = (BencodedByteSequence) announceObj;
                            torrent.getAnnounceList().add(announceByte.toUTF8String());
                        });
                    });
                    break;
                case "info":
                    BencodedDictionary infoDictionary = (BencodedDictionary) torrentDictionary.get(key);
                    // name
                    Iterator<BencodedByteSequence> infoIterable = infoDictionary.getKeysIterator();
                    while (infoIterable.hasNext()) {
                        BencodedByteSequence infoKey = infoIterable.next();
                        if (infoKey.toUTF8String().equals("name")) {
                            BencodedByteSequence name = (BencodedByteSequence) infoDictionary.get(infoKey);
                            torrent.setName(name.toUTF8String());
                        }
                    }
            }
        }
        boolean save = false;
        String announce = torrent.getAnnounce().replace(oldPasskey, passkey);
        if (announce.contains(passkey)) {
            save = true;
            // 替换passkey
            torrentDictionary.put("announce", new BencodedByteSequence(announce));
        }
        // announce list
        BencodedList announceList = new BencodedList();
        if (trackerListPolicy == DELETE_POLICY) {
            announceList.add(new BencodedByteSequence(passkey));
        }
        // 判断savePath是否存在
        File file = new File(savePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (save) {
            try (FileOutputStream fos = new FileOutputStream(savePath + SEPARATOR + fileName)) {
                torrentDictionary.writeObject(fos);
            }
            System.out.println("file: " + fileName + " name: " + torrent.getName() +
                    " announce: " + torrent.getAnnounce() + " 替换为 " + announce + " 成功");
            return torrent;
        } else {
            System.out.println("file: " + fileName + " name: " + torrent.getName() + " 替换失败");
            return null;
        }
    }
}
