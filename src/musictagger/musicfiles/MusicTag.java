package musictagger.musicfiles;

import java.util.Objects;

/**
 * A tag key/id that can be used when retrieving/modifying a MusicFile
 * @author isaac
 */
public class MusicTag implements Comparable{	
	private final String tagname;
	private final FileFormat format;
	
	private static class TagNameTable{
		private static final String[][] tbl = new String[][]{
			{}
		};
	}
	
	/**
	 * Static factory method for creating a new MusicTag
	 * Tries to parse the tag to a valid supported tag format, if
	 * it isn't already valid
	 * @param tag the raw tag name string
	 * @param f the file format to load as
	 * @return a new MusicTag object; or null, if the tag was not supported
	 */
	public static MusicTag create(String tag, FileFormat f){
		//Validated tag
		String vtag = null;
		//m4p/m4b/m4a/mp4 (Mp4Tag/Mp4FieldKey)
		//ogg (VorbisCommentTag/any)
		//ra/rm (RealTag/FieldKey)
		//wav (WavTag/FieldKey)
		//wma (AsfTag/AsfFieldKey)
		if (f.supportedTags.contains(tag))
			vtag = tag;
		else if (f.supportsCustom)
			vtag = tag.replaceAll("\\s+", " ").replaceAll("[^A-Za-z0-9_ ]", "").trim().toUpperCase();
		
		return vtag == null ? null : new MusicTag(vtag, f);
	}
	
	//Private, to restrict access to the create() method
	private MusicTag(String tag, FileFormat f){
		tagname = tag;
		format = f;
	}

	/**
	 * @return the vendor specific tag name
	 */
	public String getVendor(){
		return tagname;
	}
	/**
	 * @return the vendor specific tag name converted to a generic one
	 */
	public String getGeneric(){
		return null;
	}
	
	/**
	 * @return true if the tag is a "common tag" as per the file format's specifications
	 */
	public boolean isCommon(){
		return format.isCommon(tagname);
	}
	/**
	 * @return true if the tag is not an officially supported tag (as per format's specs)
	 */
	public boolean isCustom(){
		return !format.supports(tagname);
	}
	/**
	 * @return true if the tag can have multiple values
	 */
	public boolean supportsMultiple(){
		return true;
	}
	
	/**
	 * Converts the tag name for use with the specified format
	 * @param f the target file format
	 * @return a new MusicTag object; or null, if the format is not supported in the new format
	 */
	public static MusicTag convertTo(FileFormat f){
		return null;
	}
	
	@Override
	public String toString(){
		return tagname;
	}
	@Override
	public int compareTo(Object o) {
		return tagname.compareTo(o.toString());
	}
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof MusicTag))
			return false;
		MusicTag x = (MusicTag) o;
		return format.equals(x.format) && tagname.equals(o.toString());
	}
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 71 * hash + Objects.hashCode(this.tagname);
		hash = 71 * hash + Objects.hashCode(this.format);
		return hash;
	}
}
