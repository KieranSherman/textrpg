package util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.BootThread;
import main.ui.Window;
import network.Adapter;
import util.exceptions.ResourcesNotInitializedException;
import util.out.Colorer;
import util.out.Colorer.ColorRules;
import util.out.Logger;

/*
 * Class holds resources used throughout classes
 */
public class Resources {
	public final static int HEIGHT = 400, WIDTH = (int)(HEIGHT*1.618);	//height and width of window
	
	public final static Font USER_OUTPUT = new Font("Courier", Font.PLAIN, 12);	//font for user output
	public final static Font UI = loadFont("src/files/fonts/vador.ttf").deriveFont(15f); //font for UI
	
	public final static Color DARK_RED = new Color(185, 0, 15);		//dark red color
	public final static Color DARK_GREEN = new Color(15, 170, 0);	//dark green color
	
	private static Colorer colorer;					//Colorer object to color user input
	private static Logger logger;					//Logger to append text to user output window
	private static Adapter adapter;					//Adapter to bridge between network and UI
	
	private static boolean initialized = false;		//whether or not the call to init() has been made
	
	private static String parseDelimiter = "\\s+"; 	//delimiter used to split text in files
	public static final String VERSION = loadVersion("src/files/reference/Reference.txt");
	
	public static final BufferedImage bootImage = loadImage("src/files/imgs/pngs/booting1.png");
	public static final boolean boot = loadBoot("src/files/reference/Reference.txt");
	
	private Resources() {}							//prevent instantiation of Resources object
	
	public static void init(Window window) {
		initialized = true;

		colorer = new Colorer();
		logger = new Logger(window);
		adapter = new Adapter();
		
		loadActionWords("src/files/Actions.txt");
		loadPlaceWords("src/files/Places.txt");
		overrideOutput();
	}
	
	/*
	 * Directs standard out and standard error to the logger
	 */
	private static void overrideOutput() {
		//PrintStream stdOut = System.out;
		//PrintStream stdErr = System.err;
		
		//System.setOut(new PrintStream(new StreamCapturer("STDOUT", logger, stdOut)));
		//System.setErr(new PrintStream(new StreamCapturer("STDERR", logger, stdErr)));
	}
	
	/*
	 * Loads font from filePath
	 */
	private static Font loadFont(String filePath) {
		try {
			return Font.createFont(Font.TRUETYPE_FONT, new File(filePath));
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/*
	 * Load the action words
	 */
	private static void loadActionWords(String filePath) {
		loadWords(filePath, ColorRules.ACTION);
	}
	
	/*
	 * Load the place words
	 */
	private static void loadPlaceWords(String filePath) {
		loadWords(filePath, ColorRules.PLACE);
	}
	
	/*
	 * Load Word objects into Colorer
	 */
	private static void loadWords(String filePath, ColorRules cr) {
		for(String word : parseText(filePath))
			colorer.addWord(word, cr);
	}
	
	/*
	 * Loads the VERISON String from the Class Hierarchy
	 */
	private static String loadVersion(String filePath) {
		String[] lines = parseText(filePath);
		for(int i = 0; i < lines.length-1; i++) {
			if(lines[i].equalsIgnoreCase("version:"))
				return lines[i+1];
		}
		
		return null;
	}
	
	/*
	 * Loads the BOOT String from the Class Hierarchy
	 */
	private static boolean loadBoot(String filePath) {
		String[] lines = parseText(filePath);
		for(int i = 0; i < lines.length-1; i++) {
			if(lines[i].equalsIgnoreCase("boot:"))
				return Boolean.parseBoolean(lines[i+1]);
		}
		
		return false;
	}
	
	private static BufferedImage loadImage(String filePath) {
		try {
			return ImageIO.read(new File(filePath));
		} catch (IOException e) {
			System.err.println("error loading image");
		}
		
		return null;
	}
	
	/*
	 * Parse text from a file at filePath
	 */
	private static String[] parseText(String filePath) {
		FileReader fr = null;
		try {
			fr = new FileReader(new File(filePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
		
		String line, text = "";
		
		try {
			while((line = br.readLine()) != null) {
				if(line.indexOf("//") != -1)
					line = line.substring(0, line.indexOf("//"));
				
				text += line += "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return text.split(parseDelimiter);
	}
	
	/*
	 * Returns the colorer
	 */
	public static Colorer getColorer() throws ResourcesNotInitializedException {
		if(!checkInit())
			return null;
		
		BootThread.queueInfo("colorer loaded");
		return colorer;
	}
	
	/*
	 * Returns the logger
	 */
	public static Logger getLogger() throws ResourcesNotInitializedException {
		if(!checkInit())
			return null;
		
		BootThread.queueInfo("logger loaded");
		return logger;
	}
	
	/*
	 * Returns the adapter
	 */
	public static Adapter getAdapter() throws ResourcesNotInitializedException {
		if(!checkInit())
			return null;
		
		BootThread.queueInfo("adapter loaded");
		return adapter;
	}

	/*
	 * Check whether a call to init() has been made
	 */
	private static boolean checkInit() throws ResourcesNotInitializedException {
		if(!initialized)
			throw new ResourcesNotInitializedException("You must initialize Resources.class before using it!");
		
		return initialized;
	}
	
	public static void sleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (Exception e) {}
	}
}
