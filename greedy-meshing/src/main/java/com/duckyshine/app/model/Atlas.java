package com.duckyshine.app.model;

import java.util.List;
import java.util.ArrayList;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.duckyshine.app.debug.Debug;
import com.duckyshine.app.math.Direction;

import com.duckyshine.app.utility.ResourceFinder;

import static org.lwjgl.opengl.GL30.*;

public class Atlas {
    private final static int WIDTH = 32;
    private final static int HEIGHT = 48;

    private final static int ROWS = 3;
    private final static int COLUMNS = 2;

    private final static int IMAGE_SIZE = 16;

    private final static String PARENT_DIRECTORY = "textures/blocks/";

    public static void setup() {
        int textureId = glGenTextures();

        Atlas.initialiseTextureArray(textureId);

        Atlas.create();
    }

    private static void initialiseTextureArray(int textureId) {
        glActiveTexture(GL_TEXTURE0);

        glBindTexture(GL_TEXTURE_2D_ARRAY, textureId);

        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glTexImage3D(
                GL_TEXTURE_2D_ARRAY,
                0,
                GL_RGBA,
                Atlas.WIDTH,
                Atlas.HEIGHT,
                BlockType.values().length,
                0,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                (ByteBuffer) null);
    }

    private static void create() {
        for (BlockType blockType : BlockType.values()) {
            ByteBuffer atlas = Atlas.get(blockType);

            Atlas.add(atlas, blockType.getIndex());
        }
    }

    private static ByteBuffer get(BlockType blockType) {
        String filepath = null;

        String blockName = blockType.getName();

        List<File> files = new ArrayList<>();

        String directory = ResourceFinder.getResourcePath(Atlas.PARENT_DIRECTORY + blockName).toString();

        for (Direction direction : Direction.values()) {
            String filename = direction.getName() + ".png";

            filepath = ResourceFinder.getFile(Atlas.PARENT_DIRECTORY, blockName, filename);

            files.add(new File(filepath));
        }

        return Atlas.build(files, directory, blockName);
    }

    private static ByteBuffer build(List<File> files, String directory, String blockName) {
        BufferedImage atlas = new BufferedImage(Atlas.WIDTH, Atlas.HEIGHT, BufferedImage.TYPE_INT_ARGB);

        Graphics2D canvas = Atlas.getCanvas(atlas);

        for (int i = 0; i < files.size(); i++) {
            BufferedImage image = null;

            try {
                image = ImageIO.read(files.get(i));
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            int row = i / Atlas.COLUMNS;
            int column = i % Atlas.COLUMNS;

            int x = column * Atlas.IMAGE_SIZE;
            int y = row * Atlas.IMAGE_SIZE;

            canvas.drawImage(image, x, y, Atlas.IMAGE_SIZE, Atlas.IMAGE_SIZE, null);
        }

        canvas.dispose();

        Atlas.save(atlas, directory, blockName);

        return getByteBufferFromImage(atlas);
    }

    private static void save(BufferedImage image, String directory, String filename) {
        String outputPath = Path.of(directory, filename).toString();

        try {
            File file = new File(outputPath + ".png");

            ImageIO.write(image, "png", file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static Graphics2D getCanvas(BufferedImage image) {
        Graphics2D canvas = image.createGraphics();

        canvas.setComposite(AlphaComposite.Clear);

        canvas.fillRect(0, 0, Atlas.WIDTH, Atlas.HEIGHT);

        canvas.setComposite(AlphaComposite.SrcOver);

        return canvas;
    }

    private static ByteBuffer getByteBufferFromImage(BufferedImage image) {
        int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

        byte[] bytes = new byte[pixels.length << 2];

        for (int i = 0; i < pixels.length; i++) {
            int pixel = pixels[i];

            int offset = i << 2;

            bytes[offset] = Atlas.getRedChannelValue(pixel);
            bytes[offset + 1] = Atlas.getGreenChannelValue(pixel);
            bytes[offset + 2] = Atlas.getBlueChannelValue(pixel);
            bytes[offset + 3] = Atlas.getAlphaChannelValue(pixel);
        }

        ByteBuffer buffer = ByteBuffer.allocateDirect(bytes.length).order(ByteOrder.nativeOrder());

        buffer.put(bytes);

        buffer.flip();

        return buffer;
    }

    private static byte getRedChannelValue(int pixel) {
        return (byte) ((pixel >> 16) & 0xFF);
    }

    private static byte getGreenChannelValue(int pixel) {
        return (byte) ((pixel >> 8) & 0xFF);
    }

    private static byte getBlueChannelValue(int pixel) {
        return (byte) (pixel & 0xFF);
    }

    private static byte getAlphaChannelValue(int pixel) {
        return (byte) ((pixel >> 24) & 0xFF);
    }

    private static void add(ByteBuffer atlas, int index) {
        glTexSubImage3D(GL_TEXTURE_2D_ARRAY,
                0,
                0,
                0,
                index,
                Atlas.WIDTH,
                Atlas.HEIGHT,
                1,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                atlas);
    }
}
