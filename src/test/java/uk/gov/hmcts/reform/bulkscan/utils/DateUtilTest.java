package uk.gov.hmcts.reform.bulkscan.utils;

import static org.junit.Assert.assertEquals;
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

        // Below pattern will accept feb, 01 and 1 as month

        String dateStr = "2023-01-28";

        String pattern = "uuuu-[M][MM][MMM]-d";

        assertTrue(DateUtil.validateDate(dateStr, pattern));
    }

    @Test
    public void testDateValidMonthLowerCase() {

        String dateStr = "2023-feb-28";

        String pattern = "uuuu-MMM-d";

        assertTrue(DateUtil.validateDate(dateStr, pattern));
    }

    @Test
    public void testDateInvalidNumricMonth() {

        String dateStr = "2023-00-28";

        String pattern = "uuuu-M-d";

        assertFalse(DateUtil.validateDate(dateStr, pattern));
    }

    @Test
    public void testDateTransfrom() {

        String dateStr = "2023-feb-28";

        String expectedDate = "2023-02-28";

        String pattern = "[uuuu-M-d][uuuu-MMM-d]";

        String formatPattern = "[uuuu-MM-d]";

        assertEquals(expectedDate, DateUtil.transformDate(dateStr, pattern, formatPattern));
    }

    @Test
    public void testNumericDateTransfrom() {

        String dateStr = "2023-02-28";

        String expectedDate = "2023-02-28";

        String pattern = "[uuuu-M-d][uuuu-MMM-d]";

        String formatPattern = "[uuuu-MM-d]";

        assertEquals(expectedDate, DateUtil.transformDate(dateStr, pattern, formatPattern));
    }
}
