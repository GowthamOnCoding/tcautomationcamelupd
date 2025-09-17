package com.boa.tcautomation.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.boa.tcautomation.util.REConstants.*;

@Component
public class TestcaseRuleGen {

    private static final Logger log = LoggerFactory.getLogger(TestcaseRuleGen.class);
    private static final DateTimeFormatter TS_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Builds the artifact SQL file for Step-1 (cleanAndInsertTestData) and returns its absolute path.
     * All values are injected — no session/user variables in SQL.
     */
    public String writeArtifact(TestBlock tb) throws Exception {
        // ---------- file path ----------
        final String sanitizedTcId = tb.tcId.replaceAll("[^a-zA-Z0-9]", "_");
        final String stepName = STEP_CLEAN;
        final int seqNoForFileName = 1; // filename only
        final String extension = ".sql";

        final Path destPath = ARTIFACT_ROOT
                .resolve(sanitizedTcId)
                .resolve(sanitizedTcId + "_" + stepName + "_" + seqNoForFileName + extension);
        Files.createDirectories(destPath.getParent());

        final boolean isSqlServer = "SQLSERVER".equalsIgnoreCase(DIALECT);

        try (BufferedWriter w = Files.newBufferedWriter(destPath)) {
            // ---------- header ----------
            if (isSqlServer) w.write(String.format(SQLSERVER.HEADER_FMT, tb.aitNoBare, tb.dbTypeBiz, tb.disc));
            else             w.write(String.format(MYSQL.HEADER_FMT,     tb.aitNoBare, tb.dbTypeBiz, tb.disc));

            // ---------- DIAC (ait_config) ----------
            if (isSqlServer) {
                w.write(String.format(SQLSERVER.DIAC_FMT,
                        tb.aitNoBare, tb.aitNoBare,
                        tb.flags.funSdd, tb.flags.aimlSdd, tb.flags.idwSdd, tb.flags.iedpsSdd, tb.flags.espialSdd,
                        tb.flags.funFft, tb.flags.idwFft, tb.flags.espialFft,
                        esc(tb.topicName), DEFAULT_PROFILE, DEFAULT_USER
                ));
            } else {
                w.write(String.format(MYSQL.DIAC_FMT,
                        tb.aitNoBare, tb.aitNoBare,
                        tb.flags.funSdd, tb.flags.aimlSdd, tb.flags.idwSdd, tb.flags.iedpsSdd, tb.flags.espialSdd,
                        tb.flags.funFft, tb.flags.idwFft, tb.flags.espialFft,
                        esc(tb.topicName), DEFAULT_PROFILE, DEFAULT_USER
                ));
            }

            // ---------- DIDP (ait_dbprop) ----------
            // NOTE: TOPIC_NAME **must** be supplied (tb.topicName).
            if (isSqlServer) {
                w.write(String.format(SQLSERVER.DIDP_FMT,
                        tb.aitNoBare,                            // DELETE where AIT_NO
                        tb.aitNoBare,                            // AIT_NO
                        esc(tb.dbprop.id),                       // ID
                        esc(tb.dbTypeBiz),                       // DB_TYPE (business type)
                        esc(tb.dbprop.mn),                       // MACHINE_NAME
                        esc(tb.dbprop.dn),                       // DB_NAME
                        esc(tb.dbprop.sn),                       // SCHEMA_NAME
                        esc(tb.dbprop.uid),                      // USER_ID
                        esc(tb.dbprop.pwd),                      // PASS_WORD
                        esc(tb.topicName),                       // TOPIC_NAME  <<< (important)
                        esc(tb.dbprop.jur),                      // JDBC_URL
                        DEFAULT_USER,                            // LAST_UPDATED_USER
                        DEFAULT_PROFILE                          // PROFILE
                ));
            } else {
                w.write(String.format(MYSQL.DIDP_FMT,
                        tb.aitNoBare,                            // DELETE where AIT_NO
                        tb.aitNoBare,                            // AIT_NO
                        esc(tb.dbprop.id),                       // ID
                        esc(tb.dbTypeBiz),                       // DB_TYPE (business type)
                        esc(tb.dbprop.mn),                       // MACHINE_NAME
                        esc(tb.dbprop.dn),                       // DB_NAME
                        esc(tb.dbprop.sn),                       // SCHEMA_NAME
                        esc(tb.dbprop.uid),                      // USER_ID
                        esc(tb.dbprop.pwd),                      // PASS_WORD
                        esc(tb.topicName),                       // TOPIC_NAME  <<< (important)
                        esc(tb.dbprop.jur),                      // JDBC_URL
                        DEFAULT_USER,                            // LAST_UPDATED_USER
                        DEFAULT_PROFILE                          // PROFILE
                ));
            }

            // ---------- DIKS (kafka_stat) ----------
            // optional cleanup (to avoid duplicates)
            final String diksDelete = isSqlServer ? SQLSERVER.DIKS_HEADER_FMT : MYSQL.DIKS_HEADER_FMT;
            w.write(String.format(diksDelete, tb.aitNoKafka));

            // Calculate start/end times & duration (CTM/CTH/CTD/CTMO) in Java
            final LocalDateTime start = (tb.time.startLiteral != null)
                    ? LocalDateTime.parse(tb.time.startLiteral, TS_FMT)
                    : LocalDateTime.now();
            LocalDateTime end = start;
            if (tb.time.ctmMins   != null) end = end.plusMinutes(tb.time.ctmMins);
            if (tb.time.cthHours  != null) end = end.plusHours(tb.time.cthHours);
            if (tb.time.ctdDays   != null) end = end.plusDays(tb.time.ctdDays);
            if (tb.time.ctmoMonths!= null) end = end.plusMonths(tb.time.ctmoMonths);

            final long durationSecs = java.time.Duration.between(start, end).getSeconds();
            final String startStr = start.format(TS_FMT);
            final String endStr   = end.format(TS_FMT);

            final String rowFmt = isSqlServer ? SQLSERVER.DIKS_ROW_FMT : MYSQL.DIKS_ROW_FMT;

            // one SEQ_NO for the entire testcase (must be provided in tb.seqNo)
            for (Token t : tb.diks.tokens) {
                w.write(String.format(rowFmt,
                        tb.aitNoKafka,     // AIT_NO (prefixed per DISC rule)
                        startStr,          // START_TIME
                        esc(t.event),      // EVENT
                        endStr,            // END_TIME
                        durationSecs,      // DURATION (seconds)
                        tb.seqNo,          // SEQ_NO (same for the whole testcase AIT)
                        esc(t.status),     // STATUS
                        tb.ksTopic,        // TOPIC_NAME
                        tb.diks.gid,       // GROUP_ID
                        tb.ksDbType,       // DB_TYPE
                        tb.ksSchema,       // SCHEMA_NAME
                        tb.ksMachine,      // MACHINE_NAME
                        DEFAULT_PROFILE,   // PROFILE
                        tb.diks.cid,       // CONFIG_ID
                        DEFAULT_USER,      // LAST_UPDATED_USER
                        tb.diks.cid        // SCHEDULERID
                ));
            }

            // ---------- footer ----------
            if (isSqlServer) w.write(SQLSERVER.FOOTER);
            else             w.write(MYSQL.FOOTER);
        }

        log.info("Wrote {}", destPath.toAbsolutePath());
        return destPath.toAbsolutePath().toString();
    }

    // ===== Helper DTOs =====
    public static class TestBlock {
        public String tcId, tcDesc, dbTypeBiz, disc;
        public String aitNoBare, aitNoKafka, topicName;
        public String ksDbType, ksSchema, ksMachine, ksTopic;
        public int seqNo; // fetched once per test from DB by your runner (from ait_seq)

        public Flags  flags  = new Flags();
        public DbProp dbprop = new DbProp();
        public Diks   diks   = new Diks();
        public TimeSpec time = new TimeSpec();
    }

    public static class Flags {
        public int funSdd, aimlSdd, idwSdd, iedpsSdd, espialSdd;
        public int funFft, idwFft, espialFft;
    }

    public static class DbProp { // DIDP fields
        public String id, mn, dn, sn, uid, pwd, jur;
    }

    public static class Diks {
        public String gid, cid;
        public List<Token> tokens = new ArrayList<>();
    }

    public static class Token { public String event; public String status; }

    public static class TimeSpec {
        public String  startLiteral;   // "yyyy-MM-dd HH:mm:ss"
        public Integer ctmMins;        // minutes
        public Integer cthHours;       // hours
        public Integer ctdDays;        // days
        public Integer ctmoMonths;     // months
    }

    // Escape single quotes for SQL literals
    private static String esc(String s) { return s == null ? "" : s.replace("'", "''"); }
}
