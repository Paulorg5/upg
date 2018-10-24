package com.fulmicoton.multiregexp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;

/* https://github.com/fulmicoton/multiregexp */
public class DkBricsAutomatonHelper {

	private DkBricsAutomatonHelper() {
	}

	public static char[] pointsUnion(final Iterable<Automaton> automata) {
		Set<Character> points = new TreeSet<>();
		for (Automaton automaton : automata) {
			for (char c : getStartPoints(automaton)) {
				points.add(c);
			}
		}
		char[] pointsArr = new char[points.size()];
		int i = 0;
		for (Character c : points) {
			pointsArr[i] = c;
			i++;
		}
		return pointsArr;
	}

	private static char[] getStartPoints(Automaton automaton) {
		Set<Character> pointset = new HashSet<Character>();
		for (State s : automaton.getStates()) {
			pointset.add(Character.MIN_VALUE);
			for (Transition t : s.getTransitions()) {
				pointset.add(t.getMin());
				if (t.getMax() < Character.MAX_VALUE)
					pointset.add((char) (t.getMax() + 1));
			}
		}
		char[] points = new char[pointset.size()];
		int n = 0;
		for (Character m : pointset)
			points[n++] = m;
		Arrays.sort(points);
		return points;
	}
}
