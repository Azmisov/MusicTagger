package musictagger.old_source;

import java.util.HashMap;
import java.util.List;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.ID3v24FieldKey;
import org.jaudiotagger.tag.id3.ID3v24Tag;

/**
 * Handles all MP3 tag editing
 * ID3v24FieldKey,ID3v24Frames
 * @author isaac
 */
public class MusicTag_Mp3 extends MusicTag_Generic{
	//Tag access
	private ID3v24Tag v24Tag;
	public static final boolean supportsCustom = true;
	public static final Object[] availTags = ID3v24FieldKey.values();
	//Maps mp3 tags to generic tags
	public static final HashMap<String, String> genericMap;
	static{
		genericMap = new HashMap<>();
		ID3v24FieldKey[] keys = ID3v24FieldKey.values();
		for (int i=0; i<keys.length; i++)
			genericMap.put(keys[i].getFieldName(), keys[i].toString());
	}
	
	public MusicTag_Mp3(AudioFile af, Tag generic){
		super(af, generic);
		MP3File f_mp3 = (MP3File) af;
		v24Tag = f_mp3.getID3v2TagAsv24();
	}
	
	@Override
	public boolean supports(String tag){
		try{
			ID3v24FieldKey.valueOf(tag);
			//ID3v24Frames
			return true;
		} catch(Exception e){
			return false;
		}
	}
	
	@Override
	public String normalize(String tag){
		
		String nor = genericMap.get(tag);
		return nor == null ? tag : nor;
	}
	
	@Override
	public String[] getValues(String tag){
		return new String[]{v24Tag.getFirst(tag)};
		/*
		List<String> vals = null;
		try{
			vals = v24Tag.getAll(FieldKey.valueOf(tag));
		} catch (Exception e){
			//Try to normalize the tag name
			String vtag = normalize(tag);
			if (!vtag.equals(tag)){
				try{
					vals = v24Tag.getAll(FieldKey.valueOf(vtag));
				} catch (Exception e2){}
			}
		}
		if (vals == null) return null;
		return vals.toArray(new String[vals.size()]);
		*/
	}
}
