package com.project.iplant.plants_crud;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.persistence.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "plants")
public class Plant {

    @Id
    @GeneratedValue
    private long plant_id;

    @Column(name = "type")
    private String type;

    @Column(name = "wateringMilliliters")
    private double wateringMilliliters; // how much milliliters does the plant need

    @Column(name = "wateringCycle")
    private int wateringCycle; // how much days need to pass for next watering

    @Column(name = "lastWatered")
    private Date lastWatered; // when was the last time the plant was watered

    public long getPlant_id() {
        return plant_id;
    }

    public void setPlant_id(int plant_id) {
        this.plant_id = plant_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getWateringMilliliters() {
        return wateringMilliliters;
    }

    public void setWateringMilliliters(double wateringMilliliters) {
        this.wateringMilliliters = wateringMilliliters;
    }

    public int getWateringCycle() {
        return wateringCycle;
    }

    public void setWateringCycle(int wateringCycle) {
        this.wateringCycle = wateringCycle;
    }

    public Date getLastWatered() {
        return lastWatered;
    }

    public void setLastWatered(String lastWatered) throws ParseException {
        if(!lastWatered.isEmpty())
            this.lastWatered = new SimpleDateFormat("dd-MM-yyyy").parse(lastWatered);
        else this.lastWatered = new Date();
    }

    public BufferedImage QRCodeGenerator() throws WriterException {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix =
                barcodeWriter.encode(Integer.toString((int)this.getPlant_id()), BarcodeFormat.QR_CODE, 200, 200);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}
