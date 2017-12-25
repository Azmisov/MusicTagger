package musictagger.tagtable;

import musictagger.musicfiles.MusicTag;

/**
 * Listens to changes in the table
 * @author isaac
 */
public abstract class TagTableListen {
	public abstract void tableChanged(MusicTag tag, String oldVal, String newVal);
}
