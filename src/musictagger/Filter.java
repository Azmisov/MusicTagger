package musictagger;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import org.jaudiotagger.audio.AudioFileFilter;

/**
 * Wrapper class to convert AudioFileFilter to FileFilter
 * @author isaac
 */
public class Filter extends FileFilter{
	public AudioFileFilter af;
	
	public Filter(){
		super();
		af = new AudioFileFilter(true);
	}
	
	@Override
	public boolean accept(File f) {
		return af.accept(f);
	}
	
	@Override
	public String getDescription() {
		return "Folders, mp3, mp4, ogg, flac, wma, wav, and real";
	}
}
