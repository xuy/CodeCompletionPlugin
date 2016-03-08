package cn.yyx.contentassist.codepredict;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import cn.yyx.research.AeroSpikeHandle.AeroLifeCycle;
import cn.yyx.research.AeroSpikeHandle.PredictProbPair;

public class Sequence implements Comparable<Sequence> {

	protected Queue<Sentence> sequence = new LinkedList<Sentence>();
	protected Sentence last = null;
	protected Double prob = (double) 0;

	public void AddOneSentence(Sentence ons) {
		sequence.add(ons);
		last = ons;
		int qsize = sequence.size();
		if (qsize > PredictMetaInfo.PrePredictWindow) {
			sequence.poll();
		}
	}

	public SequenceManager PredictSentences(AeroLifeCycle alc, int neededSize) {
		SequenceManager result = new SequenceManager();
		int ssize = sequence.size();
		int maxsize = Math.min(ssize - 1, PredictMetaInfo.NgramMaxSize);
		ArrayList<Sentence> analysislist = LastOfSentenceQueue(maxsize);
		for (int i = maxsize; i > 0; i--) {
			String key = ConcatJoinLast(i, analysislist);
			List<PredictProbPair> predicts = alc.AeroModelPredict(key, neededSize);
			Iterator<PredictProbPair> itr = predicts.iterator();
			while (itr.hasNext()) {
				PredictProbPair ppp = itr.next();
				if (neededSize == 0)
				{
					continue;
				}
				Sequence s = NewSequence(ppp);
				result.AddSequence(s);
				neededSize--;
			}
			if (neededSize == 0)
			{
				break;
			}
		}
		return result;
	}

	/*
	 * public SequenceManager HandleNewInSentence(AeroLifeCycle alc, String ons,
	 * int neededSize) {
	 * 
	 * Sequence exactmatch = null; PriorityQueue<Sequence> notexactmatch = new
	 * PriorityQueue<Sequence>();
	 * 
	 * if (!this.exactmatch) { neededSize--; } int ssize = sequence.size(); int
	 * maxsize = Math.min(ssize - 1, PredictMetaInfo.NgramMaxSize);
	 * 
	 * for (int i = maxsize; i > 0; i--) { String key = ConcatJoinLast(i,
	 * analysislist); List<PredictProbPair> predicts = alc.AeroModelPredict(key,
	 * neededSize, ons); Iterator<PredictProbPair> itr = predicts.iterator();
	 * while (itr.hasNext()) { PredictProbPair ppp = itr.next(); if
	 * (!itr.hasNext()) { // last exact match if (this.exactmatch) { exactmatch
	 * = NewSequenceInExactMatch(ppp); } else { NewSequenceInNotExactMatch(ppp,
	 * notexactmatch, -1); } } else { if (neededSize == 0) { continue; } }
	 * neededSize = NewSequenceInNotExactMatch(ppp, notexactmatch, neededSize);
	 * } if (neededSize == 0) { break; } }
	 * 
	 * result.setExactmatch(exactmatch); result.setNotexactmatch(notexactmatch);
	 * return result; }
	 * 
	 * private int NewSequenceInNotExactMatch(PredictProbPair ppp,
	 * PriorityQueue<Sequence> notexactmatch, int neededSize) { Sequence s =
	 * NewSequence(ppp); notexactmatch.add(s); neededSize--; return neededSize;
	 * }
	 * 
	 * private Sequence NewSequenceInExactMatch(PredictProbPair ppp) { return
	 * NewSequence(ppp); }
	 */

	private Sequence NewSequence(PredictProbPair ppp) {
		Sequence s = (Sequence) this.clone();
		s.AddOneSentence(ppp.getPred());
		s.AddProbability(ppp.getProb());
		return s;
	}

	public void AddProbability(Double prob2) {
		this.prob += prob2;
	}
	
	public Iterator<Sentence> Iterator()
	{
		return sequence.iterator();
	}

	private String ConcatJoinLast(int size, ArrayList<Sentence> analysislist) {
		StringBuffer sb = new StringBuffer("");
		int end = analysislist.size() - 1;
		int start = end + 1 - size;
		for (int i = start; i <= end; i++) {
			String split = " ";
			if (i == end) {
				split = "";
			}
			sb.append(analysislist.get(i) + split);
		}
		return sb.toString().trim();
	}

	private ArrayList<Sentence> LastOfSentenceQueue(int neededSize) {
		ArrayList<Sentence> result = new ArrayList<Sentence>();
		int seqsize = sequence.size();
		int skipsize = seqsize - neededSize;
		int hasskipped = 0;
		Iterator<Sentence> itr = sequence.iterator();
		boolean shouldadd = false;
		while (itr.hasNext()) {
			if (hasskipped >= skipsize) {
				shouldadd = true;
			}
			Sentence sentence = itr.next();
			hasskipped++;
			if (shouldadd) {
				result.add(sentence);
			}
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("");
		Iterator<Sentence> itr = sequence.iterator();
		while (itr.hasNext()) {
			sb.append(" " + itr.next());
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	public Double getProb() {
		return prob;
	}

	public void setProb(Double prob) {
		this.prob = prob;
	}

	@SuppressWarnings("unchecked")
	public Object clone() {
		Sequence o = null;
		try {
			o = (Sequence) super.clone();
			o.sequence = (Queue<Sentence>) (((LinkedList<Sentence>) sequence).clone());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}

	@Override
	public int compareTo(Sequence o) {
		// TODO this order has changed, must change the related code.
		return getProb().compareTo(o.getProb());
	}

	public Sentence getLast() {
		return last;
	}

}