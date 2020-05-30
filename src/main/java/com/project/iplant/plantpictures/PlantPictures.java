package com.project.iplant.plantpictures;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "plant_pictures")
public class PlantPictures {
    @Id
    private long plant_id;

    @Column(name = "url")
    String url;

    @Column(name = "dateTaken")
    String dateTaken;

    public PlantPictures(long plant_id, String url, String dateTaken) {
        this.plant_id = plant_id;
        this.url = url;
        this.dateTaken = dateTaken;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(String dateTaken) {
        this.dateTaken = dateTaken;
    }
}
