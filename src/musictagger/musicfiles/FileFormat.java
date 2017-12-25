package musictagger.musicfiles;

import java.util.Set;

/**
 * An abstract class that acts like an interface for each
 * file format's settings/constants
 * @author isaac
 */
public abstract class FileFormat {
	/**
	 * True if this file format support custom tags
	 * (that is, tags not in supportedTags set)
	 */
	public final boolean supportsCustom;
	/**
	 * True if all tags can have multiple values
	 * Note: if this is true, multipleTags will be null
	 */
	public final boolean supportsMultiple;
	/**
	 * A list of common tags as specified by the file format
	 */
	public final Set<String> commonTags;
	/**
	 * A list of supported vendor specific tags
	 */
	public final Set<String> supportedTags;
	/**
	 * A list of tags that can have multiple values
	 */
	public final Set<String> multipleTags;
	
	//Protected, only sub-classes should call the constructor
	protected FileFormat(boolean cust, boolean mult, Set<String> comS, Set<String> supS, Set<String> multS){
		supportsCustom = cust;
		supportsMultiple = mult;
		commonTags = comS;
		supportedTags = supS;
		multipleTags = multS;		
	}
	
	/**
	 * @param tag
	 * @return true if this format supports this tag
	 * (either supportsCustom or in supportedTags set)
	 */
	public boolean supports(String tag){
		return supportsCustom || supportedTags.contains(tag);
	}
	
	/**
	 * @param tag
	 * @return true if this format considers this tag "common"
	 */
	public boolean isCommon(String tag){
		return commonTags.contains(tag);
	}
	
	/**
	 * @param tag
	 * @return true if this format allows multiple values for this tag
	 * (either supportsMultiple or in multipleTags set)
	 */
	public boolean isMultiple(String tag){
		return supportsMultiple || multipleTags.contains(tag);
	}
}
