package com.boa.tcautomation.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.boa.tcautomation.util.REConstants.*;
import static com.boa.tcautomation.util.TestcaseRuleGen.*;

@Component
public class TestCaseRunner implements CommandLineRunner {

    @Autowired private DbUtil dbUtil;
    @Autowired private TestcaseRuleGen gen;

    private static final Logger logger = LoggerFactory.getLogger(TestCaseRunner.class);

    @Override
    public void run(String... args) throws Exception {
        List<String> lines = loadRules();
        List<List<String>> blocks = splitByBlankLine(lines);
        int caseNo = 0;

        for (List<String> b : blocks) {
            caseNo++;
            TestBlock tb = parseBlock(b);

            // allocate TC_ID / AIT_NO
            tb.tcId       = "TC_" + String.format("%06d", allocateSeq("TC",  START_TC_SEQ));
            tb.aitNoBare  = String.valueOf(allocateSeq("AIT", START_AIT_SEQ));
            tb.aitNoKafka = ("SDD".equalsIgnoreCase(tb.disc) ? AIT_PREFIX_SDD : AIT_PREFIX_DEFAULT) + tb.aitNoBare;

            // insert tc_master
            String sqlTc = String.format(QRY_INSERT_TC_MASTER_FMT, tb.tcId, esc(tb.tcDesc), esc(tb.tcDesc), tb.aitNoBare);
            if (!dbUtil.executeQuery(sqlTc)) throw new RuntimeException("Failed to insert tc_master for " + tb.tcId);

            // write Step-1 artifact file
            String artifact = gen.writeArtifact(tb);
            String artifactPath = artifact.replace("\\", "/");

            // build Step-5 validation param (TRUE/FALSE)
            String validationParam = gen.buildValidationParam(tb);

            // Register 6 steps (sequence numbers 1..6)
            int seq = 1;
            dbUtil.executeQuery(String.format(QRY_INSERT_TC_STEP_FMT, tb.tcId, STEP_CLEAN,    artifactPath,      seq)); seq++;
            dbUtil.executeQuery(String.format(QRY_INSERT_TC_STEP_FMT, tb.tcId, STEP_EXPORT,   STEP_EXPORT_BEFORE_PARAM, seq)); seq++;
            dbUtil.executeQuery(String.format(QRY_INSERT_TC_STEP_FMT, tb.tcId, STEP_RUN_SCHED,STEP_RUN_SCHED_PARAM, seq)); seq++;
            dbUtil.executeQuery(String.format(QRY_INSERT_TC_STEP_FMT, tb.tcId, STEP_DELAY,    STEP_DELAY_PARAM,  seq)); seq++;
            dbUtil.executeQuery(String.format(QRY_INSERT_TC_STEP_FMT, tb.tcId, STEP_VALIDATE, validationParam,   seq)); seq++;
            dbUtil.executeQuery(String.format(QRY_INSERT_TC_STEP_FMT, tb.tcId, STEP_EXPORT,   STEP_EXPORT_AFTER_PARAM,  seq));

            System.out.println("✅ ["+caseNo+"] "+tb.tcId+" → "+artifact);
        }
        System.out.println("Done. Artifacts at: " + ARTIFACT_ROOT);
    }

    // ===== parsing & helpers =====
    private TestBlock parseBlock(List<String> block) {
        TestBlock tb = new TestBlock();
        for (String raw : block) {
            String line = raw.trim();
            if (line.isEmpty() || line.startsWith("--")) continue;

            if (line.startsWith("TD-"))          tb.tcDesc     = line.substring(3).trim();
            else if (line.startsWith("DBTYPE-")) tb.dbTypeBiz  = line.substring(7).trim();
            else if (line.startsWith("DISC-"))   tb.disc       = line.substring(5).trim();
            else if (line.startsWith("VALTARGET-")) {
                if (tb.validation == null) tb.validation = new ValidationSpec();
                tb.validation.target = line.substring("VALTARGET-".length()).trim();
            }
            else if (line.startsWith("DIAC:"))   parseDIAC(tb, line.substring(5).trim());
            else if (line.startsWith("DIDP:"))   parseDIDP(tb, line.substring(5).trim());
            else if (line.startsWith("DIKS:"))   parseDIKS(tb, line.substring(5).trim());
            // DIAS / DISW / DIDM / DKSS are auto-generated from current tb
        }
        require(tb.tcDesc != null,     "TD- required");
        require(tb.dbTypeBiz != null,  "DBTYPE- required");
        require(tb.disc != null,       "DISC- required");
        require(tb.topicName != null,  "DIAC: TN (topic_name) required");
        return tb;
    }

    private void parseDIAC(TestBlock tb, String spec) {
        Map<String,String> kv = kv(spec);
        tb.topicName = must(kv, "TN", "DIAC requires TN");

        Set<String> est = csvSet(kv.get("EST")); // SDD flags
        tb.flags.funSdd    = est.contains("FUNNEL") ? 1 : 0;
        tb.flags.aimlSdd   = est.contains("AIML")   ? 1 : 0;
        tb.flags.idwSdd    = est.contains("IDW")    ? 1 : 0;
        tb.flags.iedpsSdd  = est.contains("IEDPS")  ? 1 : 0;
        tb.flags.espialSdd = est.contains("ESPIAL") ? 1 : 0;

        Set<String> eft = csvSet(kv.get("EFT")); // FFT flags
        tb.flags.funFft    = eft.contains("FUNNEL") ? 1 : 0;
        tb.flags.idwFft    = eft.contains("IDW")    ? 1 : 0;
        tb.flags.espialFft = eft.contains("ESPIAL") ? 1 : 0;
    }

    private void parseDIDP(TestBlock tb, String spec) {
        Map<String,String> kv = kv(spec);
        tb.dbprop.id  = must(kv, "ID",  "DIDP requires ID");
        tb.dbprop.mn  = must(kv, "MN",  "DIDP requires MN");
        tb.dbprop.dn  = must(kv, "DN",  "DIDP requires DN");
        tb.dbprop.sn  = must(kv, "SN",  "DIDP requires SN");
        tb.dbprop.uid = must(kv, "UID", "DIDP requires UID");
        tb.dbprop.pwd = must(kv, "PWD", "DIDP requires PWD");
        tb.dbprop.jur = kv.getOrDefault("JUR", kv.get("JURL"));
        require(tb.dbprop.jur != null, "DIDP requires JUR or JURL");
    }

    private void parseDIKS(TestBlock tb, String spec) {
        Map<String,String> opts = kv(spec);
        tb.diks.gid = opts.get("GID");
        tb.diks.cid = opts.get("CID");
        if (opts.containsKey("ST"))   tb.time.startLiteral = opts.get("ST");
        if (opts.containsKey("CTM"))  tb.time.ctmMins   = parseIntSafe(opts.get("CTM"));
        if (opts.containsKey("CTH"))  tb.time.cthHours  = parseIntSafe(opts.get("CTH"));
        if (opts.containsKey("CTD"))  tb.time.ctdDays   = parseIntSafe(opts.get("CTD"));
        if (opts.containsKey("CTMO")) tb.time.ctmoMonths= parseIntSafe(opts.get("CTMO"));

        for (String part : splitCommaSafe(spec)) {
            String p = part.trim();
            int dash = p.indexOf('-');
            if (dash <= 0) continue;
            String left = p.substring(0, dash).trim().toUpperCase();
            String right= unquote(p.substring(dash+1).trim()).toUpperCase();
            if (Set.of("GID","CID","ST","CTM","CTH","CTD","CTMO").contains(left)) continue;
            Token t = new Token();
            t.event  = mapEvent(left);
            t.status = mapStatus(right);
            if (t.event != null && t.status != null) tb.diks.tokens.add(t);
        }
        require(!tb.diks.tokens.isEmpty(), "DIKS must contain at least one event-status token (e.g., P-C)");
    }

    // ===== Helpers =====
    private long allocateSeq(String id, int startAt) {
        String sel = String.format(QRY_SELECT_SEQ_FMT, id);
        List<Map<String,Object>> rows = dbUtil.queryForList(sel);
        long next;
        if (rows == null || rows.isEmpty()) {
            dbUtil.executeQuery(String.format(QRY_INIT_SEQ_FMT, id, startAt));
            next = startAt + 1L;
            dbUtil.update(QRY_UPDATE_SEQ, next, id);
        } else {
            long cur = ((Number) rows.get(0).get("value")).longValue();
            next = cur + 1L;
            dbUtil.update(QRY_UPDATE_SEQ, next, id);
        }
        return next;
    }

    private List<String> loadRules() throws Exception {
        Path cwd = Path.of("./input.txt");
        return Files.readAllLines(cwd);
    }

    private static List<List<String>> splitByBlankLine(List<String> lines) {
        List<List<String>> out = new ArrayList<>();
        List<String> cur = new ArrayList<>();
        for (String ln : lines) {
            if (ln.trim().isEmpty()) {
                if (!cur.isEmpty()) { out.add(new ArrayList<>(cur)); cur.clear(); }
            } else cur.add(ln);
        }
        if (!cur.isEmpty()) out.add(cur);
        return out;
    }

    private static Map<String,String> kv(String spec) {
        Map<String,String> m = new LinkedHashMap<>();
        for (String part : splitCommaSafe(spec)) {
            int i = part.indexOf('-');
            if (i <= 0) continue;
            String k = part.substring(0,i).trim().toUpperCase();
            String v = unquote(part.substring(i+1).trim());
            m.put(k, v);
        }
        return m;
    }

    private static Set<String> csvSet(String csv) {
        Set<String> s = new LinkedHashSet<>();
        if (csv == null) return s;
        for (String p : csv.split(",")) {
            String v = p.trim().toUpperCase();
            if (!v.isEmpty()) s.add(v);
        }
        return s;
    }

    private static List<String> splitCommaSafe(String s) {
        List<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inS=false, inD=false;
        for (char c: s.toCharArray()) {
            if (c=='\'' && !inD) inS=!inS;
            if (c=='"'  && !inS) inD=!inD;
            if (c==',' && !inS && !inD) { out.add(cur.toString().trim()); cur.setLength(0); }
            else cur.append(c);
        }
        if (cur.length()>0) out.add(cur.toString().trim());
        return out;
    }

    private static String unquote(String s) {
        if (s == null) return null;
        if ((s.startsWith("\"") && s.endsWith("\"")) || (s.startsWith("'") && s.endsWith("'")))
            return s.substring(1, s.length()-1);
        return s;
    }

    private static String esc(String s) { return s == null ? "" : s.replace("'", "''"); }
    private static void require(boolean ok, String msg){ if (!ok) throw new IllegalArgumentException(msg); }
    private static String must(Map<String,String> map, String key, String msg) {
        String v = map.get(key);
        if (v == null || v.isBlank()) throw new IllegalArgumentException(msg);
        return v;
    }
    private static Integer parseIntSafe(String v){ try { return v==null?null:Integer.parseInt(v.trim()); } catch(Exception e){ return null; } }

    private static String mapEvent(String abbr) {
        return switch (abbr) {
            case "P"   -> "Producer";
            case "MD"  -> "Metadata";
            case "FC"  -> "Consumer";
            case "IDC" -> "IDW-Consumer";
            case "AIC" -> "AIML-Consumer";
            case "IEC" -> "IEDPS-Consumer";
            case "EC"  -> "Espial-Consumer";
            default    -> null;
        };
    }

    private static String mapStatus(String abbr) {
        return switch (abbr) {
            case "C"   -> "Completed";
            case "F"   -> "Failed";
            case "PP"  -> "Parially Processed";
            case "IP"  -> "In Progress";
            case "CNS" -> "Completed -NS";
            case "TE"  -> "Terminated";
            default    -> null;
        };
    }
}
