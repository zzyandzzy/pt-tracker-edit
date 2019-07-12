package xyz.intent.pt.tracker.edit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Torrent implements Serializable {
    private static final long serialVersionUID = 8814352779387628367L;
    private String announce;
    private List<String> announceList = new ArrayList<>();
    private String name;

    @Override
    public String toString() {
        return "Torrent{" +
                "announce='" + announce + '\'' +
                ", announceList=" + announceList +
                ", name='" + name + '\'' +
                '}';
    }

    public String getAnnounce() {
        return announce;
    }

    public void setAnnounce(String announce) {
        this.announce = announce;
    }

    public List<String> getAnnounceList() {
        return announceList;
    }

    public void setAnnounceList(List<String> announceList) {
        this.announceList = announceList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
