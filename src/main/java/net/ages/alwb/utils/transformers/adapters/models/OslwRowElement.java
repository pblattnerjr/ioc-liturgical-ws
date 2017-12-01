package net.ages.alwb.utils.transformers.adapters.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import org.ocmc.ioc.liturgical.schemas.models.supers.AbstractModel;

public class OslwRowElement  extends AbstractModel {
	@Expose private List<OslwCellElement> cells = new ArrayList<OslwCellElement>();

	public OslwRowElement() {
		super();
		super.setPrettyPrint(true);
	}
	public void addCellElement(OslwCellElement e) {
		this.cells.add(e);
	}
	public List<OslwCellElement> getCells() {
		return cells;
	}

	public void setCells(List<OslwCellElement> cells) {
		this.cells = cells;
	}
	
	
}
