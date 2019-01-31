package com.github.tkutche1.jgrade;

import com.github.tkutche1.jgrade.gradedtest.GradedTestResult;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.github.tkutche1.jgrade.gradedtest.GradedTestResult.HIDDEN;
import static org.junit.Assert.assertEquals;

public class DeductiveGraderStrategyTest {

    private static final double STARTING_SCORE = 14;
    private static final double FLOOR = 2;

    private DeductiveGraderStrategy unit;

    @Before
    public void initUnit() {
        this.unit = new DeductiveGraderStrategy(STARTING_SCORE);
    }

    private static GradedTestResult failedGradedTestResult(double points) {
        GradedTestResult r = new GradedTestResult("", "", points, HIDDEN);
        r.setPassed(false);
        return r;
    }

    @Test
    public void noDeductions() {
        List<GradedTestResult> l = new ArrayList<>();
        l.add(new GradedTestResult("", "", 2.0, HIDDEN));
        l.add(new GradedTestResult("", "", 2.0, HIDDEN));
        l.add(new GradedTestResult("", "", 2.0, HIDDEN));
        l.add(new GradedTestResult("", "", 2.0, HIDDEN));

        this.unit.grade(l);

        for (GradedTestResult r : l) {
            assertEquals(0, r.getPoints(), 0.0);
            assertEquals(0, r.getScore(), 0.0);
        }

        assertEquals(0, unit.getDeductedPoints(), 0.0);
    }

    @Test
    public void deductToZero() {
        List<GradedTestResult> l = new ArrayList<>();
        l.add(failedGradedTestResult(STARTING_SCORE));

        this.unit.grade(l);

        for (GradedTestResult r : l) {
            assertEquals(0, r.getPoints(), 0.0);
            assertEquals(0 - STARTING_SCORE, r.getScore(), 0.0);
        }

        assertEquals(STARTING_SCORE, unit.getDeductedPoints(), 0.0);
    }

    @Test
    public void deductExactlyToFloor() {
        List<GradedTestResult> l = new ArrayList<>();
        l.add(failedGradedTestResult(STARTING_SCORE - 2));
        l.add(failedGradedTestResult(3));

        this.unit.grade(l);

        assertEquals(0 - STARTING_SCORE + 2, l.get(0).getScore(), 0.0);
        assertEquals(-2, l.get(1).getScore(), 0.0);
        assertEquals(STARTING_SCORE, unit.getDeductedPoints(), 0.0);
    }

    @Test
    public void deductExactlyToFloorOneTest() {
        List<GradedTestResult> l = new ArrayList<>();
        l.add(failedGradedTestResult(STARTING_SCORE + 2));

        this.unit.grade(l);

        assertEquals(0 - STARTING_SCORE, l.get(0).getScore(), 0.0);
        assertEquals(STARTING_SCORE, unit.getDeductedPoints(), 0.0);
    }

    @Test
    public void setDifferentFloor() {
        this.unit.setFloor(FLOOR);

        List<GradedTestResult> l = new ArrayList<>();
        l.add(failedGradedTestResult(STARTING_SCORE));

        this.unit.grade(l);

        assertEquals(0 - STARTING_SCORE + FLOOR, l.get(0).getScore(), 0.0);
        assertEquals(STARTING_SCORE - FLOOR, unit.getDeductedPoints(), 0.0);
    }
}
