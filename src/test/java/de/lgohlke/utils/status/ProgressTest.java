package de.lgohlke.utils.status;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProgressTest {
    @Test
    public void shouldHave50Percent() throws Exception {
        Progress progress = new Progress(100);
        progress.reduce(50);
        assertThat(progress.status()).isEqualTo(" 50% processed 50 B");
    }

    @Test
    public void shouldHave70Percent() throws Exception {
        Progress progress = new Progress(100);
        progress.reduce(70);
        assertThat(progress.status()).isEqualTo(" 70% processed 70 B");
    }
}