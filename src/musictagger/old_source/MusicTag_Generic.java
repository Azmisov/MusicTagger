package musictagger.old_source;

import java.util.Iterator;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;

/**
 * Implemented by all tag types
 * @author isaac
 */
public class MusicTag_Generic{
	public static final boolean supportsCustom = false;
	public static final Object[] availTags = FieldKey.values();
	protected Tag base_tag;
	
	public MusicTag_Generic(AudioFile af, Tag generic){
		base_tag = generic;
	}
	/**
	 * Checks whether a tag can be added to this file.
	 * Note that this returns false for custom tags (e.g. flac/ogg comments)
	 * @param tag the tag id/name
	 * @return true if supported, false otherwise
	 */
	public boolean supports(String tag){
		try{
			FieldKey.valueOf(tag);
			return true;
		}catch (Exception e){
			return false;
		}
	}
	/**
	 * Sets or creates the tag (id) equal to val
	 * @param tag the tag id
	 * @param val the new value; null to delete the tag
	 * @return true on success, false otherwise
	 */
	public boolean edit(String tag, String val, boolean allowMultiple){
		try {
			FieldKey key = FieldKey.valueOf(tag);
			boolean exists = !base_tag.hasField(key);
			if (val == null){
				if (exists)
					base_tag.deleteField(key);
			}
			else{
				if (!exists || allowMultiple)
					base_tag.addField(key, val);
				else base_tag.setField(key, val);	
			}
			return true;
		} catch (Exception e) {
			//Try validating the tag and trying again
			String vtag = validate(tag);
			if (vtag.equals(tag))
				return false;
			return edit(vtag,val,allowMultiple);
		}
	}
	/**
	 * Same as edit(String,String) except it will not attempt to 
	 * validate the tag on failure
	 * @param tag the tag id
	 * @param val the new value; null to delete the tag
	 * @return true on success, false otherwise
	 */
	public boolean editValidTag(String tag, String val, boolean allowMultiple){
		try {
			FieldKey key = FieldKey.valueOf(tag);
			boolean exists = !base_tag.hasField(key);
			if (val == null){
				if (exists)
					base_tag.deleteField(key);
			}
			else{
				if (!exists || allowMultiple)
					base_tag.addField(key, val);
				else base_tag.setField(key, val);	
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Gives the value of a tag
	 * @param tag the tag id
	 * @return the tag's value
	 */
	public String[] getValues(String tag){
		try{
			int l = base_tag.getFields(tag).size();
			return (String[]) base_tag.getAll(FieldKey.valueOf(tag)).toArray();
		} catch(Exception e){
			System.out.println(e.getMessage());
			return null;
		}
	}
	/**
	 * Gives a list of tags found in the file
	 * @return an iterator with TagField's
	 */
	public Iterator<TagField> getTagNames(){
		return base_tag.getFields();
	}
	
	/**
	 * Converts the given name to the generic tag name
	 * Doesn't actually check if the string is valid; that needs to be done
	 * separately with the return value
	 * @param tag raw tag name
	 * @return generic tag name
	 */
	public String normalize(String tag){
		return validate(tag).replaceAll("\\s","_");
	}
	protected String validate(String tag){
		return tag.replaceAll("\\s+", " ").replaceAll("[^A-Za-z0-9_ ]", "").trim().toUpperCase();
	}
}
