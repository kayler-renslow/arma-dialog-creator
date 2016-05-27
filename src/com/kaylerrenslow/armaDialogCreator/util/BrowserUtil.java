package com.kaylerrenslow.armaDialogCreator.util;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 Created by Kayler on 05/26/2016.
 */
public class BrowserUtil {
	/** Attempts to open the browser at the specified url. If the operation succeeded, this method will return true. If the operation failed, will return false. */
	public static boolean browse(String url) {
		try {
			Desktop.getDesktop().browse(new URI(url));
		} catch (IOException e) {
			e.printStackTrace(System.out);
			return false;
		} catch (URISyntaxException e) {
			e.printStackTrace(System.out);
			return false;
		}
		return true;
	}
}
