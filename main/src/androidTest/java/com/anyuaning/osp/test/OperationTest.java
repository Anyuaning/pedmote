package com.anyuaning.osp.test;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;


/**
 * Created by thom on 13-10-29.
 */
public class OperationTest {
    @Test
    public void testAddition() {
        assertThat((5+1)).isEqualTo(6);
    }

    @Test
    public void testSubtraction() {
        assertThat(7-3).isEqualTo(4);
    }

    @Test
    public void testMultiplication() {
        assertThat((3*4)).isEqualTo(12);
    }

    @Test
    public void testDivisio() {
        assertThat(9/4).isEqualTo(2);
    }

}
