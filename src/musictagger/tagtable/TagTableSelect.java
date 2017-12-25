/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musictagger.tagtable;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

/**
 * Dummy selection model so we can handle all selections ourselves
 * @author guy from this site: http://www.coderanch.com/t/346552/GUI/java/Disable-Selection-JTable
 * @author me, because I made some changes too
 */
public class TagTableSelect extends DefaultListSelectionModel{
	@Override
	public boolean isSelectionEmpty() { return true; }
	@Override
	public boolean isSelectedIndex(int index) { return false; }
	@Override
	public int getMinSelectionIndex() { return -1; }
	@Override
	public int getMaxSelectionIndex() { return -1; }
	@Override
	public int getLeadSelectionIndex() { return -1; }  
	@Override
	public int getAnchorSelectionIndex() { return -1; }
	@Override
	public void setSelectionInterval(int index0, int index1) {}
	@Override
	public void setLeadSelectionIndex(int index) { }
	@Override
	public void setAnchorSelectionIndex(int index) { }
	@Override
	public void addSelectionInterval(int index0, int index1) { }
	@Override
	public void insertIndexInterval(int index, int length, boolean before) { }
	@Override
	public void clearSelection() { }
	@Override
	public void removeSelectionInterval(int index0, int index1) { }
	@Override
	public void removeIndexInterval(int index0, int index1) { }
	@Override
	public void setSelectionMode(int selectionMode) { }
	@Override
	public int getSelectionMode() { return ListSelectionModel.SINGLE_SELECTION; }
	@Override
	public void addListSelectionListener(ListSelectionListener lsl) { }
	@Override
	public void removeListSelectionListener(ListSelectionListener lsl) { }
	@Override
	public void setValueIsAdjusting(boolean valueIsAdjusting) { }
	@Override
	public boolean getValueIsAdjusting() { return false; }
}
