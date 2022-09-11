package uk.gov.hmcts.reform.bulkscan.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DateUtilTest {

    @Test
    @ParameterizedTest
    @CsvSource({
        "2023-Feb-28,  uuuu-[M][MM][MMM]-d",
        "2023-02-28, uuuu-[M][MM][MMM]-d",
        "2023-feb-28,  uuuu-[M][MM][MMM]-d"
    })
    void testDateWithDifferentInput(String dateStr, String pattern) {

        // Below pattern will accept feb, 01 and 1 as month

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
