package musictagger.musicfiles;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.flac.FlacTag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;

/**
 * Handles flac specific music editing
 * @author isaac
 */
public class MusicFile_Flac extends MusicFile{
	private static final FileFormat format = FileFormat_Flac.getInstance();
	private VorbisCommentTag tags;
		
	public MusicFile_Flac(File file) throws Exception{
		raw_file = file;
		af = AudioFileIO.read(raw_file);
		FlacTag temp_tags = (FlacTag) af.getTagOrCreateAndSetDefault();
		tags = temp_tags.getVorbisCommentTag();
	}
	
	@Override
	public Set<MusicTag> getTags(boolean allow_empty) {
		Set<MusicTag> faulty = new HashSet<>();
		Iterator<TagField> lst = tags.getFields();
		//Convert jaudiotagger's TagFields into MusicTags
		while (lst.hasNext()){
			TagField tf = lst.next();
			if (allow_empty || !tf.isEmpty())
				faulty.add(MusicTag.create(tf.getId(), format));
		}
		return faulty;
	}

	@Override
	public List<String> getValues(MusicTag tag) {
		return tags.getAll(tag.getVendor());
	}

	@Override
	public FileFormat getFileFormat() {
		return format;
	}

	@Override
	public boolean deleteTag(MusicTag tag) {
		//As far as I know, this doesn't throw any exceptions
		tags.deleteField(tag.getVendor());
		hasEdits = true;
		return true;
	}

	@Override
	public boolean deleteValue(MusicTag tag, String val) {
		//delegate to that other method...
		return deleteValue(tag, getIndex(tag, val));
	}

	@Override
	public boolean deleteValue(MusicTag tag, int index) {
		//This one doesn't throw any exceptions either
		tags.deleteFieldAt(tag.getVendor(), index);
		hasEdits = true;
		return true;
	}

	@Override
	public boolean addTag(MusicTag tag, String val){
		try {
			tags.addField(tag.getVendor(), val);
			hasEdits = true;
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
	@Override
	public boolean setTag(MusicTag tag, String val) {
		try {
			if (!tags.hasField(tag.getVendor()))
				return addTag(tag, val);
			tags.setField(tag.getVendor(), val);
			hasEdits = true;
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean setValue(MusicTag tag, String val, int index) {
		try {
			tags.setFieldAt(tag.getVendor(), val, index);
			hasEdits = true;			
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
}
