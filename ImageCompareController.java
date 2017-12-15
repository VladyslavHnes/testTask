import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageCompareController {


    public static void main(String [] args) {
        ImageCompareController imageCompareController = new ImageCompareController();
        imageCompareController.createDiffImage("src/image1.png",
                "src/image2.png", "renderedImage");
    }

    private void createDiffImage(String firstFilePath, String secondFilePath, String newFileName) {
        try {
            BufferedImage firstImage = ImageIO.read(new File(firstFilePath));
            BufferedImage secondImage = ImageIO.read(new File(secondFilePath));
            ArrayList<Coordinate> coordinateArrayList = getRgbDifferenceList(firstImage,secondImage);
            BufferedImage diffImage = secondImage;
            Graphics2D g = diffImage.createGraphics();
            Rectangle rectangle = getRectangle(coordinateArrayList);
            g.setColor(Color.RED);
            g.draw(rectangle);
            ImageIO.write(diffImage, "png", new File("src/" + newFileName + ".png"));
        } catch (DifferentSizeException | IOException e) {
            e.printStackTrace();
        }
    }

    private Rectangle getRectangle(ArrayList<Coordinate> coordinates) {
        int minIndX = coordinates.get(0).getX();
        int minIndY = coordinates.get(0).getY();
        int maxIndX = coordinates.get(0).getX();
        int maxIndY = coordinates.get(0).getY();
        for(Coordinate coordinate : coordinates ) {
            if(minIndX > coordinate.getX())
                minIndX = coordinate.getX();
            if(minIndY > coordinate.getY())
                minIndY = coordinate.getY();
            if(maxIndX < coordinate.getX())
                maxIndX = coordinate.getX();
            if(maxIndY < coordinate.getY())
                maxIndY = coordinate.getY();
        }
        return new Rectangle(minIndX,minIndY,
                Math.abs(minIndX - maxIndX), Math.abs(minIndY - maxIndY));
    }

    private ArrayList<Coordinate> getRgbDifferenceList(BufferedImage firstImage, BufferedImage secondImage) throws DifferentSizeException {
        int width = firstImage.getWidth();
        int height = firstImage.getHeight();
        ArrayList<Coordinate> diffCoordinates = new ArrayList<>();
        if(!isSameSize(firstImage, secondImage)) {
            throw new DifferentSizeException("Your images have different size values!");
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                byte firstBlue = getBlueValue(i, j, firstImage);
                byte firstGreen = getGreenValue(i, j, firstImage);
                byte firstRed = getRedValue(i, j, firstImage);
                byte firstAlpha = getAlphaValue(i, j, firstImage);
                byte secondBlue = getBlueValue(i, j, secondImage);
                byte secondGreen = getGreenValue(i, j, secondImage);
                byte secondRed = getRedValue(i, j, secondImage);
                byte secondAlpha = getAlphaValue(i, j, secondImage);
                int percentDifference = (100*(Math.abs(firstBlue-secondBlue) + Math.abs(firstGreen - secondGreen) +
                        Math.abs(firstRed-secondRed) + Math.abs(firstAlpha-secondAlpha))/(255*4));
                if(percentDifference > 10) {
                    Coordinate coordinate = new Coordinate();
                    coordinate.setX(i);
                    coordinate.setY(j);
                    diffCoordinates.add(coordinate);
                }
            }
        }
        return diffCoordinates;
    }

    private byte getBlueValue(int x, int y, BufferedImage image) {
        return (byte) (image.getRGB(x, y) & 0xff);
    }

    private byte getGreenValue(int x, int y, BufferedImage image) {
        return (byte) ((image.getRGB(x, y) & 0xff00) >> 8);
    }

    private byte getRedValue(int x, int y, BufferedImage image) {
        return (byte) ((image.getRGB(x, y) & 0xff0000) >> 16);
    }

    private byte getAlphaValue(int x, int y, BufferedImage image) {
        return (byte) ((image.getRGB(x, y) & 0xff000000) >>> 24);
    }

    private boolean isSameSize(BufferedImage firstImage, BufferedImage secondImage) {
        int firstImageWidth = firstImage.getWidth();
        int firstImageHeight = firstImage.getHeight();
        int secondImageWidth = secondImage.getWidth();
        int secondImageHeight = secondImage.getHeight();
        return (firstImageWidth == secondImageWidth && firstImageHeight == secondImageHeight);
    }
}
