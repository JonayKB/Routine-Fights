package es.iespuertodelacruz.routinefights.shared.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChartDataTest {

    private static final String JAN = "Jan";
    private static final String FEB = "Feb";
    private static final String MAR = "Mar";
    private static final String APR = "Apr";
    private static final String MAY = "May";
    private static final String JUNE = "June";
    private static final String LABEL_X = "X";
    private static final String LABEL_A = "A";
    private static final String LABEL_B = "B";
    private static final String LABEL_C = "C";

    private static final String ERR_NULL_LABELS = "Labels list should not be null";
    private static final String ERR_NULL_DATA = "Data list should not be null";
    private static final String ERR_EMPTY_LABELS = "Labels list should be empty";
    private static final String ERR_EMPTY_DATA = "Data list should be empty";
    private static final String ERR_MATCH_INPUT_LABELS = "Labels should match input list";
    private static final String ERR_MATCH_INPUT_DATA = "Data should match input list";
    private static final String ERR_SET_GET_LABELS = "Labels setter/getter should work";
    private static final String ERR_SET_GET_DATA = "Data setter/getter should work";
    private static final String ERR_TOSTRING_LABELS = "toString should include labels";
    private static final String ERR_TOSTRING_DATA = "toString should include data";
    private static final String ERR_EQUAL_CONTENT = "Objects with same content should be equal";
    private static final String ERR_HASHCODE_MATCH = "Hash codes should match for equal objects";
    private static final String ERR_NOT_EQUAL_CONTENT = "Objects with different content should not be equal";
    private static final String ERR_HASHCODE_DIFFER = "Hash codes should differ for non-equal objects";
    private static final String ERR_EQUAL_NULL = "Should not be equal to null";
    private static final String ERR_EQUAL_DIFFERENT_CLASS = "Should not be equal to object of another class";

    private ChartData emptyChart;
    private ChartData populatedChart;

    @BeforeEach
    void setUp() {
        emptyChart = new ChartData();
        List<String> labels = Arrays.asList(JAN, FEB, MAR);
        List<Long> data = Arrays.asList(10L, 20L, 30L);
        populatedChart = new ChartData(labels, data);
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(emptyChart.getLabels(), ERR_NULL_LABELS);
        assertNotNull(emptyChart.getData(), ERR_NULL_DATA);
        assertTrue(emptyChart.getLabels().isEmpty(), ERR_EMPTY_LABELS);
        assertTrue(emptyChart.getData().isEmpty(), ERR_EMPTY_DATA);
    }

    @Test
    void testParameterizedConstructor() {
        List<String> expectedLabels = Arrays.asList(JAN, FEB, MAR);
        List<Long> expectedData = Arrays.asList(10L, 20L, 30L);

        assertEquals(expectedLabels, populatedChart.getLabels(), ERR_MATCH_INPUT_LABELS);
        assertEquals(expectedData, populatedChart.getData(), ERR_MATCH_INPUT_DATA);
    }

    @Test
    void testSettersAndGetters() {
        List<String> newLabels = Arrays.asList(APR, MAY);
        List<Long> newData = Arrays.asList(40L, 50L);

        emptyChart.setLabels(newLabels);
        emptyChart.setData(newData);

        assertEquals(newLabels, emptyChart.getLabels(), ERR_SET_GET_LABELS);
        assertEquals(newData, emptyChart.getData(), ERR_SET_GET_DATA);
    }

    @Test
    void testAddLabelAndAddData() {
        emptyChart.addLabel(JUNE);
        emptyChart.addData(60L);

        assertEquals(Collections.singletonList(JUNE), emptyChart.getLabels());
        assertEquals(Collections.singletonList(60L), emptyChart.getData());
    }

    @Test
    void testToString() {
        emptyChart.addLabel(LABEL_X);
        emptyChart.addData(1L);
        String str = emptyChart.toString();

        assertTrue(str.contains("labels=[" + LABEL_X + "]"), ERR_TOSTRING_LABELS);
        assertTrue(str.contains("data=[1]"), ERR_TOSTRING_DATA);
    }

    @Test
    void testEqualsAndHashCode() {
        ChartData copy = new ChartData(
                Arrays.asList(LABEL_A, LABEL_B),
                Arrays.asList(100L, 200L));
        ChartData same = new ChartData(
                Arrays.asList(LABEL_A, LABEL_B),
                Arrays.asList(100L, 200L));
        ChartData different = new ChartData(
                Arrays.asList(LABEL_C),
                Arrays.asList(300L));

        assertEquals(copy, same, ERR_EQUAL_CONTENT);
        assertEquals(copy.hashCode(), same.hashCode(), ERR_HASHCODE_MATCH);

        assertNotEquals(copy, different, ERR_NOT_EQUAL_CONTENT);
        assertNotEquals(copy.hashCode(), different.hashCode(), ERR_HASHCODE_DIFFER);

        assertNotEquals(null, copy, ERR_EQUAL_NULL);
        assertNotEquals(copy, new Object(), ERR_EQUAL_DIFFERENT_CLASS);
    }
}
