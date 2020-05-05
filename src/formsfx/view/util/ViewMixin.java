package formsfx.view.util;

public interface ViewMixin {
    default void init() {
        initializeSelf();
        initializeParts();
        layoutParts();
        setupEventHandlers();
        setupValueChangedListeners();
        setupBindings();
    }

    default void initializeSelf() {}
    void initializeParts();
    void layoutParts();
    default void setupEventHandlers() {}
    default void setupValueChangedListeners() {}
    default void setupBindings() {}
}
