package de.lgohlke.utils.status;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SizeFormatterTest {

    @Test
    public void shouldShow10MB() throws Exception {
        test(10 * 1024 * 1024, "10 MB");
    }

    @Test
    public void shouldShow900KB() throws Exception {
        test(900 * 1024, "900 KB");
    }

    private void test(int size, String expected) {
        String actual = SizeFormatter.readableFileSize(size);
        assertThat(actual).isEqualTo(expected);
    }
}
