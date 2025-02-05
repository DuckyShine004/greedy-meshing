package com.duckyshine.app.utilities;

import java.io.File;

import java.util.Map;
import java.util.HashMap;

import com.duckyshine.app.sound.Sound;

public class AssetPool {
    private static Map<String, Sound> sounds = new HashMap<>();

    public Sound getSound(String filename) {
        File file = new File(filename);

        String absolutePath = file.getAbsolutePath();

        if (sounds.containsKey(absolutePath)) {
            return sounds.get(absolutePath);
        }

        assert false : "Sound file is not found: '" + filename + "'";

        return null;
    }

    public static Sound addSound(String filename) {
        File file = new File(filename);

        String absolutePath = file.getAbsolutePath();

        if (sounds.containsKey(absolutePath)) {
            return sounds.get(absolutePath);
        }

        Sound sound = new Sound(absolutePath);

        AssetPool.sounds.put(absolutePath, sound);

        return sound;
    }
}
