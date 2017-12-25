package musictagger.tagtable;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import musictagger.musicfiles.MusicTag;

/**
 * Model for GUI editing of tags
 * Handles all data and row selection
 * @author isaac
 */
public class TagTableModel extends AbstractTableModel{
	private static final String[] cols = {"Tag","Value"};
	//private Map<MusicTag, List<String>> data_index;
	private ArrayList<DataItem> data_list;
	private Map<MusicTag, Integer> data_sel = new HashMap<>();
	protected class DataItem{
		/**
		 * tag: the tag
		 * list_pos: the value's index, in the list of values/entries for this tag
		 * total: the total number of values for this tag
		 * isOdd: is this item an odd numbered tag in the table?
		 * isLast: is this item the last value for this tag
		 * val: the value
		 * isSel: is this value selected in the table
		 */
		public final MusicTag tag;
		public final int list_pos, total;
		public final boolean isOdd, isLast;
		public String val;
		public boolean isSel = false;
		
		public DataItem(MusicTag t, int p, int total, boolean odd, boolean last, String val){
			tag = t;
			list_pos = p;
			isOdd = odd;
			isLast = last;
			this.total = total;
			this.val = val;
		}
		@Override
		public String toString(){
			return val;
		}
	}
	
	//A list of valid spinner tags
	private static final HashSet<String> spinEdit;
	private static SpinnerEditor spinEditor = new SpinnerEditor();
	private static DefaultCellEditor txtEditor = new DefaultCellEditor(new JTextField());
	static {
		String[] nums = {
			"TPOS","disk","DISCTOTAL","WM/DiscTotal",
			"DISCNUMBER","WM/PartOfSet","DISC_NO",
			"TRCK","trkn","TRACKTOTAL","WM/TrackTotal","TOTALTRACKS","DISC_TOTAL",
			"TRACKNUMBER","WM/TrackNumber",
			"POPM","rate","RATING","WM/SharedUserRating",
			"PLAY_COUNT"
		};
		spinEdit = new HashSet<>();
		spinEdit.addAll(Arrays.asList(nums));
	};
	
	public TagTableModel(){
		data_list = new ArrayList<>();
	}
	
	//Mimic DefaultTableModel...
	@Override
	public int getRowCount() { return data_list.size(); }
	@Override
	public int getColumnCount() { return 2; }
	@Override
	public Object getValueAt(int row, int col) {
		DataItem d = data_list.get(row);
		return d;
	}
	@Override
	public String getColumnName(int col){ return cols[col];}
	@Override
	public boolean isCellEditable(int row, int col) { return col != 0; }
	
	//Custom methods
	public void loadData(Map<MusicTag, List<String>> rows){
		boolean isOdd = true;
		data_list.clear();
		data_sel.clear();
		for (Map.Entry<MusicTag, List<String>> d: rows.entrySet()){
			MusicTag key = d.getKey();
			List<String> vals = d.getValue();
			for (int i=0, l=vals.size(); i<l; i++)
				data_list.add(new DataItem(key, i, l, isOdd, i == l-1, vals.get(i)));
			isOdd = !isOdd;
			data_sel.put(key, 0);
		}
		this.fireTableRowsInserted(0, data_list.size()-1);
	}
	/*
	public void addRow(String tag, String val){
		int l = rows.size();
		rows.add(new String[]{tag,val});
		this.fireTableRowsInserted(l, l);
	}
	*/
	public void removeAllRows(){
		int l = data_list.size()-1;
		if (l >= 0){
			data_list.clear();
			this.fireTableRowsDeleted(0, l);
		}
	}
	
	/*
	public TableCellEditor getCellEditor(int row, int col){
		if (spinEdit.contains(rows.get(row)[0]))
			return spinEditor;
		return txtEditor;
	}
	*/
	
	/*
	 * Value listeners
	 */	
	List<TagTableListen> listeners = new ArrayList<>();
	public void addTableModelListener(TagTableListen listener){
		listeners.add(listener);
	}
	//Make sure the value has actually changed before firing an event
	@Override
    public void setValueAt(Object val, int row, int column){
        //Make sure the table value has actually changed
		DataItem data = (DataItem) this.getValueAt(row, column);
		String val_str = ((String) val).trim(),
				cur_val = data.toString();
		//New value is different...
		if (!cur_val.equals(val_str)){
			DataItem d = data_list.get(row);
			d.val = val_str;
			for (TagTableListen l: listeners)
				l.tableChanged(data.tag, cur_val, val_str);			
		}
    }
	
	/*
	 * Selection model methods
	 * anchor = anchor for selection {row, col}
	 * ref = the last selected cell
	 */
	private int[] anchor, ref;
	private boolean dragging = false;
	public int[] startSelect(int row, int col, boolean ctrl, boolean shift){
		ref = new int[]{row,col};
		if (anchor == null)
			anchor = ref;
		//Default selection
		int[] sel_intvl = new int[]{row, anchor[0]};
		//Selecting a tag column with multiple values is like doing a shift select
		//We just need to figure out the start/end of the selection interval
		DataItem row_info = data_list.get(row);
		boolean isTagColSel = col == 0 && row_info.total > 1;
		if (isTagColSel){
			int[] intvl = getRowInterval(row);
			if (shift){
				//Anchor and target are the same row
				if (anchor[0] >= intvl[0] && anchor[0] <= intvl[1])
					sel_intvl = intvl;
				//Anchor is a tagname column...
				else if (anchor[1] == 0){
					int[] intcomp = getRowInterval(anchor[0]);
					boolean small_anchor = intvl[1] > intcomp[0];
					sel_intvl[0] = small_anchor ? intcomp[0] : intcomp[1];
					sel_intvl[1] = small_anchor ? intvl[1] : intvl[0];
				}
				//Anchor is some single row somewhere
				else{
					sel_intvl[0] = anchor[0];
					sel_intvl[1] = anchor[0] < intvl[0] ? intvl[1] : intvl[0];
				}
			}
			//Basic selection of a full row
			else sel_intvl = intvl;
		}
		//Perform the selection
		int[] change = selectRows(sel_intvl[0], sel_intvl[1], ctrl, shift, isTagColSel);
		
		//Normalize the change interval
		boolean no_min = change[0] == -1, no_max = change[1] == -1;
		if (no_min && no_max) return null;
		if (no_min) change[0] = change[1];
		else if (no_max) change[1] = change[0];
		
		//Expand the edited interval to include full tag rows (if necessary)
		DataItem minD = data_list.get(change[0]);
		change[0] -= minD.list_pos;
		minD = data_list.get(change[1]);
		change[1] += minD.total-minD.list_pos-1;
		return change;
	}
	public void stopSelect(){
		dragging = false;
		anchor = ref;
	}
	public int[] dragSelection(int row, int col){
		if (ref[1] != col || (col == 1 && row != ref[0])){
			//need to do this stil...
		}
		return null;
	}
	//public Map<>
	//Gets start & end index of a full row (all values for a single tag)
	private int[] getRowInterval(int row){
		DataItem row_info = data_list.get(row);
		int start = row-row_info.list_pos;
		return new int[]{start, start+row_info.total-1};
	}
	/**
	 * @param row the row to edit
	 * @param anchor the row that is the anchor selection
	 * @param ctrl whether or not to invert the selected row
	 * @param shift select everything in between row and anchor
	 * @param ctrlAND change ctrl's mode from "invert" to "and"
	 * @return 
	 */
	private int[] selectRows(int row, int anchor, boolean ctrl, boolean shift, boolean tagColSel){
		//Invert a single cell
		if (ctrl && !tagColSel){
			DataItem d = data_list.get(row);
			d.isSel = !d.isSel;
			changeSelectTotal(d);
			return new int[]{row,row};
		}
		//Select all cells in between
		else if (shift || tagColSel){
			int min = Math.min(anchor, row), max = Math.max(anchor, row);
			boolean noMeta = !ctrl && !shift;
			//Deselect, if no meta-key was pressed
			int[] desel_change = noMeta ? deselectAllExcept(min, max) : null;
			//Select in between
			int[] sel_change = selectInterval(min, max);
			//If they were all selected, invert the selection - woot! one liner
			if (sel_change[0] == -1 && ctrl)
				sel_change = deselectInterval(min, max);
			return noMeta ? new int[]{
					Math.min(sel_change[0], desel_change[0]),
					Math.max(sel_change[1], desel_change[1])} : sel_change;
		}
		//Select a single cell (and deselect all others)
		else{
			DataItem d = data_list.get(row);
			int[] change = deselectAllExcept(row);
			if (!d.isSel){
				d.isSel = true;
				changeSelectTotal(d);
				//Append this index to a *slightly* bigger array
				if (change[0] == -1 || change[0] > row)
					change[0] = row;
				else if (change[1] == -1 || change[1] < row)
					change[1] = row;
			}
			return change;
		}
		
	}
	
	//This method is kinda messy, but whatevs yo
	private void changeSelectTotal(DataItem data){
		int tag_tot = data_sel.get(data.tag)+(data.isSel ? 1 : -1);
		data_sel.put(data.tag, tag_tot);
	}
	
	//Deselects all, except in between ignore interval
	public int[] deselectAll(){ return deselectAllExcept(1,0); }
	public int[] deselectAllExcept(int ignore_row){ return deselectAllExcept(ignore_row, ignore_row); }
	public int[] deselectAllExcept(int ignore_min, int ignore_max){
		//int[] changed = new int[l-(ignore_max-ignore_min+1)];
		int[] changed = new int[]{-1,-1};
		Map<MusicTag, Integer> bad_keys = new HashMap<>();
		for (int x=0, l=data_list.size(); x<l; x++){
			DataItem d_temp = data_list.get(x);
			if (x >= ignore_min && x <= ignore_max){
				//Keep track of a list of keys to ignore
				if (d_temp.isSel){
					Integer n = (Integer) bad_keys.get(d_temp.tag);
					bad_keys.put(d_temp.tag, n == null ? 1 : n+1);
				}
				continue;
			}
			if (d_temp.isSel){
				if (changed[0] == -1 || changed[0] > x)
					changed[0] = x;
				if (changed[1] == -1 || changed[1] < x)
					changed[1] = x;
			}
			d_temp.isSel = false;
		}
		//Mass update selection counts via keys (this should be faster, I'm guessing)
		Set<MusicTag> keys = data_sel.keySet();
		for (MusicTag mt: keys){
			Integer n = (Integer) bad_keys.get(mt);
			data_sel.put(mt, n == null ? 0 : n);
		}
		//Convert to array
		return changed;
	}
	
	//Deselects an interval? well what do you know...
	public int[] deselectInterval(int min, int max){
		return selectInterval(min, max, false);
	}
	
	//Self explanatory
	public int[] selectInterval(int min, int max){
		return selectInterval(min, max, true);
	}
	
	//Selects or deselects an interval
	private int[] selectInterval(int min, int max, boolean select){
		int[] changed = new int[]{-1,-1};
		for (int x=min; x<=max; x++){
			DataItem d_temp = data_list.get(x);
			if (d_temp.isSel != select){
				d_temp.isSel = select;
				if (changed[0] == -1)
					changed[0] = x;
				else if (changed[1] < x)
					changed[1] = x;
				//Inefficient, if there are a lot of tags that have multiple values
				//Though, I confess, I'm not in the mood to optimize this line
				changeSelectTotal(d_temp);
			}
		}
		return changed;
	}
	
	/*
	//Good old concatenation function I got from the internetz
	private int[] concat(int[] A, int[] B) {
		int aLen = A.length, bLen = B.length;
		int[] C = new int[aLen+bLen];
		System.arraycopy(A, 0, C, 0, aLen);
		System.arraycopy(B, 0, C, aLen, bLen);
		return C;
	}
	//Stupid stupid stupid
	private Integer[] convertToIntegerArr(int[] arr){
		Integer[] x = new Integer[arr.length];
		for (int i=0; i<arr.length; i++)
			x[i] = Integer.valueOf(arr[i]);
		return x;
	}
	*/
	
	public boolean isCellSelected(int row, int col){
		DataItem d = data_list.get(row);
		return col == 0 ? data_sel.get(d.tag) > 0 : d.isSel;
	}
	public boolean isCellFocused(int row, int col){
		if (col == 0 && ref[1] == 0){
			DataItem d = data_list.get(row);
			int min = row-d.list_pos, max = min+d.total-1;
			return ref[0] >= min && ref[0] <= max;
		}
		else return ref[0] == row && ref[1] == col;
	}
	public List<Map.Entry<MusicTag, String>> getSelection(){
		List<Map.Entry<MusicTag, String>> map = new ArrayList<>();
		for (DataItem d: data_list){
			if (d.isSel)
				map.add(new AbstractMap.SimpleImmutableEntry(d.tag, d.val));
		}
		return map;
	}
}
