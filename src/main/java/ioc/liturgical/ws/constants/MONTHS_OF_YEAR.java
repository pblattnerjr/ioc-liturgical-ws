package ioc.liturgical.ws.constants;

public enum MONTHS_OF_YEAR {
    M01("m01", "m01 - January (Ιανουάριος)")
    ,M02("m02", "m02 - February (Φεβρουάριος)")
    ,M03("m03", "m03 - March (Μάρτιος)")
    ,M04("m04", "m04 - April (Απρίλιος)")
    ,M05("m05", "m05 - May (Μάιος)")
    ,M06("m06", "m06 - June (Ιούνιος)")
    ,M07("m07", "m07 - July (Ιούλιος)")
    ,M08("m08", "m08 - August (Αύγουστος)")
    ,M09("m09", "m09 - September (Σεπτέμβριος)")
    ,M10("m10", "m10 - October (Οκτώβριος)")
    ,M11("m11", "m11 - November (Νοέμβριος )")
    ,M12("m12", "m12 - December (Δεκέμβριος)")
	,M00("m00","unknown")
	;

	public String code = "";
	public String label = "";
	
	private MONTHS_OF_YEAR(
			String code
			, String label
			) {
		this.code = code;
		this.label = label;
	}
		
}

