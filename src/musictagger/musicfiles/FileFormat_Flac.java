package musictagger.musicfiles;

import java.util.Set;
import java.util.HashSet;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentFieldKey;

/**
 * Singleton class for all Flac file format constants
 * @author isaac
 */
public final class FileFormat_Flac extends FileFormat{
	private static final FileFormat_Flac instance;
	static{
		//Supported tags
		Set<String> supTags = new HashSet<>();
		VorbisCommentFieldKey[] keys = VorbisCommentFieldKey.values();
		for (int i=0; i<keys.length; i++)
			supTags.add(keys[i].getFieldName());
		
		//Common tags
		Set<String> comTags = new HashSet<>();
		comTags.add(VorbisCommentFieldKey.TITLE.getFieldName());
		comTags.add(VorbisCommentFieldKey.ALBUM.getFieldName());
		comTags.add(VorbisCommentFieldKey.ARTIST.getFieldName());
		comTags.add(VorbisCommentFieldKey.GENRE.getFieldName());
		comTags.add(VorbisCommentFieldKey.TRACKNUMBER.getFieldName());
		comTags.add(VorbisCommentFieldKey.DATE.getFieldName());
		comTags.add(VorbisCommentFieldKey.DESCRIPTION.getFieldName());
		comTags.add(VorbisCommentFieldKey.COMMENT.getFieldName());
		
		//Create instance
		instance = new FileFormat_Flac(supTags, comTags, null);
		
		//mult_tags contains everything except VENDOR
	}
	
	//This class is non-instantiable; it can only be used via the getInstance method
	private FileFormat_Flac(Set<String> supTags, Set<String> comTags, Set<String> multTags){		
		super(true, true, supTags, comTags, multTags);
	};
	
	public static FileFormat_Flac getInstance(){
		return instance;
	}
}
