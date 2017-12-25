package musictagger.old_source;

import java.util.HashMap;
import java.util.List;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.flac.FlacTag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentFieldKey;

/**
 * Handles all FLAC tag editing
 * same as generic, except it supports custom tags
 * @author isaac
 */
public class MusicTag_Flac extends MusicTag_Generic{
	//Tag access
	private FlacTag flac_tag;
	public static final boolean supportsCustom = true;
	//Maps mp3 tags to generic tags
	public static final HashMap<String, String> genericMap;
	static{
		genericMap = new HashMap<>();
		VorbisCommentFieldKey[] keys = VorbisCommentFieldKey.values();
		for (int i=0; i<keys.length; i++){
			//System.out.println(keys[i].getFieldName()+" to "+keys[i].toString());
			genericMap.put(keys[i].getFieldName(), keys[i].toString());
		}
	}
	
	
	public MusicTag_Flac(AudioFile af, Tag generic){
		super(af, generic);
		flac_tag = (FlacTag) generic;
	}
	
	@Override
	public boolean edit(String tag, String val, boolean allowMultiple){
		//First try generic tag names
		if (!super.editValidTag(tag, val, allowMultiple)){
			//Check to see if it exists as VorbisCommentFieldKey
			try{
				VorbisCommentFieldKey key = VorbisCommentFieldKey.valueOf(tag);
				String raw = key.getFieldName();
				boolean exists = flac_tag.hasField(key);
				if (val == null){
					if (exists)
						flac_tag.deleteField(raw);
				}
				else{
					if (!exists || allowMultiple)
						flac_tag.addField(raw, val);
					else flac_tag.setField(raw, val);
				}
				return true;
			} catch (Exception e){
				//Validate the tag name and try again
				String vtag = super.validate(tag);
				if (!vtag.equals(tag))
					return edit(vtag, val, allowMultiple);
				//Use generic vorbis comment, if nothing else works
				//Currently, using validated tag name; could change this later...
				try {
					boolean exists = flac_tag.hasField(tag);
					if (val == null){
						if (exists)
							flac_tag.deleteField(tag);
					}
					else{
						if (!exists || allowMultiple)
							flac_tag.addField(tag, val);
						else flac_tag.setField(tag, val);
					}
					return true;
				} catch (Exception e2) {
					return false;
				}
			}
		}
		else return true;
	}
	
	@Override
	public String normalize(String tag){
		String nor = genericMap.get(tag), vtag = validate(tag);
		if (nor == null)
			nor = genericMap.get(vtag);
		if (nor == null)
			nor = genericMap.get(vtag.replaceAll("\\s","_"));
		return nor == null ? tag : nor;
	}
	
	@Override
	public String[] getValues(String tag){
		List<String> vals = flac_tag.getVorbisCommentTag().getAll(tag);
		return vals.toArray(new String[vals.size()]);
	}
}
