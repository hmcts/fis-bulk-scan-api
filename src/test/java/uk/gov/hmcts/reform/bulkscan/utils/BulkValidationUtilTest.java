package uk.gov.hmcts.reform.bulkscan.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class BulkValidationUtilTest {

    String dateFormat = "dd/mm/yyyy";
    String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    String numericRegex = "^[0-9]*$";

    @Test
    void testValidDateFormat() {
        boolean dateValid = BulkScanValidationUtil.isDateValid("", "2/02/2022", dateFormat);
        assertTrue(dateValid);
    }

    @Test
    void testInvalidDateFormat() {
        boolean dateValid = BulkScanValidationUtil.isDateValid("", "invalidformat", dateFormat);
        assertFalse(dateValid);
    }

    @Test
    void testValidEmail() {
        boolean emailValid = BulkScanValidationUtil.isValidFormat("test@test.com", emailRegex);
        assertTrue(emailValid);
    }

    @Test
    void testInvalidEmail() {
        boolean emailValid = BulkScanValidationUtil.isValidFormat("test.com", emailRegex);
        assertFalse(emailValid);
    }

    @Test
    void testValidNumberString() {
        boolean numericValid = BulkScanValidationUtil.isValidFormat("123231241", numericRegex);
        assertTrue(numericValid);
    }

    @Test
    void testInvalidNumberString() {
        boolean numericValid = BulkScanValidationUtil.isValidFormat("test13323", numericRegex);
        assertFalse(numericValid);
    }
}
