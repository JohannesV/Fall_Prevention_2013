package no.ntnu.stud.fallservice;

/**
 * Interface implemented by classes that can handle notifications about steps.
 * These classes can be passed to StepDetector.
 * @author Levente Bagi
 * https://github.com/bagilevi/android-pedometer/blob/master/src/name/bagi/levente/pedometer/StepListener.java
 */
public interface StepListener {
    public void onStep();
    public void passValue();
}
