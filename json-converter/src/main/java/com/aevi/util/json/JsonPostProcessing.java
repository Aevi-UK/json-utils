package com.aevi.util.json;

/**
 * A callback interface for models to be notified when deserialisation is completed.
 */
public interface JsonPostProcessing {

    /**
     * Called once the model is fully deserialised
     */
    void onJsonDeserialisationCompleted();
}
