package com.strangeone101.headconverter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Error: Source folder not found");
            return;
        }

        String path = String.join(" ", args);

        File file = new File(path);

        if (!file.exists()) {
            System.out.println("Directory not found!");
            return;
        }

        if (!file.isDirectory()) {
            System.out.println("Path provided is not a directory!");
            return;
        }

        File output = new File(path, "Output");
        if (file.exists() && !file.isDirectory()) {
            file.delete();
        }

        int counter = 0;
        long time = System.currentTimeMillis();
        for (File innerFile : file.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"))) {
            BufferedImage image = null;
            try {
                image = ImageIO.read(innerFile);

                //Define a new image canvas with the correct format
                BufferedImage outputImage = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB_PRE);

                //Define the top and bottom layer images
                BufferedImage bottom = image.getSubimage(0, 0, 32, 16);
                BufferedImage topLayer = image.getSubimage(32, 0, 32, 16);

                //Draw the bottom layer first then the top layer over it
                Graphics2D g = (Graphics2D) outputImage.getGraphics();
                g.drawImage(bottom, 0, 0, new Color(0x00000000, true), null);
                g.drawImage(topLayer, 0, 0, null);

                //Export the file
                File outputFile = new File(output, innerFile.getName());
                outputFile.mkdirs();
                ImageIO.write(outputImage, "png", outputFile);
                counter++;

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        System.out.println("Success! Outputted " + counter + " images in " + (System.currentTimeMillis() - time) + "ms!");
    }
}
