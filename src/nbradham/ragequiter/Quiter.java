package nbradham.ragequiter;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

final class Quiter {

	public static final void main(final String[] args) throws NativeHookException, AWTException {
		GlobalScreen.registerNativeHook();
		final Robot r = new Robot();
		GlobalScreen.addNativeKeyListener(new NativeKeyListener() {

			private static final String[] CMD = new String[] { "cmd", "/C", "ipconfig", "/release&ipconfig", "/renew" };
			private static final byte WAIT = 10;

			@Override
			public final void nativeKeyPressed(final NativeKeyEvent e) {
				switch (e.getKeyCode()) {
				case NativeKeyEvent.VC_BACKQUOTE:
					try {
						GlobalScreen.unregisterNativeHook();
					} catch (final NativeHookException e1) {
						e1.printStackTrace();
					}
					break;
				case NativeKeyEvent.VC_F1:
					try {
						tap(KeyEvent.VK_T);
						press(KeyEvent.VK_CONTROL);
						tap(KeyEvent.VK_V);
						r.keyRelease(KeyEvent.VK_CONTROL);
						tap(KeyEvent.VK_ENTER);
						Thread.sleep(50);
					} catch (final InterruptedException e1) {
						e1.printStackTrace();
					}
					try {
						Runtime.getRuntime().exec(CMD);
					} catch (final IOException e1) {
						e1.printStackTrace();
					}
				}
			}

			private final void tap(final int key) throws InterruptedException {
				press(key);
				r.keyRelease(key);
			}
			
			private final void press(final int key) throws InterruptedException {
				r.keyPress(key);
				Thread.sleep(WAIT);
			}
		});
	}
}