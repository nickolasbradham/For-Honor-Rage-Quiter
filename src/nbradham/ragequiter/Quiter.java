package nbradham.ragequiter;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

/**
 * The entire program.
 */
final class Quiter {

	private boolean paste = false;

	/**
	 * All program logic.
	 * 
	 * @throws NativeHookException Thrown by
	 *                             {@link GlobalScreen#registerNativeHook()}.
	 */
	private final void start() throws NativeHookException {
		GlobalScreen.registerNativeHook();
		SwingUtilities.invokeLater(() -> {
			JCheckBox pasteCheck = new JCheckBox("Paste message before quit");
			pasteCheck.setToolTipText("Hotkey: Backquote");
			pasteCheck.addActionListener(e -> paste = pasteCheck.isSelected());
			try {
				GlobalScreen.addNativeKeyListener(new NativeKeyListener() {

					private static final Runtime rt = Runtime.getRuntime();
					private static final String[] CMD = new String[] { "cmd", "/C", "ipconfig", "/release&ipconfig",
							"/renew" };
					private static final byte WAIT = 10;

					private final Robot r = new Robot();

					@Override
					public final void nativeKeyPressed(final NativeKeyEvent e) {
						switch (e.getKeyCode()) {
						case NativeKeyEvent.VC_BACKQUOTE:
							pasteCheck.doClick();
							break;
						case NativeKeyEvent.VC_F1:
							if (paste)
								try {
									tap(KeyEvent.VK_T);
									press(KeyEvent.VK_CONTROL);
									tap(KeyEvent.VK_V);
									r.keyRelease(KeyEvent.VK_CONTROL);
									tap(KeyEvent.VK_ENTER);
								} catch (final InterruptedException e1) {
									e1.printStackTrace();
								}
							try {
								rt.exec(CMD);
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
			} catch (AWTException e) {
				e.printStackTrace();
			}
			final JFrame frame = new JFrame("Quitter");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.addWindowListener(new WindowAdapter() {
				@Override
				public final void windowClosing(WindowEvent e) {
					try {
						GlobalScreen.unregisterNativeHook();
					} catch (final NativeHookException e1) {
						e1.printStackTrace();
					}
				}
			});
			frame.add(pasteCheck);
			frame.pack();
			frame.setVisible(true);
		});
	}

	/**
	 * Constructs a new Quiter instance and calls {@link #start()} on it.
	 * 
	 * @param args Ignored.
	 * @throws NativeHookException Thrown by {@link #start()}.
	 */
	public static void main(final String[] args) throws NativeHookException {
		new Quiter().start();
	}
}