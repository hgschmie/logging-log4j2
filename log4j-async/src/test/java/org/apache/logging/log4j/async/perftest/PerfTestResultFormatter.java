package org.apache.logging.log4j.async.perftest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Utility class that can read the "Ranking" output of the PerfTestDriver and
 * format it for pasting into Excel.
 */
class PerfTestResultFormatter {
	static final String LF = System.getProperty("line.separator");
	static final NumberFormat NUM = new DecimalFormat("#,##0");

	static class Stats {
		long throughput;
		double avgLatency;
		double latency99Pct;
		double latency99_99Pct;

		Stats(String throughput, String avg, String lat99, String lat99_99)
				throws ParseException {
			this.throughput = NUM.parse(throughput.trim()).longValue();
			this.avgLatency = Double.parseDouble(avg.trim());
			this.latency99Pct = Double.parseDouble(lat99.trim());
			this.latency99_99Pct = Double.parseDouble(lat99_99.trim());
		}
	}

	private Map<String, Map<String, Stats>> results = new TreeMap<String, Map<String, Stats>>();

	public PerfTestResultFormatter() {
	}

	public String format(String text) throws ParseException {
		results.clear();
		String[] lines = text.split("[\\r\\n]+");
		for (String line : lines) {
			process(line);
		}
		return latencyTable() + LF + throughputTable();
	}

	private String latencyTable() {
		StringBuilder sb = new StringBuilder(4 * 1024);
		Set<String> subKeys = results.values().iterator().next().keySet();
		char[] tabs = new char[subKeys.size()];
		Arrays.fill(tabs, '\t');
		String sep = new String(tabs);
		sb.append("\tAverage latency" + sep + "99% less than" + sep
				+ "99.99% less than");
		sb.append(LF);
		for (int i = 0; i < 3; i++) {
			for (String subKey : subKeys) {
				sb.append("\t").append(subKey);
			}
		}
		sb.append(LF);
		for (String key : results.keySet()) {
			sb.append(key);
			for (int i = 0; i < 3; i++) {
				Map<String, Stats> sub = results.get(key);
				for (String subKey : sub.keySet()) {
					Stats stats = sub.get(subKey);
					switch (i) {
					case 0:
						sb.append("\t").append((long) stats.avgLatency);
						break;
					case 1:
						sb.append("\t").append((long) stats.latency99Pct);
						break;
					case 2:
						sb.append("\t").append((long) stats.latency99_99Pct);
						break;
					}
				}
			}
			sb.append(LF);
		}
		return sb.toString();
	}

	private String throughputTable() {
		StringBuilder sb = new StringBuilder(4 * 1024);
		Set<String> subKeys = results.values().iterator().next().keySet();
		sb.append("\tThroughput per thread (msg/sec)");
		sb.append(LF);
		for (String subKey : subKeys) {
			sb.append("\t").append(subKey);
		}
		sb.append(LF);
		for (String key : results.keySet()) {
			sb.append(key);
			Map<String, Stats> sub = results.get(key);
			for (String subKey : sub.keySet()) {
				Stats stats = sub.get(subKey);
				sb.append("\t").append((long) stats.throughput);
			}
			sb.append(LF);
		}
		return sb.toString();
	}

	private void process(String line) throws ParseException {
		String key = line.substring(line.indexOf('.') + 1, line.indexOf('('));
		String sub = line.substring(line.indexOf('(') + 1, line.indexOf(')'));
		String throughput = line.substring(line.indexOf("throughput: ")
				+ "throughput: ".length(), line.indexOf(" ops"));
		String avg = line.substring(line.indexOf("avg=") + "avg=".length(),
				line.indexOf(" 99%"));
		String pct99 = line.substring(
				line.indexOf("99% < ") + "99% < ".length(),
				line.indexOf(" 99.99%"));
		String pct99_99 = line.substring(line.indexOf("99.99% < ")
				+ "99.99% < ".length(), line.lastIndexOf('(') - 1);
		Stats stats = new Stats(throughput, avg, pct99, pct99_99);
		Map<String, Stats> map = results.get(key.trim());
		if (map == null) {
			map = new TreeMap<String, Stats>(sort());
			results.put(key.trim(), map);
		}
		String subKey = sub.trim();
		if ("single thread".equals(subKey)) {
			subKey = "1 thread";
		}
		map.put(subKey, stats);
	}

	private Comparator<String> sort() {
		return new Comparator<String>() {
			List<String> expected = Arrays.asList("1 thread", "2 threads",
					"4 threads", "8 threads", "16 threads", "32 threads",
					"64 threads");

			@Override
			public int compare(String o1, String o2) {
				int i1 = expected.indexOf(o1);
				int i2 = expected.indexOf(o2);
				if (i1 < 0 || i2 < 0) {
					return o1.compareTo(o2);
				}
				return i1 - i2;
			}
		};
	}

	public static void main(String[] args) throws Exception {
		PerfTestResultFormatter fmt = new PerfTestResultFormatter();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		String line = null;
		while ((line = reader.readLine()) != null) {
			fmt.process(line);
		}
		System.out.println(fmt.latencyTable());
		System.out.println();
		System.out.println(fmt.throughputTable());
	}
}