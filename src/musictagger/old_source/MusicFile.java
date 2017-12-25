package musictagger.old_source;

import musictagger.old_source.MusicTag;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.TagField;

public class MusicFile {
	private AudioFile af;
	private String name;
	private MusicTag tags;
	private boolean hasEdits = false;
	public String full_name;
	public File raw_file;
	
	/**
	 * Loads an audio file for tag editing
	 * @param f The file to load
	 * @throws Exception It throws a bunch of exceptions...
	 */
	public MusicFile(File f) throws CannotReadException, TagException, ReadOnlyFileException, IOException, InvalidAudioFrameException{
		raw_file = f;
		af = AudioFileIO.read(f);
		tags = new MusicTag(af);
		name = f.getName();
		full_name = f.getAbsolutePath();
	}
	
	/**
	 * Gets a list of all tags
	 * @return a multidimensional array containing [[name, val], ...]
	 */
	public String[][] getTags(boolean allow_empty, boolean normalize){
		ArrayList<String[]> lst = new ArrayList<>();
		HashSet<String> done = new HashSet<>();
		Iterator<TagField> ti = tags.getTagNames();
		while(ti.hasNext()){
			TagField t = ti.next();
			String id = t.getId();
			//For now, we only want to list string tags (no blobs/album_art)
			if (!done.contains(id) && (allow_empty || !t.isEmpty())){
				String[] data = new String[2];
				data[0] = normalize ? tags.normalize(id) : id;
				String[] vals = tags.getValues(id);
				for (int i=0; i<vals.length; i++){
					data[1] = vals[i];
					lst.add(data.clone());
				}
				done.add(id);
			}
		}
		return lst.toArray(new String[lst.size()][]);
		//System.out.println(Arrays.deepToString(x));
		//return x;		
	}
	
	public boolean editTag(String name, String val, boolean allowMultiple){
		boolean ret = tags.edit(name, val, allowMultiple);
		if (ret) hasEdits = true;
		return ret;
	}
	
	/**
	 * Commit any edits to the file's tags
	 * @throws CannotWriteException 
	 */
	public void commit() throws CannotWriteException{
		if (hasEdits){
			af.commit();
			hasEdits = false;
		}
	}
	
	@Override
	public String toString(){
		return name;
	}
}
