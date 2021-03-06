package com.kderyabin.mvvm;

/**
 * Implementing class has data that can be modified.
 */
public interface EditableInterface {
    /**
     * Returns modification status of the current class.
     * @return TRUE if class data has been modified FALSE if not.
     */
    public boolean isUpdated();

    /**
     * Saves somewhere somehow classes' data.
     */
    public void save();
}
