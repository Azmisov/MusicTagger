/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musictagger.tagtable;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Custom table for displaying tags
 * @author isaac
 */
public class TagTable extends JTable{
	final TagTableModel tbl_model;
	
	public TagTable(){
		final TagTable tbl = this;
		final TagTableSelect tbl_sel = new TagTableSelect();
		tbl_model = new TagTableModel();
		
		setModel(tbl_model);
		setDefaultRenderer(Object.class, new TagTableRender());
		setRowHeight(20);
		
		//Disable automatic selection
		setRowSelectionAllowed(false);
		setColumnSelectionAllowed(false);
		setSelectionModel(tbl_sel);
		
		//Manually delegate all the selection to the table model
		//Thats the only way we can simulate a merged cell in the "tagname" column
		this.addMouseListener(new MouseListener(){
			//Start selection
			@Override
			public void mousePressed(MouseEvent e) {
				//Refreshing the paint area is buggy if it wasn't a left click, for some reason
				//Luckily, selection with right/middle click isn't really necessary
				if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) == MouseEvent.BUTTON1_MASK){
					int btn_mask = e.getModifiers() & ~(MouseEvent.BUTTON1_MASK | MouseEvent.BUTTON2_MASK | MouseEvent.BUTTON3_MASK);
					//tbl.rowAtPoint(e.getPoint());
					int[] change = tbl_model.startSelect(
						tbl.rowAtPoint(e.getPoint()),
						tbl.columnAtPoint(e.getPoint()),
						btn_mask == MouseEvent.CTRL_MASK,
						btn_mask == MouseEvent.SHIFT_MASK
					);
					if (change != null)
						tbl.repaintChangedCells(change[0], change[1]);
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				//not sure if this is needed :/
				tbl_model.stopSelect();
			}

			//Not implemented
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
		});
		this.addMouseMotionListener(new MouseMotionListener(){
			//Initiate
			@Override
			public void mouseDragged(MouseEvent e) {
				tbl_model.dragSelection(
					tbl.rowAtPoint(e.getPoint()),
					tbl.columnAtPoint(e.getPoint())
				);
			}

			//Not implemented
			@Override
			public void mouseMoved(MouseEvent e) {}
		});
	}
	
	@Override
	public boolean isCellSelected(int row, int col){
		return tbl_model.isCellSelected(row, col);
	}
	public boolean isCellFocused(int row, int col){
		return tbl_model.isCellFocused(row, col);
	}
	
	private void repaintChangedCells(int min, int max){
		boolean no_min = min == -1, no_max = max == -1;
		if (!no_min || !no_max){
			if (no_min) min = max;
			if (no_max) max = min;
			Rectangle top_left = this.getCellRect(min, 0, true);
			Rectangle bot_right = this.getCellRect(max, 1, true);
			this.repaint(top_left.union(bot_right));
		}
	}
	
	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
        return renderer.getTableCellRendererComponent(
				this, getValueAt(row, col),
                isCellSelected(row, col),
				hasFocus() && isCellFocused(row, col),
				row, col
		);
    }
}
