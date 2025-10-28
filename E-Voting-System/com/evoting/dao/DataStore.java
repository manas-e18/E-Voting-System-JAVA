package com.evoting.dao;

import com.evoting.model.Candidate;
import com.evoting.model.Voter;

import java.io.*;
import java.util.*;

/**
 * DataStore: file-backed in-memory store for voters, candidates and votes.
 * Added resetAll() to wipe everything and start from scratch.
 */
public class DataStore {
    private static final String VOTER_FILE = "voters.ser";
    private static final String CANDIDATE_FILE = "candidates.ser";
    private static final String VOTES_FILE = "votes.ser";

    private Map<String, Voter> voters = new HashMap<>();
    private LinkedHashMap<String, Candidate> candidates = new LinkedHashMap<>();
    private Map<String, Integer> votes = new HashMap<>();

    private static DataStore instance;

    private DataStore() { loadAll(); }

    public static synchronized DataStore getInstance() {
        if (instance == null) instance = new DataStore();
        return instance;
    }

    /* Voter operations */
    public synchronized void addVoter(Voter v) {
        voters.put(v.getId(), v);
        save(VOTER_FILE, voters);
    }
    public synchronized Voter getVoterById(String id) { return voters.get(id); }
    public synchronized Collection<Voter> getAllVoters() { return voters.values(); }
    public synchronized void updateVoter(Voter v) {
        voters.put(v.getId(), v);
        save(VOTER_FILE, voters);
    }

    /* Candidate operations */
    public synchronized void addCandidate(Candidate c) {
        candidates.put(c.getId(), c);
        votes.putIfAbsent(c.getId(), 0);
        save(CANDIDATE_FILE, candidates);
        save(VOTES_FILE, votes);
    }

    public synchronized void removeCandidate(String candidateId) {
        if (candidateId == null) return;
        candidates.remove(candidateId);
        votes.remove(candidateId);
        save(CANDIDATE_FILE, candidates);
        save(VOTES_FILE, votes);
    }

    public synchronized Collection<Candidate> getAllCandidates() { return candidates.values(); }
    public synchronized Candidate getCandidate(String id) { return candidates.get(id); }

    /* Votes */
    public synchronized void castVote(String candidateId) {
        votes.put(candidateId, votes.getOrDefault(candidateId, 0) + 1);
        save(VOTES_FILE, votes);
    }
    public synchronized Map<String,Integer> getVotes() { return Collections.unmodifiableMap(votes); }

    /**
     * Reset election state but keep voter list (legacy function).
     * Kept for backward compatibility — use resetAll() to fully wipe.
     */
    public synchronized void resetElection() {
        for (Voter v : voters.values()) v.setHasVoted(false);
        votes.clear();
        for (String cid : candidates.keySet()) votes.put(cid, 0);
        save(VOTER_FILE, voters);
        save(VOTES_FILE, votes);
    }

    /**
     * FULL FACTORY RESET:
     * - Clears all voters, candidates and votes in memory
     * - Persists empty maps (overwrites .ser files)
     * - Deletes the .ser files as an extra step (so directory looks fresh)
     *
     * Use with caution. Called by Admin -> Factory Reset button.
     */
    public synchronized void resetAll() {
        // Clear in-memory
        voters.clear();
        candidates.clear();
        votes.clear();

        // Persist empty collections
        save(CANDIDATE_FILE, candidates);
        save(VOTES_FILE, votes);
        save(VOTER_FILE, voters);

        // Also attempt to delete the files for a clean working directory
        deleteFileQuiet(VOTER_FILE);
        deleteFileQuiet(CANDIDATE_FILE);
        deleteFileQuiet(VOTES_FILE);
    }

    // ---------- Persistence helpers ----------

    @SuppressWarnings("unchecked")
    private void loadAll() {
        Object o;
        o = load(VOTER_FILE); if (o instanceof Map) voters = (Map<String,Voter>) o;
        o = load(CANDIDATE_FILE); if (o instanceof Map) candidates = (LinkedHashMap<String,Candidate>) o;
        o = load(VOTES_FILE); if (o instanceof Map) votes = (Map<String,Integer>) o;

        // ensure votes has entries for all candidates
        for (String cid : candidates.keySet()) votes.putIfAbsent(cid, 0);
    }

    private void save(String filename, Object obj) {
        // attempt to write object; if fails, print stacktrace (lab/demo)
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Object load(String filename) {
        File f = new File(filename);
        if (!f.exists()) return null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            return in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void deleteFileQuiet(String filename) {
        try {
            File f = new File(filename);
            if (f.exists()) f.delete();
        } catch (Exception ignored) {}
    }
}
