package org.jd.cfb;

import java.util.Arrays;

public class Runnables {

	private static class WrapperThread extends Thread {

		private final boolean[]	threadCompleted;

		private final int		index;

		private final Runnable	runnable;

		private WrapperThread(final Runnable runnable, final boolean[] threadCompleted, final int index) {
			this.threadCompleted = threadCompleted;
			this.index = index;
			this.runnable = runnable;
		}

		@Override
		public void run() {
			try {
				runnable.run();
			}
			finally {
				threadCompleted[index] = true;
			}
		}
	}

	public static void runAndWait(final Runnable... runnables) {
		final boolean[] threadCompleted = new boolean[runnables.length];
		Arrays.fill(threadCompleted, false);

		for (int i = 0; i < runnables.length; i++) {
			new WrapperThread(runnables[i], threadCompleted, i).start();
		}

		while (!isAllCompleted(threadCompleted)) {
			Thread.yield();
		}
	}

	private static boolean isAllCompleted(final boolean[] threadCompleted) {
		for (final boolean completed : threadCompleted) {
			if (!completed) {
				return false;
			}
		}

		return true;
	}

}
