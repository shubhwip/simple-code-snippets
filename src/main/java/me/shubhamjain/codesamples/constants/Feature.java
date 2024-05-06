package me.shubhamjain.codesamples.constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Feature {

    void save() {
        if(LogsConstants.DEBUG_ENABLED) {
            log.info("Saving");
            write();
        }
    }

    void write() {
        log.info("Writing");
    }

    void delete() {
        log.info("deleting");
    }
}
