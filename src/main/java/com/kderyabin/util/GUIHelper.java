package com.kderyabin.util;

import javafx.scene.Node;

/**
 * Helper static methods to work with GUI
 */
final public class GUIHelper {

    /**
     * Renders a node visible or not visible.
     *
     * @param node    Element to hide/show
     * @param isVisible TRUE to set visible FALSE to hide
     */
    public static void renderVisible(Node node, boolean isVisible) {
        if (!isVisible) {
            // prevent size calculation
            node.setManaged(false);
            // hide
            node.setVisible(false);
            return;
        }
        node.setManaged(true);
        node.setVisible(true);
    }
}
