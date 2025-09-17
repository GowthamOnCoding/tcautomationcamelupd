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
     * Step-1 artifact (clean & insert) generator.
     * Includes: DIAC, DIDP, DIKS, DIAS, DISW, DIDM, DKSS in this order.
     * Returns absolute file path of the generated .sql file.
     */
    public String writeArtifact(TestBlock tb) throws Exception {
        // ---------- file path ----------
        final String sanitizedTcId = tb.tcId.replaceAll("[^a-zA-Z0-9]", "_");
        final Path destPath = ARTIFACT_ROOT
                .resolve(sanitizedTcId)
                .resolve(sanitizedTcId + "_" + STEP_CLEAN + "_1.sql");

        Files.createDirectories(destPath.getParent());
        final boolean isSqlServer = "SQLSERVER".equalsIgnoreCase(DIALECT);

        // Defaults for DIKS row context from DIDP/DIAC
        if (tb.ksDbType == null)  tb.ksDbType  = nvl(tb.dbTypeBiz);
        if (tb.ksSchema == null)  tb.ksSchema  = nvl(tb.dbprop.sn);
        if (tb.ksMachine == null) tb.ksMachine = nvl(tb.dbprop.mn);
        if (tb.ksTopic == null)   tb.ksTopic   = nvl(tb.topicName);

        try (BufferedWriter w = Files.newBufferedWriter(destPath)) {
            // header
            if (isSqlServer) w.write(String.format(SQLSERVER.HEADER_FMT, tb.aitNoBare, tb.dbTypeBiz, tb.disc));
            else             w.write(String.format(MYSQL.HEADER_FMT,     tb.aitNoBare, tb.dbTypeBiz, tb.disc));

            // DIAC
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

            // DIDP
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
                        esc(tb.topicName),                       // TOPIC_NAME
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
                        esc(tb.topicName),                       // TOPIC_NAME
                        esc(tb.dbprop.jur),                      // JDBC_URL
                        DEFAULT_USER,                            // LAST_UPDATED_USER
                        DEFAULT_PROFILE                          // PROFILE
                ));
            }

            // DIKS (kafka_stat)
            final String diksDelFmt = isSqlServer ? SQLSERVER.DIKS_HEADER_FMT : MYSQL.DIKS_HEADER_FMT;
            w.write(String.format(diksDelFmt, tb.aitNoKafka));

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
            final String diksRowFmt = isSqlServer ? SQLSERVER.DIKS_ROW_FMT : MYSQL.DIKS_ROW_FMT;
            final int seqForKafka = tb.seqNo > 0 ? tb.seqNo : 1; // single seq per testcase

            for (Token t : tb.diks.tokens) {
                w.write(String.format(diksRowFmt,
                        tb.aitNoKafka,
                        startStr,
                        esc(t.event),
                        endStr,
                        durationSecs,
                        seqForKafka,
                        esc(t.status),
                        tb.ksTopic, tb.diks.gid, tb.ksDbType, tb.ksSchema, tb.ksMachine,
                        DEFAULT_PROFILE, tb.diks.cid, DEFAULT_USER, tb.diks.cid
                ));
            }

            // DIAS (ait_seq)  — enforce single seq = 1
            final String diasFmt = isSqlServer ? SQLSERVER.DIAS_FMT : MYSQL.DIAS_FMT;
            final String cfgIdForDias = tb.diks.cid == null ? "NULL" : "'" + esc(tb.diks.cid) + "'";
            w.write(String.format(diasFmt,
                    tb.aitNoBare, tb.aitNoBare, esc(tb.ksSchema), esc(tb.ksDbType), cfgIdForDias
            ));

            // DISW (ait_scan_window) — 7 rows
            final String diswDelFmt = isSqlServer ? SQLSERVER.DISW_DELETE_FMT : MYSQL.DISW_DELETE_FMT;
            final String diswInsFmt = isSqlServer ? SQLSERVER.DISW_INSERT_FMT : MYSQL.DISW_INSERT_FMT;
            w.write(String.format(diswDelFmt, tb.aitNoBare, tb.ksDbType));
            w.write(String.format(diswInsFmt,
                    tb.aitNoBare, tb.ksDbType,
                    tb.aitNoBare, tb.ksDbType,
                    tb.aitNoBare, tb.ksDbType,
                    tb.aitNoBare, tb.ksDbType,
                    tb.aitNoBare, tb.ksDbType,
                    tb.aitNoBare, tb.ksDbType,
                    tb.aitNoBare, tb.ksDbType
            ));

            // DIDM (dp_ktb_mv_dispositionmetrics)
            final String didmFmt = isSqlServer ? SQLSERVER.DIDM_FMT : MYSQL.DIDM_FMT;
            final String discVal = ("UDD".equalsIgnoreCase(tb.disc)) ? "UDD" : "SDD";
            w.write(String.format(didmFmt,
                    tb.aitNoBare,                    // delete by AppID
                    tb.aitNoBare, tb.dbTypeBiz,     // insert: AppID, DBType
                    tb.aitNoKafka,                  // ProvidedOn derived from kafka_stat (MAX + 2m)
                    discVal                         // DiscoveryType
            ));

            // DKSS (scheduler stat cleanup)
            final String dkssFmt = isSqlServer ? SQLSERVER.DKSS_FMT : MYSQL.DKSS_FMT;
            w.write(String.format(dkssFmt, tb.aitNoBare));

            // footer
            if (isSqlServer) w.write(SQLSERVER.FOOTER);
            else             w.write(MYSQL.FOOTER);
        }

        log.info("Wrote {}", destPath.toAbsolutePath());
        return destPath.toAbsolutePath().toString();
    }

    /**
     * Builds the validation SQL (TRUE/FALSE single-row SELECT) to store
     * as the Step-5 parameter. Uses either, both, or one table based on VALIDATION_TARGET or rule.
     */
    public String buildValidationParam(TestBlock tb) {
        final boolean isSqlServer = "SQLSERVER".equalsIgnoreCase(DIALECT);

        // defaults (if rules not provided)
        if (tb.validation == null) tb.validation = new ValidationSpec();
        if (tb.validation.target == null || tb.validation.target.isBlank())
            tb.validation.target = VALIDATION_TARGET;

        if (tb.validation.kafka == null) tb.validation.kafka = new ValidationSpec.KafkaRules();
        if (tb.validation.kafka.minRows == null) tb.validation.kafka.minRows = 1;
        if (tb.validation.kafka.statusIn == null || tb.validation.kafka.statusIn.isEmpty())
            tb.validation.kafka.statusIn = List.of("Completed");
        if (tb.validation.kafka.eventIn == null || tb.validation.kafka.eventIn.isEmpty())
            tb.validation.kafka.eventIn = List.of("Metadata", "Producer", "Consumer");
        if (tb.validation.kafka.constSeq == null) tb.validation.kafka.constSeq = true;

        if (tb.validation.sched == null) tb.validation.sched = new ValidationSpec.SchedRules();
        if (tb.validation.sched.requireRow == null) tb.validation.sched.requireRow = true;
        if (tb.validation.sched.statusIn == null || tb.validation.sched.statusIn.isEmpty())
            tb.validation.sched.statusIn = List.of("Completed", "In Progress");

        // WHERE fragments
        final String aitNoLike = "'%" + esc(tb.aitNoBare) + "%'";
        String ksStatusIn = inList(tb.validation.kafka.statusIn);
        String ksEventIn  = inList(tb.validation.kafka.eventIn);

        String kafkaWhere =
                "AIT_NO = '" + esc(tb.aitNoKafka) + "'" +
                        " AND STATUS IN (" + ksStatusIn + ")" +
                        " AND EVENT  IN (" + ksEventIn + ")";
        if (tb.validation.kafka.constSeq) {
            kafkaWhere += " AND SEQ_NO = 1";
        }

        String schedWhere =
                "AIT_NO LIKE " + aitNoLike +
                        " AND STATUS IN (" + inList(tb.validation.sched.statusIn) + ")";

        // Boolean expressions (portable)
        final String kafkaBool =
                "(SELECT COUNT(1) FROM kafka_stat WHERE " + kafkaWhere + ") >= " + tb.validation.kafka.minRows;
        final String schedBool =
                "EXISTS(SELECT 1 FROM KAFKA_AIT_SCHEDULER_STAT WHERE " + schedWhere + ")";

        String finalBool;
        switch (tb.validation.target.toUpperCase()) {
            case "KAFKA_ONLY" -> finalBool = kafkaBool;
            case "SCHED_ONLY" -> finalBool = tb.validation.sched.requireRow ? schedBool : "1=1";
            default /* BOTH */ -> {
                String both = "(" + kafkaBool + " AND " + (tb.validation.sched.requireRow ? schedBool : "1=1") + ")";
                finalBool = both;
            }
        }

        final String wrapFmt = isSqlServer ? SQLSERVER.WRAP_BOOL_TO_RESULT_FMT : MYSQL.WRAP_BOOL_TO_RESULT_FMT;
        return String.format(wrapFmt, finalBool);
    }

    private static String esc(String s) { return s == null ? "" : s.replace("'", "''"); }
    private static String nvl(String s) { return s == null ? "" : s; }
    private static String inList(List<String> values) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String v : values) {
            if (!first) sb.append(',');
            sb.append('\'').append(esc(v)).append('\'');
            first = false;
        }
        if (first) return "''";
        return sb.toString();
    }

    // ===== Helper DTOs =====
    public static class TestBlock {
        public String tcId, tcDesc, dbTypeBiz, disc;
        public String aitNoBare, aitNoKafka, topicName;

        // For DIKS rows (derived from DIAC/DIDP if not provided)
        public String ksDbType, ksSchema, ksMachine, ksTopic;

        // Single seq for whole testcase; runner can set; default 1
        public int seqNo;

        public Flags flags  = new Flags();
        public DbProp dbprop = new DbProp();
        public Diks   diks   = new Diks();
        public TimeSpec time = new TimeSpec();

        // Validation (optional)
        public ValidationSpec validation;
    }

    public static class Flags {
        public int funSdd, aimlSdd, idwSdd, iedpsSdd, espialSdd;
        public int funFft, idwFft,  espialFft;
    }

    public static class DbProp { public String id, mn, dn, sn, uid, pwd, jur; }
    public static class Diks { public String gid, cid; public List<Token> tokens = new ArrayList<>(); }
    public static class Token { public String event; public String status; }

    public static class TimeSpec {
        public String  startLiteral;   // "yyyy-MM-dd HH:mm:ss"
        public Integer ctmMins;        // minutes
        public Integer cthHours;       // hours
        public Integer ctdDays;        // days
        public Integer ctmoMonths;     // months
    }

    // ===== Static inner ValidationSpec =====
    public static class ValidationSpec {
        public String target; // KAFKA_ONLY | SCHED_ONLY | BOTH
        public KafkaRules kafka = new KafkaRules();
        public SchedRules sched = new SchedRules();

        public static class KafkaRules {
            public Integer minRows;
            public Boolean constSeq;     // true -> SEQ_NO=1 enforced
            public List<String> statusIn;
            public List<String> eventIn;
        }
        public static class SchedRules {
            public Boolean requireRow;   // require any scheduler row
            public List<String> statusIn;
        }
    }
}
