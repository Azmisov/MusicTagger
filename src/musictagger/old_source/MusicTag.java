/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musictagger.old_source;

import java.util.HashMap;
import java.util.Iterator;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.SupportedFileFormat;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;

/**
 * Wrapper class for all format-specific tag classes
 * @author isaac
 */
public class MusicTag{
	private MusicTag_Generic base_tag;
	
	//This variable maps file formats to their respective TagKey/Tag classes
	private static final HashMap<String, String> ffMap;
	static {
		ffMap = new HashMap<>();
		ffMap.put(SupportedFileFormat.OGG.getFilesuffix(), "ogg");
		ffMap.put(SupportedFileFormat.FLAC.getFilesuffix(), "flac");
		ffMap.put(SupportedFileFormat.MP3.getFilesuffix(), "mp3");
		ffMap.put(SupportedFileFormat.MP4.getFilesuffix(), "mp4");
		ffMap.put(SupportedFileFormat.M4A.getFilesuffix(), "mp4");
		ffMap.put(SupportedFileFormat.M4P.getFilesuffix(), "mp4");
		ffMap.put(SupportedFileFormat.M4B.getFilesuffix(), "mp4");
		ffMap.put(SupportedFileFormat.WAV.getFilesuffix(), "wav");
		ffMap.put(SupportedFileFormat.WMA.getFilesuffix(), "asf");
		ffMap.put(SupportedFileFormat.AIF.getFilesuffix(), "aif");
		ffMap.put(SupportedFileFormat.RA.getFilesuffix(), "real");
		ffMap.put(SupportedFileFormat.RM.getFilesuffix(), "real");
	}
	
	public MusicTag(AudioFile af){
		Tag generic = af.getTagAndConvertOrCreateAndSetDefault();
		/*
		 *	 m4p/m4b/m4a/mp4 (Mp4Tag/Mp4FieldKey)
		 *	 ogg (VorbisCommentTag/any)
		 *	 ra/rm (RealTag/FieldKey)
		 *	 wav (WavTag/FieldKey)
		 *	 wma (AsfTag/AsfFieldKey)
		 *   aif...
		 */
		switch (ffMap.get(Utils.getExtension(af.getFile()))){
			case "flac":
				base_tag = (MusicTag_Generic) new MusicTag_Flac(af, generic);
				break;
			case "mp3":
				base_tag = (MusicTag_Generic) new MusicTag_Mp3(af, generic);
				break;
			default:
				base_tag = new MusicTag_Generic(af, generic);
		}
	}
	
	public Iterator<TagField> getTagNames(){
		/*
		ArrayList<String> out = new ArrayList<>();
		Iterator<TagField> fields = base_tag.getTagNames();
		while (fields.hasNext()){
			String f = fields.next().toString();
			out.add(f);
		}
		return out;
		*/
		return base_tag.getTagNames();
	}
	public String[] getValues(String tag){
		return base_tag.getValues(tag);
	}
	public String normalize(String tag){
		return base_tag.normalize(tag);
	}
	public boolean edit(String tag, String val, boolean allowMultiple){
		return base_tag.edit(tag, val, allowMultiple);
	}
}
