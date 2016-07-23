package com.ginkgocap.ywxt.knowledge.model;

import java.util.Comparator;

import com.ginkgocap.ywxt.person.model.Person;

public class TimeComparator implements Comparator<Person> {

	@Override
	public int compare(Person p1, Person p2) {

		if (p1.getCreateTime() != null) {

			if (p2.getCreateTime() == null)

				return 1;

			long p1Time = p1.getCreateTime().getTime();
			long p2Time = p2.getCreateTime().getTime();

			if (p1Time == p2Time) {

				return 0;

			}

			return p1Time < p2Time ? 1 : -1;

		}

		if (p2.getCreateTime() == null) {// c2's
			// nameChar
			// is
			// blank
			return p1.getId() < p2.getId() ? -1 : 1;
		}

		return -1;
	}

}
