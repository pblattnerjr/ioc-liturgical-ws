package ioc.liturgical.ws.calendar;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

public class DateGeneratorTest {

	/**
	 * gr_gr_cog~calendar~y2018.m05.d01.ymd
	 * gr_gr_cog~calendar~y2018.m05.d01.md
	 */
	@Test
	public void test() {
		DateGenerator generator = new DateGenerator("spa_gt_odg", 2018);
		Map<String,String> days = generator.getDays();
		for (Entry<String,String> entry : days.entrySet()) {
			System.out.println(entry.getKey() + " = " + entry.getValue());
		}
		assertTrue(days.size() > 364);
	}

}
