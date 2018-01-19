package net.ages.alwb.utils.transformers.adapters.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import org.ocmc.ioc.liturgical.schemas.models.supers.AbstractModel;
import net.ages.alwb.utils.transformers.adapters.models.AgesIndexTableRowData;

public class AgesIndexTableData extends AbstractModel {
	@Expose List<AgesIndexTableRowData> tableData = new ArrayList<AgesIndexTableRowData>();
	
	public AgesIndexTableData() {
		super();
	}

	public AgesIndexTableData(boolean printPretty) {
		super();
		super.setPrettyPrint(printPretty);
	}

	public List<AgesIndexTableRowData> getTableData() {
		return tableData;
	}

	public void setTableData(List<AgesIndexTableRowData> tableData) {
		this.tableData = tableData;
	}
	
	public void addRow(AgesIndexTableRowData row) {
		this.tableData.add(row);
	}
	
	public void addList(AgesIndexTableData list) {
		for (AgesIndexTableRowData row : list.tableData) {
			this.tableData.add(row);
		}
	}
}
