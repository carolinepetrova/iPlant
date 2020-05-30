package com.project.iplant.plantpictures;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class PlantPicturesWriterService {
    @Autowired
    PlantPicturesRepository plantPicturesRepository;

    public void savePicture(String payload) {
        JSONObject obj = new JSONObject(payload);
        byte[] byteData = obj.get("imageData").toString().getBytes();
        String url = obj.get("plantID") + "_" + obj.get("dateTaken");
        try {
            this.saveImage(byteData, url);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        PlantPictures plantPictures = new PlantPictures(Long.parseLong(obj.get("plantID").toString()),
                url, obj.get("dateTaken").toString());
        plantPicturesRepository.save(plantPictures);

    }

    private void saveImage(byte[] bytes, String url) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Iterator<?> readers = ImageIO.getImageReadersByFormatName("jpg");


        ImageReader reader = (ImageReader) readers.next();
        Object source = bis;
        ImageInputStream iis = ImageIO.createImageInputStream(source);
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();

        Image image = reader.read(0, param);
        //got an image file

        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        //bufferedImage is the RenderedImage to be written

        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, null, null);

        File imageFile = new File(url);
        ImageIO.write(bufferedImage, "jpg", imageFile);
    }
}
