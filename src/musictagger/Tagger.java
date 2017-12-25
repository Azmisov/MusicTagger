package musictagger;

/*
 * TODO
 * Tag Table Display:
 * - show generic names
 * - filter custom, generic, common
 * - shift select not perfect...
 * - arrow keys
 * Other:
 * - make MusicTag an enum class???
 * - add tag "All" combo box
 * - delete only one tag at a time
 * - delete songs from song list
 * - save addTagDialog combobox selections
 * - all file formats
 * - custom editors
 * - getTags method more efficient
 * Tools:
 * - rename utility, remove duplicates
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import musictagger.musicfiles.FileFormat;
import musictagger.musicfiles.MusicFile;
import musictagger.musicfiles.MusicTag;

public class Tagger {
	private ArrayList<MusicFile> files;
	private HashSet<String> fpaths;
	public Filter filter;
	private static final Comparator<String[]> sorter = new Comparator<String[]>() {
		@Override
		public int compare(String[] a, String[] b) {
			return a[0].compareTo(b[0]);
		}
	};
	
	public Tagger(){
		files = new ArrayList<>();
		fpaths = new HashSet<>();
		//Filter for audio formats JAudioTagger supports
		filter = new Filter();
	}
	/**
	 * Loads files from the GUI for tag manipulation
	 * @param fs an array of files
	 * @param reset removes previous files that were loaded
	 * @return Number of files we failed to load
	 */
	public int load(File[] fs, boolean reset){
		if (reset){
			files.clear();
			fpaths.clear();
		}
		//Number of files we failed to load
		if (fs == null) return 0;
		int errors = 0;
		for(int i=0; i<fs.length; i++){
			//Load all files (recursively) from a directory
			if (fs[i].isDirectory()){
				ArrayList<File> subFiles = getFileList(fs[i]);
				for(File f: subFiles){
					if (!loadFile(f)) {
						errors++;
					}
				}
			}
			//Individual files
			else if (filter.accept(fs[i]) && !loadFile(fs[i])) {
				errors++;
			}
		}
		return errors;
	}
	//Loads an individual file
	private boolean loadFile(File f){
		if (!fpaths.contains(f.getAbsolutePath())){
			MusicFile mf = MusicFile.create(f);
			if (mf == null) return false;
			files.add(mf);
			fpaths.add(mf.raw_file.getAbsolutePath());
		}
		return true;
	}
	//Returns a list of sub-files from a directory
	private ArrayList<File> getFileList(File dir){
		ArrayList<File> flist = new ArrayList<>();
		Stack<File> dirs = new Stack<>();
		dirs.push(dir);
		while(!dirs.empty()){
			File d = dirs.pop();
			File[] d_sub = d.listFiles(filter.af);
			for (int i=0; i<d_sub.length; i++){
				if (d_sub[i].isDirectory()) {
					dirs.push(d_sub[i]);
				}
				else {
					flist.add(d_sub[i]);
				}
			}
		}
		return flist;
	}
	
	public MusicFile[] listFiles(){
		return files.toArray(new MusicFile[files.size()]);
	}
	
	
	/**
	 * Gets the length of files loaded into the tagger
	 * @return number of files that are loaded
	 */
	public int numFiles(){
		return files.size();
	}
	
	/**
	 * Returns the tags that are the same among the selected audio files
	 * @param index an array of selected indices
	 * @return returns a two-dimensional array of tags
	 */
	public Map<MusicTag, List<String>> getMatchingTags(int[] index, boolean accept_empty, boolean normalize){
		Map<MusicTag, List<String>> map = new TreeMap<>();
		//Use the first music file to compare against
		MusicFile mf = files.get(index[0]);
		List<MusicTag> raw_tags = new ArrayList<>();
		raw_tags.addAll(mf.getTags(accept_empty));
		for (MusicTag tag: raw_tags)
			map.put(tag, mf.getValues(tag));
		//If there was only one selected, don't do any matching
		if (index.length == 1)
			return map;
		
		//Checks for matching tags (single value and arrays, respectively)
		Set<MusicTag> temp_tag;
		Set<String> temp_val = new HashSet<>(9);
		for (int i=1; i<index.length; i++){
			mf = files.get(index[i]);
			temp_tag = mf.getTags(accept_empty);
			//Check each tag to see if they match
			tagchck: for (int j=0, raw_z = raw_tags.size(), del_tags = 0; j<raw_z; j++){
				MusicTag mt = raw_tags.get(j-del_tags);
				//First check if they both have this tag name
				if (temp_tag.contains(mt)){
					List<String> vals = mf.getValues(mt), ref = map.get(mt);
					int vals_z = vals.size(), ref_z = ref.size(), del = 0;
					boolean isEmpty = vals_z == 0;
					//Handle empty values, then check if they have the same values
					if (isEmpty == (ref_z == 0)){
						if (isEmpty) continue;
						//Most of the time, there will only be one item in a value list
						//Don't know if this optimization really even matters, but what the heck
						if (ref_z == 1 && vals_z == 1){
							if (!ref.get(0).equals(vals.get(0)))
								ref.remove(0);
							else continue;
						}
						else if (ref_z == 1){
							String comp = ref.get(0);
							for (int k=0; k<vals_z; k++){
								if (comp.equals(vals.get(k)))
									continue tagchck;
							}
							ref.remove(0);
						}
						else if (vals_z == 1){
							String comp = vals.get(0);
							for (int k=0; k<ref_z; k++){
								if (!comp.equals(ref.get(k-del))){
									ref.remove(k-del);
									del++;
								}
							}
						}
						//Otherwise, we'll use a set, for faster lookup
						else{
							temp_val.addAll(vals);
							for (int k=0; k<ref_z; k++){
								if (!temp_val.contains(ref.get(k-del))){
									ref.remove(k-del);
									del++;
								}
							}
							temp_val.clear();
						}
						//Save the filtered value list back to the map
						if (!ref.isEmpty()){
							if (del > 0) map.put(mt, ref);
							continue;
						}
					}
				}
				else{
					//Can't remember if this is still a bug or not...
					//System.out.println("why does it not contain this item???");
				}
				//Remove the item
				raw_tags.remove(j-del_tags);
				map.remove(mt);
				del_tags++;
			}
		}
		return map;
	}
	
	/**
	 * Modifies tags
	 * @param index A list of file indexes
	 * @param tag The tag name we want to edit
	 * @param val The new value of the tag
	 * @return the number of errors
	 */
	public int editTags(int[] index, MusicTag tag, String oldVal, String newVal){
		int err = 0;
		for (int i=0; i<index.length; i++){
			MusicFile mf = files.get(index[i]);
			if (!mf.setValue(tag, newVal, mf.getIndex(tag, oldVal)))
				err++;
		}
		return err;
	}
	
	/**
	 * Adds tags
	 * @param index A list of file indexes
	 * @param tag The tag name
	 * @param val The new value
	 * @return the number of errors
	 */
	public int addTags(int[] index, String tag, String val){
		int err = 0;
		Map<FileFormat, MusicTag> tag_map = new HashMap<>();
		for (int i=0; i<index.length; i++){
			MusicFile mf = files.get(index[i]);
			FileFormat f = mf.getFileFormat();
			//Only create the music tag once, for each file format
			if (!tag_map.containsKey(f)){
				MusicTag new_tag = MusicTag.create(tag, f);
				tag_map.put(f, new_tag);
			}
			if (!mf.addTag(tag_map.get(f), val))
				err++;
		}
		return err;
	}
	
	/**
	 * Deletes tags
	 * @param index A list of file indexes
	 * @param tags A list of tags to delete
	 * @return the number of errors
	 */
	public int deleteTags(int[] index, List<Map.Entry<MusicTag, String>> tags){
		int err = 0;
		for (int i=0; i<index.length; i++){
			for (Map.Entry<MusicTag, String> tag: tags){
				if (!files.get(index[i]).deleteValue(tag.getKey(), tag.getValue()))
					err++;
			}
		}
		return err;
	}
	
	/**
	 * Commits all tag edits
	 */
	public int commit(){
		int errors = 0;
		for (MusicFile f: files){
			try{
				f.commit();
			} catch(Exception e){
				errors++;
			}
		}
		return errors;
	}
}
