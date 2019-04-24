package com.bmusic.bmusicworld.horizontal;

import com.bmusic.bmusicworld.model.Track;

import java.util.ArrayList;
import java.util.List;

import dm.audiostreamer.MediaMetaData;

public class SectionDataModel {



    private String headerTitle;
    private String headerId;
   // private ArrayList<SingleItemModel> allItemsInSection;
    private ArrayList<Track> allItemsInSection;
    private ArrayList<MediaMetaData>  allItemsInSection2;


    public SectionDataModel() {

    }
    public SectionDataModel(String headerTitle, ArrayList<Track> allItemsInSection) {
        this.headerTitle = headerTitle;
        this.allItemsInSection = allItemsInSection;
    }

    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public ArrayList<Track> getAllItemsInSection() {
        return allItemsInSection;
    }
    public ArrayList<MediaMetaData> getAllItemsInSection2() {
        return allItemsInSection2;
    }
    public void setAllItemsInSection(ArrayList<Track> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }
    public void setAllItemsInSection2(ArrayList<MediaMetaData>  allItemsInSection) {
        this.allItemsInSection2 = allItemsInSection;
    }


}
