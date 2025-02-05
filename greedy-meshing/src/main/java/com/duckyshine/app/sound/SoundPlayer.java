package com.duckyshine.app.sound;

import org.lwjgl.openal.*;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.lwjgl.openal.ALC11.*;

public class SoundPlayer {
    private long audioDevice;
    private long audioContext;

    private String deviceName;

    private List<Sound> playlist;

    public SoundPlayer() {
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
            uri = classLoader.getResource("sound/music").toURI();
        } catch (URISyntaxException exception) {
            exception.printStackTrace();

            return;
        }

        Path filepath = Paths.get(uri);

        List<String> files = getFiles(filepath);

        if (files == null) {
            assert false : "Something went wrong...";

            return;
        }

        this.playlist.addAll(files.stream().map(Sound::new).toList());
    }

    public List<String> getFiles(Path filepath) {
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
