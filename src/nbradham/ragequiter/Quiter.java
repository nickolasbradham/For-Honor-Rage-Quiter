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

	public static final void main(String[] args) throws NativeHookException, AWTException {
		GlobalScreen.registerNativeHook();
		Robot r = new Robot();
		GlobalScreen.addNativeKeyListener(new NativeKeyListener() {

			private static final String[] CMD = new String[] { "cmd", "/C", "ipconfig", "/release&ipconfig", "/renew" };

			@Override
			public final void nativeKeyPressed(NativeKeyEvent e) {
				switch (e.getKeyCode()) {
				case NativeKeyEvent.VC_BACKQUOTE:
					try {
						GlobalScreen.unregisterNativeHook();
					} catch (NativeHookException e1) {
						e1.printStackTrace();
					}
					break;
				case NativeKeyEvent.VC_1:
					tap(KeyEvent.VK_T);
					r.keyPress(KeyEvent.VK_CONTROL);
					tap(KeyEvent.VK_V);
					r.keyRelease(KeyEvent.VK_CONTROL);
					tap(KeyEvent.VK_ENTER);
					try {
						Runtime.getRuntime().exec(CMD);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}

			private final void tap(int key) {
				r.keyPress(key);
				r.keyRelease(key);
			}
		});
	}
}