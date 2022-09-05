package uk.gov.hmcts.reform.bulkscan.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DateUtilTest {

    @Test
    public void testDate() {

        String dateStr = "2023-Feb-28";

        String pattern = "uuuu-MMM-d";

        assertTrue(DateUtil.validateDate(dateStr, pattern));
    }

    @Test
    public void testDateWithNumericMonth() {

        String dateStr = "2023-01-28";

        String pattern = "uuuu-M-d";

        assertTrue(DateUtil.validateDate(dateStr, pattern));
    }

    @Test
    public void testDateInvalidMonth() {

        String dateStr = "2023-feb-28";

        String pattern = "uuuu-MMM-d";

        assertFalse(DateUtil.validateDate(dateStr, pattern));
    }

    @Test
    public void testDateInvalidNumricMonth() {

        String dateStr = "2023-00-28";

        String pattern = "uuuu-M-d";

        assertFalse(DateUtil.validateDate(dateStr, pattern));
    }
}
