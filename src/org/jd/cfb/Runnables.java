package org.jd.cfb;

import java.util.Arrays;
import java.util.Collection;

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

	public static void runAndWait(final Collection<Runnable> runnables) {
		if (runnables.isEmpty()) {
			return;
		}

		final boolean[] threadCompleted = new boolean[runnables.size()];
		Arrays.fill(threadCompleted, false);

		int i = 0;
		for (final Runnable runnable : runnables) {
			new WrapperThread(runnable, threadCompleted, i).start();
			i++;
		}

		while (!isAllCompleted(threadCompleted)) {
			Thread.yield();
		}
	}

	public static void runAndWait(final Runnable... runnables) {
		runAndWait(Collections.newCollection(runnables));
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
