package com.duckyshine.app.sound;

import org.lwjgl.openal.*;

import com.duckyshine.app.debug.Debug;
import com.duckyshine.app.math.RandomNumber;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import java.util.stream.Stream;
import java.util.stream.Collectors;

import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.openal.ALC11.*;

public class SoundPlayer {
    private long audioDevice;
    private long audioContext;

    private String deviceName;

    private Sound music;

    private List<Sound> playlist;

    public SoundPlayer() {
        this.music = null;

        this.playlist = new ArrayList<>();

        this.initialise();
    }

    private void initialise() {
        int[] attributes = { 0 };

        this.deviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        this.audioDevice = alcOpenDevice(this.deviceName);
        this.audioContext = alcCreateContext(this.audioDevice, attributes);

        alcMakeContextCurrent(this.audioContext);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(this.audioDevice);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

        if (!alCapabilities.OpenAL10) {
            assert false : "Audio library is not supported";
        }

        this.initialisePlaylist();
    }

    private void initialisePlaylist() {
        ClassLoader classLoader = SoundPlayer.class.getClassLoader();
        URI uri = null;

        try {
            uri = classLoader.getResource("sound/music/").toURI();
        } catch (URISyntaxException exception) {
            exception.printStackTrace();

            return;
        }

        Path filepath = Paths.get(uri);

        List<String> files = this.getFiles(filepath);

        if (files == null) {
            assert false : "Something went wrong...";

            return;
        }

        this.playlist.addAll(files.stream()
                .map(filename -> filepath.resolve(filename).toString())
                .map(Sound::new)
                .toList());
    }

    public void playMusic() {
        if (this.music == null || !this.music.isPlaying()) {
            this.music = this.getRandomMusic();
            this.music.play();
        }
    }

    private Sound getRandomMusic() {
        int index = RandomNumber.getRandomInteger(this.playlist.size());

        return this.playlist.get(index);
    }

    private List<String> getFiles(Path filepath) {
        try (Stream<Path> paths = Files.list(filepath)) {
            return paths.map(path -> path.getFileName().toString()).collect(Collectors.toList());
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public void cleanUp() {
        alcDestroyContext(this.audioContext);
        alcCloseDevice(this.audioDevice);
    }
}
