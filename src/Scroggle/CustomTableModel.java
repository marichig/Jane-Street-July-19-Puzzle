package Scroggle;

import javax.swing.table.DefaultTableModel;

public class CustomTableModel extends DefaultTableModel{

	private static final long serialVersionUID = 1L;
	public CustomTableModel(int i, int j) {
		super(i,j);
	}
	@Override
	public boolean isCellEditable(int row, int column) {
		if(row == 0 || row == 2 || row == 4) {
			if(column == 1 || column == 3 || column == 5) {
				return false;
			}
		}
		else if(row == 1  || row == 3|| row == 5) {
			if(column == 0 || column == 2 || column == 4) {
				return false;
			}
		}
		
		return true;
	}
	
	public void setValueAt(Cell cell) {
		setValueAt(cell.value, cell.row, cell.column);
	}
	
}