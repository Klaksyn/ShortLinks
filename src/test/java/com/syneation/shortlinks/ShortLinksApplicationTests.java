package com.syneation.shortlinks;

import com.syneation.shortlinks.controllers.links.helpers.HelperLink;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ShortLinksApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    @DisplayName("Test: generateLinksOnLen")
    public void testGenerateLinksOnLen() {
        int expected = 6;
        int actual = HelperLink.generateLink((short) 6).length();

        assertEquals(expected, actual);
    }

}
