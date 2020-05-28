package com.kderyabin.viewmodels;

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
     * Save class data.
     */
    public void save();
}
