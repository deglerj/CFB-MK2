package org.jd.cfb;

import java.io.File;

import lejos.nxt.Sound;

public class Samples {

	public static void play(final String... files) {
		for (final String fileName : files) {
			final File file = new File(fileName);
			final int duration = Sound.playSample(file);
			try {
				Thread.sleep(duration + 50);
			}
			catch (final InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
