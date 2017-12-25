package musictagger;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.Serializable;

/**
 * Stores all user settings
 * @author isaac
 */
public class UserSettings implements Serializable{
	/*
	 * dir: directory from "file open dialog"
	 * renameUtility: the rename expression in the "rename files utility dialog"
	 * customTag: the custom tag text in the "add tag dialog"
	 * tagValue: the value in the "add tag dialog"
	 * showGeneric: from view menu
	 * showEmpty: from view menu
	 * files: the list of files
	 * sel_files: the list of selected files
	 * winMain/winFile...: the respective windows
	 */
	public String renameUtility, customTag, tagValue;
	public boolean showGeneric, showEmpty, allowMultiple;
	public File[] files;
	public int[] sel_files;
	public WindowSettings winMain, winFile, winRename, winAdd;
	public File dir;
	public int split_loc, tblcol_width;
	
	public UserSettings(){
		winMain = new WindowSettings();
		winFile = new WindowSettings();
		winRename = new WindowSettings();
		winAdd = new WindowSettings();
		split_loc = 320;
	}
	
	public class WindowSettings implements Serializable{
		/*
		 * x/y: window position
		 * w/h: window dimensions
		 * max: is window maxified?
		 */
		public Point loc;
		public Dimension dims;
		public int max;
	}
}
