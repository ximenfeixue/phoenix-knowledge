package com.ginkgocap.ywxt.knowledge.model;

import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;

import com.ginkgocap.ywxt.model.Connections;

public class NameComparator  implements Comparator<Connections> {

	public int compare(Connections c1, Connections c2) {

		if (StringUtils.isNotBlank(c1.getJtContactMini().getNameChar())) {
			if (StringUtils.isBlank(c2.getJtContactMini().getNameChar()))
				return -1;
			int result = c1.getJtContactMini().getNameChar()
					.compareTo(c2.getJtContactMini().getNameChar());
			if (result == 0) {
				return c1.getId() < c2.getId() ? -1 : 1;
			} else {
				return result;
			}
		}

		// c1's nameChar is blank
		if (StringUtils.isBlank(c2.getJtContactMini().getNameChar())) {// c2's
																		// nameChar
																		// is
																		// blank
			return c1.getId() < c2.getId() ? -1 : 1;
		}

		// c1's nameChar is blank, c2's nameChar is not blank
		return -1;
	}
}
