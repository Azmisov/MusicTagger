package musictagger.musicfiles;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.SupportedFileFormat;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.generic.Utils;

/**
 * Holds the music file and all methods for editing it
 * @author isaac
 */
public abstract class MusicFile {
	protected AudioFile af;
	protected boolean hasEdits;
	public File raw_file;
	
	//This variable maps file formats to their respective MusicFile implementations
	private static final HashMap<String, Class> ffMap;
	static {
		ffMap = new HashMap<>();
		ffMap.put(SupportedFileFormat.OGG.getFilesuffix(), null);
		ffMap.put(SupportedFileFormat.FLAC.getFilesuffix(), MusicFile_Flac.class);
		ffMap.put(SupportedFileFormat.MP3.getFilesuffix(), null);
		ffMap.put(SupportedFileFormat.WAV.getFilesuffix(), null);
		ffMap.put(SupportedFileFormat.WMA.getFilesuffix(), null); //asf
		ffMap.put(SupportedFileFormat.AIF.getFilesuffix(), null);
		//Mp4 classes
		ffMap.put(SupportedFileFormat.MP4.getFilesuffix(), null);
		ffMap.put(SupportedFileFormat.M4A.getFilesuffix(), null);
		ffMap.put(SupportedFileFormat.M4P.getFilesuffix(), null);
		ffMap.put(SupportedFileFormat.M4B.getFilesuffix(), null);
		//Real classes
		ffMap.put(SupportedFileFormat.RA.getFilesuffix(), null);
		ffMap.put(SupportedFileFormat.RM.getFilesuffix(), null);
	}
	
	/**
	 * Static factory method for creating a new MusicFile
	 * @param f the file to load
	 * @return new MusicFile object; or null, if there was an error reading the file
	 */
	public static MusicFile create(File f){
		try{
			Constructor cstruct = ffMap.get(Utils.getExtension(f)).getConstructor(File.class);
			return (MusicFile) cstruct.newInstance(f);
		} catch (Exception e){
			//Log any errors loading files
			if (e.getClass() == InvocationTargetException.class){
				InvocationTargetException x = (InvocationTargetException) e;
				Throwable e2 = x.getTargetException();
				Logger.getGlobal().log(Level.WARNING, e2.getMessage());
			}
			return null;
		}
	}
		
	/**
	 * @param allow_empty Whether we should return tags with empty values
	 * @return a list of all the tag names in the file
	 */
	public abstract Set<MusicTag> getTags(boolean allow_empty);
	
	/**
	 * @param tag the tag key/id to retrieve
	 * @return a list of values for this tag
	 */
	public abstract List<String> getValues(MusicTag tag);
	
	/**
	 * Gets the index of an entry/value within a particular tag
	 * This is equivalent to searching the output of getValues(MusicTag)
	 * for the value in question.
	 * @param tag the tag key/id to retrieve
	 * @param val the value to look for
	 * @return the index location of the value
	 */
	public int getIndex(MusicTag tag, String val){
		List<String> search = getValues(tag);
		if (search == null)
			return -1;
		return search.indexOf(val);
	}
	
	/**
	 * @return the file format associated with this MusicFile
	 */
	public abstract FileFormat getFileFormat();
	
	/**
	 * Deletes an entire tag
	 * @param tag the tag key/id to delete
	 * @return true on success
	 */
	public abstract boolean deleteTag(MusicTag tag);

	/**
	 * Deletes a specific tag entry value
	 * @param tag the tag key/id to delete
	 * @param val the value to remove
	 * @return true on success
	 */
	public abstract boolean deleteValue(MusicTag tag, String val);
	
	/**
	 * Deletes a specific tag entry value
	 * @param tag the tag key/id to delete
	 * @param val the index of the value to remove
	 * @return true on success
	 */
	public abstract boolean deleteValue(MusicTag tag, int index);
	
	/**
	 * Adds a new tag, appending to the list, if it already has values
	 * @param tag the tag/key to add
	 * @param val the new value
	 * @return true on success
	 */
	public abstract boolean addTag(MusicTag tag, String val);
	
	/**
	 * Sets the first available entry of a tag to a value
	 * Creates a new tag, if none existed already
	 * @param tag the tag key/id to edit
	 * @param val the value to insert
	 * @return true on success
	 */
	public abstract boolean setTag(MusicTag tag, String val);
	
	/**
	 * Sets the entry/value at the specified index to the value
	 * @param tag the tag key/id to edit
	 * @param val the value to insert
	 * @param index the index of the entry to edit
	 * @return true on success
	 */
	public abstract boolean setValue(MusicTag tag, String val, int index);
	
	/**
	 * Commits any edits made to the music file
	 * @return true on success
	 */
	public boolean commit(){
		if (!hasEdits)
			return true;
		try {
			af.commit();
			hasEdits = false;
			return true;
		} catch (CannotWriteException ex) {
			return false;
		}
	}
	
	@Override
	public String toString(){
		return raw_file.getName();
	}
}
