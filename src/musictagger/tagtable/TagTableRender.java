package musictagger.tagtable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author isaac
 */
public class TagTableRender extends JLabel implements TableCellRenderer{
	private static final Color
			commonCol = new Color(227,194,182),
			customCol = Color.MAGENTA,
			borderCol = new Color(110,110,110),
			focusCol = new Color(139,172,202);
	private static final Border
			emptyBord, tagBord, valBord;
	private static final Border[] focusBord;
	private static final Font
			normFont = new Font("Sans", Font.PLAIN, 13),
			boldFont = new Font("Sans", Font.BOLD, 13);
	private static final int pad = 8, off = 30, fwidth = 2;
	static{
		Border tagBase = BorderFactory.createMatteBorder(0, 0, 1, 0, borderCol),
				valBase = BorderFactory.createMatteBorder(0, 1, 1, 0, borderCol),
				femptBord;
		
		emptyBord = new EmptyBorder(pad, pad, pad, pad);
		int dif = pad-fwidth;
		femptBord = new EmptyBorder(dif, dif, dif, dif);
		tagBord = BorderFactory.createCompoundBorder(tagBase, emptyBord);
		valBord = BorderFactory.createCompoundBorder(valBase, emptyBord);
		
		focusBord = new Border[5];
		focusBord[0] = BorderFactory.createCompoundBorder(tagBase, BorderFactory.createLineBorder(focusCol, fwidth)); //single col=0
		focusBord[1] = BorderFactory.createCompoundBorder(valBase, BorderFactory.createLineBorder(focusCol, fwidth)); //single col=1
		focusBord[2] = BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(fwidth, fwidth, 0, fwidth, focusCol), femptBord); //multiple, top
		focusBord[3] = BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, fwidth, 0, fwidth, focusCol), femptBord); //multiple, middle
		focusBord[4] = BorderFactory.createCompoundBorder(tagBase, BorderFactory.createMatteBorder(0, fwidth, fwidth, fwidth, focusCol)); //multiple, bottom
		
		for (int i=0; i<3; i++){
			if (i == 2) i = 4;
			focusBord[i] = BorderFactory.createCompoundBorder(focusBord[i], femptBord);
		}
	}
	
	
	public TagTableRender(){
		setOpaque(true);
		setBackground(Color.WHITE);
		Border b = BorderFactory.createMatteBorder(0, 0, 1, 1, borderCol);
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		TagTableModel.DataItem d = (TagTableModel.DataItem) value;
		boolean isCom = d.tag.isCommon(),
				isCust = d.tag.isCustom(),
				isSel = isSelected,
				isCol = column == 0;
		
		//Text
		setText(isCol ? (d.list_pos == 0 ? d.tag.toString() : "") : d.val);
		setFont(isSel ? boldFont : normFont);
				
		//Background color
		//setBackground(isCom ? commonCol : isCust ? customCol : Color.WHITE);
		setBackground(Color.WHITE);
		if (d.isOdd || isSel){
			int dark = isSel ? off*2 : off;
			Color base = getBackground();
			setBackground(new Color(
				clamp(base.getRed() - dark),
				clamp(base.getGreen() - dark),
				clamp(base.getBlue() - dark)
			));
		}
		
		//Border
		if (hasFocus){
			//Blueify the background
			Color base = getBackground();
			setBackground(new Color(
				base.getRed(),
				base.getGreen(),
				clamp(base.getBlue()+10)
			));
			
			if (!isCol || d.total == 1)
				setBorder(focusBord[isCol ? 0 : 1]);
			else{
				//System.out.println("using border: "+(d.list_pos == 0 ? 2 : (d.isLast ? 4 : 3)));
				//setBorder(d.list_pos == 0 ? emptyBord : emptyBord);
				setBorder(focusBord[d.list_pos == 0 ? 2 : (d.isLast ? 4 : 3)]);
			}
		}
		else setBorder(isCol ? (d.isLast ? tagBord : emptyBord) : valBord);
		
		return this;
	}
	
	private int clamp(int col){
		if (col < 0) return 0;
		if (col > 255) return 255;
		return col;
	}
}